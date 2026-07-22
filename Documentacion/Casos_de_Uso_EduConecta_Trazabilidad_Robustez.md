# ESCUELA POLITÉCNICA NACIONAL

## FACULTAD DE INGENIERÍA DE SISTEMAS

### APLICACIONES WEB

**PROYECTO FINAL:**  
**PLATAFORMA WEB DE VINCULACIÓN Y GESTIÓN DE ATENCIÓN PSICOPEDAGÓGICA - EduConecta**

---

# 1. DESCRIPCIÓN DE CASOS DE USO

## Criterio de redacción aplicado

Los flujos fueron ajustados para mantener una trazabilidad directa con los diagramas de robustez y, posteriormente, con los diagramas de secuencia.

Cuando el sistema necesita consultar, verificar, relacionar o actualizar información antes de mostrar una vista o completar una acción, dichas operaciones se presentan como pasos independientes. De esta manera:

- Cada comunicación puede asociarse con un número específico del flujo.
- No se agrupan varias responsabilidades del sistema en un mismo paso.
- Se evita repetir el mismo número para operaciones diferentes.
- Las acciones de obtener información se presentan antes de las acciones de mostrarla.
- Las acciones de validar y verificar se separan cuando representan comprobaciones diferentes.
- Las actualizaciones posteriores a un registro se muestran de forma explícita.

Los únicos actores considerados son **Familiar** y **Especialista**.

---

## 1.1 CU-01: Iniciar Sesión

**Actor principal:** Usuario (Familiar o Especialista).

### Descripción

El caso de uso comienza cuando un Usuario solicita ingresar al sistema. El sistema presenta el formulario de inicio de sesión. Después de que el Usuario proporciona sus credenciales, el sistema verifica la información ingresada, busca la cuenta correspondiente, comprueba la contraseña e identifica el rol asociado. Finalmente, obtiene la información necesaria para presentar la página principal correspondiente.

### Precondición

- El Usuario debe tener una cuenta registrada en el sistema.

### Postcondición

- El Usuario inicia sesión y accede a la página principal correspondiente a su rol.

### Flujo principal

1. El Usuario solicita ingresar al sistema.
2. El sistema muestra el formulario de inicio de sesión.
3. El Usuario ingresa su correo electrónico y contraseña.
4. El Usuario confirma el ingreso.
5. El sistema verifica que los campos obligatorios estén completos.
6. El sistema busca la cuenta asociada con el correo electrónico ingresado.
7. El sistema comprueba que la contraseña corresponda a la cuenta encontrada.
8. El sistema identifica el rol asociado con la cuenta.
9. El sistema obtiene la información del Usuario necesaria para iniciar su sesión.
10. El sistema muestra la Página principal del Familiar o la Página principal del Especialista, según corresponda.

### Flujos alternativos

#### 5a. Datos incompletos

1. El sistema identifica que existen campos obligatorios sin completar.
2. El sistema muestra el mensaje: «Complete los campos obligatorios».
3. El sistema mantiene visible el formulario de inicio de sesión con la información previamente ingresada.

#### 6a. Cuenta no registrada

1. El sistema identifica que no existe una cuenta asociada con el correo electrónico ingresado.
2. El sistema muestra el mensaje: «El correo electrónico o la contraseña son incorrectos».
3. El sistema mantiene visible el formulario de inicio de sesión.

#### 7a. Contraseña incorrecta

1. El sistema identifica que la contraseña no corresponde a la cuenta encontrada.
2. El sistema muestra el mensaje: «El correo electrónico o la contraseña son incorrectos».
3. El sistema mantiene visible el formulario de inicio de sesión.

---

## 1.2 CU-02: Registrar Usuario

**Actor principal:** Familiar o Especialista.

### Descripción

