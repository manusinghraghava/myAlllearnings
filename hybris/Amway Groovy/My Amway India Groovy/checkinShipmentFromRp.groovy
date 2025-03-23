import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import com.amway.indcore.dto.*;
import com.amway.indcore.order.shipment.service.*;




final IndRPShipmentService indRPShipmentService=(IndRPShipmentService) Registry.getApplicationContext().getBean(
				"indRPShipmentService");
				
try{
def jsonSTring="{\"shipments\":[{\"shipmentID\":\"SIE008WWVZ\",\"carriercode\":\"153\",\"airwayBillNo\":\"76709543876\",\"warehouseCode\":\"80679\",\"noofboxes\":\"2\",\"packageDate\":\"2020-04-02T04:02:03.000-0500\",\"orderDropDate\":\"\",\"status\":\"Packaged\",\"weight\":\"120.008\",\"weightUnit\":\"kg\",\"consignment\":[{\"code\":\"702126212_c0\",\"consignmentEntries\":[{\"orderEntryCode\":0,\"shippedQuantity\":\"1\",\"productCode\":\"265002ID\",\"itemShipDetails\":[{\"revision\":\"01\",\"lot\":\"2019324AEZB\"}]},{\"orderEntryCode\":1,\"shippedQuantity\":\"1\",\"productCode\":\"291285ID\",\"itemShipDetails\":[{\"revision\":\"00\",\"lot\":\"2020093Y9VB\"}]}]}]}]}";



def orderId="702126212";
ObjectMapper mapper = new ObjectMapper();

IndShipmentListDTO shipmentListDTO = mapper.readValue(jsonSTring, IndShipmentListDTO.class);

 def isSuccess = indRPShipmentService.updateOrderRPShipmentDetails(orderId, shipmentListDTO);
 
 println "result===> "+isSuccess;

}
catch(Exception e)
{
e.printStackTrace();
}