package madstodolist.controller;

import madstodolist.model.Usuario;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

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

        List<UsuarioData> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);

        return "listadoDeUsuarios";
    }

    public String perfilUsuario(@PathVariable(value = "id") Long idUsuario, Model model, HttpSession session) {
        Long idLogeado = (Long) session.getAttribute("idUsuarioLogeado");
        if (idLogeado == null) {
            return "redirect:/login";
        }


        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            return "redirect:/registrados";
        }

        model.addAttribute("usuario", usuario);

        return "perfilUsuario";
    }

}
