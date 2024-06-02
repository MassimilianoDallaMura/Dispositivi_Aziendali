package backend_esame4.Dispositivi_Aziendali.service;

import backend_esame4.Dispositivi_Aziendali.dto.DipendenteDto;
import backend_esame4.Dispositivi_Aziendali.dto.DispositivoDto;
import backend_esame4.Dispositivi_Aziendali.enums.StatoDispositivo;
import backend_esame4.Dispositivi_Aziendali.exception.DipendenteNonTrovatoException;
import backend_esame4.Dispositivi_Aziendali.exception.DispositivoNonTrovatoException;
import backend_esame4.Dispositivi_Aziendali.model.Dipendente;
import backend_esame4.Dispositivi_Aziendali.model.Dispositivo;
import backend_esame4.Dispositivi_Aziendali.repository.DipendenteRepository;
import backend_esame4.Dispositivi_Aziendali.repository.DispositivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;
    @Autowired
    DipendenteService dipendenteService;


    public String saveDispositivo(DispositivoDto dispositivoDto){     //l'aula arriva dal client e va salvata nel db
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setTipo(dispositivoDto.getTipo());
        dispositivo.setStatoDispositivo(dispositivoDto.getStatoDispositivo());

        Optional<Dipendente> dipendenteOptional = dipendenteService.getDipendenteByUsername(dispositivoDto.getDipendenteId());

        if(dipendenteOptional.isPresent()){
            Dipendente dipendente = dipendenteOptional.get();
            dispositivo.setDipendente(dipendente);

            dispositivoRepository.save(dispositivo);
            return "Dispositivo con id=" + dispositivo.getId() + " aggiunto con successo";
        }
        else{
            throw new DipendenteNonTrovatoException("Dipendente con id=" + dispositivoDto.getDipendenteId() + " da associare al dispositivo non trovato");
        }

    }


    public Optional<Dispositivo> getDispositivoById(int id){
        return dispositivoRepository.findById((long) id);
    }

    public List<Dispositivo> getDispositivi(){
        return dispositivoRepository.findAll();
    }

    public String deleteDispositivo(int id) {
        Optional<Dispositivo> dispositivoOptional = getDispositivoById(id);

        if (dispositivoOptional.isPresent()) {
            dispositivoRepository.delete(dispositivoOptional.get());
            return "Dispositivo con id=" + id + " cancellato con successo";
        } else {
            throw new DispositivoNonTrovatoException("Dispositivo con id=" + id + " non trovato");
        }
    }


    public  Dispositivo updateDispositivo(int id, DispositivoDto dispositivoDto){
        Optional<Dispositivo> dispositivoOptional = getDispositivoById(id);

        if (dispositivoOptional.isPresent()){
            Dispositivo dispositivo = dispositivoOptional.get();
            dispositivo.setStatoDispositivo(dispositivoDto.getStatoDispositivo());
            dispositivo.setTipo(dispositivoDto.getTipo());

            Optional<Dipendente> dipendenteOptional = dipendenteService.getDipendenteByUsername(dispositivoDto.getDipendenteId());

            if(dipendenteOptional.isPresent()){
                Dipendente dipendente = dipendenteOptional.get();
                dispositivo.setDipendente(dipendente);

                return dispositivoRepository.save(dispositivo);

            }
            else{
                throw new DipendenteNonTrovatoException("Dipendente con id=" + dispositivoDto.getDipendenteId() + " da associare al dispositivo non trovato");
            }


        }
        else {
            throw new DispositivoNonTrovatoException("Dispositivo con id=" + getDispositivoById(id) + " non trovato");
        }
    }

    public Dispositivo assignDipendenteToDispositivo(int id, int dipendenteId) {
        Optional<Dispositivo> dispositivoOptional = getDispositivoById(id);
        Optional<Dipendente> dipendenteOptional = dipendenteService.getDipendenteByUsername(dipendenteId);

        if (dispositivoOptional.isPresent() && dipendenteOptional.isPresent()) {
            Dispositivo dispositivo = dispositivoOptional.get();
            Dipendente dipendente = dipendenteOptional.get();
            dispositivo.setStatoDispositivo(StatoDispositivo.ASSEGNATO);
            dispositivo.setDipendente(dipendente);
            return dispositivoRepository.save(dispositivo);
        } else {
            if (!dispositivoOptional.isPresent()) {
                throw new DispositivoNonTrovatoException("Dispositivo con id=" + id + " non trovato");
            } else {
                throw new DipendenteNonTrovatoException("Dipendente con id=" + dipendenteId + " non trovato");
            }
        }
    }

}
