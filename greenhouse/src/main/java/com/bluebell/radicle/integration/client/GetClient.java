package com.bluebell.radicle.integration.client;

import org.springframework.util.MultiValueMap;

import java.io.File;

/**
 * A generic HTTP client that performs GET requests
 *
 * @author Stephen Prizio
 * @version 0.2.5
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
    String doGet(final String endpoint, final MultiValueMap<String, String> queryParams);

    /**
     * Performs an HTTP GET request to download a file at the given endpoint
     *
     * @param endpoint endpoint
     * @param queryParams query params
     * @param fileName file to save
     * @return {@link File}
     */
    File downloadFile(final String endpoint, final MultiValueMap<String, String> queryParams, final String fileName);
}
