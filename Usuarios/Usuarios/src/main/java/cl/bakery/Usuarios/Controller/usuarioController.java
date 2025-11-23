package cl.bakery.Usuarios.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import cl.bakery.Usuarios.Assembler.usuarioModelAssembler;
import cl.bakery.Usuarios.DTO.EditarUsuarioDTO;
import cl.bakery.Usuarios.Model.usuario;
import cl.bakery.Usuarios.Services.usuarioServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/Usuarios")
public class usuarioController {

    @Autowired
    private usuarioServices usuarioservices;
    @Autowired
    private usuarioModelAssembler assambler;

    // ENDPOINT PARA listar todos los usuarios
    // Funcionamiento verificado || FUNCIONANDO
    @GetMapping
    @Operation(summary = "ENDPOINT QUE LISTA TODOS LOS USUARIOS", description = "Operacion que lista todos los Usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se listaron correctamente los Usuarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = usuario.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro ningun usuario", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", example = "No se encuentran Datos")))

    })
    public ResponseEntity<?> ListarUsuarios() {
        List<usuario> usuarios = usuarioservices.BuscarTodoUsuario();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentran datos");
        } else {
            return ResponseEntity.ok(assambler.toCollectionModel(usuarios));
        }
    }

    // ENDPOINT PARA listar un usuario por id
    @GetMapping("/{ID_USUARIO}")
    @Operation(summary = "ENDPOINT QUE LISTA UN USUARIO", description = "Operacion que lista un usuario")
    @Parameters(value = {
            @Parameter(name = "ID_USUARIO", description = "ID del usuario que se buscara", in = ParameterIn.PATH, required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se lista correctamente el usuario ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = usuario.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro ningun usuario", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", example = "No se encuentran Datos")))
    })

    public ResponseEntity<?> BuscarUsuario(@PathVariable Integer ID_USUARIO) {

        try {
            usuario usuarioBuscado = usuarioservices.BuscarUnUsuario(ID_USUARIO);
            return ResponseEntity.ok(assambler.toModel(usuarioBuscado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra Usuario");
        }

    }

    // Funcionamiento verificado || FUNCIONANDO
    @GetMapping("/uid/{uidFb}")
    @Operation(summary = "ENDPOINT QUE OBTIENE UN USUARIO POR UID", description = "Operación que obtiene un usuario registrado mediante su UID de Firebase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", example = "No se encuentra Usuario")))
    })
    public ResponseEntity<?> buscarUsuarioUID(@PathVariable String uidFb) {

        usuario usuarioBuscado = usuarioservices.buscarUsuarioUID(uidFb);

        if (usuarioBuscado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encuentra Usuario");
        }

        return ResponseEntity.ok(assambler.toModel(usuarioBuscado));
    }
    

