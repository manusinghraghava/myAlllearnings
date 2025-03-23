import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.HashMap;
import com.amway.indfacades.order.payload.*;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.model.ModelService;
import com.amway.indcore.loyalty.service.IndCalculateLoyaltyPointsService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import com.amway.eventstream.model.AmwayEventStagingEntryModel;
import com.amway.eventstream.enums.AmwayEventSyncState;
import com.amway.indfacades.order.payload.*;
import java.util.List;
import java.util.Optional;
import com.google.gson.*;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringEscapeUtils;
import com.amway.eventstream.enums.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.model.ModelService;
import com.amway.indcore.loyalty.service.IndCalculateLoyaltyPointsService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import com.amway.eventstream.model.AmwayEventStagingEntryModel;
import com.amway.eventstream.enums.AmwayEventSyncState;
import com.amway.indfacades.order.payload.*;
import java.util.List;
import java.util.Optional;
import com.google.gson.*;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.site.BaseSiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import com.amway.eventstream.enums.*;


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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amway.eventstream.dto.AmwayEventData;
import com.amway.eventstream.enums.EventName;
import com.amway.eventstream.exception.AmwayEventRetryException;
import com.amway.eventstream.service.AmwayEventQueueService;
import com.amway.indcore.builder.factory.IndPushBusinessEventBuilderFactory;
import com.amway.indfacades.builder.order.IndOrderPushBusinessEventBuilder;
import com.amway.lynxcore.site.LynxBaseSiteService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.amway.core.model.AmwayAccountModel;

import de.hybris.platform.processengine.model.BusinessProcessModel;

        final CatalogVersionService catalogVersionService = (CatalogVersionService) Registry.getApplicationContext().getBean("catalogVersionService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final EventService eventService = (EventService) Registry.getApplicationContext().getBean("eventService");
		final PayloadUtil payloadUtil  = (PayloadUtil) Registry.getApplicationContext().getBean("payloadUtil");
		final IndPushBusinessEventBuilderFactory builderFactory=(IndPushBusinessEventBuilderFactory) Registry.getApplicationContext().getBean("indPushBusinessEventBuilderFactory");   
		final UserService userService = (UserService) Registry.getApplicationContext().getBean("userService");
		final BaseStoreService baseStoreService = spring.getBean("baseStoreService");
        final BaseSiteService baseSiteService = spring.getBean("baseSiteService");
		String query1="select {pk} from {CMSSite as o} where {o.pk} ='"+8796093056040+"'";
        final SearchResult<CMSSiteModel> searchResult1 = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query1);
        final List<CMSSiteModel> results1 = searchResult1.getResult();
        CMSSiteModel cmsSiteModel = results1.stream().findFirst().get();


        def baseSiteModel =baseSiteService.getCurrentBaseSite();



        baseSiteService.setCurrentBaseSite(cmsSiteModel, true);
		
		final BusinessProcessService businessProcessService   = (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
		
		
		
		

		String query = "Select {o.pk} from {order as o} where ({o.lynxGroupNumber} is null or {o.lynxGroupNumber}={o.code}) and {o.code} in ()";
		final SearchResult<OrderModel> searchResult = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query);
		
		final List<OrderModel> results = searchResult.getResult();
		if (CollectionUtils.isNotEmpty(results))
	    {
		for(OrderModel order :results)
		{
		IndOrderPayloadData indOrderPayloadData=new IndOrderPayloadData();
		if(order!=null)
		{
		
		try{
		indOrderPayloadData=indOrderPayloadConverter.convert(order);
		} 
		catch(Exception e)
		{
		println "Issue come in the Order :"+order.getCode();
		continue;
		}
		} 
		indOrderPayloadData.setCurrency(order.getCurrency().getSymbol());
		
		def consignments = indOrderPayloadData.getConsignments();
		def childOrders =  indOrderPayloadData.getChildOrders(); 
		def subTotal = indOrderPayloadData.getSubTotal(); 
		def suborderIDs = indOrderPayloadData.getSuborderIDs();
		
		println consignments;
		
		try{
		userService.setCurrentUser(order.getUser());
		final CatalogVersionModel catalog = order.getEntries().get(0).getProduct().getCatalogVersion();
			if (catalog != null)
			{
				catalogVersionService.setSessionCatalogVersion(catalog.getCatalog().getId(), catalog.getVersion());
			}
			final String eventName = "ORDER_CREATED_INVOICED";
			final IndOrderPushBusinessEventBuilder eventBuilder = (IndOrderPushBusinessEventBuilder) (builderFactory
					.getPushBusinessEventBuilder(order, eventName));
			if (eventBuilder != null)
			{
				final AmwayEventData eventData = eventBuilder.build();
				
				if(eventData.getEventPayload().getConsignments()==null)
				{
				eventData.getEventPayload().setConsignments(consignments);
				
				println eventData.getEventPayload().getConsignments();
				}
				IndOrderPayloadData indOrderPayloadData = eventData.getEventPayload();
                if (indOrderPayloadData.getPaymentTransactions() == null || indOrderPayloadData.getPaymentTransactions().isEmpty()) {
                    LOG.error("Payment Transactions Null for Order :" + orderModel.getCode());
                    payloadUtil.setPaymentTransactions(indOrderPayloadData, orderModel);
                }
                for (IndConsignmentPayloadData consignmentData : indOrderPayloadData.getConsignments()) {
              
                    try {
                        final ConsignmentModel consignment = orderModel.getConsignments().stream().filter(s -> StringUtils.equalsIgnoreCase(s.getCode(), consignmentData.getCode())).findAny().orElseThrow(Exception::new);

                        if (StringUtils.isBlank(consignmentData.getShipmentType())) {
                           
                            payloadUtil.setShipmentType(consignmentData, consignment);
                        }
                        if (consignmentData.getCarrierDetails() == null) {
            
                            payloadUtil.setCarrierDetails(consignmentData, consignment);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }				
				eventData.getEventPayload().setChildOrders(childOrders);
				eventData.getEventPayload().setSubTotal(subTotal);
				eventData.getEventPayload().setSuborderIDs(suborderIDs)
				amwayEventQueueService.publishEvent(eventData);
			}
			println "event genrated for order===> "+order.getCode(); 
		
		}
		catch(Exception e)
		{
		e.printStackTrace();
		continue;
		}
		
		}
		}
		baseSiteService.setCurrentBaseSite(baseSiteModel, true);