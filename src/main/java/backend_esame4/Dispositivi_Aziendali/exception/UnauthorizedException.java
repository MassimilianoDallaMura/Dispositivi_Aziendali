package backend_esame4.Dispositivi_Aziendali.exception;


public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
