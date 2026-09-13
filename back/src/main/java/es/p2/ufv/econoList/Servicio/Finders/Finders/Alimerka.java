package es.p2.ufv.econoList.Servicio.Finders.Finders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import es.p2.ufv.econoList.Modelo.Market;
import es.p2.ufv.econoList.Modelo.Product;
import es.p2.ufv.econoList.Servicio.Finders.Abstracto;
import es.p2.ufv.econoList.Servicio.Finders.Finder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Order(Orden.ALIMERKA)
public class Alimerka extends Abstracto implements Finder {

    private final Logger logger = LoggerFactory.getLogger(Alimerka.class);

    private final String marketUri = "https://www.alimerkaonline.es/ali-ws/tienda/busqueda/%s/false/1/1000/0";
    private final String tokenUri = "https://www.alimerkaonline.es/ali-ws/acceso/cp/33402";
    private final String imageHost = "https://storage.googleapis.com/storage.alimerka.es/recursos";

    private String iauthtoken = null;
    private LocalDateTime lastTokenDate = LocalDateTime.now();

    @Override
    public Market getMarket() {
        return Market.ALIMERKA;
    }

    protected String getMarketUri() {
        return this.marketUri + "?iauthtoken=" + this.getToken();
    }

    private String getToken() {
        final LocalDateTime now = LocalDateTime.now();
        final Duration duration = Duration.between(now, this.lastTokenDate);
        final long diff = Math.abs(duration.toMinutes());

        if (diff > 5 || this.iauthtoken == null) {
            try {
                final HttpRequest request = HttpRequest.newBuilder().uri(new URI(this.tokenUri))
                        .timeout(Duration.ofSeconds(10)).POST(BodyPublishers.noBody()).build();

                final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                        BodyHandlers.ofString());

                final JsonObject responseJsonObj = new Gson().fromJson(response.body(), JsonObject.class);

                this.iauthtoken = responseJsonObj.get("iauthtoken").getAsString();
                this.lastTokenDate = LocalDateTime.now();

            } catch (final URISyntaxException | IOException | InterruptedException e) {
                this.logger.error("Alimerka get token error", e);
            }
        }

        return this.iauthtoken;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj) {
        final List<Product> productList = new ArrayList<>();
        final JsonArray productsJsonList = responseJsonObj.get("listPage").getAsJsonArray();

        int count = 0;

        if (productsJsonList != null) {
            for (final JsonElement productJson : productsJsonList) {
                final JsonObject productObj = productJson.getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.ALIMERKA);
                product.setBrand("-");
                product.setPrice(Float.valueOf(productObj.get("pvpnormal").getAsString().replace(",", ".")));
                try {
                    final JsonElement unidad = productObj.get("unidad");
                    if (unidad != null) {
                        if ("GR.".equals(unidad.getAsString())) {
                            final String price = productObj.get("pvp").getAsString();
                            product.setPriceUnitOrKg(StringUtils.join(price, " €/Kg"));
                        } else if ("Unidad".equals(unidad.getAsString()) && productObj.get("textounidad") != null) {
                            final String unitPriceStr = productObj.get("textounidad").getAsString();
                            final String unitPrice = unitPriceStr.split(StringUtils.SPACE)[3].replace(",", ".");
                            if (NumberUtils.isCreatable(unitPrice) && !product.getPrice().equals(Float.valueOf(unitPrice))) {
                                product.setPriceUnitOrKg(StringUtils.join(unitPriceStr.split(StringUtils.SPACE)[3], " €/Unidad"));
                            }
                        }
                    }
                } catch (final Exception e) {
                    this.logger.error("Alimerka get product unitPrice error", e);
                }

                product.setName(productObj.get("descripcion").getAsString());

                final String imagePath = productObj.get("urlimagen") != null ? productObj.get("urlimagen").getAsString() : StringUtils.EMPTY;
                if (!StringUtils.isBlank(imagePath)) {
                    product.setImage(StringUtils.join(this.imageHost, imagePath));
                }

                productList.add(product);

                count++;

                if (count > 20) {
                    break;
                }
            }
        }

        return productList;
    }

    protected String preProcessResponse(final String responseString) {
        String response = responseString.replace("fn(", "");
        response = response.substring(0, response.length() - 1);

        return response;
    }
}