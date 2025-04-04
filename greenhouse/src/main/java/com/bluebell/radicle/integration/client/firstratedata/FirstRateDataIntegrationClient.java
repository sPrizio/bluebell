package com.bluebell.radicle.integration.client.firstratedata;

import com.bluebell.radicle.integration.client.GetClient;
import com.bluebell.radicle.integration.client.IntegrationClient;
import com.bluebell.radicle.integration.exceptions.IntegrationException;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.File;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Client that fetches data from the firstratedata servers
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Component("firstRateDataIntegrationClient")
public class FirstRateDataIntegrationClient implements IntegrationClient, GetClient {

    private static final String FILE_URL_ID = "fileUrlId";


    //  METHODS

    /**
     * Downloads the data files from the given endpoint
     *
     * @param url download url
     * @param queryParams query params
     * @param fileName target file name
     * @return downloaded file
     */
    public File downloadDataFiles(final String url, final @NonNull MultiValueMap<String, String> queryParams, final String fileName) {
        validateParameterIsNotNull(url, "url cannot be null");
        validateParameterIsNotNull(fileName, "target file name cannot be null");

        if (!queryParams.containsKey(FILE_URL_ID)) {
            throw new IntegrationException(String.format("%s cannot be null", FILE_URL_ID));
        }

        final File downloadedZip = downloadFile(url, queryParams, fileName);
        if (downloadedZip == null || !downloadedZip.exists()) {
            throw new IntegrationException(String.format("No file was received from %s with params %s", url, queryParams));
        }

        return downloadedZip;
    }

    @Override
    public String get(String url, MultiValueMap<String, String> queryParams) {
        validateParameterIsNotNull(url, "url cannot be null");
        return doGet(url, queryParams);
    }
}
