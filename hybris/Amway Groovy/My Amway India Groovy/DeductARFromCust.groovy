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
import com.amway.core.balance.services.AmwayAccountBalanceService;
import java.math.BigDecimal; 


		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final EventService eventService = (EventService) Registry.getApplicationContext().getBean("eventService");
		
		
		final DefaultAmwayOrderService defaultAmwayOrderService = (DefaultAmwayOrderService) Registry.getApplicationContext().getBean("defaultAmwayOrderService");
		final IndOrderPaymentService orderPaymentService = (IndOrderPaymentService) Registry.getApplicationContext().getBean("indOrderPaymentService");
		final DefaultAmwayPaymentService defaultAmwayPaymentService = (DefaultAmwayPaymentService) Registry.getApplicationContext().getBean("defaultAmwayPaymentService");
		
		final IndDefaultPaymentFacade indPaymentFacade = (IndDefaultPaymentFacade) Registry.getApplicationContext().getBean("indPaymentFacade");
		final AmwayAccountBalanceService amwayAccountBalanceService = (AmwayAccountBalanceService) Registry.getApplicationContext().getBean("amwayAccountBalanceService");
		
		final String orderNumber="711722687"; // Need to reverse again - 707993553 
		Long orderpk=8854333554733;
		final OrderModel orderModel = modelService.get(new PK(orderpk));
		//Monetary
		//indPaymentFacade.releaseAndRefundOrder(orderModel, OrderStatus.PAYMENT_FAILED, PaymentStatus.NOTPAID);
		//indPaymentFacade.reversePaymentByArBalance(orderModel.getCode());
		
		BigDecimal amount = new BigDecimal("2071");
		amwayAccountBalanceService.debitAccountBalance(orderModel, amount, "Monetary");

				
				println "success!!";
				
	
		

			