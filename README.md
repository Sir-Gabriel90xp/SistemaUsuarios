# Sistema de Gestión de Usuarios (Java + SQL Server)

Proyecto con interfaz Swing para gestión de usuarios y login.

## Estudiante 
- Daury Gabriel Robles Mejia
- 2025-2023

## Requisitos

- SQL Server (LocalDB o instancia completa)
- SQL Server Management Studio (SSMS)
- JDK 8+ (para ejecutar la app Java)

## Base de datos (SQL Server)

1. Abre **SSMS** y conéctate a tu instancia.
2. Crea un query nuevo y ejecuta el siguiente script (cópialo tal cual).

```sql
/* ============================================================
   Script SQL Server - Sistema de Gestión de Usuarios (Login)
   Base de datos: SistemaUsuariosDB
   Listo para ejecutar en SQL Server Management Studio (SSMS)
   ============================================================ */

SET NOCOUNT ON;
GO

/* 1) Crear base de datos */
IF DB_ID(N'SistemaUsuariosDB') IS NULL
BEGIN
    CREATE DATABASE SistemaUsuariosDB;
END
GO

USE SistemaUsuariosDB;
GO

/* 2) Tabla Usuarios */
IF OBJECT_ID(N'dbo.Usuarios', N'U') IS NOT NULL
    DROP TABLE dbo.Usuarios;
GO

CREATE TABLE dbo.Usuarios
(
    id        INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Usuarios PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL,
    nombre    VARCHAR(100) NOT NULL,
    apellido  VARCHAR(100) NOT NULL,
    telefono  VARCHAR(30)  NOT NULL,
    email     VARCHAR(150) NOT NULL,
    password  VARCHAR(255) NOT NULL
);
GO

/* 5) Restricciones */
ALTER TABLE dbo.Usuarios
ADD CONSTRAINT UQ_Usuarios_username UNIQUE (username);
GO

/* 3) Usuarios de prueba */
INSERT INTO dbo.Usuarios (username, nombre, apellido, telefono, email, password)
VALUES
('admin', 'Administrador', 'Sistema', '555-0001', 'admin@sistema.com', 'admin123'),
('jperez', 'Juan', 'Pérez', '555-0002', 'juan.perez@correo.com', 'clave123');
GO

/* 4) Procedimientos almacenados */

/* Registrar usuario */
IF OBJECT_ID(N'dbo.sp_RegistrarUsuario', N'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_RegistrarUsuario;
GO

CREATE PROCEDURE dbo.sp_RegistrarUsuario
    @username VARCHAR(50),
    @nombre   VARCHAR(100),
    @apellido VARCHAR(100),
    @telefono VARCHAR(30),
    @email    VARCHAR(150),
    @password VARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.Usuarios (username, nombre, apellido, telefono, email, password)
    VALUES (@username, @nombre, @apellido, @telefono, @email, @password);

    SELECT SCOPE_IDENTITY() AS id;
END
GO

/* Validar login (username + password) */
IF OBJECT_ID(N'dbo.sp_ValidarLogin', N'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_ValidarLogin;
GO

CREATE PROCEDURE dbo.sp_ValidarLogin
    @username VARCHAR(50),
    @password VARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT id, username, nombre, apellido, telefono, email
    FROM dbo.Usuarios
    WHERE username = @username
      AND password = @password;
END
GO

/* Obtener todos los usuarios */
IF OBJECT_ID(N'dbo.sp_ObtenerUsuarios', N'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_ObtenerUsuarios;
GO

CREATE PROCEDURE dbo.sp_ObtenerUsuarios
AS
BEGIN
    SET NOCOUNT ON;

    SELECT id, username, nombre, apellido, telefono, email
    FROM dbo.Usuarios
    ORDER BY id;
END
GO

/* Actualizar usuario */
IF OBJECT_ID(N'dbo.sp_ActualizarUsuario', N'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_ActualizarUsuario;
GO

CREATE PROCEDURE dbo.sp_ActualizarUsuario
    @id       INT,
    @username VARCHAR(50),
    @nombre   VARCHAR(100),
    @apellido VARCHAR(100),
    @telefono VARCHAR(30),
    @email    VARCHAR(150),
    @password VARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.Usuarios
    SET username = @username,
        nombre   = @nombre,
        apellido = @apellido,
        telefono = @telefono,
        email    = @email,
        password = @password
    WHERE id = @id;

    SELECT @@ROWCOUNT AS filas_afectadas;
END
GO

/* Eliminar usuario */
IF OBJECT_ID(N'dbo.sp_EliminarUsuario', N'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_EliminarUsuario;
GO

CREATE PROCEDURE dbo.sp_EliminarUsuario
    @id INT
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM dbo.Usuarios
    WHERE id = @id;

    SELECT @@ROWCOUNT AS filas_afectadas;
END
GO
```

## Pruebas rápidas (SSMS)

```sql
USE SistemaUsuariosDB;
GO

-- Login OK
EXEC dbo.sp_ValidarLogin @username='admin', @password='admin123';

-- Listar usuarios
EXEC dbo.sp_ObtenerUsuarios;

-- Registrar usuario
EXEC dbo.sp_RegistrarUsuario
  @username='mlopez',
  @nombre='María',
  @apellido='López',
  @telefono='555-0003',
  @email='maria.lopez@correo.com',
  @password='pass123';

-- Actualizar usuario (ejemplo: id=1)
EXEC dbo.sp_ActualizarUsuario
  @id=1,
  @username='admin',
  @nombre='Administrador',
  @apellido='Sistema',
  @telefono='555-9999',
  @email='admin@sistema.com',
  @password='admin123';

-- Eliminar usuario (ejemplo: id=2)
EXEC dbo.sp_EliminarUsuario @id=2;
```

