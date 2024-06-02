package backend_esame4.Dispositivi_Aziendali.controller;


import backend_esame4.Dispositivi_Aziendali.dto.DispositivoDto;
import backend_esame4.Dispositivi_Aziendali.exception.BadRequestException;
import backend_esame4.Dispositivi_Aziendali.exception.DispositivoNonTrovatoException;
import backend_esame4.Dispositivi_Aziendali.model.Dispositivo;
import backend_esame4.Dispositivi_Aziendali.service.DispositivoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Optional;

@RestController
public class DispositivoController {
    @Autowired
    private DispositivoService dispositivoService;

    @PostMapping("/api/dispositivi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveDispositivo(@RequestBody @Validated DispositivoDto dispositivoDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException((bindingResult.getAllErrors().stream(). //creiamo una stringa unica con tt gli errori
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s+s2))); //map da ad ogni errore il messaggio; reduce per fusione che concatena accumulatore con ogni stringa
        }
        return dispositivoService.saveDispositivo(dispositivoDto);
    }

    @GetMapping("/api/dispositivi")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Dispositivo> getDispositivi(){
        return dispositivoService.getDispositivi();
    }

    @GetMapping("/api/dispositivi/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Dispositivo getDispositivoById(@PathVariable int id) throws DispositivoNonTrovatoException{
        Optional<Dispositivo> dispositivoOptional = dispositivoService.getDispositivoById(id);

        if (dispositivoOptional.isPresent()){
            return dispositivoOptional.get();
        }
        else{
            throw new DispositivoNonTrovatoException("Dispositivo con id=" + id + " non trovato");
        }
    }

    @PutMapping("/api/dispositivi/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dispositivo updateDispositivo(@PathVariable int id, @RequestBody @Validated DispositivoDto dispositivoDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException((bindingResult.getAllErrors().stream(). //creiamo una stringa unica con tt gli errori
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s+s2))); //map da ad ogni errore il messaggio; reduce per fusione che concatena accumulatore con ogni stringa
        }
        return dispositivoService.updateDispositivo(id, dispositivoDto);

    }
    @PostMapping("/api/dispositivi/{id}/assegna")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dispositivo assignDipendenteToDispositivo(@PathVariable int id, @RequestParam int dipendenteId) {
        return dispositivoService.assignDipendenteToDispositivo(id, dipendenteId);
    }


    @DeleteMapping("/api/dispositivi/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteDispositivo(@PathVariable int id) throws DispositivoNonTrovatoException {
        return dispositivoService.deleteDispositivo(id);
    }
}