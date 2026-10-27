package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.Equipo;
import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model) {
        List<Equipo> equipos = equipoService.findAllOrdenadosPorNombre();
        model.addAttribute("equipos", equipos);
        return "equipos";
    }

    @PostMapping("/equipos")
    public String crearEquipo(@ModelAttribute EquipoData equipoData,
                              Model model, RedirectAttributes flash) {
        Long idUsuario = managerUserSession.usuarioLogeado();
        if (idUsuario == null) {
            throw new UsuarioNoLogeadoException();
        }
        if (equipoData.getNombre() == null || equipoData.getNombre().trim().isEmpty()) {
            model.addAttribute("equipos", equipoService.findAllOrdenadosPorNombre());
            model.addAttribute("error", "El nombre del equipo no puede estar vacío");
            return "equipos";
        }

        equipoService.crearEquipo(equipoData.getNombre().trim(), idUsuario);
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/equipos";
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