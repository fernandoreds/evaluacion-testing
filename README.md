# Evaluación de Testing e Integración Continua

Proyecto desarrollado en Java para implementar y demostrar distintas estrategias de testing automatizado e Integración Continua.

El proyecto incluye pruebas unitarias, escenarios BDD, pruebas de rendimiento y un pipeline de GitHub Actions encargado de ejecutar automáticamente las validaciones y almacenar sus resultados.

## Tecnologías utilizadas

- Java 17
- Maven
- JUnit
- Cucumber
- Gherkin
- k6
- GitHub Actions
- Git

## Estructura del proyecto

```text
evaluacion-testing/
├── .github/
│   └── workflows/
│       └── ci.yml
├── performance/
│   └── login-performance.js
├── src/
│   ├── main/
│   │   └── java/
│   │       └── cl/iplacex/testing/
│   │           ├── Calculadora.java
│   │           └── LoginService.java
│   └── test/
│       ├── java/
│       │   └── cl/iplacex/testing/
│       │       ├── CalculadoraTest.java
│       │       └── bdd/
│       │           ├── LoginSteps.java
│       │           └── RunCucumberTest.java
│       └── resources/
│           └── features/
│               └── login.feature
├── pom.xml
└── README.md
```

## Pruebas unitarias

Las pruebas unitarias se implementaron utilizando JUnit.

La clase `CalculadoraTest.java` valida el comportamiento de las operaciones implementadas en `Calculadora.java`.

Estas pruebas forman parte de la ejecución automatizada realizada mediante Maven.

Para ejecutarlas junto con las demás pruebas configuradas en el proyecto:

```bash
mvn test
```

## Pruebas BDD con Cucumber

Para validar el comportamiento del inicio de sesión se implementaron pruebas BDD utilizando Cucumber y Gherkin.

Los escenarios se encuentran definidos en:

```text
src/test/resources/features/login.feature
```

La implementación de los pasos se encuentra en:

```text
src/test/java/cl/iplacex/testing/bdd/LoginSteps.java
```

La configuración para ejecutar Cucumber se encuentra en:

```text
src/test/java/cl/iplacex/testing/bdd/RunCucumberTest.java
```

Las pruebas consideran un inicio de sesión exitoso y diferentes combinaciones de credenciales inválidas.

## Pruebas de rendimiento con k6

La prueba de rendimiento se encuentra definida en:

```text
performance/login-performance.js
```

Durante su ejecución se realizan solicitudes HTTP y se comprueba tanto la respuesta obtenida como su tiempo de respuesta.

Entre las métricas analizadas se encuentran:

- Cantidad de solicitudes HTTP.
- Latencia promedio.
- Percentil 95 de latencia.
- Tasa de errores HTTP.
- Checks exitosos y fallidos.

Los resultados son exportados a un archivo JSON para posteriormente ser almacenados como evidencia y utilizados por el dashboard del pipeline.

## Integración Continua

El proceso de Integración Continua se implementó mediante GitHub Actions.

El workflow se encuentra en:

```text
.github/workflows/ci.yml
```

El pipeline se ejecuta automáticamente ante:

- Push a la rama `main`.
- Push a ramas `feature/**`.
- Pull Request hacia `main`.

Durante cada ejecución se realizan las siguientes tareas:

1. Obtención del código del repositorio.
2. Configuración de Java 17.
3. Compilación del proyecto con Maven.
4. Ejecución de pruebas unitarias y BDD.
5. Publicación de los reportes de pruebas.
6. Instalación de k6.
7. Ejecución de la prueba de rendimiento.
8. Publicación de los resultados de performance.
9. Generación del Dashboard de Integración Continua.

## Artifacts generados

GitHub Actions almacena los resultados de las pruebas como artifacts para conservar evidencia de cada ejecución.

Se generan los siguientes artifacts:

- `reporte-pruebas-unitarias`
- `reporte-cucumber`
- `reporte-performance`

El reporte de performance contiene el resumen generado por k6 y la salida correspondiente a su ejecución.

## Dashboard de Integración Continua

Se implementó un dashboard mediante GitHub Actions Job Summary para visualizar los principales resultados obtenidos durante el pipeline.

El dashboard presenta el estado de los distintos tipos de prueba y las principales métricas obtenidas desde el reporte generado por k6:

- Latencia promedio.
- Latencia p95.
- Tasa de errores HTTP.
- Checks exitosos.
- Checks fallidos.

El resumen se genera automáticamente durante cada ejecución del workflow.

## Thresholds y alertas automáticas de performance

Las pruebas de rendimiento ejecutadas con k6 contienen criterios automáticos de aceptación mediante thresholds.

Los criterios definidos son:

- La latencia del percentil 95 (p95) debe ser menor a 1000 ms.
- La tasa de errores HTTP debe ser menor al 5%.

Estos thresholds permiten detectar automáticamente degradaciones de rendimiento durante la ejecución del pipeline de Integración Continua.

Si alguno de los thresholds no se cumple, k6 finaliza la ejecución con error, provocando que el job de GitHub Actions falle. De esta forma, el estado del pipeline funciona como un mecanismo de alerta automática ante problemas de rendimiento.

Además, los resultados de performance se almacenan como artifacts y las principales métricas se presentan en el Dashboard de Integración Continua mediante GitHub Actions Job Summary.

## Definición de escenarios mediante Three Amigos

Para definir el comportamiento del inicio de sesión se consideraron las tres perspectivas del enfoque Three Amigos: negocio, desarrollo y testing.

### Negocio

Desde la perspectiva del usuario se definió la siguiente necesidad:

> Como usuario registrado, quiero iniciar sesión para acceder al sistema.

Se acordó que un usuario registrado debe poder acceder cuando ingresa sus credenciales correctas y que el sistema debe rechazar el acceso cuando el usuario o la contraseña no corresponden.

### Desarrollo

Desde desarrollo se identificaron los datos necesarios para validar el comportamiento del login: usuario y contraseña.

Para el escenario válido se utilizan las credenciales configuradas para el usuario de prueba. También se consideran variaciones en el usuario y la contraseña para comprobar el comportamiento ante datos incorrectos.

### Testing

Los criterios acordados se transformaron en escenarios BDD utilizando Gherkin y Cucumber.

Se definieron los siguientes casos:

- Inicio de sesión exitoso con usuario `fernando` y contraseña `1234`.
- Inicio de sesión rechazado con usuario correcto y contraseña incorrecta.
- Inicio de sesión rechazado con un usuario incorrecto, aunque se utilice la contraseña válida del usuario registrado.

Los casos inválidos se implementaron mediante un `Scenario Outline`, lo que permite reutilizar el mismo escenario con distintas combinaciones de datos.

### Criterios de aceptación acordados

1. Dado un usuario registrado, cuando ingresa credenciales válidas, el acceso debe ser exitoso.
2. Dado un usuario registrado, cuando ingresa una contraseña incorrecta, el acceso debe ser rechazado.
3. Dado un usuario registrado, cuando ingresa un usuario incorrecto, el acceso debe ser rechazado.

Estos criterios se encuentran implementados como escenarios ejecutables de Cucumber y se validan automáticamente mediante `mvn test` dentro del pipeline de Integración Continua.

## Ejecución local

Para ejecutar las pruebas localmente se requiere tener instalado Java 17 y Maven.

Desde la raíz del proyecto se puede ejecutar:

```bash
mvn clean test
```

Este comando compila el proyecto y ejecuta las pruebas automatizadas configuradas con JUnit y Cucumber.