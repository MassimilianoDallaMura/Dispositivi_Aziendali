package backend_esame4.Dispositivi_Aziendali.security;

import backend_esame4.Dispositivi_Aziendali.exception.UnauthorizedException;
import backend_esame4.Dispositivi_Aziendali.model.Dipendente;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    //dichiaro variabili d'istanza
    @Value("${jwt.secret}")     //recupera chiave segreta nel .properties
    private String secret;
    @Value("${jwt.duration}")
    private long duration;

    //metodi per gestire il token
    public String createToken(Dipendente dipendente){   //effettua la verifica del token ricevuto. Verifica la veridicità del token e la sua scadenza
        return Jwts.builder().issuedAt(new Date(System.currentTimeMillis())). //jwts fa parte della libreria. Il builder crea a cascata oggetto con i dati. IssuedAt imposta data creazione e vuole metodo date
                expiration(new Date(System.currentTimeMillis()+duration)). //imposta scadenza. duration(quanto deve durate, impostato nel enc.properies
                subject(String.valueOf(dipendente.getUsername())). //imposta il subject: indica a quale utente associare il token. Si indica il parametro con cui associare (username)
                signWith(Keys.hmacShaKeyFor(secret.getBytes())). //firma del token. La classe keys ha metodo statico hmacSh che crea firma criptata del token. vuole array di byte come parametro di ingresso
                compact(); //chiude la creazione del token
    }

    public void verifyToken(String token){      //verifica token quando c'è una richiesta
        try {
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).   //viene creata un'altra firma con la chiave segreta. se le due firme sono uguali vuole dire che non era stato modificata
                build().parse(token); //parse lancia molte eccezion. O si gestiscono singolarmente, o si mette try catch
    }
        catch (Exception e){
            throw new UnauthorizedException("Error in authorization, relogin!");       //qualsiasi eccezione lanciata intercetterà il catch e verrà trasformata in unauthorizedExc
        }
    }

    public int getUsernameFromDipendente(String token){     //estraggo l'id criptato dentro il token e in jwtfilter...
        return Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).
                build().parseSignedClaims(token).getPayload().getSubject());
    }
}
