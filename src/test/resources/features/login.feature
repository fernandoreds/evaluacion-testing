Feature: Inicio de sesion

  Como usuario registrado
  quiero iniciar sesion
  para acceder al sistema

  Scenario: Inicio de sesion exitoso
    Given que el usuario se encuentra registrado
    When ingresa el usuario "fernando" y la password "1234"
    Then el acceso debe ser exitoso

  Scenario Outline: Inicio de sesion con credenciales invalidas
    Given que el usuario se encuentra registrado
    When ingresa el usuario "<usuario>" y la password "<password>"
    Then el acceso debe ser rechazado

    Examples:
      | usuario  | password   |
      | fernando | incorrecta |
      | otro     | 1234       |