El caso de uso comienza cuando un Usuario solicita registrarse desde el formulario de inicio de sesión. El sistema presenta los tipos de cuenta disponibles. Después de que el Usuario selecciona su tipo de cuenta, el sistema obtiene los datos requeridos para dicho registro y muestra el formulario correspondiente. Cuando el Usuario confirma el registro, el sistema valida la información, verifica que no exista una cuenta con los mismos datos, registra la cuenta y muestra la confirmación.

### Precondiciones

- El Usuario no debe tener una cuenta registrada en el sistema.
- El Usuario debe encontrarse en el formulario de inicio de sesión.

### Postcondición

- La cuenta del Familiar o del Especialista queda registrada en el sistema.

### Datos solicitados al Familiar

- Nombres completos.
- Apellidos completos.
- Número de cédula.
- Correo electrónico.
- Provincia.
- Ciudad.
- Contraseña.

### Datos solicitados al Especialista

- Nombres completos.
- Apellidos completos.
- Número de cédula.
- RUC.
- Correo electrónico.
- Provincia.
- Ciudad.
- Dirección del consultorio o lugar de atención.
- Título o títulos profesionales.
- Contraseña.
- Documentos que respalden sus títulos y credenciales profesionales.

### Flujo principal

1. El Usuario solicita registrarse desde el formulario de inicio de sesión.
2. El sistema muestra los tipos de cuenta disponibles: Familiar y Especialista.
3. El Usuario selecciona el tipo de cuenta con el que desea registrarse.
4. El sistema obtiene los datos requeridos para el tipo de cuenta seleccionado.
5. El sistema muestra el formulario de registro correspondiente.
6. El Usuario completa la información solicitada.
7. Si seleccionó el tipo de cuenta Especialista, carga los documentos requeridos.
8. El Usuario confirma el registro.
9. El sistema verifica que los campos obligatorios estén completos y que la información tenga un formato válido.
10. El sistema verifica que el correo electrónico no se encuentre registrado.
11. El sistema verifica que el número de cédula no se encuentre registrado.
12. Si el Usuario se registra como Especialista, el sistema verifica que el RUC no se encuentre registrado.
13. Si el Usuario se registra como Especialista, el sistema verifica que los documentos requeridos hayan sido cargados.
14. El sistema crea la cuenta con el tipo seleccionado.
15. El sistema registra la información proporcionada por el Usuario.
16. El sistema muestra el mensaje: «Registro realizado correctamente».
17. El sistema muestra nuevamente el formulario de inicio de sesión.

### Flujos alternativos

#### 8a. Cancelar registro

1. El Usuario solicita cancelar el registro.
2. El sistema descarta la información no confirmada.
3. El sistema muestra nuevamente el formulario de inicio de sesión.

#### 9a. Información incompleta o no válida

1. El sistema identifica campos obligatorios incompletos o información con un formato no válido.
2. El sistema muestra el mensaje correspondiente.
3. El sistema mantiene visible el formulario con la información previamente ingresada.

#### 10a. Correo electrónico registrado

1. El sistema identifica que el correo electrónico pertenece a otra cuenta.
2. El sistema muestra el mensaje: «El correo electrónico ingresado ya se encuentra registrado».
3. El sistema mantiene visible el formulario para que el Usuario corrija la información.

#### 11a. Número de cédula registrado

1. El sistema identifica que el número de cédula pertenece a otra cuenta.
2. El sistema muestra el mensaje: «El número de cédula ingresado ya se encuentra registrado».
3. El sistema mantiene visible el formulario para que el Usuario corrija la información.

#### 12a. RUC registrado

1. El sistema identifica que el RUC pertenece a otro Especialista.
2. El sistema muestra el mensaje: «El RUC ingresado ya se encuentra registrado».
3. El sistema mantiene visible el formulario para que el Usuario corrija la información.

#### 13a. Documentos no cargados

1. El sistema identifica que el Especialista no cargó los documentos requeridos.
2. El sistema muestra el mensaje: «Revise y cargue correctamente los documentos solicitados».
3. El sistema mantiene visible el formulario de registro.

---

## 1.3 CU-03: Registrar Perfil de Menor

