<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- «JSP» DetalleCita — CU-05, paso 10: detalles de la cita e información del menor --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle de la cita · EduConecta</title>
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
       href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=consultarCitasAsignadas">
        &#8592; Volver a Citas asignadas
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>Detalle de la cita</h2>
            <p>
                <fmt:formatDate value="${cita.fecha}" pattern="EEEE dd/MM/yyyy"/> ·
                <fmt:formatDate value="${cita.hora}" pattern="HH:mm"/>
            </p>
        </div>
        <div>
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
        </div>
    </div>

    <div class="grid-tarjetas">
        <div class="tarjeta">
            <h3>Información del menor</h3>
            <p class="detalle"><strong>Nombre:</strong> ${perfilMenor.nombresCompletos} ${perfilMenor.apellidosCompletos}</p>
            <p class="detalle"><strong>Cédula:</strong> ${perfilMenor.cedula}</p>
            <p class="detalle"><strong>Edad:</strong> ${perfilMenor.edad} años</p>
            <p class="detalle"><strong>Año escolar:</strong> ${perfilMenor.anioEscolar}</p>
            <p class="detalle"><strong>Ubicación:</strong> ${perfilMenor.ciudad}, ${perfilMenor.provincia}</p>
            <c:if test="${not empty perfilMenor.diagnosticoOCondicion}">
                <p class="detalle"><strong>Diagnóstico o condición:</strong> ${perfilMenor.diagnosticoOCondicion}</p>
            </c:if>
            <div class="pie">
                <a class="btn btn-contorno"
                   href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=consultarHistorialMenor&amp;idPerfilMenor=${perfilMenor.id}&amp;idCita=${cita.id}">
                    Consultar historial del menor
                </a>
            </div>
        </div>

        <div class="tarjeta">
            <h3>Datos de la cita</h3>
            <p class="detalle"><strong>Familiar responsable:</strong>
                ${cita.familiar.nombresCompletos} ${cita.familiar.apellidosCompletos}</p>
            <p class="detalle"><strong>Correo del familiar:</strong> ${cita.familiar.correoElectronico}</p>
            <c:if test="${not empty cita.observacionInasistencia}">
                <p class="detalle"><strong>Observación de inasistencia:</strong> ${cita.observacionInasistencia}</p>
            </c:if>
            <c:if test="${cita.estado eq 'AGENDADA'}">
                <div class="pie acciones-formulario">
                    <a class="btn btn-primario"
                       href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=solicitarRegistroAtencion&amp;idCita=${cita.id}">
                        Registrar atención
                    </a>
                    <a class="btn btn-secundario"
                       href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=solicitarRegistroInasistencia&amp;idCita=${cita.id}">
                        Registrar inasistencia
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</main>
</body>
</html>
