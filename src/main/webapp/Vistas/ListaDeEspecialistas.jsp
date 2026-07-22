<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- «JSP» ListaDeEspecialistas — CU-04, paso 6 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Especialistas · EduConecta</title>
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
        <a class="activo"
           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=listarEspecialistas">Especialistas</a>
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
            <h2>Especialistas</h2>
            <p>Seleccione un especialista para ver su información y horarios disponibles</p>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty especialistas}">
            <div class="estado-vacio">
                <span class="icono">&#127891;</span>
                <p>Por el momento no hay especialistas registrados en la plataforma.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-tarjetas">
                <c:forEach var="especialista" items="${especialistas}">
                    <div class="tarjeta">
                        <h3>${especialista.nombresCompletos} ${especialista.apellidosCompletos}</h3>
                        <c:if test="${not empty especialista.titulosProfesionales}">
                            <p class="detalle"><strong>Títulos:</strong> ${especialista.titulosProfesionales}</p>
                        </c:if>
                        <p class="detalle"><strong>Ubicación:</strong> ${especialista.ciudad}, ${especialista.provincia}</p>
                        <p class="detalle"><strong>Atención:</strong> ${especialista.direccionDeAtencion}</p>
                        <div class="pie">
                            <a class="btn btn-primario"
                               href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=obtenerEspecialista&amp;idEspecialista=${especialista.id}">
                                Ver información
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
