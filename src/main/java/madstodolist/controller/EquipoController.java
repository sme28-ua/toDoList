package madstodolist.controller;

import madstodolist.model.Equipo;
import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model) {
        List<Equipo> equipos = equipoService.findAllOrdenadosPorNombre();
        model.addAttribute("equipos", equipos);
        return "equipos";
    }

    @GetMapping("/equipos/{id}")
    public String usuariosEquipo(@org.springframework.web.bind.annotation.PathVariable Long id,
                                 Model model) {
        EquipoData equipo = equipoService.recuperarEquipo(id);
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(id);
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);
        return "usuariosEquipo";
    }
}