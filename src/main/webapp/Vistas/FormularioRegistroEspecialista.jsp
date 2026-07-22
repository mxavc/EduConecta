<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- «JSP» FormularioRegistroEspecialista — CU-02, paso 5 (tipoCuenta = ESPECIALISTA) --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Especialista · EduConecta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Vistas/css/estilos.css">
</head>
<body>
<div class="fondo-acceso">
    <main class="tarjeta-acceso ancha">
        <div class="marca">
            <span class="logo">EC</span>
            <h1>Registro de Especialista</h1>
        </div>
        <p class="subtitulo">Complete la información y cargue los documentos que respalden sus títulos</p>

        <c:if test="${not empty camposRequeridos}">
            <div class="leyenda-campos">
                <strong>Datos solicitados:</strong>
                <c:forEach var="campo" items="${camposRequeridos}" varStatus="estado">
                    ${campo}<c:if test="${not estado.last}"> · </c:if>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${not empty mensajeError}">
            <div class="alerta alerta-error">${mensajeError}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/RegistrarUsuarioControlador"
              method="post" enctype="multipart/form-data">
            <input type="hidden" name="accion" value="confirmarRegistro">
            <input type="hidden" name="tipoCuenta" value="ESPECIALISTA">

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
                    <label for="cedula">Número de cédula</label>
                    <input type="text" id="cedula" name="cedula" maxlength="10"
                           placeholder="10 dígitos" value="${param.cedula}">
                </div>
                <div class="campo">
                    <label for="ruc">RUC</label>
                    <input type="text" id="ruc" name="ruc" maxlength="13"
                           placeholder="13 dígitos" value="${param.ruc}">
                </div>
            </div>

            <div class="fila-doble">
                <div class="campo">
                    <label for="correo">Correo electrónico</label>
                    <input type="email" id="correo" name="correo"
                           placeholder="nombre@correo.com" value="${param.correo}">
                </div>
                <div class="campo">
                    <label for="direccionDeAtencion">Dirección del consultorio o lugar de atención</label>
                    <input type="text" id="direccionDeAtencion" name="direccionDeAtencion"
                           value="${param.direccionDeAtencion}">
                </div>
            </div>

            <div class="fila-doble">
                <div class="campo">
                    <label for="provincia">Provincia</label>
                    <input type="text" id="provincia" name="provincia" value="${param.provincia}">
                </div>
                <div class="campo">
                    <label for="ciudad">Ciudad</label>
                    <input type="text" id="ciudad" name="ciudad" value="${param.ciudad}">
                </div>
            </div>

            <div class="campo">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena"
                       placeholder="Mínimo 6 caracteres" autocomplete="new-password">
            </div>

            <div class="campo">
                <label>Títulos profesionales y documentos de respaldo</label>
                <div id="credenciales">
                    <div class="fila-credencial">
                        <div class="campo">
                            <label>Título profesional</label>
                            <input type="text" name="titulos" placeholder="Ej.: Psicólogo Educativo">
                        </div>
                        <div class="campo">
                            <label>Documento de respaldo</label>
                            <input type="file" name="documentos" accept=".pdf,.jpg,.jpeg,.png">
                        </div>
                        <button type="button" class="btn btn-secundario" onclick="quitarCredencial(this)">Quitar</button>
                    </div>
                </div>
                <button type="button" class="btn btn-contorno" onclick="agregarCredencial()">
                    + Agregar otro título
                </button>
            </div>

            <div class="acciones-formulario">
                <button type="submit" class="btn btn-primario">Confirmar registro</button>
                <a class="btn btn-secundario"
                   href="${pageContext.request.contextPath}/RegistrarUsuarioControlador?accion=cancelarRegistro">
                    Cancelar
                </a>
            </div>
        </form>
    </main>
</div>

<script>
    function agregarCredencial() {
        var contenedor = document.getElementById('credenciales');
        var fila = contenedor.firstElementChild.cloneNode(true);
        fila.querySelector('input[name="titulos"]').value = '';
        fila.querySelector('input[name="documentos"]').value = '';
        contenedor.appendChild(fila);
    }

    function quitarCredencial(boton) {
        var contenedor = document.getElementById('credenciales');
        if (contenedor.children.length > 1) {
            boton.closest('.fila-credencial').remove();
        }
    }
</script>
</body>
</html>
