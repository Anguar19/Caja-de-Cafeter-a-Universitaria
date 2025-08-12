Sistema de Cafetería Universitaria
Descripción
Sistema de punto de venta (POS) para cafetería universitaria desarrollado en Java con interfaz gráfica Swing, base de datos SQL Server y arquitectura en capas.
Características Principales

🔐 Login seguro con encriptación SHA-256
📦 Gestión de Productos (CRUD completo)
💰 Procesamiento de Ventas con cálculo automático de impuestos
🧮 Calculadora integrada con operaciones básicas y avanzadas
📊 Historial de ventas del día
🎫 Generación de tickets en formato TXT
📝 Sistema de logs para auditoría

Requisitos del Sistema

Java JDK 11 o superior
SQL Server 2017 o superior (Express Edition es suficiente)
Maven 3.6 o superior
512 MB RAM mínimo
100 MB espacio en disco
SQL Server Management Studio (SSMS) - Opcional pero recomendado

Instalación
1. Clonar el repositorio
bashgit clone https://github.com/tu-usuario/cafeteria-universitaria.git
cd cafeteria-universitaria
2. Configurar SQL Server
Verificar que SQL Server esté ejecutándose:
bash# En Windows Services
services.msc
# Buscar: SQL Server (MSSQLSERVER) o SQL Server (SQLEXPRESS)
Crear la base de datos y tablas:
bash# Opción 1: Usando sqlcmd
sqlcmd -S localhost\SQLEXPRESS -i src/main/resources/database/schema.sql

# Opción 2: Usando SQL Server Management Studio (SSMS)
# Abrir SSMS → Conectar → Archivo → Abrir → Ejecutar schema.sql
Cargar datos de prueba:
bash# Opción 1: Usando sqlcmd
sqlcmd -S localhost\SQLEXPRESS -d cafeteria_db -i src/main/resources/database/data.sql

# Opción 2: Usando SSMS
# En SSMS → Seleccionar cafeteria_db → Ejecutar data.sql
3. Configurar conexión a BD
Agregar dependencia de SQL Server en pom.xml:
xml<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.4.2.jre11</version>
</dependency>
Editar el archivo DatabaseConnection.java:
java// Para SQL Server con instancia nombrada (Express)
private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=cafeteria_db;encrypt=false;trustServerCertificate=true";

// Para SQL Server con instancia predeterminada
// private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=cafeteria_db;encrypt=false;trustServerCertificate=true";

private static final String USER = "sa";  // o tu usuario de SQL Server
private static final String PASSWORD = "tu_password";
4. Compilar el proyecto
bashmvn clean compile
5. Ejecutar la aplicación
bashmvn exec:java -Dexec.mainClass="com.cafeteria.app.MainApp"
O generar JAR ejecutable:
bashmvn clean package
java -jar target/cafeteria-universitaria-1.0.0.jar
Configuración de SQL Server
Habilitar Autenticación Mixta (si es necesario)

Abrir SQL Server Management Studio
Conectar al servidor → Click derecho → Properties
Security → SQL Server and Windows Authentication mode
Reiniciar el servicio de SQL Server

Crear usuario para la aplicación (opcional pero recomendado)
sql-- Crear login
CREATE LOGIN cafeteria_user WITH PASSWORD = 'Caf3t3r1a2024!';

-- Usar la base de datos
USE cafeteria_db;

-- Crear usuario y asignar permisos
CREATE USER cafeteria_user FOR LOGIN cafeteria_user;
EXEC sp_addrolemember 'db_owner', 'cafeteria_user';
Verificar conectividad
bash# Test de conexión con sqlcmd
sqlcmd -S localhost\SQLEXPRESS -U sa -P tu_password -Q "SELECT @@VERSION"

# Verificar puerto (default 1433)
netstat -an | findstr 1433
Uso del Sistema
Credenciales de Acceso

Usuario: admin / cajero1 / cajero2
Contraseña: admin123

Flujo Principal

Iniciar sesión con las credenciales proporcionadas
Gestionar productos desde el menú Productos
Crear nueva venta desde Archivo > Nueva Venta
Agregar productos a la venta actual
Aplicar descuento si es necesario
Procesar venta y generar ticket
Consultar historial desde el menú Ventas

