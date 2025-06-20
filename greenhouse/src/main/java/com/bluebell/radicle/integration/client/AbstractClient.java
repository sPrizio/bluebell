package com.bluebell.radicle.integration.client;

import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.integration.exceptions.IntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Parent-level client logic
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@Slf4j
public abstract class AbstractClient implements GetClient {

    @Override
    public String doGet(final String endpoint, final MultiValueMap<String, String> queryParams) {
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

    @Override
    public File downloadFile(final String endpoint, final MultiValueMap<String, String> queryParams, final String fileName) {
        try {
            final File output = new File(DirectoryUtil.getBaseProjectDirectory() + File.separator + fileName);
            final String httpEndpoint = UriComponentsBuilder.fromUriString(endpoint).queryParams(queryParams).encode().build().toString();

            LOGGER.info("Copying data from {} to output directory {}", httpEndpoint, output.getAbsolutePath());
            FileUtils.copyURLToFile(new URL(httpEndpoint), output);

            LOGGER.info("File successfully downloaded.");
            return output;
        } catch (Exception e) {
            LOGGER.info("Failed to download file", e);
            throw new IntegrationException(String.format("There was an error connecting to %s with parameters %s", endpoint, queryParams), e);
        }
    }
}
