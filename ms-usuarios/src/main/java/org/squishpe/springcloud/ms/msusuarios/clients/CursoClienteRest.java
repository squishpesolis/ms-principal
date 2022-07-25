package org.squishpe.springcloud.ms.msusuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-cursos", url = "${ms.cursos.url}:${ms.cursos.port}/api/cursos")
public interface CursoClienteRest {

    @DeleteMapping("/{id}/eliminar-curso-usuario-por-id")
    void eliminarUsuario(@PathVariable Long id);
}
