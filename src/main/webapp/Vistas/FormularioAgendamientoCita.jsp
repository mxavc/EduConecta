<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- «JSP» FormularioAgendamientoCita — CU-04, paso 13: perfiles de menores y horarios
     disponibles. Paso 14: el Familiar selecciona Perfil del Menor, fecha disponible
     (calendario mensual) y hora disponible (solo horas libres). --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agendar cita · EduConecta</title>
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
       href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=obtenerEspecialista&amp;idEspecialista=${especialista.id}">
        &#8592; Volver a la información del especialista
    </a>

    <div class="encabezado-seccion">
        <div>
            <h2>Agendar cita</h2>
            <p>Especialista: <strong>${especialista.nombresCompletos} ${especialista.apellidosCompletos}</strong></p>
        </div>
    </div>

    <c:if test="${not empty mensajeError}">
        <div class="alerta alerta-error">${mensajeError}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty perfilesMenores}">
            <div class="estado-vacio">
                <span class="icono">&#128106;</span>
                <p>Debe tener al menos un Perfil de Menor registrado para agendar una cita.</p>
                <p>
                    <a class="enlace-simple"
                       href="${pageContext.request.contextPath}/RegistrarPerfilMenorControlador?accion=solicitarRegistroPerfilMenor">
                        Registrar un perfil de menor
                    </a>
                </p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="tarjeta">
                <form action="${pageContext.request.contextPath}/AgendarCitaControlador" method="post"
                      onsubmit="return confirm('¿Confirma el agendamiento de la cita?');">
                    <input type="hidden" name="accion" value="confirmarAgendamiento">
                    <input type="hidden" name="idEspecialista" value="${especialista.id}">
                    <%-- CU-04, paso 14: fecha y hora seleccionadas en el calendario. --%>
                    <input type="hidden" id="fecha" name="fecha" value="">
                    <input type="hidden" id="hora" name="hora" value="">

                    <div class="campo">
                        <label for="idPerfilMenor">Perfil del Menor</label>
                        <select id="idPerfilMenor" name="idPerfilMenor">
                            <option value="">— Seleccione un menor —</option>
                            <c:forEach var="perfil" items="${perfilesMenores}">
                                <option value="${perfil.id}">
                                    ${perfil.nombresCompletos} ${perfil.apellidosCompletos} (${perfil.edad} años)
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo">
                        <label>Fecha disponible</label>
                        <div class="nav-calendario">
                            <c:choose>
                                <c:when test="${not empty mesAnterior}">
                                    <a class="btn btn-secundario"
                                       href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=mostrarFormularioAgendamiento&amp;idEspecialista=${especialista.id}&amp;mes=${mesAnterior}">
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
                                       href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=mostrarFormularioAgendamiento&amp;idEspecialista=${especialista.id}&amp;mes=${mesSiguiente}">
                                        Mes siguiente &#8250;
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="btn btn-secundario deshabilitado">Mes siguiente &#8250;</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="leyenda-calendario">
                            <span><span class="punto disponible"></span> Día con horarios disponibles (clic para seleccionar)</span>
                            <span><span class="punto pasado"></span> Día pasado o sin disponibilidad</span>
                        </div>
                        <div class="envoltura-calendario">
                            <div id="calendarioMes" class="calendario-mes"></div>
                        </div>
                    </div>

                    <div class="campo">
                        <label>Hora disponible</label>
                        <%-- Solo se muestran las horas disponibles de la fecha elegida (paso 13). --%>
                        <div id="horasDisponibles" class="grupo-horarios">
                            <p class="detalle">Seleccione primero una fecha en el calendario.</p>
                        </div>
                    </div>

                    <p id="resumenSeleccion" class="detalle"></p>

                    <div class="acciones-formulario">
                        <button type="submit" class="btn btn-primario">Confirmar agendamiento</button>
                        <a class="btn btn-secundario"
                           href="${pageContext.request.contextPath}/AgendarCitaControlador?accion=cancelarAgendamiento&amp;idEspecialista=${especialista.id}">
                            Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:if test="${not empty perfilesMenores}">
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

    // Horas disponibles agrupadas por fecha (solo disponible = true — paso 13).
    var disponiblesPorFecha = {};
    for (var i = 0; i < horarios.length; i++) {
        if (horarios[i].d) {
            if (!disponiblesPorFecha[horarios[i].f]) { disponiblesPorFecha[horarios[i].f] = []; }
            disponiblesPorFecha[horarios[i].f].push(horarios[i].h);
        }
    }

    function dosDigitos(n) { return n < 10 ? '0' + n : '' + n; }

    (function dibujarCalendario() {
        var nombresDias = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
        var html = '';
        for (var e = 0; e < 7; e++) {
            html += '<div class="celda-encabezado">' + nombresDias[e] + '</div>';
        }
        var desfase = (new Date(anio, mes - 1, 1).getDay() + 6) % 7;
        for (var v = 0; v < desfase; v++) {
            html += '<div class="celda-dia vacia"></div>';
        }
        var diasEnMes = new Date(anio, mes, 0).getDate();
        for (var dia = 1; dia <= diasEnMes; dia++) {
            var fecha = anio + '-' + dosDigitos(mes) + '-' + dosDigitos(dia);
            var horas = disponiblesPorFecha[fecha] || [];
            var clases = 'celda-dia';
            if (fecha < hoy) { clases += ' pasado'; }
            if (fecha === hoy) { clases += ' hoy'; }
            if (horas.length > 0) { clases += ' dia-seleccionable'; }
            html += '<div class="' + clases + '" data-fecha="' + (horas.length > 0 ? fecha : '') + '">';
            html += '<span class="numero-dia">' + dia + '</span>';
            if (horas.length > 0) {
                html += '<div class="horas-dia"><span class="chip-horario mini disponible">'
                        + horas.length + ' disponibles</span></div>';
            }
            html += '</div>';
        }
        document.getElementById('calendarioMes').innerHTML = html;
    })();

    // CU-04, paso 14: el Familiar selecciona la fecha disponible.
    document.getElementById('calendarioMes').addEventListener('click', function (evento) {
        var celda = evento.target.closest('.dia-seleccionable');
        if (!celda) { return; }
        var fecha = celda.getAttribute('data-fecha');
        document.getElementById('fecha').value = fecha;
        document.getElementById('hora').value = '';
        var seleccionadas = document.querySelectorAll('.celda-dia.seleccionado');
        for (var i = 0; i < seleccionadas.length; i++) {
            seleccionadas[i].classList.remove('seleccionado');
        }
        celda.classList.add('seleccionado');
        mostrarHoras(fecha);
        actualizarResumen();
    });

    // CU-04, paso 14: el Familiar selecciona la hora disponible (solo horas libres).
    function mostrarHoras(fecha) {
        var contenedor = document.getElementById('horasDisponibles');
        var horas = disponiblesPorFecha[fecha] || [];
        var html = '';
        for (var i = 0; i < horas.length; i++) {
            html += '<button type="button" class="chip-horario opcion" data-hora="' + horas[i] + '">'
                    + horas[i] + '</button>';
        }
        contenedor.innerHTML = html;
        var botones = contenedor.querySelectorAll('.chip-horario.opcion');
        for (var j = 0; j < botones.length; j++) {
            botones[j].addEventListener('click', function () {
                document.getElementById('hora').value = this.getAttribute('data-hora');
                var elegidas = contenedor.querySelectorAll('.chip-horario.opcion.seleccionada');
                for (var k = 0; k < elegidas.length; k++) {
                    elegidas[k].classList.remove('seleccionada');
                }
                this.classList.add('seleccionada');
                actualizarResumen();
            });
        }
    }

    function actualizarResumen() {
        var fecha = document.getElementById('fecha').value;
        var hora = document.getElementById('hora').value;
        var resumen = document.getElementById('resumenSeleccion');
        if (fecha && hora) {
            resumen.innerHTML = '<strong>Seleccionado:</strong> ' + fecha + ' a las ' + hora;
        } else if (fecha) {
            resumen.innerHTML = '<strong>Fecha:</strong> ' + fecha + ' — seleccione una hora disponible.';
        } else {
            resumen.innerHTML = '';
        }
    }
</script>
</c:if>
</body>
</html>