**Actor principal:** Familiar.

### Descripción

El caso de uso comienza cuando el Familiar accede a su página principal y solicita consultar los perfiles de menores vinculados con su cuenta. El sistema obtiene dichos perfiles y presenta la sección correspondiente. Cuando el Familiar solicita registrar un nuevo menor, el sistema muestra el formulario. Después de la confirmación, valida la información, verifica que el menor no se encuentre registrado, crea el perfil, lo vincula con el Familiar y obtiene nuevamente la lista actualizada.

### Precondiciones

- El Familiar debe haber iniciado sesión.
- El Familiar debe encontrarse en la Página principal del Familiar.

### Postcondiciones

- El Perfil del Menor queda registrado en el sistema.
- El Perfil del Menor queda vinculado con el Familiar.
- La lista de perfiles de menores queda actualizada.

### Flujo principal

1. El Familiar solicita ingresar a la Página principal del Familiar.
2. El sistema obtiene la información del Familiar necesaria para presentar su página principal.
3. El sistema muestra la Página principal del Familiar.
4. El Familiar solicita consultar sus perfiles de menores.
5. El sistema busca los perfiles de menores vinculados con el Familiar.
6. El sistema muestra la sección Perfiles de menores con la lista obtenida.
7. El Familiar solicita registrar un nuevo Perfil de Menor.
8. El sistema muestra el formulario de registro de menor.
9. El Familiar ingresa la siguiente información:
   - Nombres completos.
   - Apellidos completos.
   - Cédula.
   - Edad.
   - Año escolar de referencia.
   - Provincia.
   - Ciudad.
   - Diagnóstico o condición, si aplica.
10. El Familiar confirma el registro.
11. El sistema verifica que los campos obligatorios estén completos y que la información sea válida.
12. El sistema verifica que el menor no se encuentre registrado previamente por el Familiar.
13. El sistema crea el Perfil del Menor.
14. El sistema vincula el Perfil del Menor con el Familiar.
15. El sistema obtiene la lista actualizada de perfiles de menores vinculados con el Familiar.
16. El sistema muestra el mensaje: «Perfil de menor registrado con éxito».
17. El sistema muestra la sección Perfiles de menores con la lista actualizada.

### Flujos alternativos

#### 10a. Cancelar registro

1. El Familiar solicita cancelar el registro.
2. El sistema descarta la información no confirmada.
3. El sistema obtiene la lista de perfiles de menores vinculados con el Familiar.
4. El sistema muestra nuevamente la sección Perfiles de menores.

#### 11a. Datos incompletos o no válidos

1. El sistema identifica campos obligatorios incompletos o información no válida.
2. El sistema muestra el mensaje correspondiente.
3. El sistema mantiene visible el formulario con la información previamente ingresada.

#### 12a. Menor registrado previamente

1. El sistema identifica que el menor ya se encuentra registrado por el Familiar.
2. El sistema muestra el mensaje: «El menor ya se encuentra registrado».
3. El sistema mantiene visible el formulario de registro.
4. El sistema no crea un nuevo Perfil de Menor.

---

## 1.4 CU-04: Agendar Cita

**Actor principal:** Familiar.

### Descripción

El caso de uso comienza cuando el Familiar accede a su página principal y solicita consultar los especialistas disponibles. El sistema obtiene la lista correspondiente y la presenta. Cuando el Familiar selecciona un Especialista, el sistema obtiene por separado su información y sus horarios disponibles antes de mostrarlos. Para iniciar el agendamiento, obtiene los perfiles de menores vinculados con el Familiar y presenta el formulario. Después de la confirmación, valida la información, verifica nuevamente la disponibilidad, registra la cita, reserva el horario y obtiene la lista actualizada de citas del Familiar.

### Precondiciones

- El Familiar debe haber iniciado sesión.
- El Familiar debe tener al menos un Perfil de Menor registrado.
- Debe existir al menos un Especialista con horarios disponibles.

### Postcondiciones

