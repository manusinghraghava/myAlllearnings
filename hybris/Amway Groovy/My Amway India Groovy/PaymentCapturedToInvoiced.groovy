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


		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final EventService eventService = (EventService) Registry.getApplicationContext().getBean("eventService");
		final BusinessProcessService businessProcessService   = (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");

		String query = "select {o.pk} from {Order as o} where {o.status} = ({{select {os.pk} from {OrderStatus as os} where {os.code} = 'PAYMENT_CAPTURED'}}) and {o.code}  IN({{select {key} from {AmwayEventStagingEntry} where {eventName} = 8796131033179 and {creationTime} >= TIMESTAMP ('2020-01-23 12:05:00', 'YYYY-MM-DD HH24:MI:SS')}})";
		
		final SearchResult<OrderModel> searchResult = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query);

		final List<OrderModel> results = searchResult.getResult();

		if (CollectionUtils.isNotEmpty(results))
			{
			for (final OrderModel order : results) 
			{
				if(CollectionUtils.isNotEmpty(order.getOrderProcess()) && CollectionUtils.isNotEmpty(order.getAmwayInvoices()))
				{
					
				order.setStatus(OrderStatus.INVOICED);
				modelService.save(order);
				
				if(order.isGroupOrder()){
					println "In isgrouporder";
					final List<AbstractOrderModel> subOrders = order.getLynxSubcarts();
					if (CollectionUtils.isNotEmpty(subOrders))
					{
						for (final AbstractOrderModel subOrder : subOrders)
						{
							subOrder.setStatus(OrderStatus.INVOICED);
						}
						
						modelService.saveAll(subOrders);
					}
				}
				
				}
				println "Status Changed to Invoiced for Order"+order.getCode()+"...";
			}
			}
			

			