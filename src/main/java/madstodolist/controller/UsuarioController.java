package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registrados")
    public String listadoUsuarios(Model model, HttpSession session) {
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        List<UsuarioData> usuarios = usuarioService.todosUsuarios();
        model.addAttribute("usuarios", usuarios);

        return "listadoDeUsuarios";
    }
}