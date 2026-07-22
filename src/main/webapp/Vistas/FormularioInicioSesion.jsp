<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- «JSP» FormularioInicioSesion — CU-01 / CU-02 (Mapa navegacional: Acceso y registro) --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<div class="fondo-acceso">
    <main class="tarjeta-acceso">
        <div class="marca">
            <span class="logo">EC</span>
            <h1>EduConecta</h1>
        </div>
        <p class="subtitulo">Plataforma de vinculación y gestión de atención psicopedagógica</p>

        <c:if test="${not empty mensajeError}">
            <div class="alerta alerta-error">${mensajeError}</div>
        </c:if>
        <c:if test="${not empty mensajeExito}">
            <div class="alerta alerta-exito">${mensajeExito}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/IniciarSesionControlador" method="post">
            <input type="hidden" name="accion" value="confirmarIngreso">
            <div class="campo">
                <label for="correo">Correo electrónico</label>
                <input type="email" id="correo" name="correo" value="${param.correo}"
                       placeholder="nombre@correo.com" autocomplete="username">
            </div>
            <div class="campo">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena"
                       placeholder="Su contraseña" autocomplete="current-password">
            </div>
            <button type="submit" class="btn btn-primario btn-bloque">Entrar</button>
        </form>

        <p class="pie-acceso">
            ¿Aún no tiene una cuenta?
            <a class="enlace-simple"
               href="${pageContext.request.contextPath}/RegistrarUsuarioControlador?accion=solicitarRegistro">
                Registrarse
            </a>
        </p>
    </main>
</div>
</body>
</html>
