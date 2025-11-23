package cl.bakery.Usuarios.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Todos los productos registrados en la empresa")
public class EditarUsuarioDTO {

    private String apellidoPaterno;
    private String apellidoMaterno;
    private String usuario;
    private String correo;
    private int telefono;
    private String fechaNacimiento;
    private String pais;
    private String ciudad;
    private String direccion;
    private String codigoDesc;
    private String estado;

}
