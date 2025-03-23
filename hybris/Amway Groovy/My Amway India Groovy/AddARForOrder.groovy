import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.core.enums.OrderStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.processengine.BusinessProcessService;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.event.EventService;

import de.hybris.platform.order.events.SubmitOrderEvent;

import de.hybris.platform.store.BaseStoreModel;
import java.lang.System;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.amway.core.model.AmwayAccountModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import com.amway.indpayment.service.IndOrderPaymentService;
import com.amway.core.order.services.impl.DefaultAmwayOrderService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import com.amway.core.enums.INDTransactionModeEnum;
import java.lang.Long;
import de.hybris.platform.core.PK;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import com.amway.core.payment.service.impl.DefaultAmwayPaymentService;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import com.amway.indpayment.facade.impl.IndDefaultPaymentFacade;
import com.amway.lynxcore.account.LynxRoleService;

import com.amway.core.model.AmwayCreditPaymentInfoModel;
import com.amway.core.model.AmwayMonetaryPaymentInfoModel;

import com.amway.core.checkout.services.impl.DefaultAmwayCommerceCheckoutService;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;

		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final EventService eventService = (EventService) Registry.getApplicationContext().getBean("eventService");
		
		
		final DefaultAmwayOrderService defaultAmwayOrderService = (DefaultAmwayOrderService) Registry.getApplicationContext().getBean("defaultAmwayOrderService");
		final IndOrderPaymentService orderPaymentService = (IndOrderPaymentService) Registry.getApplicationContext().getBean("indOrderPaymentService");
		final DefaultAmwayPaymentService defaultAmwayPaymentService = (DefaultAmwayPaymentService) Registry.getApplicationContext().getBean("defaultAmwayPaymentService");
		
		DefaultAmwayCommerceCheckoutService defaultAmwayCommerceCheckoutService = (DefaultAmwayCommerceCheckoutService) Registry.getApplicationContext().getBean("defaultAmwayCommerceCheckoutService");
		
		final IndDefaultPaymentFacade indPaymentFacade = (IndDefaultPaymentFacade) Registry.getApplicationContext().getBean("indPaymentFacade");
		final LynxRoleService lynxRoleService = (LynxRoleService) Registry.getApplicationContext().getBean("lynxRoleService");
		
		String query = "select {o.pk} from {Order as o} where ({o.lynxGroupNumber} is null or {o.lynxGroupNumber}={o.code}) and {o.code}='714255461'";
				
		final SearchResult<OrderModel> searchResult = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query);

		final List<OrderModel> results = searchResult.getResult();

		if (CollectionUtils.isNotEmpty(results))
			{
			for (final OrderModel orderModel : results) 
			{
			 println "Initiating reversal of arBalance for order: "+orderModel.getCode();
			  
			 if (lynxRoleService.isAbo(orderModel.getUser())) 
			  {
			 if (orderModel.getPaymentTransactions() != null)
				{
					for (PaymentTransactionModel paymentTransactionModel:orderModel.getPaymentTransactions())
					{
						if (paymentTransactionModel.getTransactionMode()!=null && paymentTransactionModel.getTransactionMode().equals(INDTransactionModeEnum.AUTHHORIZED_AR))
						{
						//Reverse Payment
						paymentTransactionModel.getEntries().forEach{entry-> if (entry.getType().equals(PaymentTransactionType.CAPTURE)
						&& entry.getTransactionStatus()!=null && entry.getTransactionStatus().equals(TransactionStatus.ACCEPTED.toString()))
						{
						entry.setCurrency(orderModel.getCurrency());
						//Cancel the Transaction
						
						println "Initiating arBalance Reversal: Amount to be reversed: "+ entry.getAmount();
						
						//Adding Paymentinfo model
						if(entry.getPaymentTransaction().getInfo() == null){
							final PaymentInfoModel arPaymentInfoModel = defaultAmwayCommerceCheckoutService.createARCreditPaymentInfo((CustomerModel)orderModel.getUser(),orderModel);
							entry.getPaymentTransaction().setInfo(arPaymentInfoModel);
						}
						
						PaymentTransactionEntryModel reversalPaymentTransactionEntryModelResponse = defaultAmwayPaymentService.cancel(entry);

						if (reversalPaymentTransactionEntryModelResponse.getTransactionStatus().equals(TransactionStatus.ACCEPTED.toString())){
							println "arBalance Amount successfully reversed: "+ reversalPaymentTransactionEntryModelResponse.getAmount();
							//orderModel.setPaymentStatus(PaymentStatus.NOTPAID);
							//modelService.save(orderModel);
							println "Payment Status set as Not Paid, for Order: "+ orderModel.getCode();
						}else{
							println "arBalance Amount reversal failed: "+ reversalPaymentTransactionEntryModelResponse.getAmount();
						}
						}};
						}
						
						
					}
				}
			}
			else
			{
			
				if (orderModel.getPaymentTransactions() != null)
				{
					for (PaymentTransactionModel paymentTransactionModel:orderModel.getPaymentTransactions())
					{
						if (paymentTransactionModel.getTransactionMode()!=null && paymentTransactionModel.getTransactionMode().equals(INDTransactionModeEnum.LOYALTY))
						{
							//Reverse Payment
							paymentTransactionModel.getEntries().forEach{entry-> if (entry.getType().equals(PaymentTransactionType.CAPTURE)
									&& entry.getTransactionStatus()!=null && entry.getTransactionStatus().equals(TransactionStatus.ACCEPTED.toString()))
							{
								entry.setCurrency(orderModel.getCurrency());
								//Cancel the Transaction
								println "=====>Initiating Loyalty Points Reversal: Amount to be reversed: "+ Math.floor(entry.getAmount().doubleValue());
								PaymentTransactionEntryModel reversalPaymentTransactionEntryModelResponse = defaultAmwayPaymentService.cancel(entry);
								if (reversalPaymentTransactionEntryModelResponse.getTransactionStatus().equals(TransactionStatus.ACCEPTED.toString()))
								{
									println "=======>Loyalty Points Amount successfully reversed: " +reversalPaymentTransactionEntryModelResponse.getAmount().toPlainString();
									//orderModel.setPaymentStatus(PaymentStatus.NOTPAID);
									//modelService.save(orderModel);
									println "======> Payment Status set as Not Paid, for Order: "+ orderModel.getCode();
								}else
								{
									println "=====>Loyalty Points Amount reversal failed for amount: "+ reversalPaymentTransactionEntryModelResponse.getAmount();
								}
							}
							};
						}
					}
				}
			
			}
				//orderModel.setStatus(OrderStatus.PAYMENT_FAILED);
				//modelService.save(orderModel);
				
			}
			
			}
			
			println "success!!";
		
		//indPaymentFacade.releaseAndRefundOrder(orderModel, OrderStatus.PAYMENT_FAILED, PaymentStatus.NOTPAID);
		//indPaymentFacade.reversePaymentByArBalance(orderModel.getCode());
		
		
				
				
				
				
	
		

			