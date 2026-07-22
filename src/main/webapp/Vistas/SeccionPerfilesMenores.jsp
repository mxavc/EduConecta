<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- «JSP» SeccionPerfilesMenores — CU-03, pasos 6 y 17 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfiles de menores · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<c:set var="usuario" value="${sessionScope.usuarioSesion}"/>

<header class="barra-superior">
    <div class="marca">
        <span class="logo">EC</span>
        <h1>EduConecta</h1>
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=solicitarIngreso">Inicio</a>
        <a class="activo"
           href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=consultarPerfilesMenores">Perfiles de menores</a>
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
    <a class="enlace-volver"
       href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=solicitarIngreso">
        &#8592; Volver al inicio
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>Perfiles de menores</h2>
            <p>Menores vinculados con su cuenta</p>
        </div>
        <a class="btn btn-primario"
           href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=solicitarRegistroPerfilMenor">
            + Registrar nuevo menor
        </a>
    </div>

    <c:if test="${not empty mensajeExito}">
        <div class="alerta alerta-exito">${mensajeExito}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty perfiles}">
            <div class="estado-vacio">
                <span class="icono">&#128106;</span>
                <p>Aún no tiene perfiles de menores registrados.</p>
                <p>Utilice el botón «Registrar nuevo menor» para crear el primero.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-tarjetas">
                <c:forEach var="perfil" items="${perfiles}">
                    <div class="tarjeta">
                        <h3>${perfil.nombresCompletos} ${perfil.apellidosCompletos}</h3>
                        <p class="detalle"><strong>Cédula:</strong> ${perfil.cedula}</p>
                        <p class="detalle"><strong>Edad:</strong> ${perfil.edad} años</p>
                        <p class="detalle"><strong>Año escolar:</strong> ${perfil.anioEscolar}</p>
                        <p class="detalle"><strong>Ubicación:</strong> ${perfil.ciudad}, ${perfil.provincia}</p>
                        <c:if test="${not empty perfil.diagnosticoOCondicion}">
                            <p class="detalle"><strong>Diagnóstico o condición:</strong> ${perfil.diagnosticoOCondicion}</p>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
