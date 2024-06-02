package backend_esame4.Dispositivi_Aziendali.model;

import backend_esame4.Dispositivi_Aziendali.enums.StatoDispositivo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Dispositivo {
    @Id
    @GeneratedValue
    private int id;
    @Enumerated(EnumType.STRING)
    private StatoDispositivo statoDispositivo;

    private String tipo;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

}