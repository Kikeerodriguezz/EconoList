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
@Order(Orden.DIA)
public class Dia extends Abstracto implements Finder
{
    private final String marketUri = "https://www.dia.es/api/v1/search-back/search/reduced?q=%s&page=1";
    private final String imageHost = "https://www.dia.es";

    @Override
    public Market getMarket()
    {
        return Market.DIA;
    }

    protected String getMarketUri()
    {
        return this.marketUri;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj)
    {
        final List<Product> productList = new ArrayList<Product>();

        final JsonArray productsJsonList = responseJsonObj.get("search_items").getAsJsonArray();

        if (productsJsonList != null)
        {
            for (final JsonElement productJson : productsJsonList)
            {
                final JsonObject productObj = productJson.getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.DIA);
                product.setBrand("-");
                final JsonObject pricesObj = productObj.get("prices").getAsJsonObject();
                product.setPrice(pricesObj.get("price").getAsFloat());

                if (pricesObj.get("price_per_unit") != null) {
                    product.setPriceUnitOrKg(String.format("%s €/ %s", pricesObj.get("price_per_unit").getAsString().replace(".", ","), pricesObj.get("measure_unit").getAsString()));
                }

                product.setName(productObj.get("display_name").getAsString());

                final String imagePath = productObj.get("image").getAsString();
                if (!StringUtils.isBlank(imagePath)) {
                    product.setImage(StringUtils.join(this.imageHost, imagePath));
                }

                productList.add(product);
            }
        }
        return productList;
    }
}