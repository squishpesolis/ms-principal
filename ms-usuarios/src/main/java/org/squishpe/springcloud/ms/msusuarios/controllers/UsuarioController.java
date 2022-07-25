package org.squishpe.springcloud.ms.msusuarios.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.squishpe.springcloud.ms.msusuarios.entity.Usuario;
import org.squishpe.springcloud.ms.msusuarios.service.UsuarioService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public List<Usuario> listar(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return  usuarioService.listar(pageNo,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);

        if(usuarioOptional.isPresent()) {
            return  ResponseEntity.ok(usuarioOptional.get());
        }
        return  ResponseEntity.notFound().build();
    }

    @GetMapping("usuarios-por-curso")
    public ResponseEntity<?> obtenerUsuariosPorCurso(@RequestParam List<Long> ids) {
        return  ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if(result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {

        if(result.hasErrors()) {
            return validar(result);
        }

        Optional<Usuario> usuarioO = usuarioService.porId(id);

        if(usuarioO.isPresent()) {
            Usuario usuario1 = usuarioO.get();
            usuario1.setEmail(usuario.getNombre());

            return  ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario1));
        }

        return  ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Optional<Usuario> usuarioO = usuarioService.porId(id);

        if(usuarioO.isPresent()) {
            usuarioService.eliminar(id);
            return  ResponseEntity.noContent().build();
        }

        return  ResponseEntity.notFound().build();

    }


    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error ->  {
            errores.put(error.getField(), "El campo " + error.getField() + " "+ error.getDefaultMessage());

        });

        return ResponseEntity.badRequest().body(errores);
    }

}
