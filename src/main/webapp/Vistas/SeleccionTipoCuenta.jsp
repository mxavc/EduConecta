<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- «JSP» SeleccionTipoCuenta — CU-02, paso 2: tipos de cuenta disponibles --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tipo de cuenta · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<div class="fondo-acceso">
    <main class="tarjeta-acceso ancha">
        <div class="marca">
            <span class="logo">EC</span>
            <h1>EduConecta</h1>
        </div>
        <p class="subtitulo">Seleccione el tipo de cuenta con el que desea registrarse</p>

        <div class="opciones-tipo">
            <a class="opcion-tipo"
               href="${pageContext.request.contextPath}/RegistrarUsuarioControlador?accion=seleccionarTipoCuenta&amp;tipoCuenta=FAMILIAR">
                <span class="icono">&#128106;</span>
                <h3>Familiar</h3>
                <p>Registre a los menores a su cargo y agende citas con especialistas.</p>
            </a>
            <a class="opcion-tipo"
               href="${pageContext.request.contextPath}/RegistrarUsuarioControlador?accion=seleccionarTipoCuenta&amp;tipoCuenta=ESPECIALISTA">
                <span class="icono">&#127891;</span>
                <h3>Especialista</h3>
                <p>Atienda citas, registre atenciones y consulte el historial de los menores.</p>
            </a>
        </div>

        <p class="pie-acceso">
            ¿Ya tiene una cuenta?
            <a class="enlace-simple"
               href="${pageContext.request.contextPath}/IniciarSesionControlador?accion=solicitarIngreso">
                Iniciar sesión
            </a>
        </p>
    </main>
</div>
</body>
</html>
