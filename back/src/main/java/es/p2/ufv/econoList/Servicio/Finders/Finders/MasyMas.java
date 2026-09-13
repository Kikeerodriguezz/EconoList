package es.p2.ufv.econoList.Servicio.Finders.Finders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import es.p2.ufv.econoList.Modelo.Market;
import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Servicio.Finders.Abstracto;
import es.p2.ufv.econoList.Servicio.Finders.Finder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Order(Orden.MASYMAS)
public class MasyMas extends Abstracto implements Finder
{
    /** The logger. */
    // private final Logger logger = LoggerFactory.getLogger(MasymasFinder.class);

    private final String marketUri = "https://www.supermasymasonline.com/listado_PDO.php?buscar=%s";

    private final String imageHost ="https://masymas-services.supermasymas.com/fotos/";


    @Override
    public Market getMarket()
    {
        return Market.MASYMAS;
    }

    protected String getMarketUri()
    {
        return this.marketUri;
    }

    protected List<Product> getProductList(JsonObject responseJsonObj)
    {
        List<Product> productList = new ArrayList<Product>();

        if (responseJsonObj != null)
        {
            final JsonArray productsJsonList = (JsonArray) responseJsonObj.get("list");

            int count = 0;

            if (productsJsonList != null)
            {
                for (JsonElement productJson : productsJsonList)
                {
                    JsonObject productObj = (JsonObject) ((JsonObject) productJson);
                    Product product = new Product();
                    product.setMarket(Market.MASYMAS);
                    product.setBrand(productObj.get("brand").getAsString());
                    product.setPrice(productObj.get("price").getAsFloat());
                    product.setName(productObj.get("name").getAsString());

                    productList.add(product);

                    count++;

                    if (count > 20)
                    {
                        break;
                    }
                }
            }
        }
        return productList;
    }

    protected String preProcessResponse(String responseString)
    {
        String responseStr = responseString;
        int startJsonPos = responseStr.indexOf("listado_products");
        if (startJsonPos < 0)
        {
            return "";
        }

        responseStr = responseStr.substring(startJsonPos);
        int endJsonPos = responseStr.indexOf("filter_secciones_menu_desktop");
        responseStr = responseStr.substring(0, endJsonPos);
        // responseStr = responseStr.replace("\n", "").replace("\r", "");

        return responseStr;

    }

    @Override
    protected List<Product> postProcessResponse(String responseStr)
    {
        List<Product> productList = new ArrayList<Product>();
        Pattern p = Pattern.compile("((nombre_.*\">).*(<\\/s))|((item_price\">))([^<]*)<");
        Matcher m = p.matcher(responseStr);

        while (m.find())
        {
            Product product = new Product();

            String name = m.group(0);
            int majorSimbol = name.indexOf('>');
            int minorSimbol = name.indexOf('<');

            String id = name.substring(name.indexOf('_')+1,majorSimbol-1);

            product.setImage(String.format("%s%s%s", imageHost, id, ".jpg"));

            name = name.substring(majorSimbol + 1, minorSimbol);
            product.setName(name);


            m.find();

            String price = m.group(0);
            majorSimbol = price.indexOf('>');
            minorSimbol = price.indexOf('<');

            price = price.substring(majorSimbol + 1, minorSimbol);
            product.setPrice(Float.valueOf(price));

            product.setMarket(Market.MASYMAS);

            productList.add(product);
        }
        return productList;
    }
}