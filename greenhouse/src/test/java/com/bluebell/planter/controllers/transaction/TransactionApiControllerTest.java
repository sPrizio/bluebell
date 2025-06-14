package com.bluebell.planter.controllers.transaction;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.controllers.account.AccountApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.transaction.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.planter.constants.ApiPaths.Transaction.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class TransactionApiControllerTest extends AbstractPlanterTest {

    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";
    private static final String ACCOUNT_NUMBER = "accountNumber";

    private final Account TEST_ACCOUNT = generateTestAccount();

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.accountService.findAccountByAccountNumber(1234L)).thenReturn(Optional.empty());
        Mockito.when(this.accountService.findAccountByAccountNumber(5678L)).thenReturn(Optional.of(TEST_ACCOUNT));
        Mockito.when(this.transactionService.findRecentTransactions(any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findAllTransactionsForAccount(any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findAllTransactionsByTypeForAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findAllTransactionsByStatusForAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findTransactionsWithinTimespanForAccount(any(), any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findTransactionForNameAndAccount("test 1", TEST_ACCOUNT)).thenReturn(Optional.empty());
        Mockito.when(this.transactionService.findTransactionForNameAndAccount("test 2", TEST_ACCOUNT)).thenReturn(Optional.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.createNewTransaction(any(), any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
        Mockito.when(this.transactionService.updateTransaction(any(), any(), any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
        Mockito.when(this.transactionService.deleteTransaction(any())).thenReturn(true);
        Mockito.when(this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(any(), any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount())), Pageable.ofSize(10), 10));
        Mockito.when(this.transactionService.findAllTransactionsForStatusWithinDatePaged(any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount())), Pageable.ofSize(10), 10));
        Mockito.when(this.transactionService.findAllTransactionsForTypeWithinDatePaged(any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount())), Pageable.ofSize(10), 10));
        Mockito.when(this.transactionService.findAllTransactionsWithinDatePaged(any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount())), Pageable.ofSize(10), 10));
    }


    //  ----------------- getRecentTransactionsForAccount -----------------

    @Test
    void test_getRecentTransactionsForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_RECENT_FOR_ACCOUNT))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getRecentTransactionsForAccount_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_RECENT_FOR_ACCOUNT))
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getAllTransactionsForAccount -----------------

    @Test
    void test_getAllTransactionsForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_ACCOUNT))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getAllTransactionsForAccount_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_ACCOUNT))
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsByTypeForAccount -----------------

    @Test
    void test_getTransactionsByTypeForAccount_badEnum() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_TYPE_FOR_ACCOUNT))
                        .queryParam("transactionType", "BAD")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid transaction type")));
    }

    @Test
    void test_getTransactionsByTypeForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_TYPE_FOR_ACCOUNT))
                        .queryParam("transactionType", "DEPOSIT")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionsByTypeForAccount_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_TYPE_FOR_ACCOUNT))
                        .queryParam("transactionType", "DEPOSIT")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsByStatusForAccount -----------------

    @Test
    void test_getTransactionsByStatusForAccount_badEnum() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_STATUS_FOR_ACCOUNT))
                        .queryParam("transactionStatus", "BAD")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid transaction status")));
    }

    @Test
    void test_getTransactionsByStatusForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_STATUS_FOR_ACCOUNT))
                        .queryParam("transactionStatus", "COMPLETED")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionsByStatusForAccount_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_STATUS_FOR_ACCOUNT))
                        .queryParam("transactionStatus", "COMPLETED")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsWithinTimespanForAccount -----------------

    @Test
    void test_getTransactionsWithinTimespanForAccount_missingParamStart() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_WITHIN_TIMESPAN_FOR_ACCOUNT))
                        .queryParam("start", "asdadadas")
                        .queryParam("end", "2022-08-25T00:00:00")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTransactionsWithinTimespanForAccount_missingParamEnd() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_WITHIN_TIMESPAN_FOR_ACCOUNT))
                        .queryParam("start", "2022-08-24T00:00:00")
                        .queryParam("end", "adasdas")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTransactionsWithinTimespanForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_WITHIN_TIMESPAN_FOR_ACCOUNT))
                        .queryParam("start", "2022-08-24T00:00:00")
                        .queryParam("end", "2022-08-25T00:00:00")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionsWithinTimespanForAccount_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_WITHIN_TIMESPAN_FOR_ACCOUNT))
                        .queryParam("start", "2022-08-24T00:00:00")
                        .queryParam("end", "2022-08-25T00:00:00")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionForNameAndAccount -----------------

    @Test
    void test_getTransactionForNameAndAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_NAME_FOR_ACCOUNT))
                        .queryParam("transactionName", "test 1")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionForNameAndAccount_missingTransaction() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_NAME_FOR_ACCOUNT))
                        .queryParam("transactionName", "test 1")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(is(String.format("No transaction was found for account %d and transaction name %s", 5678, "test 1")))));
    }

    @Test
    void test_getTransactionForNameAndAccount_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_BY_NAME_FOR_ACCOUNT))
                        .queryParam("transactionName", "test 2")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsWithinIntervalPaged -----------------

    @Test
    void test_getTransactionsWithinIntervalPaged_transactionStatus_transactionType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("page", List.of("0"));
        map.put("transactionStatus", List.of("COMPLETED"));
        map.put("transactionType", List.of("DEPOSIT"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.transactions[0].transactionType.code", is("DEPOSIT")))
                .andExpect(jsonPath("$.data.transactions[0].amount", is(123.45)));
    }

    @Test
    void test_getTransactionsWithinIntervalPaged__transactionStatus_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("transactionStatus", List.of("COMPLETED"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.transactions[0].transactionType.code", is("DEPOSIT")))
                .andExpect(jsonPath("$.data.transactions[0].amount", is(123.45)));
    }

    @Test
    void test_getTransactionsWithinIntervalPaged_transactionType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("transactionType", List.of("DEPOSIT"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.transactions[0].transactionType.code", is("DEPOSIT")))
                .andExpect(jsonPath("$.data.transactions[0].amount", is(123.45)));
    }

    @Test
    void test_getTransactionsWithinIntervalPaged_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.transactions[0].transactionType.code", is("DEPOSIT")))
                .andExpect(jsonPath("$.data.transactions[0].amount", is(123.45)));
    }


    //  ----------------- postCreateNewTransaction -----------------

    @Test
    void test_postCreateNewTransaction_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post(getApiPath(BASE, CREATE_TRANSACTION))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewTransaction_missingAccount() throws Exception {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .name("Test Deposit")
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE_TRANSACTION))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewTransaction_success() throws Exception {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .name("Test Deposit")
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE_TRANSACTION))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount", is(123.45)));
    }


    //  ----------------- putUpdateTransaction -----------------

    @Test
    void test_putUpdateTransaction_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRANSACTION)).queryParam(ACCOUNT_NUMBER, "5678").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateTransaction_missingAccount() throws Exception {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .originalName("test 1")
                .name("Test Deposit")
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRANSACTION))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .with(testUserContext())
                        .queryParam(ACCOUNT_NUMBER, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateTransaction_missingTransaction() throws Exception {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .originalName("test 1")
                .name("Test Deposit")
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRANSACTION))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .with(testUserContext())
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No transaction was found for account %d and transaction name %s", 1234, "test 1"))));
    }

    @Test
    void test_putUpdateTransaction_success() throws Exception {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .originalName("test 2")
                .name("Test Deposit")
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRANSACTION))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .with(testUserContext())
                        .queryParam("accountNumber", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount", is(123.45)));
    }


    //  ----------------- deleteTransaction -----------------

    @Test
    void test_deleteTransaction_missingAccount() throws Exception {
        this.mockMvc.perform(delete(getApiPath(BASE, DELETE_TRANSACTION))
                        .with(testUserContext())
                        .queryParam("transactionName", "test 1")
                        .queryParam("accountNumber", "5678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_deleteTransaction_missingTransaction() throws Exception {
        this.mockMvc.perform(delete(getApiPath(BASE, DELETE_TRANSACTION))
                        .with(testUserContext())
                        .queryParam("transactionName", "test 1")
                        .queryParam("accountNumber", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No transaction was found for account %d and transaction name %s", 1234, "test 1"))));
    }

    @Test
    void test_deleteTransaction_success() throws Exception {
        this.mockMvc.perform(delete(getApiPath(BASE, DELETE_TRANSACTION))
                        .with(testUserContext())
                        .queryParam("accountNumber", "1234")
                        .queryParam("transactionName", "test 2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
