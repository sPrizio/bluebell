package com.bluebell.planter.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Testing class for {@link UniqueIdentifierService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
class UniqueIdentifierServiceTest extends AbstractPlanterTest {

    private static final String TEST_UID = "MTE4JWNvbS5ibHVlYmVsbC5wbGF0Zm9ybS5tb2RlbHMuY29yZS5lbnRpdGllcy50cmFkZS5UcmFkZQ==";
    private final UniqueIdentifierService uniqueIdentifierService = new UniqueIdentifierService();

    private final Trade mockedTrade = Mockito.mock(Trade.class, Mockito.RETURNS_DEEP_STUBS);

    @BeforeEach
    void setUp() {
        Mockito.when(mockedTrade.getId()).thenReturn(118L);
    }


    //  ----------------- generateUid -----------------

    @Test
    void test_generateUid_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.generateUid(null))
                .withMessage("entity cannot be null");
    }

    @Test
    void test_generateUid_success() {
        assertThat(this.uniqueIdentifierService.generateUid(this.mockedTrade))
                .isEqualTo(TEST_UID);
    }


    //  ----------------- retrieveId -----------------

    @Test
    void test_retrieveId_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(null))
                .withMessage(CorePlatformConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setUid(StringUtils.EMPTY);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(tradeDTO.getUid()))
                .withMessage("uid is missing");
    }

    @Test
    void test_retrieveId_success() {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setUid(TEST_UID);

        assertThat(this.uniqueIdentifierService.retrieveId(tradeDTO.getUid()))
                .isEqualTo(118L);
    }


    //  ----------------- retrieveIdForUid -----------------

    @Test
    void test_retrieveIdForUid_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveIdForUid(null))
                .withMessage(CorePlatformConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);
    }

    @Test
    void test_retrieveIdForUid_success() {
        assertThat(this.uniqueIdentifierService.retrieveIdForUid(TEST_UID))
                .isEqualTo(118L);
    }
}
