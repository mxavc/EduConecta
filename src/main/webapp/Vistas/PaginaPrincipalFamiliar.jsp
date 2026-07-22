<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- «JSP» PaginaPrincipalFamiliar — CU-01 paso 10 / CU-03 paso 3 / CU-04 paso 3 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página principal · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<c:set var="usuario" value="${not empty familiar ? familiar : sessionScope.usuarioSesion}"/>

<header class="barra-superior">
    <div class="marca">
        <span class="logo">EC</span>
        <h1>EduConecta</h1>
    </div>
    <nav>
        <a class="activo"
           href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=solicitarIngreso">Inicio</a>
        <a href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=consultarPerfilesMenores">Perfiles de menores</a>
        <a href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=listarEspecialistas">Especialistas</a>
        <a href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=consultarCitas">Mis citas</a>
    </nav>
    <div class="usuario-sesion">
        <span class="avatar">${fn:substring(usuario.nombresCompletos, 0, 1)}</span>
        <span>${usuario.nombresCompletos}</span>
        <a href="${pageContext.request.contextPath}/IniciarSesionControlador?accion=cerrarSesion">Cerrar sesión</a>
    </div>
</header>

<main class="contenedor">
    <section class="heroe">
        <h2>Bienvenido/a, ${usuario.nombresCompletos}</h2>
        <p>
            Desde su página principal puede gestionar los perfiles de los menores a su cargo,
            consultar los especialistas disponibles y hacer el seguimiento de sus citas.
        </p>
    </section>

    <div class="grid-tarjetas">
        <a class="tarjeta tarjeta-enlace"
           href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=consultarPerfilesMenores">
            <h3>&#128106; Perfiles de menores</h3>
            <p class="detalle">Consulte y registre los perfiles de los menores vinculados con su cuenta.</p>
            <div class="pie"><span class="btn btn-secundario">Ir a la sección</span></div>
        </a>
        <a class="tarjeta tarjeta-enlace"
           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=listarEspecialistas">
            <h3>&#127891; Especialistas</h3>
            <p class="detalle">Explore los especialistas disponibles y agende una cita en sus horarios libres.</p>
            <div class="pie"><span class="btn btn-secundario">Ir a la sección</span></div>
        </a>
        <a class="tarjeta tarjeta-enlace"
           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=consultarCitas">
            <h3>&#128197; Mis citas</h3>
            <p class="detalle">Revise las citas agendadas para los menores a su cargo y su estado.</p>
            <div class="pie"><span class="btn btn-secundario">Ir a la sección</span></div>
        </a>
    </div>
</main>
</body>
</html>
