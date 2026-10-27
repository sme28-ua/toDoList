package madstodolist.controller;

import madstodolist.model.Equipo;
import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.EquipoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipoService equipoService;

    @Test
    public void getEquiposDevuelveListaYVistaCorrecta() throws Exception {
        // GIVEN: Dos equipos simulados en el servicio
        Equipo equipo1 = new Equipo("Proyecto A");
        Equipo equipo2 = new Equipo("Proyecto B");

        when(equipoService.findAllOrdenadosPorNombre()).thenReturn(Arrays.asList(equipo1, equipo2));

        // WHEN & THEN: Petición GET /equipos devuelve estado 200 OK y renderiza los nombres
        this.mockMvc.perform(get("/equipos"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipos"))
                .andExpect(content().string(containsString("Proyecto A")))
                .andExpect(content().string(containsString("Proyecto B")));
    }

    @Test
    public void getEquipoDevuelveUsuariosYVistaCorrecta() throws Exception {
        EquipoData equipo = new EquipoData();
        equipo.setId(1L);
        equipo.setNombre("Proyecto A");

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua");

        when(equipoService.recuperarEquipo(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(Collections.singletonList(usuario));

        this.mockMvc.perform(get("/equipos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuariosEquipo"))
                .andExpect(model().attribute("equipo", equipo))
                .andExpect(model().attribute("usuarios",
                        Collections.singletonList(usuario)))
                .andExpect(content().string(containsString("Proyecto A")))
                .andExpect(content().string(containsString("user@ua")));
    }
}