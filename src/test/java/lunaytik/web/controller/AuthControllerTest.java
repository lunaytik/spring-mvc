package lunaytik.web.controller;

import lunaytik.web.domain.User;
import lunaytik.web.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void shouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRegisterUserAndRedirect() throws Exception {

        mockMvc.perform(post("/register")
                        .param("username", "john")
                        .param("password", "secret"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(userService).register("john", "secret");
    }

    @Test
    void shouldFailIfUserAlreadyExists() throws Exception {

        User fakeUser = new User();
        fakeUser.setUsername("john");

        when(userService.findByUsername("john")).thenReturn(fakeUser);

        mockMvc.perform(post("/register")
                        .param("username", "john")
                        .param("password", "secret"))
                .andExpect(status().isOk())             // revient sur la page car erreur
                .andExpect(view().name("auth/register"))    // tu peux adapter selon ton comportement
                .andExpect(model().attributeExists("error"));
    }


}
