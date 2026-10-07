package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class navbarWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void navbarMuestraLoginYRegistroSiNoHaySesion() throws Exception {
        this.mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("Registro")))
                .andExpect(content().string(not(containsString("Tareas"))))
                .andExpect(content().string(not(containsString("Cerrar sesión"))));
    }

    @Test
    public void navbarMuestraTareasYCuentaSiHaySesion() throws Exception {
        // Simular un usuario autenticado en la sesión de Spring
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("idUsuarioLogeado", 1L);

        this.mockMvc.perform(get("/about").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tareas")))
                .andExpect(content().string(containsString("Cerrar sesión")))
                .andExpect(content().string(not(containsString("Login"))));
    }
}