package com.bluebell.planter.converters.portfolio;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.portfolio.PortfolioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link PortfolioDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioDTOConverterTest extends AbstractPlanterTest {

    @MockitoBean
    private AccountDTOConverter accountDTOConverter;

    @Autowired
    private PortfolioDTOConverter portfolioDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.accountDTOConverter.convertAll(any())).thenReturn(List.of(generateTestAccountDTO()));
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.portfolioDTOConverter.convert(null))
                .isNotNull()
                .satisfies(PortfolioDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.portfolioDTOConverter.convert(generateTestPortfolio()))
                .isNotNull()
                .extracting("active", "name")
                .containsExactly(true, "Test Portfolio");

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.portfolioDTOConverter.convertAll(List.of(generateTestPortfolio())))
                .isNotEmpty()
                .first()
                .extracting("active", "name")
                .containsExactly(true, "Test Portfolio");
    }
}
