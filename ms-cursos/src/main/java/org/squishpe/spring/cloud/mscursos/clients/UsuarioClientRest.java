package org.squishpe.spring.cloud.mscursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.squishpe.spring.cloud.mscursos.models.Usuario;

import java.util.List;

@FeignClient(name = "ms-usuarios", url = "${ms.usuarios.url}:${ms.usuarios.port}/api/usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam List<Long> ids);

}
