import de.hybris.platform.core.model.LynxPromotionApplicationRecordModel;
import java.util.*;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;  

Logger LOG = Logger.getLogger("")
Date currentDate = new Date();
Calendar cal = Calendar.getInstance();
cal.setTime(currentDate);
//change this for fetching order on different time
cal.add(Calendar.HOUR, -3);
Date twoHourBack = cal.getTime();
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
String strDate= formatter.format(twoHourBack);  
try
{
String query = "SELECT {pk} from {LynxPromotionApplicationRecord as pa} where {pa.order} IN ({{SELECT {pk} from {Order as o JOIN OrderStatus as os on {os.pk} = {o.status}} where {o.date} >= TIMESTAMP ('"+strDate+"', 'YYYY-MM-DD HH24:MI:SS') and {os.code} = 'PAYMENT_FAILED'}}) ";

def  promoApplications = flexibleSearchService.search(query).getResult();

if(promoApplications!=null){
println("############### Removed Promotion Appliations For  ###############" )
println("Order ~ User ~ Promotion Code ~ Promotion Name" )
promoApplications.each{		
		println(it.getOrder().getCode()+" ~ "+it.getUser().getName()+" ~ "+it.getRule().getCode()+" ~ "+it.getRule().getName());
		modelService.removeAll(it)
}
}
}
catch(Exception e)
{
LOG.error("Exception occured during removing Promotion", e);
}



==========

SELECT {pk} from {LynxPromotionApplicationRecord as pa} where {pa.order} IN ({{SELECT {pk} from {Order as o JOIN OrderStatus as os on {os.pk} = {o.status}} where {o.date} >= TIMESTAMP ('2020-02-17 06:39:25', 'YYYY-MM-DD HH24:MI:SS') and {os.code} = 'PAYMENT_FAILED'}})


Code for job==>removePromoApplicationScriptingCronjob