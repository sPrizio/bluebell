package com.bluebell.planter.controllers.security;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.security.UsernamePasswordRequestDTO;
import com.bluebell.radicle.security.services.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.javatuples.Triplet;
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

import static com.bluebell.planter.constants.ApiPaths.Security.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link SecurityApiController}
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class SecurityApiControllerTest extends AbstractPlanterTest {

    @MockitoBean
    private SecurityService securityService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.securityService.isUserTaken(anyString())).thenReturn(Triplet.with(false, false, null));
        Mockito.when(this.securityService.login(anyString(), anyString())).thenReturn(Pair.with(null, generateTestUser()));
    }


    //  ----------------- postIsUserTaken -----------------

    @Test
    void test_postIsUserTaken_success() throws Exception {

        final UsernamePasswordRequestDTO data = UsernamePasswordRequestDTO
                .builder()
                .usernameEmail("test1")
                .password("password")
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, IS_USER_TAKEN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }


    //  ----------------- postLogin -----------------

    @Test
    void test_login_success() throws Exception {

        final UsernamePasswordRequestDTO data = UsernamePasswordRequestDTO
                .builder()
                .usernameEmail("test1")
                .password("password")
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