- La cita queda vinculada con el Familiar, el Perfil del Menor y el Especialista.
- El horario seleccionado queda reservado.
- La cita se muestra en la sección Mis citas del Familiar.

### Flujo principal

1. El Familiar solicita ingresar a la Página principal del Familiar.
2. El sistema obtiene la información del Familiar necesaria para presentar su página principal.
3. El sistema muestra la Página principal del Familiar.
4. El Familiar solicita consultar los Especialistas.
5. El sistema obtiene la lista de Especialistas disponibles.
6. El sistema muestra la sección Especialistas con la lista obtenida.
7. El Familiar selecciona un Especialista.
8. El sistema obtiene la información del Especialista seleccionado.
9. El sistema obtiene los horarios disponibles del Especialista seleccionado.
10. El sistema muestra la información y los horarios disponibles del Especialista.
11. El Familiar solicita agendar una cita.
12. El sistema obtiene los perfiles de menores vinculados con el Familiar.
13. El sistema muestra el formulario de agendamiento con los perfiles de menores y los horarios disponibles.
14. El Familiar selecciona:
    - Perfil del Menor.
    - Fecha disponible.
    - Hora disponible.
15. El Familiar confirma el agendamiento.
16. El sistema verifica que los campos obligatorios estén completos y que la fecha seleccionada sea válida.
17. El sistema verifica que la fecha y la hora seleccionadas continúen disponibles.
18. El sistema crea la cita con estado **AGENDADA**.
19. El sistema vincula la cita con el Familiar, el Perfil del Menor, el Especialista y el horario seleccionado.
20. El sistema actualiza el estado del horario seleccionado a **RESERVADO**.
21. El sistema registra la cita.
22. El sistema guarda la actualización del horario.
23. El sistema obtiene la lista actualizada de citas del Familiar.
24. El sistema muestra el mensaje: «Cita agendada con éxito».
25. El sistema muestra la sección Mis citas con la cita registrada.

### Flujos alternativos

#### 15a. Cancelar agendamiento

1. El Familiar solicita cancelar el agendamiento.
2. El sistema no registra la cita.
3. El sistema obtiene nuevamente la información del Especialista seleccionado.
4. El sistema obtiene los horarios disponibles del Especialista.
5. El sistema muestra nuevamente la información y los horarios disponibles del Especialista.

#### 16a. Datos incompletos o fecha no válida

1. El sistema identifica información obligatoria incompleta o una fecha no permitida.
2. El sistema muestra el mensaje: «Complete la información obligatoria o seleccione una fecha permitida».
3. El sistema mantiene visible el formulario de agendamiento con la información previamente seleccionada.

#### 17a. Horario no disponible

1. El sistema identifica que la fecha y la hora seleccionadas ya no se encuentran disponibles.
2. El sistema obtiene nuevamente los horarios disponibles del Especialista.
3. El sistema muestra el mensaje: «La fecha y hora seleccionadas ya no se encuentran disponibles».
4. El sistema actualiza los horarios presentados en el formulario de agendamiento.
5. El sistema mantiene visible el formulario para que el Familiar seleccione otro horario.

---

## 1.5 CU-05: Atender Cita

**Actor principal:** Especialista.

### Descripción

El caso de uso comienza cuando el Especialista accede a su página principal y solicita consultar sus citas asignadas. El sistema obtiene únicamente las citas vinculadas con el Especialista y presenta la sección correspondiente. Cuando el Especialista selecciona una cita, el sistema obtiene los detalles de la cita y la información del menor antes de mostrarlos. Si el Especialista consulta el historial, el sistema obtiene sus registros. Al finalizar la atención, valida la información, registra la atención, la vincula con la cita y el historial del menor, actualiza el estado de la cita y obtiene nuevamente las citas asignadas.

### Precondiciones

- El Especialista debe haber iniciado sesión.
- La cita debe estar registrada y asignada al Especialista.

### Postcondiciones

