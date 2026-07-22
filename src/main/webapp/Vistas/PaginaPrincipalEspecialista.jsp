<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- «JSP» PaginaPrincipalEspecialista — CU-01 paso 10 / CU-05 paso 3 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página principal · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<c:set var="usuario" value="${not empty especialista ? especialista : sessionScope.usuarioSesion}"/>

<header class="barra-superior">
    <div class="marca">
        <span class="logo">EC</span>
        <h1>EduConecta</h1>
    </div>
    <nav>
        <a class="activo"
           href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=solicitarIngreso">Inicio</a>
        <a href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=consultarCitasAsignadas">Citas asignadas</a>
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
            Desde su página principal puede consultar las citas asignadas, registrar las
            atenciones realizadas y revisar el historial de atención de los menores.
        </p>
    </section>

    <div class="grid-tarjetas">
        <a class="tarjeta tarjeta-enlace"
           href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=consultarCitasAsignadas">
            <h3>&#128197; Citas asignadas</h3>
            <p class="detalle">
                Consulte sus citas, registre la atención brindada o la inasistencia del menor.
            </p>
            <div class="pie"><span class="btn btn-secundario">Ir a la sección</span></div>
        </a>
        <div class="tarjeta">
            <h3>&#128100; Mi información</h3>
            <p class="detalle"><strong>Correo:</strong> ${usuario.correoElectronico}</p>
            <p class="detalle"><strong>Ubicación:</strong> ${usuario.ciudad}, ${usuario.provincia}</p>
            <c:if test="${not empty especialista}">
                <p class="detalle"><strong>Dirección de atención:</strong> ${especialista.direccionDeAtencion}</p>
                <c:if test="${not empty especialista.titulosProfesionales}">
                    <p class="detalle"><strong>Títulos:</strong> ${especialista.titulosProfesionales}</p>
                </c:if>
            </c:if>
        </div>
    </div>
</main>
</body>
</html>
