package com.bluebell.radicle.services.portfolio;

import com.bluebell.AbstractGenericTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.portfolio.CreateUpdatePortfolioDTO;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link PortfolioService}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioServiceTest extends AbstractGenericTest {

    private User user;

    private User anotherUser;

    private Portfolio portfolio;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = generateTestUser();
        user.setPortfolios(Collections.emptyList());
        user = this.userRepository.save(user);

        anotherUser = generateTestUser();
        anotherUser.setEmail("testanother@email.com");
        anotherUser.setUsername("test.another");
        anotherUser.setPortfolios(Collections.emptyList());
        anotherUser = this.userRepository.save(anotherUser);

        portfolio = generateTestPortfolio();
        portfolio.setId(null);
        portfolio.setAccounts(Collections.emptyList());
        portfolio = this.portfolioRepository.save(portfolio);
        portfolio.setUser(anotherUser);

        anotherUser.setPortfolios(List.of(portfolio));
        anotherUser = this.userRepository.save(anotherUser);
    }

    @AfterEach
    void tearDown() {
        this.portfolioRepository.deleteAll();
        this.userRepository.deleteAll();
    }


    //  ----------------- findPortfolioByUid -----------------

    @Test
    void test_findPortfolioByUid_badUid() {
        assertThat(this.portfolioService.findPortfolioByUid(null)).isEmpty();
    }

    @Test
    void test_findPortfolioByUid_success() {
        final String uid = this.uniqueIdentifierService.generateUid(this.portfolio);
        assertThat(this.portfolioService.findPortfolioByUid(uid)).isNotEmpty();
        assertThat(this.portfolioService.findPortfolioByUid(uid)).isEqualTo(Optional.of(this.portfolio));
    }


    //  ----------------- createPortfolio -----------------

    @Test
    void test_createPortfolio_missingData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.portfolioService.createPortfolio(null, null))
                .withMessageContaining(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.portfolioService.createPortfolio(null, generateTestUser()))
                .withMessageContaining("The required data for creating a Portfolio entity was null or empty");
    }

    @Test
    void test_createPortfolio_success() {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test")
                .defaultPortfolio(true)
                .active(true)
                .build();

        assertThat(this.portfolioService.createPortfolio(data, this.user))
                .isNotNull()
                .extracting("name", "defaultPortfolio", "active")
                .containsExactly("Test", true, true);
    }


    //  ----------------- updatePortfolio -----------------

    @Test
    void test_updatePortfolio_missingData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.portfolioService.updatePortfolio(null, null, null))
                .withMessageContaining(CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.portfolioService.updatePortfolio(generateTestPortfolio(), null, null))
                .withMessageContaining(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.portfolioService.updatePortfolio(generateTestPortfolio(), null, generateTestUser()))
                .withMessageContaining("The required data for updating a Portfolio was null or empty");
    }

    @Test
    void test_updatePortfolio_success() {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test Updated")
                .defaultPortfolio(true)
                .active(true)
                .build();

        assertThat(this.portfolioService.updatePortfolio(this.portfolio, data, this.anotherUser))
                .isNotNull()
                .extracting("name", "defaultPortfolio", "active")
                .containsExactly("Test Updated", true, true);
    }

    @Test
    void test_updatePortfolio_onlyActive_unchanged() {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test Updated")
                .defaultPortfolio(true)
                .active(false)
                .build();

        assertThat(this.portfolioService.updatePortfolio(this.portfolio, data, this.anotherUser))
                .isNotNull()
                .extracting("name", "defaultPortfolio", "active")
                .containsExactly("Test Updated", true, true);
    }

    @Test
    void test_updatePortfolio_onlyActive_success() {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test Updated")
                .defaultPortfolio(true)
                .active(false)
                .build();

        Portfolio yetAnotherPortfolio = generateTestPortfolio();
        yetAnotherPortfolio.setId(null);
        yetAnotherPortfolio.setAccounts(Collections.emptyList());
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);
        yetAnotherPortfolio.setUser(this.anotherUser);
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);

        final List<Portfolio> temp = this.anotherUser.getPortfolios();
        temp.add(yetAnotherPortfolio);
        this.anotherUser.setPortfolios(temp);
        this.anotherUser = this.userRepository.save(this.anotherUser);

        assertThat(this.portfolioService.updatePortfolio(this.portfolio, data, this.anotherUser))
                .isNotNull()
                .extracting("name", "defaultPortfolio", "active")
                .containsExactly("Test Updated", true, false);
    }

    @Test
    void test_updatePortfolio_default_false_success() {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test Updated")
                .defaultPortfolio(false)
                .active(false)
                .build();

        Portfolio yetAnotherPortfolio = generateTestPortfolio();
        yetAnotherPortfolio.setId(null);
        yetAnotherPortfolio.setAccounts(Collections.emptyList());
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);
        yetAnotherPortfolio.setUser(this.anotherUser);
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);

        final List<Portfolio> temp = this.anotherUser.getPortfolios();
        temp.add(yetAnotherPortfolio);
        this.anotherUser.setPortfolios(temp);
        this.anotherUser = this.userRepository.save(this.anotherUser);

        assertThat(this.portfolioService.updatePortfolio(this.portfolio, data, this.anotherUser))
                .isNotNull()
                .extracting("name", "defaultPortfolio", "active")
                .containsExactly("Test Updated", false, false);

        assertThat(this.portfolioRepository.findById(yetAnotherPortfolio.getId()).get().isDefaultPortfolio()).isTrue();
    }

    @Test
    void test_updatePortfolio_default_changed_success() {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test Updated")
                .defaultPortfolio(true)
                .active(false)
                .build();

        Portfolio yetAnotherPortfolio = generateTestPortfolio();
        yetAnotherPortfolio.setId(null);
        yetAnotherPortfolio.setAccounts(Collections.emptyList());
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);
        yetAnotherPortfolio.setUser(this.anotherUser);
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);

        final List<Portfolio> temp = this.anotherUser.getPortfolios();
        temp.add(yetAnotherPortfolio);
        this.anotherUser.setPortfolios(temp);
        this.anotherUser = this.userRepository.save(this.anotherUser);

        assertThat(this.portfolioService.updatePortfolio(this.portfolio, data, this.anotherUser))
                .isNotNull()
                .extracting("name", "defaultPortfolio", "active")
                .containsExactly("Test Updated", true, false);

        assertThat(this.portfolioRepository.findById(yetAnotherPortfolio.getId()).get().isDefaultPortfolio()).isFalse();
    }


    //  ----------------- deletePortfolio -----------------

    @Test
    void test_deletePortfolio_missingData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.portfolioService.deletePortfolio(null))
                .withMessageContaining(CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);

        final Portfolio yetAnotherPortfolio = generateTestPortfolio();
        yetAnotherPortfolio.setUser(null);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.portfolioService.deletePortfolio(yetAnotherPortfolio))
                .withMessageContaining(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    void test_deletePortfolio_onlyActive_failure() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.portfolioService.deletePortfolio(this.portfolio))
                .withMessageContaining("Cannot delete only active portfolio");
    }

    @Test
    void test_deletePortfolio_onlyActive_success() {

        Portfolio yetAnotherPortfolio = generateTestPortfolio();
        yetAnotherPortfolio.setActive(true);
        yetAnotherPortfolio.setId(null);
        yetAnotherPortfolio.setAccounts(Collections.emptyList());
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);
        yetAnotherPortfolio.setUser(this.anotherUser);
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);

        final List<Portfolio> temp = this.anotherUser.getPortfolios();
        temp.add(yetAnotherPortfolio);
        this.anotherUser.setPortfolios(temp);
        this.anotherUser = this.userRepository.save(this.anotherUser);

        yetAnotherPortfolio.setUser(this.anotherUser);

        assertThat(this.portfolioService.deletePortfolio(yetAnotherPortfolio)).isTrue();
        assertThat(this.portfolioRepository.findById(yetAnotherPortfolio.getId())).isEmpty();
    }

    @Test
    void test_deletePortfolio_inactive_success() {

        Portfolio yetAnotherPortfolio = generateTestPortfolio();
        yetAnotherPortfolio.setActive(false);
        yetAnotherPortfolio.setId(null);
        yetAnotherPortfolio.setAccounts(Collections.emptyList());
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);
        yetAnotherPortfolio.setUser(this.anotherUser);
        yetAnotherPortfolio = this.portfolioRepository.save(yetAnotherPortfolio);

        final List<Portfolio> temp = this.anotherUser.getPortfolios();
        temp.add(yetAnotherPortfolio);
        this.anotherUser.setPortfolios(temp);
        this.anotherUser = this.userRepository.save(this.anotherUser);

        yetAnotherPortfolio.setUser(this.anotherUser);

        assertThat(this.portfolioService.deletePortfolio(yetAnotherPortfolio)).isTrue();
        assertThat(this.portfolioRepository.findById(yetAnotherPortfolio.getId())).isEmpty();
    }

    @Test
    void test_deletePortfolio_random_success() {

        final Portfolio p = generateTestPortfolio();
        p.setUser(generateTestUser());
        p.getUser().setPortfolios(Collections.emptyList());

        assertThat(this.portfolioService.deletePortfolio(p)).isFalse();
    }
}
