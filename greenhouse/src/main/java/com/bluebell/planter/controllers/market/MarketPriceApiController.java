package com.bluebell.planter.controllers.market;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.platform.util.MetaTrader4FileUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.bluebell.radicle.importing.validation.ImportValidator.validateImportFileExtension;

/**
 * Api controller for {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}/market-price")
@Tag(name = "MarketPrice", description = "Handles endpoints & operations related to market prices.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class MarketPriceApiController extends AbstractApiController {

    @Value("${bluebell.ingress.root}")
    private String ingressRoot;


    //  METHODS

    /**
     * Ingests a market price data file from MT4 and saves it to the file system
     *
     * @param symbol symbol for the price
     * @param priceInterval time interval
     * @param file multipart file
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     * @throws IOException io exception during file processing
     */
    @ValidateApiToken
    @Operation(summary = "Ingests a market price data file from MT4", description = "Takes in a file of MT4 market price data and saves it to the file system")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given a bad file format.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Format .sdada is not a supported file format")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given a bad symbol.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Symbol <bad_symbol> is not a valid symbol name")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given a bad time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Time interval <bad_interval> is not a valid time interval")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully saves the file to the system.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @PostMapping("/ingest")
    public StandardJsonResponse<Boolean> postIngestMarketPriceDataFromMT4(
            @Parameter(name = "Market Price Sy,bol", description = "The symbol for the price data")
            final @RequestParam("symbol") String symbol,
            @Parameter(name = "Market Price Time Interval", description = "The time interval for the price data")
            final @RequestParam("priceInterval") String priceInterval,
            @Parameter(name = "Market Price File", description = "The file containing market price data")
            final @RequestParam("file") MultipartFile file,
            final HttpServletRequest request
            ) throws IOException {

        final String[] extensions = {".csv"};
        if (file == null || StringUtils.isEmpty(file.getOriginalFilename())) {
            return StandardJsonResponse
                    .<Boolean>builder()
                    .success(false)
                    .message("Input file was null")
                    .build();
        }

        validateImportFileExtension(file, extensions, "The given file %s was not of a valid format", file.getOriginalFilename());

        if (validateSymbol(symbol)) {
            if (!EnumUtils.isValidEnumIgnoreCase(MarketPriceTimeInterval.class, priceInterval)) {
                return StandardJsonResponse
                        .<Boolean>builder()
                        .success(false)
                        .message(String.format("%s is not a valid time interval", priceInterval))
                        .build();
            }


            final MarketPriceTimeInterval marketPriceTimeInterval = GenericEnum.getByCode(MarketPriceTimeInterval.class, priceInterval);
            final Path targetDirPath = Paths.get(String.format("%s%s%s%s%s%s%s", getIngressPath(), File.separator, DataSource.METATRADER4.getDataRoot(), File.separator, symbol, File.separator, marketPriceTimeInterval.getCode()));
            final Path targetFilePath = Paths.get(targetDirPath.toString(), file.getOriginalFilename());
            Files.createDirectories(targetDirPath);

            if (Files.notExists(targetFilePath)) {
                Files.createFile(targetFilePath);
            } else {
                LOGGER.info("{} already exists", targetFilePath);
            }

            try (FileOutputStream fos = new FileOutputStream(targetFilePath.toString())) {
                fos.write(file.getBytes());
            }

            if (MetaTrader4FileUtil.isValidCsvFile(new File(targetFilePath.toString()), ';')) {
                return StandardJsonResponse
                        .<Boolean>builder()
                        .success(true)
                        .message(String.format("File %s was successfully saved to the ingress", file.getOriginalFilename()))
                        .build();
            } else {
                FileUtils.delete(new File(targetFilePath.toString()));
                return StandardJsonResponse
                        .<Boolean>builder()
                        .success(false)
                        .message("File did not contain valid mt4 .csv")
                        .build();
            }
        }

        return StandardJsonResponse
                .<Boolean>builder()
                .success(false)
                .message(String.format("%s was not a valid symbol. It contained illegal symbols or characters", symbol))
                .build();
    }


    //  HELPERS

    /**
     * Validates the given symbol is of the correct format
     *
     * @param symbol symbol
     * @return true if no illegal characters are present
     */
    private boolean validateSymbol(final String symbol) {

        if (StringUtils.isEmpty(symbol)) {
            return false;
        }

        return Pattern.compile(CorePlatformConstants.Regex.MARKET_PRICE_VALID_SYMBOL_REGEX).matcher(symbol).matches();
    }

    /**
     * Returns the ingress path depending on the runtime environment
     *
     * @return ingress path
     */
    private String getIngressPath() {

        if (this.ingressRoot.contains("test")) {
            return String.format("%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, this.ingressRoot);
        } else {
            return String.format("%s", DirectoryUtil.getIngressDataRoot(this.ingressRoot));
        }
    }
}
