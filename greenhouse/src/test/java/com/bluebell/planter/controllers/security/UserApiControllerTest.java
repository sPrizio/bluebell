package com.bluebell.planter.controllers.security;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.converters.transaction.TransactionDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.security.CreateUpdateUserDTO;
import com.bluebell.platform.models.api.dto.system.CreateUpdatePhoneNumberDTO;
import com.bluebell.radicle.services.security.UserService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.planter.constants.ApiPaths.User.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link UserApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.9
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class UserApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockitoBean
    private AccountDTOConverter accountDTOConverter;

    @MockitoBean
    private TransactionDTOConverter transactionDTOConverter;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.userService.createUser(any())).thenReturn(generateTestUser());
        Mockito.when(this.userService.updateUser(any(), any())).thenReturn(generateTestUser());
        Mockito.when(this.userService.findUserByUsername("bad_username")).thenReturn(Optional.empty());
        Mockito.when(this.userService.findUserByUsername("stephen")).thenReturn(Optional.of(generateTestUser()));
        Mockito.when(this.transactionDTOConverter.convert(any())).thenReturn(generateTransactionDTO());
        Mockito.when(this.transactionDTOConverter.convertAll(any())).thenReturn(List.of(generateTransactionDTO()));
    }


    //  ----------------- getUser -----------------

    @Test
    void test_getUser_missingUser() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET)).queryParam("username", "bad_username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(String.format("No user found for username %s", "bad_username"))));
    }

    @Test
    void test_getUser_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET)).queryParam("username", "stephen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", is("Stephen")));
    }


    //  ----------------- getCountryCodes -----------------

    @Test
    void test_getCountryCodes_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, COUNTRY_CODES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1]", is("1")));
    }


    //  ----------------- getPhoneTypes -----------------

    @Test
    void test_getPhoneTypes_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, PHONE_TYPES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", is("MOBILE")));
    }


    //  ----------------- getCurrencies -----------------

    @Test
    void test_getCurrencies_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, CURRENCIES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", is("ARS")));
    }


    //  ----------------- getCountries -----------------

    @Test
    void test_getCountries_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, COUNTRIES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1]", is("ARGENTINA")));
    }


    //  ----------------- getLanguages -----------------

    @Test
    void test_getLanguages_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, LANGUAGES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", is("ENGLISH")));
    }

    //  ----------------- getRecentTransactions -----------------

    @Test
    void test_getRecentTransactions_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, RECENT_TRANSACTIONS)).with(testUserContext()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name", is("Test")));
    }


    //  ----------------- postCreateUser -----------------

    @Test
    void test_postCreateUser_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post(getApiPath(BASE, CREATE)).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateUser_success() throws Exception {

        final CreateUpdateUserDTO data = CreateUpdateUserDTO
                .builder()
                .email("test@email.com")
                .password("2022-09-05")
                .lastName("Prizio")
                .firstName("Stephen")
                .username("s.prizio")
                .phoneNumbers(
                        List.of(
                                CreateUpdatePhoneNumberDTO
                                        .builder()
                                        .phoneType("MOBILE")
                                        .countryCode((short) 1)
                                        .telephoneNumber(5149411025L)
                                        .build()
                        )
                )
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE)).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", is("Stephen")));
    }


    //  ----------------- putUpdateUser -----------------

    @Test
    void test_putUpdateUser_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of("test@email.com"));

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE)).params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateUser_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of("test@email.com"));

        final CreateUpdateUserDTO data = CreateUpdateUserDTO
                .builder()
                .email("test@email.com")
                .password("2022-09-05")
                .lastName("Prizio")
                .firstName("Stephen")
                .username("s.prizio")
                .phoneNumbers(
                        List.of(
                                CreateUpdatePhoneNumberDTO
                                        .builder()
                                        .phoneType("MOBILE")
                                        .countryCode((short) 1)
                                        .telephoneNumber(5149411025L)
                                        .build()
                        )
                )
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE)).params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", is("Stephen")));
    }
}
