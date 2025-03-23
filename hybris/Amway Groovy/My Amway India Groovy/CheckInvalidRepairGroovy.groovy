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
import de.hybris.platform.core.model.*;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.processengine.BusinessProcessService;
import org.apache.commons.lang.*;
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
import java.text.SimpleDateFormat; 
import java.util.*;
import de.hybris.platform.orderprocessing.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.amway.core.model.AmwayAccountModel;

import de.hybris.platform.processengine.model.BusinessProcessModel;


final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
final EventService eventService = (EventService) Registry.getApplicationContext().getBean("eventService");
final BusinessProcessService businessProcessService = (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
Date currentDate = new Date();
Calendar cal = Calendar.getInstance();
cal.setTime(currentDate);
//change this for fetching order on different time
cal.add(Calendar.DATE, -1);
Date previouDay = cal.getTime();
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String strDate= formatter.format(previouDay);

String query = "select {op.pk} from {OrderProcess as op}  where {op.creationTime} >= '"+strDate+"'and {op.processDefinitionName} = 'order-process-version2' and {op.state} IN ('8796102459483','8796102492251')";

println query;

final SearchResult<OrderProcessModel> searchResult = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query);

final String CHECK_ORDER = "checkOrder";
final String GENERATE_IRN = "generateIRN";

final List<OrderProcessModel> results = searchResult.getResult();

println results.size();

if (CollectionUtils.isNotEmpty(results))
{
for (final OrderProcessModel process : results) 
{
try{
def order =process.getOrder();

String taskName ="";

def taskLog=process.getTaskLogs().stream().sorted(Comparator.comparing({s->s.getModifiedtime()}).reversed()).findFirst();

if(taskLog.isPresent())
{
taskName = taskLog.get().getActionId();
}

if(StringUtils.equalsIgnoreCase(taskName,CHECK_ORDER)){
if(CollectionUtils.isNotEmpty(order.getOrderProcess()) && CollectionUtils.isNotEmpty(order.getAmwayInvoices())&& CollectionUtils.isNotEmpty(order.getConsignments())&& order.getOrderPeriod()!=null && order.getBonusPeriod()!=null && order.getPaymentStatus().getCode().equalsIgnoreCase("PAID")){

order.setStatus(OrderStatus.INVOICED);
modelService.save(order); 

if(order.isGroupOrder()){

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

order.getOrderProcess().stream().filter{process1 -> process1.getCode().startsWith(order.getStore().getSubmitOrderProcessCode())}.forEach{orderProcess -> businessProcessService.restartProcess(orderProcess,CHECK_ORDER)};

}
}
else if(StringUtils.equalsIgnoreCase(taskName,GENERATE_IRN)){
def customerTaxDetail = order.getAccount().getPrimaryParty().getTaxCertDetails().stream().filter({e -> Objects.nonNull((e.getTaxType()))&& TaxTypeEnum.GSTIN.getCode().equalsIgnoreCase(e.getTaxType().getCode())}).findFirst();
if (!(customerTaxDetail.isPresent() && (order.getDeliveryAddress().getRegion().getGstStateCode().equalsIgnoreCase(customerTaxDetail.get().getCustomer().getDefaultPaymentAddress().getRegion().getGstStateCode()))))
{

 println "GENERATE_IRN Repair order "+order.getCode();
order.getOrderProcess().stream().filter{process1 -> process1.getCode().startsWith(order.getStore().getSubmitOrderProcessCode())}.forEach{orderProcess -> businessProcessService.restartProcess(orderProcess,GENERATE_IRN)};
}
}
}
catch(Exception e)
{

continue;
}
}
}