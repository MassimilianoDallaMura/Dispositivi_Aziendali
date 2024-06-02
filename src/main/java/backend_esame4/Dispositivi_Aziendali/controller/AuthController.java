package backend_esame4.Dispositivi_Aziendali.controller;

import backend_esame4.Dispositivi_Aziendali.dto.DipendenteDto;
import backend_esame4.Dispositivi_Aziendali.dto.DipendenteLoginDto;
import backend_esame4.Dispositivi_Aziendali.exception.BadRequestException;
import backend_esame4.Dispositivi_Aziendali.service.AuthService;
import backend_esame4.Dispositivi_Aziendali.service.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private AuthService authService;
    @PostMapping("/auth/register")
    public String register(@RequestBody @Validated DipendenteDto dipendenteDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).
                    reduce("", (s, s2) -> s+s2));
        }

        return dipendenteService.saveDipendente(dipendenteDto);
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody @Validated DipendenteLoginDto dipendenteLoginDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).
                    reduce("", (s, s2) -> s+s2));
        }

        return authService.authenticateDipendenteAndCreateToken(dipendenteLoginDto);
    }
}