# DOSW-Library
Es una API REST para la gestión de una biblioteca universitaria, desarrollada como proyecto académico del curso DOSW 2 
en la Escuela Colombiana de Ingeniería Julio Garavito. El sistema permite administrar usuarios, libros y préstamos, 
implementando una arquitectura en capas, con validaciones de negocio, manejo 
centralizado de excepciones y cobertura de pruebas

### Funcionalidades principales
- Registro y consulta de usuarios
- Gestión del catálogo de libros (CRUD completo)
- Control de préstamos: creación, devolución y consulta de historial
- Documentación interactiva de la API con Swagger UI
- Validaciones de negocio: límite de préstamos activos, disponibilidad de libros, etc.

# Tabla de contenido  
[1. Descripción del proyecto](#descripción-del-proyecto)  

[2. Tecnologías y requisitos previos](#tecnologías-y-requisitos-previos) 

[3. Instalación y ejecución](#instalación-y-ejecución) 

[4. Estructura del proyecto](#estructura-del-proyecto) 

[5. Diagramas](#diagramas) 

[5.1 Diagrama general](#diagrama-general) 

[5.2 Diagrama específico](#diagrama-específico) 

[5.3 Diagrama de clases](#diagrama-de-clases) 

[6. Documentación de la API (Swagger)](#documentación-de-la-api) 

[7. Pruebas y cobertura](#pruebas-y-cobertura) 

[8. Manejo de errores](#manejo-de-errores) 

[9. Autores](#autores) 

# Tecnologías y requisitos previos

### Stack utilizado
- Java 21 — lenguaje principal

- Spring Boot 3.x — framework web y de inyección de dependencias

- Springdoc OpenAPI (Swagger UI) — documentación interactiva

- JUnit 5 + Mockito — pruebas unitarias y funcionales

- JaCoCo — reporte de cobertura de pruebas

- Checkstyle / SpotBugs — análisis estático

- Maven — gestión de dependencias y ciclo de build

### Requisitos previos
- Java 21 instalado (java -version)

- Maven 3.8+ (mvn -version)

- Git

# Estructura de proyecto
```
src/
├── main/
│   └── java/edu/eci/dosw/tdd/
│       ├── controller/
│       │   ├── dto/
│       │   │   ├── BookDTO.java
│       │   │   ├── LoanDTO.java
│       │   │   └── UserDTO.java
│       │   ├── mapper/
│       │   │   ├── BookMapper.java
│       │   │   ├── LoanMapper.java
│       │   │   └── UserMapper.java
│       │   ├── BookController.java
│       │   ├── ErrorResponse.java
│       │   ├── GlobalExceptionHandler.java
│       │   ├── LoanController.java
│       │   └── UserController.java
│       ├── core/
│       │   ├── exception/
│       │   │   ├── BookNotAvailableException.java
│       │   │   ├── LoanLimitExceededException.java
│       │   │   └── UserNotFoundException.java
│       │   ├── model/
│       │   │   ├── Book.java
│       │   │   ├── Loan.java
│       │   │   ├── Status.java
│       │   │   └── User.java
│       │   ├── service/
│       │   │   ├── BookService.java
│       │   │   ├── LoanService.java
│       │   │   └── UserService.java
│       │   ├── util/
│       │   │   ├── DateUtil.java
│       │   │   ├── IdGeneratorUtil.java
│       │   │   └── ValidationUtil.java
│       │   └── validator/
│       │       ├── BookValidator.java
│       │       ├── LoanValidator.java
│       │       └── UserValidator.java
│       └── DoswLibraryApplication.java
└── test/
    └── java/edu/eci/dosw/tdd/
        ├── BookControllerTest.java
        ├── BookServiceTest.java
        ├── LoanControllerTest.java
        ├── LoanServiceTest.java
        ├── UserControllerTest.java
        └── UserServiceTest.java
```

# Diagramas 

## Diagrama de Componentes Generales



