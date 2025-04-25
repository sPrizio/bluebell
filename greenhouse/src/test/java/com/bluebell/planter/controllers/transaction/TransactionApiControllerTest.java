package com.bluebell.planter.controllers.transaction;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.controllers.account.AccountApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class TransactionApiControllerTest extends AbstractPlanterTest {

    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";
    private static final String ACCOUNT_NUMBER = "accountNumber";

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.accountService.findAccountByAccountNumber(1234)).thenReturn(Optional.empty());
        Mockito.when(this.accountService.findAccountByAccountNumber(5678)).thenReturn(Optional.of(generateTestAccount()));
        Mockito.when(this.transactionService.findRecentTransactions(any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findAllTransactionsForAccount(any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findAllTransactionsByTypeForAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findAllTransactionsByStatusForAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findTransactionsWithinTimespanForAccount(any(), any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.findTransactionForNameAndAccount("test 1", generateTestAccount())).thenReturn(Optional.empty());
        Mockito.when(this.transactionService.findTransactionForNameAndAccount("test 2", generateTestAccount())).thenReturn(Optional.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionService.createNewTransaction(any(), any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
        Mockito.when(this.transactionService.updateTransaction(any(), any(), any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
        Mockito.when(this.transactionService.deleteTransaction(any())).thenReturn(true);
    }


    //  ----------------- getRecentTransactionsForAccount -----------------

    @Test
    void test_getRecentTransactionsForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-recent-for-account")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getRecentTransactionsForAccount_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-recent-for-account")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getAllTransactionsForAccount -----------------

    @Test
    void test_getAllTransactionsForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-for-account")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getAllTransactionsForAccount_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-for-account")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsByTypeForAccount -----------------

    @Test
    void test_getTransactionsByTypeForAccount_badEnum() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-type-for-account")
                        .queryParam("transactionType", "BAD")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid transaction type")));
    }

    @Test
    void test_getTransactionsByTypeForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-type-for-account")
                        .queryParam("transactionType", "DEPOSIT")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionsByTypeForAccount_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-type-for-account")
                        .queryParam("transactionType", "DEPOSIT")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsByStatusForAccount -----------------

    @Test
    void test_getTransactionsByStatusForAccount_badEnum() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-status-for-account")
                        .queryParam("transactionStatus", "BAD")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid transaction status")));
    }

    @Test
    void test_getTransactionsByStatusForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-status-for-account")
                        .queryParam("transactionStatus", "COMPLETED")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionsByStatusForAccount_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-status-for-account")
                        .queryParam("transactionStatus", "COMPLETED")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getTransactionsWithinTimespanForAccount -----------------

    @Test
    void test_getTransactionsWithinTimespanForAccount_missingParamStart() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-within-timespan-for-account")
                        .queryParam("start", "asdadadas")
                        .queryParam("end", "2022-08-25T00:00:00")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTransactionsWithinTimespanForAccount_missingParamEnd() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-within-timespan-for-account")
                        .queryParam("start", "2022-08-24T00:00:00")
                        .queryParam("end", "adasdas")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTransactionsWithinTimespanForAccount_missingAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-within-timespan-for-account")
                        .queryParam("start", "2022-08-24T00:00:00")
                        .queryParam("end", "2022-08-25T00:00:00")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionsWithinTimespanForAccount_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-within-timespan-for-account")
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
        this.mockMvc.perform(get("/api/v1/transaction/get-by-name-for-account")
                        .queryParam("transactionName", "test 1")
                        .queryParam(ACCOUNT_NUMBER, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
    }

    @Test
    void test_getTransactionForNameAndAccount_missingTransaction() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-name-for-account")
                        .queryParam("transactionName", "test 1")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(is(String.format("No transaction was found for account %d and transaction name %s", 5678, "test 1")))));
    }

    @Test
    void test_getTransactionForNameAndAccount_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/transaction/get-by-name-for-account")
                        .queryParam("transactionName", "test 2")
                        .queryParam(ACCOUNT_NUMBER, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- postCreateNewTransaction -----------------

    @Test
    void test_postCreateNewTransaction_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/transaction/create-transaction")
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

        this.mockMvc.perform(post("/api/v1/transaction/create-transaction").queryParam(ACCOUNT_NUMBER, "1234").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
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

        this.mockMvc.perform(post("/api/v1/transaction/create-transaction")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(ACCOUNT_NUMBER, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount", is(123.45)));
    }


    //  ----------------- putUpdateTransaction -----------------

    @Test
    void test_putUpdateTransaction_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put("/api/v1/transaction/update-transaction").queryParam(ACCOUNT_NUMBER, "5678").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
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

        this.mockMvc.perform(put("/api/v1/transaction/update-transaction")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, 1234))));
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

        this.mockMvc.perform(put("/api/v1/transaction/update-transaction")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(ACCOUNT_NUMBER, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No transaction was found for account %d and transaction name %s", 5678, "test 1"))));
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

        this.mockMvc.perform(put("/api/v1/transaction/update-transaction")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam("accountNumber", "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount", is(123.45)));
    }


    //  ----------------- deleteTransaction -----------------

    @Test
    void test_deleteTransaction_missingAccount() throws Exception {
        this.mockMvc.perform(delete("/api/v1/transaction/delete-transaction")
                        .queryParam("transactionName", "test 1")
                        .queryParam("accountNumber", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No account was found for account number %d", 1234))));
    }

    @Test
    void test_deleteTransaction_missingTransaction() throws Exception {
        this.mockMvc.perform(delete("/api/v1/transaction/delete-transaction")
                        .queryParam("transactionName", "test 1")
                        .queryParam("accountNumber", "5678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No transaction was found for account %d and transaction name %s", 5678, "test 1"))));
    }

    @Test
    void test_deleteTransaction_success() throws Exception {
        this.mockMvc.perform(delete("/api/v1/transaction/delete-transaction")
                        .queryParam("accountNumber", "5678")
                        .queryParam("transactionName", "test 2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
