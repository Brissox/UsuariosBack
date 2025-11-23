package cl.bakery.Usuarios.Services;

import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.bakery.Usuarios.DTO.EditarUsuarioDTO;
import cl.bakery.Usuarios.Model.usuario;
import cl.bakery.Usuarios.Repository.usuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional

public class usuarioServices {
    @Autowired

    private usuarioRepository usuarioRepository;

    public List<usuario> BuscarTodoUsuario() {
        return usuarioRepository.findAll();
    }

    public usuario BuscarUnUsuario(Integer ID_USUARIO) {
        return usuarioRepository.findById(ID_USUARIO).get();

    }

    public usuario GuardarUsuario(usuario usuario) {
        return usuarioRepository.save(usuario);

    }

    public void EliminarUsuario(Integer ID_USUARIO) {
        usuarioRepository.deleteById(ID_USUARIO);
    }

    public usuario buscarUsuarioUID(String uidFb) {
        return usuarioRepository.findByUidFb(uidFb)
                .orElse(null);
    }

    public usuario editarUsuario(String uid, EditarUsuarioDTO datos) {

        // Buscar usuario por UID
        usuario usuario = usuarioRepository.findByUidFb(uid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar solo los campos permitidos
        usuario.setApellidoPaterno(datos.getApellidoPaterno());
        usuario.setApellidoMaterno(datos.getApellidoMaterno());
        usuario.setUsuario(datos.getUsuario());
        usuario.setCorreo(datos.getCorreo());
        usuario.setTelefono((long) datos.getTelefono());

        // Convertir fechaNacimiento (String en DTO) a java.sql.Date antes de setearla
        if (datos.getFechaNacimiento() == null || datos.getFechaNacimiento().isBlank()) {
            usuario.setFechaNacimiento(null);
        } else {
            try {
                Date parsed = Date.valueOf(datos.getFechaNacimiento()); 
                usuario.setFechaNacimiento(parsed); 
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Formato de fechaNacimiento inválido. Se espera 'yyyy-MM-dd'", ex);
            }
        }
        usuario.setPais(datos.getPais());
        usuario.setCiudad(datos.getCiudad());
        usuario.setDireccion(datos.getDireccion());
        usuario.setCodigoDesc(datos.getCodigoDesc());
        usuario.setEstado(datos.getEstado());

        return usuarioRepository.save(usuario);
    }
}