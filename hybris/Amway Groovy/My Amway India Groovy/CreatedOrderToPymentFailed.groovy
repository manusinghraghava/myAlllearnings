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


		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final EventService eventService = (EventService) Registry.getApplicationContext().getBean("eventService");
		
		final DefaultAmwayOrderService defaultAmwayOrderService = (DefaultAmwayOrderService) Registry.getApplicationContext().getBean("defaultAmwayOrderService");
		final IndOrderPaymentService orderPaymentService = (IndOrderPaymentService) Registry.getApplicationContext().getBean("indOrderPaymentService");
		final DefaultAmwayPaymentService defaultAmwayPaymentService = (DefaultAmwayPaymentService) Registry.getApplicationContext().getBean("defaultAmwayPaymentService");
		
		final String orderNumber="707658046"; //707993553
		Long orderpk=8833270579245;
		final OrderModel orderModel = modelService.get(new PK(orderpk));
		println "Order>>"+orderModel;

				println "Initiating reversal of arBalance for order: "+orderNumber;
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
						PaymentTransactionEntryModel reversalPaymentTransactionEntryModelResponse = defaultAmwayPaymentService.cancel(entry);
						if (reversalPaymentTransactionEntryModelResponse.getTransactionStatus().equals(TransactionStatus.ACCEPTED.toString())){
							println "arBalance Amount successfully reversed: "+ reversalPaymentTransactionEntryModelResponse.getAmount();
							orderModel.setPaymentStatus(PaymentStatus.NOTPAID);
							modelService.save(orderModel);
							println "Payment Status set as Not Paid, for Order: "+ orderModel.getCode();
						}else{
							println "arBalance Amount reversal failed: "+ reversalPaymentTransactionEntryModelResponse.getAmount();
						}
						}};
						}
					}
			
		 
				}
