package cl.iplacex.testing.bdd;

import cl.iplacex.testing.LoginService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private LoginService loginService;
    private boolean resultado;

    @Given("que el usuario se encuentra registrado")
    public void usuarioRegistrado() {
        loginService = new LoginService();
    }

    @When("ingresa el usuario {string} y la password {string}")
    public void ingresarCredenciales(String usuario, String password) {
        resultado = loginService.login(usuario, password);
    }

    @Then("el acceso debe ser exitoso")
    public void accesoExitoso() {
        assertTrue(resultado);
    }

    @Then("el acceso debe ser rechazado")
    public void accesoRechazado() {
        assertFalse(resultado);
    }
}