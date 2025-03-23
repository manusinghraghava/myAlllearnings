import org.apache.commons.lang3.StringUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.core.model.product.*;
import de.hybris.platform.europe1.model.*;
import de.hybris.platform.catalog.enums.*;
String query = "select {pk} from {product as p join catalogVersion as cv on {p.catalogVersion}={cv.pk} join Unit as u on {p.unit}={u.pk}} where {cv.version} = 'ERP_IMPORT'";

			
		final SearchResult<ProductModel> searchResult = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query);
final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService"); 
final List<ProductModel> results = searchResult.getResult();
println results.size();
if(CollectionUtils.isNotEmpty(results))
{
for(ProductModel product :results)
{

final List<PriceRowModel> prices = product.getEurope1Prices();
if(CollectionUtils.isNotEmpty(prices) )
{
def put = prices.stream().filter{s->s.getCurrency().getIsocode().equalsIgnoreCase('USD')}.findAny();
if(put.isPresent())
{
println product.getCode();
}
}
}
}