package org.squishpe.spring.cloud.mscursos.service;

import org.squishpe.spring.cloud.mscursos.models.Usuario;
import org.squishpe.spring.cloud.mscursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar(Integer pageNo, Integer pageSize, String sortBy);
    Optional<Curso> porId(Long id);
    Optional<Curso> porIdConUsuarios(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

    void eliminarCursoUsuarioPorId(Long id);

    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);

    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);

    Optional<Usuario> desasignarUsuario(Usuario usuario, Long cursoId);



}
