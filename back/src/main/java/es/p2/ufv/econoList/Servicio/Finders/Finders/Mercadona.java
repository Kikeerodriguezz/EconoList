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

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Order(Orden.MERCADONA)
public class Mercadona extends Abstracto implements Finder
{
    private final Logger logger = LoggerFactory.getLogger(Mercadona.class);

    private final String marketUri =
            "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/products_prod_4315_es/query?x-algolia-application-id=7UZJKL1DJ0&x-algolia-api-key"
                    + "=9d8f2e39e90df472b4f2e559a116fe17";

    @Override
    public Market getMarket()
    {
        return Market.MERCADONA;
    }

    protected String getMarketUri()
    {
        return this.marketUri;
    }

    protected HttpRequest.BodyPublisher getBodyPost(final String term)
    {
        return HttpRequest.BodyPublishers.ofString(
                "{\"params\":\"query=" + term + "&clickAnalytics=true&analyticsTags=%5B%22web%22%5D&getRankingInfo=true\"}");
    }

    protected HttpMethod getHttpMethod()
    {
        return HttpMethod.POST;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj)
    {
        final List<Product> productList = new ArrayList<Product>();
        final JsonArray productsJsonList = responseJsonObj.get("hits").getAsJsonArray();

        if (productsJsonList != null)
        {
            for (final JsonElement productJson : productsJsonList)
            {
                final JsonObject productObj = productJson.getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.MERCADONA);
                product.setBrand("-");

                final JsonObject priceObj = productObj.get("price_instructions").getAsJsonObject();

                product.setPrice((priceObj).get("unit_price").getAsFloat());

                try {
                    if (priceObj.get("reference_price") != null && priceObj.get("reference_format") != null) {
                        final String price = priceObj.get("reference_price").getAsString().replace(".", ",");
                        final String unit = priceObj.get("reference_format").getAsString();
                        product.setPriceUnitOrKg(String.format("%s €/%s", price, unit));
                    }
                } catch (final Exception e) {
                    this.logger.error("Mercadona get product unitPrice error", e);
                }

                product.setName(productObj.get("display_name").getAsString());

                final String imagePath = productObj.get("thumbnail").getAsString();
                if (StringUtils.isNotBlank(imagePath)) {
                    product.setImage(imagePath);
                }

                productList.add(product);
            }
        }

        return productList;
    }
}