Calculadora
Accesible desde Herramientas > Calculadora

Operaciones básicas: +, -, ×, ÷
Operaciones avanzadas: Potencia (^), Porcentaje (%), Módulo (MOD)

Estructura del Proyecto
cafeteria-universitaria/
├── pom.xml                    # Configuración Maven
├── README.md                  # Este archivo
├── src/
│   └── main/
│       ├── java/
│       │   └── com/cafeteria/
│       │       ├── app/       # Clase principal
│       │       ├── ui/        # Interfaces gráficas
│       │       ├── dominio/   # Modelos de datos
│       │       ├── servicio/  # Lógica de negocio
│       │       ├── infrastructura/ # Acceso a datos
│       │       └── utilidades/    # Utilidades
│       └── resources/
│           └── database/      # Scripts SQL
└── target/                    # Archivos compilados
Arquitectura
Patrón de Diseño

Arquitectura en Capas (Layered Architecture)
Patrón Repository para acceso a datos
Patrón Singleton para servicios y conexión BD
MVC para la interfaz gráfica

Tecnologías Utilizadas

Java 11 - Lenguaje de programación
Swing - Interfaz gráfica
SQL Server - Base de datos
JDBC - Conexión a BD
Maven - Gestión de dependencias
SHA-256 - Encriptación de contraseñas

Características Técnicas Implementadas
POO y Modularidad

✅ Clases bien definidas con encapsulación
✅ Herencia y polimorfismo donde aplica
✅ Separación en paquetes por responsabilidad
✅ Bajo acoplamiento entre capas

Control de Flujo

✅ Estructuras if/else para validaciones
✅ Switch para operaciones de calculadora
✅ Bucle for tradicional para cálculos
✅ While para procesamiento iterativo
✅ Do-while para búsquedas

Manejo de Errores

✅ Try-catch en todas las operaciones críticas
✅ Logging de errores con stacktrace
✅ Mensajes amigables al usuario
✅ Rollback en transacciones de BD

Base de Datos

✅ PreparedStatements (prevención SQL Injection)
✅ Transacciones para integridad de datos
✅ Índices para optimización
✅ Foreign Keys para integridad referencial

Diagrama UML de Clases
mermaidclassDiagram
    class Usuario {
        -int id
        -String username
        -String password
        -String rol
        -boolean activo
        -LocalDateTime creado
        +getters()
        +setters()
    }
    
    class Producto {
        -int id
        -String nombre
        -double precioUnitario
        -boolean activo
        -LocalDateTime creado
        +getters()
        +setters()
    }
    
    class Venta {
        -int id
        -int userId
        -LocalDateTime fechaHora
        -double subtotal
        -double impuestoIVA
        -double impuestoIVI
        -double descuento
        -double total
        -List~DetalleVenta~ detalles
        +calcularTotales()
        +getters()
        +setters()
    }
    
    class DetalleVenta {
        -int id
        -int ventaId
        -int productoId
        -String productoNombre
        -int cantidad
        -double precioUnitario
        -double totalLinea
        +getters()
        +setters()
    }
    
    class AuthService {
        -Usuario usuarioActual
        -int intentosFallidos
        +login(username, password)
        +logout()
        +getUsuarioActual()
    }
    
    class ProductoService {
        +obtenerTodosLosProductos()
        +crearProducto()
        +actualizarProducto()
        +toggleActivoProducto()
    }
    
    class VentaService {
        +procesarVenta()
        +obtenerVentasDelDia()
        +calcularTotalVentasDelDia()
    }
    
    class DatabaseConnection {
        -Connection connection
        +getInstance()
        +getConnection()
    }
    
    Venta "1" --> "*" DetalleVenta
    Venta "*" --> "1" Usuario
    DetalleVenta "*" --> "1" Producto
    AuthService --> Usuario
    ProductoService --> Producto
    VentaService --> Venta
    AuthService --> DatabaseConnection
    ProductoService --> DatabaseConnection
    VentaService --> DatabaseConnection
