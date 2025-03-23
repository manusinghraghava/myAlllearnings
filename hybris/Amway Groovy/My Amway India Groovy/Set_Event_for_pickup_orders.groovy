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
import com.amway.eventstream.enums.EventName;


final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");


String query="""SELECT {ese.pk}
	FROM {
			AmwayEventStagingEntry as ese 
        JOIN 
            Order as o on {o.code} like  CONCAT(SUBSTR({ese.key}, 1, 9), '%') 
	    } 
		WHERE
			{eventName} IN ('8796131033179') 
		AND
			{syncState} IN ('8796131000411','8796130967643','8796130934875') 
        AND 
			{o.deliveryMode}='8796093120552'
		AND
			{o.code} in ('713345049', '713615396', '712699831', '712730150', '712784387', '712841558', '712968366', '712985781', '713051232', '713437605', '713626819', '713651185', '713695162', '711726457', '711794979', '711835928', '711852023', '711934001', '711980324', '711988236', '712160723', '712185313', '712326558', '712368567', '712375293', '712498774', '712551480', '711446793', '711448213', '711506112', '711507985', '711522965', '711544507')""";
final PageableData pageableData = new PageableData();

pageableData.setCurrentPage(0);

pageableData.setPageSize(3000);

SearchPageData<AmwayEventStagingEntryModel> searchResult = Registry.getApplicationContext().getBean("pagedFlexibleSearchService").search(query.toString(), null, pageableData);

final List<AmwayEventStagingEntryModel> results = searchResult.getResults();

if(CollectionUtils.isNotEmpty(results))

{

	for (final AmwayEventStagingEntryModel amwayEventStagingEntry : results)
   {
		
		amwayEventStagingEntry.setEventName(EventName.ORDER_CREATED_INVOICED_SHIPPED);
		
		modelService.save(amwayEventStagingEntry);
		println amwayEventStagingEntry.getKey();
		

  }

}