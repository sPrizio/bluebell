package com.bluebell.planter.services;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link UniqueIdentifierService}
 *
 * @author Stephen Prizio
 * @version 0.1.6
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

        TradeDTO tradeDTO = TradeDTO.builder().build();
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(tradeDTO.getUid()))
                .withMessage("uid is missing");
    }

    @Test
    void test_retrieveId_success() {
        TradeDTO tradeDTO = TradeDTO.builder().uid(TEST_UID).build();
        assertThat(this.uniqueIdentifierService.retrieveId(tradeDTO.getUid()))
                .isEqualTo(118L);
    }


    //  ----------------- generateUniqueIdentifierAsLong -----------------

    @Test
    void test_generateUniqueIdentifierAsLong_badParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.generateUniqueIdentifierAsLong(null))
                .withMessage(CorePlatformConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);
    }

    @Test
    void test_generateUniqueIdentifierAsLong_success_same() {
        String input = "hello-world";
        final long result1 = this.uniqueIdentifierService.generateUniqueIdentifierAsLong(input);
        final long result2 = this.uniqueIdentifierService.generateUniqueIdentifierAsLong(input);

        assertThat(result1).isEqualTo(result2);
    }

    @Test
    void test_generateUniqueIdentifierAsLong_success_unique() {
        String input1 = "string-one";
        String input2 = "string-two";
        long result1 = this.uniqueIdentifierService.generateUniqueIdentifierAsLong(input1);
        long result2 = this.uniqueIdentifierService.generateUniqueIdentifierAsLong(input2);

        assertThat(result1).isNotEqualTo(result2);
    }
}
