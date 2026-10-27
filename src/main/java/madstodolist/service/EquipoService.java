package madstodolist.service;

import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.repository.EquipoRepository;
import madstodolist.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public EquipoData crearEquipo(String nombre) {
        Equipo equipo = equipoRepository.save(new Equipo(nombre));
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional
    public EquipoData crearEquipo(String nombre, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("No existe usuario con id " + idUsuario);
        }

        Equipo equipo = new Equipo(nombre);
        equipo.addUsuario(usuario);
        equipo = equipoRepository.save(equipo);
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public EquipoData recuperarEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + id);
        }
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public List<EquipoData> findAllOrdenadoPorNombre() {
        return equipoRepository.findAllByOrderByNombreAsc().stream()
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAllOrdenadosPorNombre() {
        return equipoRepository.findAllByOrderByNombreAsc();
    }

    @Transactional
    public void añadirUsuarioAEquipo(Long id, Long idUsuario) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + id);
        }
        if (usuario == null) {
            throw new EquipoServiceException("No existe usuario con id " + idUsuario);
        }
        equipo.addUsuario(usuario);
    }

    @Transactional
    public void eliminarUsuarioDeEquipo(Long id, Long idUsuario) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + id);
        }
        if (usuario == null) {
            throw new EquipoServiceException("No existe usuario con id " + idUsuario);
        }
        equipo.removeUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPerteneceAEquipo(Long id, Long idUsuario) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + id);
        }
        return equipo.getUsuarios().stream()
                .anyMatch(usuario -> usuario.getId().equals(idUsuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> usuariosEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + id);
        }
        return equipo.getUsuarios().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipoData> equiposUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("No existe usuario con id " + idUsuario);
        }
        return usuario.getEquipos().stream()
                .sorted(Comparator.comparing(Equipo::getId))
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .collect(Collectors.toList());
    }
}
