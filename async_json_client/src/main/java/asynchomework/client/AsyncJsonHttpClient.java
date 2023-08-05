package asynchomework.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.UriComponentsBuilder;

public class AsyncJsonHttpClient {
  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule());

  private final String host;
  private final int port;
  private final String basePath;

  public AsyncJsonHttpClient(String host, int port, String basePath) {
    this.host = host;
    this.port = port;
    this.basePath = basePath;
  }

  private final AsyncHttpClient httpClient = Dsl.asyncHttpClient(
      Dsl.config()
          .setConnectTimeout(500)
          .setRequestTimeout(5000)
          .setReadTimeout(5000)
  );

  public <T> CompletableFuture<T> get(String requestPath, Class<T> responseClass) {
    return get(requestPath, new MultiValueMapAdapter<>(Map.of()), responseClass);
  }

  public <T> CompletableFuture<T> get(String requestPath, MultiValueMap<String, String> queryParams, Class<T> responseClass) {
    RequestBuilder requestBuilder = Dsl.get(generateUrl(requestPath, queryParams));
    addHeaders(requestBuilder);

    return sendRequestAndConvertResponse(requestBuilder, responseClass);
  }

  public <T> CompletableFuture<T> post(String requestPath, Object body, Class<T> responseClass) {
    RequestBuilder requestBuilder = Dsl.post(generateUrl(requestPath, new MultiValueMapAdapter<>(Map.of())));
    addHeaders(requestBuilder);
    try {
      requestBuilder.setBody(OBJECT_MAPPER.writeValueAsBytes(body));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize body", e);
    }

    return sendRequestAndConvertResponse(requestBuilder, responseClass);
  }

  private String generateUrl(String requestPath, MultiValueMap<String, String> queryParams) {
    return UriComponentsBuilder.newInstance()
        .scheme("http")
        .host(host)
        .port(port)
        .path(basePath)
        .path(requestPath)
        .queryParams(queryParams)
        .build().toUriString();
  }

  private void addHeaders(RequestBuilder requestBuilder) {
    requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
  }

  private <T> CompletableFuture<T> sendRequestAndConvertResponse(RequestBuilder requestBuilder, Class<T> responseClass) {
    CompletableFuture<Response> whenResponse = httpClient.executeRequest(requestBuilder.build()).toCompletableFuture();
    return whenResponse.thenApply(response -> {
      if (!HttpStatus.valueOf(response.getStatusCode()).is2xxSuccessful()) {
        throw new IllegalStateException("Got incorrect http code %d".formatted(response.getStatusCode()));
      }

      try {
        return OBJECT_MAPPER.readValue(response.getResponseBody(), responseClass);
      } catch (JsonProcessingException e) {
        throw new IllegalStateException("Failed to parse json", e);
      }
    });
  }
}
