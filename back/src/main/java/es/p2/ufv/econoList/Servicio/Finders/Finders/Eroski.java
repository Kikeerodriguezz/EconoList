package es.p2.ufv.econoList.Servicio.Finders.Finders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import es.p2.ufv.econoList.Modelo.Market;
import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Servicio.Finders.Abstracto;
import es.p2.ufv.econoList.Servicio.Finders.Finder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Order(Orden.EROSKI)
public class Eroski extends Abstracto implements Finder
{
    // private final Logger logger = LoggerFactory.getLogger(EroskiFinder.class);
    private final String marketUri = "https://supermercado.eroski.es/es/search/results/?q=%s&suggestionsFilter=false";

    private final String imageHost = "https://supermercado.eroski.es/images/";

    @Override
    public Market getMarket()
    {
        return Market.EROSKI;
    }

    protected String getMarketUri()
    {
        return this.marketUri;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj)
    {
        final List<Product> productList = new ArrayList<Product>();

        if (responseJsonObj != null)
        {
            final JsonArray productsJsonList = responseJsonObj.get("list").getAsJsonArray();

            int count = 0;

            if (productsJsonList != null)
            {
                for (final JsonElement productJson : productsJsonList) {
                    final JsonObject productObj = productJson.getAsJsonObject();
                    final Product product = new Product();
                    product.setMarket(Market.EROSKI);
                    product.setBrand(productObj.get("brand").getAsString());
                    product.setPrice(productObj.get("price").getAsFloat());
                    product.setName(productObj.get("name").getAsString());

                    final String id = productObj.get("id") != null ? productObj.get("id").getAsString() : StringUtils.EMPTY;
                    if (!StringUtils.isBlank(id)) {
                        product.setImage(String.format("%s%s%s", this.imageHost, id, ".jpg"));
                    }

                    productList.add(product);

                    count++;

                    if (count > 20) {
                        break;
                    }
                }
            }
        }

        return productList;
    }

    protected String preProcessResponse(final String responseString) {

        String responseStr = responseString;
        final int startJsonPos = responseString.indexOf("impressions");
        if (startJsonPos < 0) {
            return "";
        }
        responseStr = responseString.substring("impressions".length() + 3);
        responseStr = responseStr.substring(startJsonPos);
        final int endJsonPos = responseStr.indexOf("]");
        responseStr = responseStr.substring(0, endJsonPos + 1);
        responseStr = responseStr.replace("\\", "");

        return "{\"list\":" + responseStr + "}";

    }
}