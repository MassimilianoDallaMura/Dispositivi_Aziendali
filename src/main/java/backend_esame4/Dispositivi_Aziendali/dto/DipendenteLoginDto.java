package backend_esame4.Dispositivi_Aziendali.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DipendenteLoginDto {

    @Email(message = "Deve essere un indirizzo email valido")
    @NotBlank(message = "L'email non può essere vuota")
    private String email;
    @NotBlank(message = "La password non può essere vuota")
    private String password;
}
