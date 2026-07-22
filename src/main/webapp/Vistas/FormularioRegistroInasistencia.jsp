<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- «JSP» FormularioRegistroInasistencia — CU-05, paso 7a.6 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar inasistencia · EduConecta</title>
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
       href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=seleccionarCita&amp;idCita=${cita.id}">
        &#8592; Volver al detalle de la cita
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>Registrar inasistencia</h2>
            <p>
                Cita del <fmt:formatDate value="${cita.fecha}" pattern="dd/MM/yyyy"/>
                a las <fmt:formatDate value="${cita.hora}" pattern="HH:mm"/> ·
                Menor: <strong>${cita.perfilMenor.nombresCompletos} ${cita.perfilMenor.apellidosCompletos}</strong>
            </p>
        </div>
    </div>

    <c:if test="${not empty mensajeError}">
        <div class="alerta alerta-error">${mensajeError}</div>
    </c:if>

    <div class="tarjeta">
        <form action="${pageContext.request.contextPath}/AtenderCitaControlador" method="post">
            <input type="hidden" name="accion" value="ingresarObservacionYConfirmar">
            <input type="hidden" name="idCita" value="${cita.id}">

            <div class="campo">
                <label for="observacion">Observación</label>
                <textarea id="observacion" name="observacion"
                          placeholder="Describa la inasistencia del Familiar y el menor">${param.observacion}</textarea>
            </div>

            <div class="acciones-formulario">
                <button type="submit" class="btn btn-primario">Confirmar registro</button>
                <a class="btn btn-secundario"
                   href="${pageContext.request.contextPath}/AtenderCitaControlador?accion=seleccionarCita&amp;idCita=${cita.id}">
                    Cancelar
                </a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
