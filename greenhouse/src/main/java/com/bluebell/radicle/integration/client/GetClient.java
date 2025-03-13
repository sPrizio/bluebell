package com.bluebell.radicle.integration.client;

import com.bluebell.radicle.integration.exceptions.IntegrationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * A generic HTTP client that performs GET requests
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface GetClient {


    //  METHODS

    /**
     * Performs an HTTP GET request for the given url & request parameters
     *
     * @param endpoint    endpoint url
     * @param queryParams query params
     * @return JSON string
     */
    default String doGet(final String endpoint, final MultiValueMap<String, String> queryParams) {

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final String httpEndpoint = UriComponentsBuilder.fromUriString(endpoint).queryParams(queryParams).encode().build().toString();
            final var httpRequest = new HttpGet(httpEndpoint);
            final HttpResponse response = client.execute(httpRequest);
            final var builder = new StringBuilder();
            final var bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            return builder.toString();
        } catch (Exception e) {
            throw new IntegrationException(String.format("There was an error connecting to %s with parameters %s", endpoint, queryParams), e);
        }
    }
}
