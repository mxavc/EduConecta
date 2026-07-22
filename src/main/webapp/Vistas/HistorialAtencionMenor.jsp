<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- «JSP» HistorialAtencionMenor — CU-05, paso 14: antecedentes y atenciones anteriores --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de atención · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<c:set var="usuario" value="${sessionScope.usuarioSesion}"/>
<c:set var="menor" value="${historial.perfilMenor}"/>

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
    <c:if test="${not empty idCita}">
        <a class="enlace-volver"
           href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=seleccionarCita&amp;idCita=${idCita}">
            &#8592; Volver al detalle de la cita
        </a>
    </c:if>

    <div class="encabezado-seccion">
        <div>
            <h2>Historial de atención del menor</h2>
            <p>${menor.nombresCompletos} ${menor.apellidosCompletos}</p>
        </div>
    </div>

    <div class="tarjeta" style="margin-bottom: 1.4rem;">
        <h3>Antecedentes</h3>
        <p class="detalle"><strong>Edad:</strong> ${menor.edad} años</p>
        <p class="detalle"><strong>Año escolar:</strong> ${menor.anioEscolar}</p>
        <c:choose>
            <c:when test="${not empty menor.diagnosticoOCondicion}">
                <p class="detalle"><strong>Diagnóstico o condición:</strong> ${menor.diagnosticoOCondicion}</p>
            </c:when>
            <c:otherwise>
                <p class="detalle">Sin diagnóstico o condición registrada.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="encabezado-seccion">
        <div>
            <h2 style="font-size:1.2rem;">Registros de atenciones anteriores</h2>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty atenciones and empty historial.inasistencias}">
            <div class="estado-vacio">
                <span class="icono">&#128203;</span>
                <p>El historial del menor aún no contiene registros de atención.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="linea-tiempo">
                <c:forEach var="atencion" items="${atenciones}">
                    <div class="evento-historial">
                        <h4>
                            Atención ·
                            <fmt:formatDate value="${atencion.cita.fecha}" pattern="dd/MM/yyyy"/>
                            <fmt:formatDate value="${atencion.cita.hora}" pattern="HH:mm"/>
                            — ${atencion.especialista.nombresCompletos} ${atencion.especialista.apellidosCompletos}
                        </h4>
                        <p><strong>Observaciones:</strong> ${atencion.observaciones}</p>
                        <p><strong>Información relevante:</strong> ${atencion.informacionRelevante}</p>
                        <p><strong>Recomendaciones:</strong> ${atencion.recomendaciones}</p>
                        <c:if test="${not empty atencion.diagnostico}">
                            <p><strong>Diagnóstico:</strong> ${atencion.diagnostico}</p>
                        </c:if>
                        <c:if test="${not empty atencion.indicacionesDeSeguimiento}">
                            <p><strong>Indicaciones de seguimiento:</strong> ${atencion.indicacionesDeSeguimiento}</p>
                        </c:if>
                        <c:if test="${not empty atencion.notasAdicionales}">
                            <p><strong>Notas adicionales:</strong> ${atencion.notasAdicionales}</p>
                        </c:if>
                    </div>
                </c:forEach>

                <c:forEach var="inasistencia" items="${historial.inasistencias}">
                    <div class="evento-historial inasistencia">
                        <h4>
                            Inasistencia ·
                            <fmt:formatDate value="${inasistencia.fecha}" pattern="dd/MM/yyyy"/>
                            <fmt:formatDate value="${inasistencia.hora}" pattern="HH:mm"/>
                        </h4>
                        <p><strong>Observación:</strong> ${inasistencia.observacionInasistencia}</p>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
