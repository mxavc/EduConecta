<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Sección Mis citas — CU-04, paso 25 (Mapa navegacional: consultar_citas / {rol=familiar}) --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis citas · EduConecta</title>
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
        <a href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=consultarPerfilesMenores">Perfiles de menores</a>
        <a href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=listarEspecialistas">Especialistas</a>
        <a class="activo"
           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=consultarCitas">Mis citas</a>
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
            <h2>Mis citas</h2>
            <p>Citas agendadas para los menores a su cargo</p>
        </div>
        <a class="btn btn-primario"
           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=listarEspecialistas">
            + Agendar nueva cita
        </a>
    </div>

    <c:if test="${not empty mensajeExito}">
        <div class="alerta alerta-exito">${mensajeExito}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty citas}">
            <div class="estado-vacio">
                <span class="icono">&#128197;</span>
                <p>Aún no tiene citas agendadas.</p>
                <p>Consulte la sección Especialistas para agendar la primera.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="envoltura-tabla">
                <table class="tabla">
                    <thead>
                    <tr>
                        <th>Fecha</th>
                        <th>Hora</th>
                        <th>Menor</th>
                        <th>Especialista</th>
                        <th>Estado</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="cita" items="${citas}">
                        <tr>
                            <td><fmt:formatDate value="${cita.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${cita.hora}" pattern="HH:mm"/></td>
                            <td>${cita.perfilMenor.nombresCompletos} ${cita.perfilMenor.apellidosCompletos}</td>
                            <td>${cita.especialista.nombresCompletos} ${cita.especialista.apellidosCompletos}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${cita.estado eq 'AGENDADA'}">
                                        <span class="insignia insignia-agendada">AGENDADA</span>
                                    </c:when>
                                    <c:when test="${cita.estado eq 'ATENDIDA'}">
                                        <span class="insignia insignia-atendida">ATENDIDA</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="insignia insignia-no-asistida">NO ASISTIDA</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
