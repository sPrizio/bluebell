package com.bluebell.planter.integration.client.forexfactory;

import com.bluebell.planter.integration.client.GetClient;
import com.bluebell.planter.integration.client.IntegrationClient;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Web client that interfaces with the forex factor API
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Component("forexFactoryIntegrationClient")
public class ForexFactoryIntegrationClient implements IntegrationClient, GetClient {


    //  METHODS

    @Override
    public String get(final String url, final @NonNull MultiValueMap<String, String> queryParams) {
        validateParameterIsNotNull(url, "url cannot be null");
        return doGet(url, queryParams);
    }
}
