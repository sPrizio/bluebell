package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.integration.client.firstratedata.FirstRateDataIntegrationClient;
import com.bluebell.radicle.performable.ActionPerformable;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Performs the action of fetching market data from the FirstRateData servers
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Component("fetchMarketDataFromFirstRateDataActionPerformable")
@Scope("prototype")
public class FetchMarketDataFromFirstRateDataActionPerformable implements ActionPerformable {

    @Setter
    private String fileUrlId;

    @Value("${bluebell.frd.base.api.url}")
    private String endpoint;

    @Value("${bluebell.ingress.root}")
    private String ingressRoot;

    @Resource(name = "firstRateDataIntegrationClient")
    private FirstRateDataIntegrationClient firstRateDataIntegrationClient;


    //  METHODS

    @Override
    public ActionData perform() {

        final File file =
                this.firstRateDataIntegrationClient.downloadDataFiles(
                        this.endpoint,
                        CollectionUtils.toMultiValueMap(Map.of("fileUrlId", List.of(this.fileUrlId))),
                        String.format("%s%s%s%s", this.ingressRoot, File.separator, this.fileUrlId, ".zip")
                );

        if (file == null || !file.exists()) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(String.format("Could not download file at source %s for id %s", this.endpoint, this.fileUrlId))
                    .build();
        } else {
            return ActionData
                    .builder()
                    .success(true)
                    .data(file.getName())
                    .logs(String.format("Successfully download file %s from source %s for file id %s", file.getName(), this.endpoint, this.fileUrlId))
                    .build();
        }
    }
}
