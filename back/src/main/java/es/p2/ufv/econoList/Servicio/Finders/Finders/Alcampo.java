package es.p2.ufv.econoList.Servicio.Finders.Finders;

import com.google.gson.JsonArray;
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
@Order(Orden.ALCAMPO)
public class Alcampo extends Abstracto implements Finder
{
    private final String marketUri =
            "https://www.compraonline.alcampo.es/api/v5/products/search?limit=50&offset=0&sort=price&term=%s";

    private final String imageHost = "https://www.dia.es";

    @Override
    public Market getMarket()
    {
        return Market.ALCAMPO;
    }

    protected String getMarketUri()
    {
        return this.marketUri;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj)
    {
        final List<Product> productList = new ArrayList<>();

        final JsonObject productsJsonList = responseJsonObj.get("entities").getAsJsonObject().get("product").getAsJsonObject();

        if (productsJsonList != null)
        {
            for (final String productKey : productsJsonList.keySet())
            {
                final JsonObject productObj = productsJsonList.get(productKey).getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.ALCAMPO);
                product.setBrand(productObj.get("brand") != null ? productObj.get("brand").getAsString() : "");
                product.setName(productObj.get("name").getAsString());

                final JsonObject price = productObj.getAsJsonObject().get("price").getAsJsonObject();

                product.setPrice(price.getAsJsonObject().get("current").getAsJsonObject().get("amount").getAsFloat());

                if (price.get("unit") != null)
                {
                    String label = price.get("unit").getAsJsonObject().get("label").getAsString();
                    label = label.substring(label.lastIndexOf(".") + 1);

                    label = String.format("%s €/ %s",
                            price.get("unit").getAsJsonObject().get("current").getAsJsonObject().get("amount").getAsString().replace(".", ","),
                            label);
                    label = label.replace("litre", "Litro").replace("each", "unidad");
                    product.setPriceUnitOrKg(label);
                }

                final JsonArray media = productObj.get("imagePaths").getAsJsonArray();
                if (media != null && !media.isEmpty())
                {
                    product.setImage(StringUtils.join(media.get(0).getAsString(), "/300x300.jpg"));
                }
                productList.add(product);
            }
        }
        return productList;
    }
}