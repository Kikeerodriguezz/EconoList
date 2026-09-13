package es.p2.ufv.econoList.Servicio.Finders;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import es.p2.ufv.econoList.Modelo.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class Abstracto
{
    private final Logger logger = LoggerFactory.getLogger(Abstracto.class);

    public List<Product> findProductsByTerm(String term)
    {
        List<Product> productList = new ArrayList<>();
        try
        {
            String uriTerm = URLEncoder.encode(term, StandardCharsets.UTF_8.toString());
            final HttpRequest request;
            if (HttpMethod.GET.equals(getHttpMethod()))
            {
                request = HttpRequest.newBuilder()
                        .uri(new URI(String.format(this.getMarketUri(), uriTerm)))
                        .timeout(Duration.ofSeconds(10))
                        .GET()
                        .build();
            }
            else
            {
                final BodyPublisher body = this.getBodyPost(term);
                Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(new URI(String.format(this.getMarketUri(), uriTerm)))
                        .timeout(Duration.ofSeconds(10))
                        .POST(body);
                this.addHeaders(requestBuilder);
                request = requestBuilder.build();
            }

            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
            String responseStr = preProcessResponse(response.body());
            productList = postProcessResponse(responseStr);
        }
        catch (Exception e)
        {
            logger.error("Market get Product exception", e);
        }
        return productList;
    }

    protected Builder addHeaders(Builder requestBuilder)
    {
        return requestBuilder;
    }

    protected String preProcessResponse(String responseString)
    {
        return responseString;
    }

    protected List<Product> postProcessResponse(String responseStr)
    {
        Gson gson = new Gson();
        System.out.println("Respuesta JSON: " + responseStr);
        JsonElement jsonElement;
        try
        {
            jsonElement = gson.fromJson(responseStr, JsonElement.class);
        }
        catch (JsonSyntaxException e)
        {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            return new ArrayList<>();
        }

        if (!jsonElement.isJsonObject())
        {
            System.err.println("Respuesta inesperada: " + responseStr);
            return new ArrayList<>();
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return this.getProductList(jsonObject);
    }

    protected abstract String getMarketUri();
    protected abstract List<Product> getProductList(JsonObject responseJsonObj);

    protected HttpMethod getHttpMethod()
    {
        return HttpMethod.GET;
    }

    protected BodyPublisher getBodyPost(String term)
    {
        return HttpRequest.BodyPublishers.noBody();
    }

    public enum HttpMethod
    {
        POST, GET
    }
}