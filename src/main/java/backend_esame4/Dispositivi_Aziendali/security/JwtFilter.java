package backend_esame4.Dispositivi_Aziendali.security;

import backend_esame4.Dispositivi_Aziendali.exception.DipendenteNonTrovatoException;
import backend_esame4.Dispositivi_Aziendali.exception.UnauthorizedException;
import backend_esame4.Dispositivi_Aziendali.model.Dipendente;
import backend_esame4.Dispositivi_Aziendali.service.DipendenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTool jwtTool;
    @Autowired
    private DipendenteService dipendenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            throw  new UnauthorizedException("La richiesta non ha un token. Riloggarsi!");
        }

        String token = authHeader.substring(7);

        jwtTool.verifyToken(token); // dopo verifica token

        int username = jwtTool.getUsernameFromDipendente(token);  //...estraggo dal token l'utername del dipendente col metodo del jwtTool

        Optional<Dipendente> dipendenteOptional = dipendenteService.getDipendenteByUsername(username);

        if(dipendenteOptional.isPresent()){
            Dipendente dipendente = dipendenteOptional.get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(dipendente, null, dipendente.getAuthorities()); //3 parametri vuole le credenziali ma non le diamo perché le gestiamo col token
            SecurityContextHolder.getContext().setAuthentication(authentication); //richiamo contesto sicurezza, e lo setto con autentication appena creata
        }
        else{
            throw new UnauthorizedException("Dipendente non trovato nel token. Riloggati");
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
