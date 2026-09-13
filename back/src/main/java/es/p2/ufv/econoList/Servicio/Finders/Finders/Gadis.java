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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Order(Orden.GADIS)
public class Gadis extends Abstracto implements Finder {
    private final Logger logger = LoggerFactory.getLogger(Gadis.class);

    private final String marketUri = "https://www.gadisline.com/content/themes/gadislineth/view/functions/ajax_apiCinfo.php";
    private final String imageUri = "https://www.gadisline.com";
    private final String sessionUri = "https://www.gadisline.com";

    private String iSessionId = null;
    private LocalDateTime lastSessionDate = LocalDateTime.now();

    @Override
    public Market getMarket() {
        return Market.GADIS;
    }

    protected String getMarketUri() {
        return this.marketUri;
    }

    private void getSession() {
        final LocalDateTime now = LocalDateTime.now();
        final Duration duration = Duration.between(now, this.lastSessionDate);
        final long diff = Math.abs(duration.toMinutes());

        if (diff > 30 || this.iSessionId == null) {
            try {
                final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("resource=postalCode&cl_lang=es&cl_postal_code=15001");

                final HttpRequest request = HttpRequest.newBuilder().uri(new URI(this.marketUri))
                        .timeout(Duration.ofSeconds(10)).POST(body).header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").build();

                final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                        HttpResponse.BodyHandlers.ofString());

                this.iSessionId = response.headers().firstValue("Set-Cookie").get();
                this.lastSessionDate = LocalDateTime.now();

            } catch (final URISyntaxException | IOException | InterruptedException e) {
                this.logger.error("Gadis get sessionId error", e);
            }
        }
    }

    protected HttpRequest.BodyPublisher getBodyPost(final String term) {
        return HttpRequest.BodyPublishers.ofString(
                "resource=productsListInfiniteScroll&lang=es&currentPostalCode=15010&currentUserId=&checksBrandsFilter=&checksPropertiesFilters"
                        + "=&productsListFilterSearch=&productsPage=0&orderProducts=&templateName=&isSearch=true&searchData="
                        + URLEncoder.encode(term, StandardCharsets.UTF_8));
    }

    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    protected HttpRequest.Builder addHeaders(final HttpRequest.Builder requestBuilder) {
        this.getSession();
        requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestBuilder.setHeader("Cookie", this.iSessionId
                + "; melindres_options={%22required%22:true%2C%22analytics%22:true}; _gid=GA1.2.1207255210.1695080623; _dc_gtm_UA-68572192-1=1; "
                + "_dc_gtm_UA-68572192-2=1; _ga_FN6H4R59BJ=GS1.1.1695080622.1.1.1695080715.0.0.0; _ga_X30YGGL58X=GS1.2.1695080623.1.1.1695080715"
                + ".60.0.0; _ga=GA1.2.844044833.1695080623; _gat_UA-68572192-2=1; _ga_D36JQJ7Q0R=GS1.2.1695080623.1.1.1695080715.0.0.0");

        return requestBuilder;
    }

    protected List<Product> getProductList(final JsonObject responseJsonObj) {
        final List<Product> productList = new ArrayList<>();

        final JsonArray productsJsonList = responseJsonObj.get("dato").getAsJsonObject().get("productos").getAsJsonArray();

        if (productsJsonList != null) {
            for (final JsonElement productJson : productsJsonList) {
                final JsonObject productObj = productJson.getAsJsonObject();
                final Product product = new Product();
                product.setMarket(Market.GADIS);
                product.setBrand(productObj.get("marca").getAsString());
                product.setPrice(productObj.get("precio").getAsFloat());

                if (productObj.get("precioUnidad") != null && productObj.get("medidaTxt") != null && StringUtils.isNotBlank(productObj.get("medidaTxt").getAsString())) {
                    final String price = productObj.get("precioUnidad").getAsString().replace(".", ",");
                    final String unit = productObj.get("medidaTxt").getAsString();
                    product.setPriceUnitOrKg(String.format("%s €/%s", price, unit));
                }
                product.setName(productObj.get("descripcionLarga").getAsString());

                final JsonArray imagesList = productObj.get("imagenes").getAsJsonArray();

                if (imagesList != null && !imagesList.isEmpty()) {
                    String imagePath = imagesList.get(0).getAsJsonObject().get("path").getAsString();
                    if (!StringUtils.isBlank(imagePath)) {
                        imagePath = imagePath.replace("/var/www/html", this.imageUri);
                        product.setImage(imagePath);
                    }
                }
                productList.add(product);
            }
        }
        return productList;
    }
}