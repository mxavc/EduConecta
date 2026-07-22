<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- «JSP» InformacionEspecialista — CU-04, paso 10: información y horarios disponibles --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Información del Especialista · EduConecta</title>
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
       href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=listarEspecialistas">
        &#8592; Volver a la lista de especialistas
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>${especialista.nombresCompletos} ${especialista.apellidosCompletos}</h2>
            <p>Información del Especialista y horarios disponibles</p>
        </div>
        <a class="btn btn-primario"
           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=mostrarFormularioAgendamiento&amp;idEspecialista=${especialista.id}">
            Agendar cita
        </a>
    </div>

    <div class="tarjeta" style="margin-bottom: 1.4rem;">
        <h3>Información profesional</h3>
        <c:if test="${not empty especialista.titulosProfesionales}">
            <p class="detalle"><strong>Títulos profesionales:</strong> ${especialista.titulosProfesionales}</p>
        </c:if>
        <p class="detalle"><strong>Correo:</strong> ${especialista.correoElectronico}</p>
        <p class="detalle"><strong>Provincia:</strong> ${especialista.provincia}</p>
        <p class="detalle"><strong>Ciudad:</strong> ${especialista.ciudad}</p>
        <p class="detalle"><strong>Dirección de atención:</strong> ${especialista.direccionDeAtencion}</p>
    </div>

    <div class="tarjeta">
        <div class="nav-calendario">
            <c:choose>
                <c:when test="${not empty mesAnterior}">
                    <a class="btn btn-secundario"
                       href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=obtenerEspecialista&amp;idEspecialista=${especialista.id}&amp;mes=${mesAnterior}">
                        &#8249; Mes anterior
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="btn btn-secundario deshabilitado">&#8249; Mes anterior</span>
                </c:otherwise>
            </c:choose>
            <h3>${mesEtiqueta}</h3>
            <c:choose>
                <c:when test="${not empty mesSiguiente}">
                    <a class="btn btn-secundario"
                       href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=obtenerEspecialista&amp;idEspecialista=${especialista.id}&amp;mes=${mesSiguiente}">
                        Mes siguiente &#8250;
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="btn btn-secundario deshabilitado">Mes siguiente &#8250;</span>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="leyenda-calendario">
            <span><span class="punto disponible"></span> Horario disponible</span>
            <span><span class="punto reservado"></span> Horario ocupado (RESERVADO)</span>
            <span><span class="punto pasado"></span> Día pasado (sus citas registradas se conservan)</span>
        </div>

        <%-- Calendario mensual: cada hora se pinta según isDisponible()
             — CU-04: disponible=false ⇒ RESERVADO. --%>
        <div class="envoltura-calendario">
            <div id="calendarioMes" class="calendario-mes"></div>
        </div>
    </div>
</main>

<script>
    // Disponibilidad del mes mostrado (CU-04, paso 9: obtenerHorariosDisponibles).
    var horarios = [
        <c:forEach var="horario" items="${horarios}" varStatus="estado">
        {f: '${horario.fecha}', h: '<fmt:formatDate value="${horario.hora}" pattern="HH:mm"/>', d: ${horario.disponible}}<c:if test="${not estado.last}">,</c:if>
        </c:forEach>
    ];
    var anio = ${anio};
    var mes = ${mesNumero};   // 1-12
    var hoy = '${fechaHoy}';  // yyyy-MM-dd (fecha del servidor)

    (function dibujarCalendario() {
        var porFecha = {};
        for (var i = 0; i < horarios.length; i++) {
            var franja = horarios[i];
            if (!porFecha[franja.f]) { porFecha[franja.f] = []; }
            porFecha[franja.f].push(franja);
        }
        function dosDigitos(n) { return n < 10 ? '0' + n : '' + n; }

        var nombresDias = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
        var html = '';
        for (var e = 0; e < 7; e++) {
            html += '<div class="celda-encabezado">' + nombresDias[e] + '</div>';
        }
        // Desfase: día de la semana del 1.° del mes (semana iniciando en lunes).
        var desfase = (new Date(anio, mes - 1, 1).getDay() + 6) % 7;
        for (var v = 0; v < desfase; v++) {
            html += '<div class="celda-dia vacia"></div>';
        }
        var diasEnMes = new Date(anio, mes, 0).getDate();
        for (var dia = 1; dia <= diasEnMes; dia++) {
            var fecha = anio + '-' + dosDigitos(mes) + '-' + dosDigitos(dia);
            var clases = 'celda-dia';
            if (fecha < hoy) { clases += ' pasado'; }
            if (fecha === hoy) { clases += ' hoy'; }
            html += '<div class="' + clases + '"><span class="numero-dia">' + dia + '</span>';
            html += '<div class="horas-dia">';
            var franjas = porFecha[fecha] || [];
            for (var j = 0; j < franjas.length; j++) {
                html += '<span class="chip-horario mini ' + (franjas[j].d ? 'disponible' : 'reservado') + '">'
                        + franjas[j].h + '</span>';
            }
            html += '</div></div>';
        }
        document.getElementById('calendarioMes').innerHTML = html;
    })();
</script>
</body>
</html>
