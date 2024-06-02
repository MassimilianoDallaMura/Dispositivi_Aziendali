package backend_esame4.Dispositivi_Aziendali.service;

import backend_esame4.Dispositivi_Aziendali.dto.DipendenteLoginDto;
import backend_esame4.Dispositivi_Aziendali.exception.UnauthorizedException;
import backend_esame4.Dispositivi_Aziendali.model.Dipendente;
import backend_esame4.Dispositivi_Aziendali.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtTool jwtTool;    //va annotato jwt tool che permette di creare token quando utente si logga.
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //gestisce creazione token quando utente si logga
    public String authenticateDipendenteAndCreateToken(DipendenteLoginDto dipendenteLoginDto){
        Optional<Dipendente> dipendenteOptional =dipendenteService.getDipendenteByEmail(dipendenteLoginDto.getEmail());


        if(dipendenteOptional.isPresent()){
            Dipendente dipendente = dipendenteOptional.get();

            if(passwordEncoder.matches(dipendenteLoginDto.getPassword(), dipendente.getPassword())){
                return jwtTool.createToken(dipendente);
            }
            else{
                throw  new UnauthorizedException("Dipendente non presente. Riloggarsi!");
            }
        }
        else{
            throw  new UnauthorizedException("Dipendente non presente. Riloggarsi!");
        }

    }
}