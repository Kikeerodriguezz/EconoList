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
@Order(Orden.CARREFOUR)
public class Carrefour extends Abstracto implements Finder {

    private final String marketUri =
            "https://www.carrefour.es/search-api/query/v1/search?query=%s&scope=desktop&lang=es&rows=24&start=0&origin=default&f.op=OR";

    @Override
    public Market getMarket() {
        return Market.CARREFOUR;
    }

    protected String getMarketUri() {
        return this.marketUri;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj) {
        final List<Product> productList = new ArrayList<>();

        final JsonArray productsJsonList = responseJsonObj.get("content").getAsJsonObject().get("docs").getAsJsonArray();

        if (productsJsonList != null) {
            for (final JsonElement productJson : productsJsonList) {
                final JsonObject productObj = productJson.getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.CARREFOUR);
                product.setBrand(productObj.get("brand") == null ? "-" : productObj.get("brand").getAsString());
                product.setPrice(productObj.get("active_price").getAsFloat());

                if (productObj.get("price_per_unit_text") != null) {
                    product.setPriceUnitOrKg(productObj.get("price_per_unit_text").getAsString());
                }

                product.setName(productObj.get("display_name").getAsString());
                product.setImage(productObj.get("image_path") != null ? productObj.get("image_path").getAsString() : StringUtils.EMPTY);
                productList.add(product);
            }
        }
        return productList;
    }
}