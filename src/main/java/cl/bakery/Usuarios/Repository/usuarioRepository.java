package cl.bakery.Usuarios.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import  cl.bakery.Usuarios.Model.usuario;

public interface usuarioRepository extends JpaRepository<usuario, Integer>{

   Optional<usuario> findByUidFb(String uidFb);
    
}


