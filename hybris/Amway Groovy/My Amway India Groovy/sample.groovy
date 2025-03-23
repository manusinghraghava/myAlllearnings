import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.Collection;
import java.util.ArrayList;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import de.hybris.platform.servicelayer.session.SessionService;

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
import om.amway.indfacades.product.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.amway.core.model.AmwayAccountModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.hybris.platform.commercefacades.product.*;
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
import com.amway.indfacades.product.IndProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;

def catalog = "indProductCatalog";
def catalogVersion = "Online";
def userService = spring.getBean('userService');



baseSiteService = spring.getBean('baseSiteService');
catalogVersionService = spring.getBean('catalogVersionService');

String query1="select {pk} from {CMSSite as o} where {o.pk} ='"+8796093056040+"'";
final SearchResult<CMSSiteModel> searchResult1 = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query1);
final List<CMSSiteModel> results1 = searchResult1.getResult();
CMSSiteModel cmsSiteModel = results1.stream().findFirst().get();


def baseSiteModel =baseSiteService.getCurrentBaseSite();
baseSiteService.setCurrentBaseSite(cmsSiteModel, true);
searchRestrictionService = spring.getBean('searchRestrictionService');
fss = spring.getBean('flexibleSearchService');

sessionService.setAttribute("currentSite", baseSiteService.getBaseSiteForUID('indsite'));

sessionService.executeInLocalView(new SessionExecutionBody()
{
	@Override
	public Object execute()
	{
		try
		{
			CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalog, catalogVersion);
			CatalogModel catalogModel = catalogService.getCatalogForId(catalog);
			Collection<CatalogVersionModel> catalogVersions = new ArrayList<CatalogVersionModel>();
			catalogVersions.add(catalogVersionModel);
			sessionService.setAttribute("catalogversions", catalogVersions);
			final List<ProductOption> extraOptions = Arrays.asList(ProductOption.VARIANT_MATRIX_BASE, ProductOption.VARIANT_MATRIX_URL,
				ProductOption.VARIANT_MATRIX_MEDIA, ProductOption.PRICE, ProductOption.ORDERABLE,
				ProductOption.KEYWORDS, ProductOption.DESCRIPTION, ProductOption.TNA_SUBSCRIPTION);
			
			String query2="select {pk} from {Product as o} where {o.code} ='282128ID'";
final SearchResult<ProductModel> searchResult2 = ((FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService")).search(query2);
final List<ProductModel> results12= searchResult2.getResult();
ProductModel product = results12.stream().findFirst().get();

try{
ProductData productData = lynxProductFacade.getProductForAliasOrCode(product.getCode(), extraOptions);
}
catch(Exception e)
{
e.printStackTrace();

}

		}
		finally
		{
			searchRestrictionService.enableSearchRestrictions();
			baseSiteService.setCurrentBaseSite(baseSiteModel, true);
		}
	}
},userService.getUserForUID("2a0a9d37-1243-45b1-bab1-d253940f35b0"));