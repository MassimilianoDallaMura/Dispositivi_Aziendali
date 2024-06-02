package backend_esame4.Dispositivi_Aziendali.dto;

import backend_esame4.Dispositivi_Aziendali.enums.StatoDispositivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data



public class DispositivoDto {

    @NotNull(message = "Lo stato del dispositivo non può essere nullo")
    private StatoDispositivo statoDispositivo;
    @NotNull(message = "L'ID del dipendente non può essere nullo")
    private  int dipendenteId;
    @NotBlank(message = "Il tipo non può essere vuoto")
    private String tipo;


}