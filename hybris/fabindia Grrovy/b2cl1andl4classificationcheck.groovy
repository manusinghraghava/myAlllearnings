import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.core.model.product.*;
import de.hybris.platform.variants.model.*;
import de.hybris.platform.europe1.model.*;
import de.hybris.platform.catalog.enums.*;
import org.apache.commons.lang3.*;
import java.util.*;
import java.util.stream.*;

String vpQuery = "select {pk} from {Product as vp join catalogVersion as cv on {vp.catalogVersion}={cv.pk} join Unit as u on {vp.unit}={u.pk}} where {cv.version} = 'ERP_IMPORT'";

final SearchResult<ProductModel> vpSearchResult = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(vpQuery);
final List<ProductModel> vpResults = vpSearchResult.getResult();

def vpCount = 0;
if(CollectionUtils.isNotEmpty(vpResults))
{
for(ProductModel vProduct :vpResults)
{

if(vProduct.getApprovalStatus().getCode().equalsIgnoreCase("approved"))
{
print vProduct.getCode()+';';
  vProduct.getFeatures().stream().map{t->t.getClassificationAttributeAssignment().getClassificationAttribute()}.filter{p->StringUtils.containsIgnoreCase(p.getCode(),'B2CL')}.forEach{k->print k.getCode()+" ; "};

  println " "
}
}
}