//ENDPOINT PARA editar un usuario segun id
    @PutMapping("/{ID_USUARIO}") //SOLO PERMITE ACTUALIZAR ESCRIBIENDO TODOS LOS DATOS
    
    @Operation(summary = "ENDPOINT QUE EDITA UN USUARIO", description = "ENDPOINT QUE EDITA UN USUARIO", requestBody=@io.swagger.v3.oas.annotations.parameters.RequestBody(description="USUARIO QUE SE VA A EDITAR", required = true, content = @Content(schema = @Schema(implementation = usuario.class))))
    @Parameters (value = {
        @Parameter (name="ID_USUARIO", description= "ID del usuario que se editara", in = ParameterIn.PATH, required= true)})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se edito correctamente el usuario", content = @Content(mediaType = "application/json", schema = @Schema(implementation = usuario.class))),
            @ApiResponse(responseCode = "500", description = "Usuario no esta registrado", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", example = "No se puede registrar el usuario")))
    })

    public ResponseEntity<?> ActualizarUsuarios(@PathVariable Integer ID_USUARIO,
            @RequestBody usuario usuarioActualizar) {
        try {
            usuario usuarioActualizado = usuarioservices.BuscarUnUsuario(ID_USUARIO);
            usuarioActualizado.setNombre(usuarioActualizar.getNombre());
            usuarioActualizado.setApellidoPaterno(usuarioActualizar.getApellidoPaterno());
            usuarioActualizado.setApellidoMaterno(usuarioActualizar.getApellidoMaterno());
            usuarioActualizado.setFechaNacimiento(usuarioActualizar.getFechaNacimiento());
            usuarioActualizado.setCorreo(usuarioActualizar.getCorreo());
            usuarioActualizado.setDireccion(usuarioActualizar.getDireccion());
            usuarioActualizado.setTelefono(usuarioActualizar.getTelefono());
            usuarioActualizado.setContrasena(usuarioActualizar.getContrasena());
            usuarioActualizado.setRun(usuarioActualizar.getRun());
            usuarioActualizado.setDv(usuarioActualizar.getDv());
            usuarioActualizado.setEstado(usuarioActualizar.getEstado());
            usuarioActualizado.setPais(usuarioActualizar.getPais());
            usuarioActualizado.setCiudad(usuarioActualizar.getCiudad());
            usuarioActualizado.setCodigoDesc(usuarioActualizar.getCodigoDesc());
            usuarioservices.GuardarUsuario(usuarioActualizado);
            return ResponseEntity.ok(assambler.toModel(usuarioActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no esta registrado");
        }
    }

    // Funcionamiento verificado || FUNCIONANDO
    @PostMapping("/Registrar")
    @Operation(summary = "Registra  un usuario autenticado con Firebase", description = "Verifica el token enviado desde Firebase y guarda el usuario en la BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = usuario.class))),
    })

    public ResponseEntity<?> registrarUsuario(@RequestBody usuario usuarioNuevo) {

        String firebaseUid = null;

        try {
            // CREAR USUARIO EN FIREBASE
            CreateRequest request = new CreateRequest()
                    .setEmail(usuarioNuevo.getCorreo())
                    .setPassword(usuarioNuevo.getContrasena())
                    .setDisplayName(usuarioNuevo.getNombre());

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            firebaseUid = userRecord.getUid();

            // UID generado por Firebase
            String uid = userRecord.getUid();

            // GUARDAR EN BD
            usuarioNuevo.setUidFb(uid);
            usuario usuarioRegistrado = usuarioservices.GuardarUsuario(usuarioNuevo);

            return ResponseEntity.ok(assambler.toModel(usuarioRegistrado));

        } catch (Exception e) {

            if (firebaseUid != null) {
                try {
                    FirebaseAuth.getInstance().deleteUser(firebaseUid);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("⚠ Error eliminando usuario en Firebase durante rollback: " + ex.getMessage());
                }
            }

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario en Firebase: " + e.getMessage());
        }
    }

    // Funcionamiento verificado || FUNCIONANDO
    @GetMapping("/Personal")
    @Operation(summary = "Devuelve el usuario autenticado y sus datos mediante Firebase")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la BD")
    })
    public ResponseEntity<?> obtenerUsuarioActual(@RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }

            String idToken = authHeader.replace("Bearer ", "").trim();

            String uidFirebase = FirebaseAuth.getInstance().verifyIdToken(idToken).getUid();

            usuario usuarioBD = usuarioservices.buscarUsuarioUID(uidFirebase);

            if (usuarioBD == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no registrado en la BD");
            }

            return ResponseEntity.ok(assambler.toModel(usuarioBD));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + e.getMessage());
        }
    }

    @PutMapping("/Editar")
    @Operation(summary = "Edita los datos del usuario autenticado (excepto rol, run, dv, nombre, uidFb e idUsuario)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en la actualización"),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la BD")
    })
    public ResponseEntity<?> editarUsuario(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EditarUsuarioDTO datos) {

        try {
            // Validar token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado o inválido");
            }

            String idToken = authHeader.replace("Bearer ", "").trim();

            // Decodificar token y obtener UID de Firebase
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uidFirebase = decodedToken.getUid();

            // Buscar y actualizar usuario
            usuario usuarioActualizado = usuarioservices.editarUsuario(uidFirebase, datos);

            // Retornar usuario actualizado (puede ser DTO si quieres no exponer contrasena)
            return ResponseEntity.ok(assambler.toModel(usuarioActualizado));

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado en la BD");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    /*
     * //ENDPOINT PARA editar un usuario segun id
     * 
     * @PutMapping("/E/{ID_USUARIO}") //SOLO PERMITE ACTUALIZAR ESCRIBIENDO TODOS
     * LOS DATOS
     * 
     * @Operation(summary = "ENDPOINT QUE EDITA UN ESTADO DE USUARIO", description =
     * "ENDPOINT QUE EDITA UN ESTADO DE USUARIO",
     * requestBody=@io.swagger.v3.oas.annotations.parameters.RequestBody(
     * description="USUARIO QUE SE VA A EDITAR", required = true, content
     * = @Content(schema = @Schema(implementation = usuario.class))))
     * 
     * @Parameters (value = {
     * 
     * @Parameter (name="ID_USUARIO", description= "ID del usuario que se editara",
     * in = ParameterIn.PATH, required= true)})
     * 
     * @ApiResponses (value = {
     * 
     * @ApiResponse(responseCode = "200", description =
     * "Se edito correctamente el estado del usuario", content = @Content(mediaType
     * = "application/json", schema = @Schema(implementation = usuario.class))),
     * 
     * @ApiResponse(responseCode = "500", description =
     * "Usuario no esta registrado", content = @Content(mediaType =
     * "application/json", schema = @Schema(type = "string", example =
     * "No se puede registrar el usuario")))
     * })
     */

    @DeleteMapping("/{ID_USUARIO}")
    public ResponseEntity<String> EliminarUsuario(@PathVariable Integer ID_USUARIO) {
        try {
            usuario usuarioBuscado = usuarioservices.BuscarUnUsuario(ID_USUARIO);
            usuarioservices.EliminarUsuario(ID_USUARIO);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimina Usuario");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no esta registrado");
        }
    }
    /*
     * @PutMapping("/{uid}")
     * 
     * @Operation(summary = "Actualiza los datos de un usuario por UID")
     * public ResponseEntity<?> actualizarUsuario(@PathVariable String
     * uid, @RequestBody usuario usuarioActualizar) {
     * try {
     * usuario usuarioActualizado = usuarioservices.BuscarUnUsuario(uid);
     * usuarioActualizado.setNombre(usuarioActualizar.getNombre());
     * usuarioActualizado.setDireccion(usuarioActualizar.getDireccion());
     * usuarioActualizado.setCelular(usuarioActualizar.getCelular());
     * usuarioActualizado.setEstado(usuarioActualizar.getEstado());
     * usuarioservices.GuardarUsuario(usuarioActualizado);
     * return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
     * } catch (Exception e) {
     * return
     * ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no está registrado"
     * );
     * }
     * 
     */


}