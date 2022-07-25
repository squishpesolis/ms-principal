package org.squishpe.springcloud.ms.msusuarios.service;

import org.squishpe.springcloud.ms.msusuarios.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar(Integer pageNo, Integer pageSize, String sortBy);
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);

    List<Usuario> listarPorIds(Iterable<Long> ids);


}
