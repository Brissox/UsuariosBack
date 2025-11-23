package cl.bakery.Usuarios.Model;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USUARIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Todos los productos registrados en la empresa")

public class usuario {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "ID_USUARIO")
@Schema(description = "Identificador del usuario", example = "1")
private Long idUsuario;

// Si usas Firebase, descomenta esta línea y elimina el ID autogenerado

@Column(name = "U_ID", length = 128)
@Schema(description = "UID proporcionado por Firebase", example = "hYtU92lKp1sXq9zT2N7Q8W")
private String uidFb;

@Column(name = "NOMBRE", nullable = false, length = 50)
@Schema(description = "Nombre del usuario", example = "Juan")
private String nombre;

@Column(name = "APELLIDO_PATERNO", nullable = false, length = 30)
@Schema(description = "Apellido paterno del usuario", example = "Perez")
private String apellidoPaterno;

@Column(name = "APELLIDO_MATERNO", length = 30)
@Schema(description = "Apellido materno del usuario", example = "Rojas")
private String apellidoMaterno;

@Column(name = "USUARIO", nullable = false, length = 30, unique = true)
@Schema(description = "Nombre de usuario del sistema", example = "juan123")
private String usuario;

@Column(name = "CORREO", nullable = false, length = 100, unique = true)
@Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
private String correo;

@Column(name = "CONTRASENA", nullable = false, length = 20)
@Schema(description = "Contraseña del usuario", example = "********")
private String contrasena;

@Column(name = "TELEFONO", length = 9)
@Schema(description = "Teléfono de contacto", example = "987654321")
private Long telefono;

@Column(name = "FECHA_NACIMIENTO")
@Schema(description = "Fecha de nacimiento", example = "1990-05-21")
private Date fechaNacimiento;

@Column(name = "PAIS", length = 30)
@Schema(description = "País del usuario", example = "Chile")
private String pais;

@Column(name = "CIUDAD", length = 30)
@Schema(description = "Ciudad del usuario", example = "Santiago")
private String ciudad;

@Column(name = "DIRECCION", length = 100)
@Schema(description = "Dirección del domicilio", example = "Av. Libertador 1234")
private String direccion;

@Column(name = "CODIGO_DESC", length = 30)
@Schema(description = "Código de descuento asignado al usuario", example = "WELCOME10")
private String codigoDesc;

@Column(name = "RUN", nullable = false, length = 9)
@Schema(description = "RUN sin puntos ni DV", example = "11222333")
private Integer run;

@Column(name = "DV", nullable = false, length = 1)
@Schema(description = "Dígito verificador del RUN", example = "K")
private String dv;


@Column(name = "ESTADO", nullable = false, length = 1)
@Schema(description = "Estado del usuario (A = Activo, I = Inactivo)", example = "A")
private String estado = "A";

@ManyToOne
@JoinColumn(name = "ID_ROL", nullable = true)
private Rol rol;
}
