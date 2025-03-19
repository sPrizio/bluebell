package com.bluebell.platform.models.api.dto.portfolio;

import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Request object for creating and updating {@link Portfolio}s
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Builder
@Schema(title = "CreateUpdatePortfolioDTO", name = "CreateUpdatePortfolioDTO", description = "Payload for creating and updating portfolios")
public record CreateUpdatePortfolioDTO(
        @Schema(description = "Portfolio name") String name,
        @Schema(description = "Is the portfolio active") boolean active,
        @Schema(description = "Is this the default portfolio") boolean defaultPortfolio
) { }
