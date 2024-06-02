package backend_esame4.Dispositivi_Aziendali.service;

import backend_esame4.Dispositivi_Aziendali.dto.DipendenteDto;

import backend_esame4.Dispositivi_Aziendali.enums.Role;
import backend_esame4.Dispositivi_Aziendali.exception.BadRequestException;
import backend_esame4.Dispositivi_Aziendali.exception.DipendenteNonTrovatoException;

import backend_esame4.Dispositivi_Aziendali.model.Dipendente;


import backend_esame4.Dispositivi_Aziendali.repository.DipendenteRepository;
import backend_esame4.Dispositivi_Aziendali.repository.DispositivoRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class DipendenteService {
    @Autowired
    private DipendenteRepository dipendenteRepository;
    @Autowired
    private DispositivoRepository dispositivoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    public String saveDipendente(DipendenteDto dipendenteDto) {
        if(getDipendenteByEmail(dipendenteDto.getEmail()).isEmpty()){
            Dipendente dipendente = new Dipendente();
            dipendente.setNome(dipendenteDto.getNome());
            dipendente.setCognome(dipendenteDto.getCognome());
            dipendente.setEmail(dipendenteDto.getEmail());
            dipendente.setPassword(passwordEncoder.encode(dipendenteDto.getPassword())); //serve per codificare la password
            dipendente.setRole(Role.USER);

            dipendenteRepository.save(dipendente);
            return "Dipendente con username " + dipendente.getUsername() + " salvato correttamente";
        }
        else{
            throw new BadRequestException("L'email del dipendente " + dipendenteDto.getEmail() + " già presente");
        }

    }

    public List<Dipendente> getDipendenti(){
        return dipendenteRepository.findAll();
    }

    public Optional<Dipendente> getDipendenteByUsername(int username){
        return dipendenteRepository.findById(String.valueOf(username));
    }

    public  Dipendente updateDipendente(int username, DipendenteDto dipendenteDto){
        Optional<Dipendente> dipendenteOptional = getDipendenteByUsername(username);

        if (dipendenteOptional.isPresent()) {
            Dipendente dipendente = dipendenteOptional.get();
            dipendente.setNome(dipendenteDto.getNome());
            dipendente.setCognome(dipendenteDto.getCognome());
            dipendente.setEmail(dipendenteDto.getEmail());
            dipendente.setPassword(passwordEncoder.encode(dipendenteDto.getPassword())); //serve per codificare la password
            return dipendenteRepository.save(dipendente);
        }
        else {
            throw new DipendenteNonTrovatoException("Dipendente con username=" + username + " non trovato");
        }
    }

    public String deleteDipendente(int username) {
        Optional<Dipendente> dipendenteOptional = getDipendenteByUsername(username);

        if (dipendenteOptional.isPresent()) {
            dipendenteRepository.delete(dipendenteOptional.get());
            return "Studente con username=" + username + " cancellato con successo";
        } else {
            throw new DipendenteNonTrovatoException("Dipendente con username=" + username + " non trovato");
        }
    }


    public String patchFotoDipendente(int username, MultipartFile foto) throws IOException {
        Optional<Dipendente> dipendenteOptional = getDipendenteByUsername(username);

        if(dipendenteOptional.isPresent()){           //upload invio della foto    prendiamo la mappa   get da il valore di map
            String url = (String) cloudinary.uploader().upload(foto.getBytes(), Collections.emptyMap()).get("url");  //restituisce una mappa. Da questa estraiamo il valore della chiave url
            Dipendente dipendente = dipendenteOptional.get(); //estrazione del dipendente e associazione
            dipendente.setFoto(url);
            dipendenteRepository.save(dipendente);
            return "Dipendente con username=" + username + " aggiornato correttamente con la foto inviata";
        }
        else{
            throw new DipendenteNonTrovatoException("Dipendente con username=" + username + " non trovato");
        }
    }

    public Optional<Dipendente> getDipendenteByEmail(String email){
        return dipendenteRepository.findByEmail(email);

    }

}
