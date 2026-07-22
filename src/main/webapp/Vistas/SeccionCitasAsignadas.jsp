<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- «JSP» SeccionCitasAsignadas — CU-05, pasos 6, 27 y 7a.16 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Citas asignadas · EduConecta</title>
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
        <a href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=solicitarIngreso">Inicio</a>
        <a class="activo"
           href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=consultarCitasAsignadas">Citas asignadas</a>
    </nav>
    <div class="usuario-sesion">
        <span class="avatar">${fn:substring(usuario.nombresCompletos, 0, 1)}</span>
        <span>${usuario.nombresCompletos}</span>
        <a href="${pageContext.request.contextPath}/IniciarSesionControlador?accion=cerrarSesion">Cerrar sesión</a>
    </div>
</header>

<main class="contenedor">
    <a class="enlace-volver"
       href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=solicitarIngreso">
        &#8592; Volver al inicio
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>Citas asignadas</h2>
            <p>Seleccione una cita para ver su detalle y registrar la atención o la inasistencia</p>
        </div>
    </div>

    <c:if test="${not empty mensajeExito}">
        <div class="alerta alerta-exito">${mensajeExito}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty citas}">
            <div class="estado-vacio">
                <span class="icono">&#128197;</span>
                <p>No tiene citas asignadas por el momento.</p>
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
                        <th>Familiar</th>
                        <th>Estado</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="cita" items="${citas}">
                        <tr>
                            <td><fmt:formatDate value="${cita.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${cita.hora}" pattern="HH:mm"/></td>
                            <td>${cita.perfilMenor.nombresCompletos} ${cita.perfilMenor.apellidosCompletos}</td>
                            <td>${cita.familiar.nombresCompletos} ${cita.familiar.apellidosCompletos}</td>
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
                            <td>
                                <a class="btn btn-contorno"
                                   href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=seleccionarCita&amp;idCita=${cita.id}">
                                    Ver detalle
                                </a>
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
