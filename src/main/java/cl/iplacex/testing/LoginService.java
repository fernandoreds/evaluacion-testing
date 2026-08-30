package cl.iplacex.testing;

public class LoginService {

    public boolean login(String usuario, String password) {
        return "fernando".equals(usuario)
                && "1234".equals(password);
    }
}