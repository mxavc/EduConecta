<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- «JSP» FormularioRegistroMenor — CU-03, paso 8 --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar menor · EduConecta</title>
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
       href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=consultarPerfilesMenores">
        &#8592; Volver a Perfiles de menores
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>Registrar Perfil de Menor</h2>
            <p>Ingrese la información del menor a su cargo</p>
        </div>
    </div>

    <c:if test="${not empty mensajeError}">
        <div class="alerta alerta-error">${mensajeError}</div>
    </c:if>

    <div class="tarjeta">
        <form action="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador" method="post">
            <input type="hidden" name="accion" value="confirmarRegistro">

            <div class="fila-doble">
                <div class="campo">
                    <label for="nombres">Nombres completos</label>
                    <input type="text" id="nombres" name="nombres" value="${param.nombres}">
                </div>
                <div class="campo">
                    <label for="apellidos">Apellidos completos</label>
                    <input type="text" id="apellidos" name="apellidos" value="${param.apellidos}">
                </div>
            </div>

            <div class="fila-doble">
                <div class="campo">
                    <label for="cedula">Cédula</label>
                    <input type="text" id="cedula" name="cedula" maxlength="10"
                           placeholder="10 dígitos" value="${param.cedula}">
                </div>
                <div class="campo">
                    <label for="edad">Edad</label>
                    <input type="number" id="edad" name="edad" min="1" max="17" value="${param.edad}">
                </div>
            </div>

            <div class="fila-doble">
                <div class="campo">
                    <label for="anioEscolar">Año escolar de referencia</label>
                    <input type="text" id="anioEscolar" name="anioEscolar"
                           placeholder="Ej.: Quinto de básica" value="${param.anioEscolar}">
                </div>
                <div class="campo">
                    <label for="provincia">Provincia</label>
                    <input type="text" id="provincia" name="provincia" value="${param.provincia}">
                </div>
            </div>

            <div class="fila-doble">
                <div class="campo">
                    <label for="ciudad">Ciudad</label>
                    <input type="text" id="ciudad" name="ciudad" value="${param.ciudad}">
                </div>
                <div class="campo">
                    <label for="diagnosticoOCondicion">Diagnóstico o condición <span class="opcional">(si aplica)</span></label>
                    <input type="text" id="diagnosticoOCondicion" name="diagnosticoOCondicion"
                           value="${param.diagnosticoOCondicion}">
                </div>
            </div>

            <div class="acciones-formulario">
                <button type="submit" class="btn btn-primario">Confirmar registro</button>
                <a class="btn btn-secundario"
                   href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=cancelarRegistro">
                    Cancelar
                </a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
