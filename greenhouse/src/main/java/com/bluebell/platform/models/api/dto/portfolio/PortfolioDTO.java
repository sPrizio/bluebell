package com.bluebell.platform.models.api.dto.portfolio;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO representation for {@link Portfolio}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Getter
@Setter
@Builder
@Schema(title = "PortfolioDTO", name = "PortfolioDTO", description = "A client-facing reduction of the Portfolio entity that displays key information in a safe way.")
public class PortfolioDTO implements GenericDTO {

    @Schema(description = "Portfolio uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Portfolio number")
    private long portfolioNumber;

    @Schema(description = "Portfolio name")
    private String name;

    @Schema(description = "is the portfolio active?")
    private boolean active;

    @Schema(description = "Portfolio creation time")
    private LocalDateTime created;

    @Schema(description = "Is this the main portfolio?")
    private boolean defaultPortfolio;

    @Schema(description = "Portfolio accounts")
    private List<AccountDTO> accounts;
}
