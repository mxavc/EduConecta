<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Mapa navegacional: entrar → Formulario de inicio de sesión (CU-01, paso 1). --%>
<% response.sendRedirect(request.getContextPath() + "/IniciarSesionControlador?accion=solicitarIngreso"); %>
