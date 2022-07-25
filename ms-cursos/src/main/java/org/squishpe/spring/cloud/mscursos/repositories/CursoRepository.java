package org.squishpe.spring.cloud.mscursos.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.squishpe.spring.cloud.mscursos.models.entity.Curso;

public interface CursoRepository extends CrudRepository<Curso, Long>, PagingAndSortingRepository<Curso, Long> {

    @Modifying
    @Query("delete from CursoUsuario cu where cu.usuarioId = ?1")
    void eliminarCursoUsuarioPorId(Long id);



}
