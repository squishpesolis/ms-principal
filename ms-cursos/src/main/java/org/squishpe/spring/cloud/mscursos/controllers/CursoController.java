package org.squishpe.spring.cloud.mscursos.controllers;


import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.squishpe.spring.cloud.mscursos.models.Usuario;
import org.squishpe.spring.cloud.mscursos.models.entity.Curso;
import org.squishpe.spring.cloud.mscursos.service.CursoService;

import javax.validation.Valid;
import java.util.*;


/*
* Buenas practicas Rest
* 1. Representar recursos no acciones
*       DELETE /cursos/1234
*     Ejemplos de recursos
*       1.1 Colecciones (plural)
*           /cursos
*       1.2 Documentos (singular)
*           /cursos/1
*           /cursos/matematica
*       1.3 Stores (plural)
*           /usuarios/20/favoritos  -> Lista de favoritos del usuario 20
*       1.4 Controladores (verbos)
*           /usuarios/20/reset-password -> resetear la contraseña del usuario 20
*
*   2. No devuelvas siempre 200
*       201 -> Recurso creado
*       202 -> Solicitud recibida. Em proceso
*       204 -> Solicitud exitosa. respuesta sin contenido
*       401 -> No autorizado
*       403 -> Acceso Prohibido
*       404 -> Recurso no encontrado
*       405 -> metodo no permitido
*       500 -> Error interno del servidor
*   3. No hagas todo con POST
*      GET -> Obtener un recurso
*           - Siempre devolver un pegaable
*      POST -> Crear un recurso
*      PUT -> Actualizar un recurso
*      DELETE -> Eliminar un recurso
*      PATCH -> Actualizar un recurso. Solo con cambios
*      OPTIONS -> Obtener metadatos para interactuar
*
*   4. Asegurar Tu Api
*       JWT
*
*   5. Versiona tu api
*      https://www.googleapis.com/youtube/v1/videos
*
*   6. Usa JSON, pero usalo bien
*      No devolver un string, devolver siempre JSON
*   7. Documentar la API REST
* */

@RestController
public class CursoController {


    @Autowired
    private CursoService cursoService;


    @GetMapping
    public List<Curso> listar(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return  cursoService.listar(pageNo,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> cursoOptional = cursoService.porIdConUsuarios(id);

        if(cursoOptional.isPresent()) {
            return  ResponseEntity.ok(cursoOptional.get());
        }
        return  ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if(result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso,  BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()) {
            return validar(result);
        }
        Optional<Curso> cursoOptional = cursoService.porId(id);

        if(cursoOptional.isPresent()) {
            Curso curso1 = cursoOptional.get();
            curso1.setNombre(curso.getNombre());

            return  ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso1));
        }

        return  ResponseEntity.notFound().build();

    }

    @PutMapping("/{cursoId}/asignar-usuario")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {

        Optional<Usuario> o;

        try {
            o = cursoService.asignarUsuario(usuario,cursoId);
        }catch (FeignException.FeignClientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "" +
                    "error " + e.getMessage()));
        }

        if(o.isPresent()) {
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/{cursoId}/crear-usuario")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {

        Optional<Usuario> o;

        try {
            o = cursoService.crearUsuario(usuario,cursoId);
        }catch (FeignException.FeignClientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
                    "error " + e.getMessage()));
        }

        if(o.isPresent()) {
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{cursoId}/desasignar-usuario")
    public ResponseEntity<?> desasignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {

        Optional<Usuario> o;

        try {
            o = cursoService.desasignarUsuario(usuario,cursoId);
        }catch (FeignException.FeignClientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
                    "error " + e.getMessage()));
        }

        if(o.isPresent()) {
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Optional<Curso> curso = cursoService.porId(id);

        if(curso.isPresent()) {
            cursoService.eliminar(id);
            return  ResponseEntity.noContent().build();
        }

        return  ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}/eliminar-curso-usuario-por-id")
    public ResponseEntity<?> eliminarCursoUsuario(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return  ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error ->  {
            errores.put(error.getField(), "El campo " + error.getField() + " "+ error.getDefaultMessage());

        });

        return ResponseEntity.badRequest().body(errores);
    }





}
