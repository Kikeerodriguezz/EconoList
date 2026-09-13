package es.p2.ufv.econoList.Servicio.Finders.Finders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import es.p2.ufv.econoList.Modelo.Market;
import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Servicio.Finders.Abstracto;
import es.p2.ufv.econoList.Servicio.Finders.Finder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Order(Orden.HIPERCOR)
public class Hipercor extends Abstracto implements Finder {
    private final Logger logger = LoggerFactory.getLogger(Hipercor.class);

    private final String marketUri =
            "https://www.hipercor.es/alimentacion/api/catalog/supermercado/type_ahead/?question=%s&scope=supermarket&center=010MOH&results=10";

    private final String imageHost = "https:";

    @Override
    public Market getMarket() {
        return Market.HIPERCOR;
    }

    protected String getMarketUri() {
        return this.marketUri;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj) {
        final List<Product> productList = new ArrayList<>();

        final JsonArray productsJsonList = responseJsonObj
                .get("catalog_result").getAsJsonObject().get("products_list").getAsJsonObject().get("items").getAsJsonArray();

        if (productsJsonList != null) {
            for (final JsonElement productJson : productsJsonList) {
                final JsonObject productObj = productJson.getAsJsonObject().get("product").getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.HIPERCOR);
                product.setBrand("-");

                final JsonObject priceObj = productObj.get("price").getAsJsonObject();

                product.setPrice(priceObj.get("seo_price").getAsFloat());
                try {
                    if (priceObj.get("pum_price_only") != null) {
                        product.setPriceUnitOrKg(priceObj.get("pum_price_only").getAsString().replace("&euro; ", "€"));
                    } else if (priceObj.get("pum_price") != null) {
                        product.setPriceUnitOrKg(priceObj.get("pum_price").getAsString().replace("&euro; ", "€"));
                    }
                } catch (final Exception e) {
                    this.logger.error("Hipercor get product unitPrice error", e);
                }

                product.setName(productObj.get("name").getAsString());

                String imagePath = productObj.get("media").getAsJsonObject().get("thumbnail_url").getAsString();
                if (!StringUtils.isBlank(imagePath)) {
                    imagePath = imagePath.replace("40x40", "325x325");
                    product.setImage(StringUtils.join(this.imageHost, imagePath));
                }
                productList.add(product);
            }
        }
        return productList;
    }
}