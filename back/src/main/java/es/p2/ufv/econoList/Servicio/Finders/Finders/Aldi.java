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

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Order(Orden.ALDI)
public class Aldi extends Abstracto implements Finder {

    private final String marketUri =
            "https://l9knu74io7-dsn.algolia.net/1/indexes/*/queries?X-Algolia-Api-Key=19b0e28f08344395447c7bdeea32da58&X-Algolia-Application-Id"
                    + "=L9KNU74IO7";

    protected String getMarketUri() {
        return this.marketUri;
    }

    protected HttpRequest.BodyPublisher getBodyPost(final String term) {
        return HttpRequest.BodyPublishers.ofString(
                "{\"requests\":[{\"indexName\":\"prod_es_es_es_offers\","
                        + "\"params\":\"clickAnalytics=true&facets=%5B%5D&highlightPostTag=%3C%2Fais-highlight-0000000000%3E&highlightPreTag=%3Cais"
                        + "-highlight-0000000000%3E&hitsPerPage=12&page=0&query="
                        + term
                        + "&tagFilters=\"},{\"indexName\":\"prod_es_es_es_assortment\","
                        + "\"params\":\"clickAnalytics=true&facets=%5B%5D&highlightPostTag=%3C%2Fais-highlight-0000000000%3E&highlightPreTag=%3Cais"
                        + "-highlight-0000000000%3E&hitsPerPage=12&page=0&query="
                        + term + "&tagFilters=\"}]}");
    }

    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj) {
        final List<Product> productList = new ArrayList<>();

        final JsonArray resultList = responseJsonObj.get("results").getAsJsonArray();
        for (final JsonElement result : resultList) {
            final JsonArray productsJsonList = result.getAsJsonObject().get("hits").getAsJsonArray();

            if (productsJsonList != null) {
                for (final JsonElement productJson : productsJsonList) {
                    final JsonObject productObj = productJson.getAsJsonObject();
                    final Product product = new Product();
                    product.setMarket(Market.ALDI);
                    product.setBrand("-");
                    if (productObj.get("salesPrice") != null) {
                        product.setPrice(productObj.get("salesPrice").getAsFloat());
                        product.setName(productObj.get("productName").getAsString());

                        final String imagePath = productObj.get("productPicture").getAsString();
                        if (!StringUtils.isBlank(imagePath)) {
                            product.setImage(imagePath);
                        }

                        productList.add(product);
                    }
                }
            }
        }

        return productList;
    }

    @Override
    public Market getMarket() {
        return Market.ALDI;
    }
}