- La atención queda registrada y vinculada con la cita.
- La información de la atención queda incorporada al historial del menor.
- La cita queda actualizada con el estado **ATENDIDA**.
- La lista de citas asignadas al Especialista queda actualizada.

### Flujo principal

1. El Especialista solicita ingresar a la Página principal del Especialista.
2. El sistema obtiene la información del Especialista necesaria para presentar su página principal.
3. El sistema muestra la Página principal del Especialista.
4. El Especialista solicita consultar sus citas asignadas.
5. El sistema obtiene las citas vinculadas con el Especialista.
6. El sistema muestra la sección Citas asignadas con el calendario o la lista obtenida.
7. El Especialista selecciona una cita.
8. El sistema obtiene los detalles de la cita seleccionada.
9. El sistema obtiene la información del Perfil del Menor vinculado con la cita.
10. El sistema muestra los detalles de la cita y la información disponible del menor.
11. El Especialista solicita consultar el historial del menor.
12. El sistema obtiene el historial vinculado con el Perfil del Menor.
13. El sistema obtiene los registros de atenciones anteriores contenidos en el historial.
14. El sistema muestra los antecedentes y los registros de atenciones anteriores del menor.
15. El Especialista solicita registrar la atención.
16. El sistema muestra el formulario de registro de atención.
17. El Especialista ingresa:
    - Observaciones de la atención.
    - Información relevante identificada.
    - Recomendaciones para el Familiar y el menor.
    - Diagnóstico, si aplica.
    - Indicaciones de seguimiento, si aplica.
    - Notas adicionales, si aplica.
18. El Especialista confirma el registro.
19. El sistema verifica que los campos obligatorios estén completos y que la información sea válida.
20. El sistema crea el registro de la atención.
21. El sistema vincula la atención con la cita, el Especialista y el Perfil del Menor.
22. El sistema incorpora la atención al historial del menor.
23. El sistema actualiza el estado de la cita a **ATENDIDA**.
24. El sistema registra los cambios realizados.
25. El sistema obtiene la lista actualizada de citas asignadas al Especialista.
26. El sistema muestra el mensaje: «La atención ha sido registrada correctamente».
27. El sistema muestra la sección Citas asignadas con la información actualizada.

### Flujos alternativos

#### 7a. Registrar inasistencia

1. El Especialista selecciona una cita a la que el Familiar y el menor no asistieron.
2. El sistema obtiene los detalles de la cita seleccionada.
3. El sistema obtiene la información del Perfil del Menor vinculado con la cita.
4. El sistema muestra los detalles de la cita.
5. El Especialista solicita registrar la inasistencia.
6. El sistema muestra el formulario de registro de inasistencia.
7. El Especialista ingresa una observación y confirma el registro.
8. El sistema verifica que la información obligatoria esté completa.
9. El sistema crea el registro de inasistencia.
10. El sistema vincula la inasistencia con la cita y el Perfil del Menor.
11. El sistema incorpora la inasistencia al historial del menor.
12. El sistema actualiza el estado de la cita a **NO ASISTIDA**.
13. El sistema registra los cambios realizados.
14. El sistema obtiene la lista actualizada de citas asignadas al Especialista.
15. El sistema muestra el mensaje: «La inasistencia ha sido registrada correctamente».
16. El sistema muestra la sección Citas asignadas con la información actualizada.

#### 18a. Cancelar registro de atención

1. El Especialista solicita cancelar el registro de la atención.
2. El sistema descarta la información no confirmada.
3. El sistema obtiene nuevamente los detalles de la cita seleccionada.
4. El sistema obtiene la información del Perfil del Menor vinculado con la cita.
5. El sistema muestra nuevamente los detalles de la cita y la información del menor.

#### 19a. Datos incompletos

1. El sistema identifica que existen campos obligatorios sin completar.
2. El sistema muestra el mensaje: «Complete los campos obligatorios».
3. El sistema mantiene visible el formulario de registro de atención con la información previamente ingresada.
