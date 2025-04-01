package com.bluebell.planter.controllers.market;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.platform.util.FileUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import static com.bluebell.radicle.importing.validation.ImportValidator.validateImportFileExtension;

/**
 * Api controller for {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}/market-price")
@Tag(name = "MarketPrice", description = "Handles endpoints & operations related to market prices.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class MarketPriceApiController extends AbstractApiController {

    @Value("${bluebell.ingress.root}")
    private String ingressRoot;


    //  METHODS

    @ValidateApiToken
    @Operation(summary = "Ingests a market price data file from MT4", description = "Takes in a file of MT4 market price data and saves it to the file system")
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
            final File targetDir = new File(String.format("%s%s%s%s%s%s", DirectoryUtil.getBaseProjectDirectory(), File.separator, this.ingressRoot, DataSource.METATRADER4.getDataRoot(), File.separator, marketPriceTimeInterval.getCode()));
            final File fileToSave = new File(targetDir, file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                fos.write(file.getBytes());
            }

            //  TODO: test this
            //  TODO: add validation for MT4 Price data file
            if (FileUtil.isValidCsvFile(fileToSave, ';')) {
                return StandardJsonResponse
                        .<Boolean>builder()
                        .success(true)
                        .message(String.format("File %s was successfully saved to the ingress", file.getOriginalFilename()))
                        .build();
            } else {
                FileUtils.delete(fileToSave);
                return StandardJsonResponse
                        .<Boolean>builder()
                        .success(false)
                        .message("File did not contain valid .csv")
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
}
