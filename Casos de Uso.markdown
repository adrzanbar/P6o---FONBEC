# Documento de Casos de Uso - FONBEC

**Sistema de Gestión de Cartas de Fonbec**  
**Fecha**: 11/06/2025  
**Versión**: 1.1  
**Autores**: Adrián Zangla, Emmanuel Longhino, Mauro Molina

---

## 1. Introducción

Este documento detalla los casos de uso del sistema de gestión de cartas de Fonbec, diseñado para la administración, seguimiento y envío de cartas entre becarios, padrinos y mediadores. El objetivo es modelar los flujos funcionales principales, definir los roles involucrados y garantizar el cumplimiento de los requerimientos funcionales identificados.

---

## 2. Actores del Sistema

-   **Administrador**
-   **Voluntario**
-   **Mediador**
-   **Becario Universitario** (caso especial de Mediador)
-   **Sistema** (para procesos automáticos, como el envío de correos)

---

## 3. Casos de Uso

### 3.1. Administrador

#### CU-01 – Gestión de Usuarios (ABM de Usuarios)

**Actor principal**: Administrador  
**Descripción**: El Administrador puede dar de alta, modificar o eliminar usuarios del sistema, asignando roles (Voluntario, Mediador, Superusuario, etc.). Un superusuario tiene permisos para realizar todas las acciones de cualquier rol.  
**Precondiciones**: El usuario debe estar autenticado como Administrador.  
**Flujo principal**:

1. Ingresar a la sección “Usuarios”.
2. Visualizar listado de usuarios existentes.
3. Seleccionar opción: Alta / Modificación / Baja.
4. Para alta: ingresar nombre, email, tipo de usuario.
5. Guardar cambios.  
   **Postcondición**: La lista de usuarios queda actualizada.

---

#### CU-02 – Importar Información de Sistemas Externos

**Actor principal**: Administrador  
**Descripción**: Permite cargar datos masivos de becarios, padrinos y mediadores desde archivos externos (CSV, XLSX).  
**Flujo principal**:

1. Seleccionar tipo de entidad a importar (becario, padrino, mediador).
2. Cargar archivo externo.
3. Visualizar previsualización de datos.
4. Confirmar e importar.  
   **Reglas de negocio**:

-   Validación de estructura del archivo.
-   Rechazo de registros duplicados o inválidos.  
    **Postcondición**: Nuevas entidades cargadas al sistema.

---

#### CU-03 – Gestión de Becarios, Padrinos y Mediadores (ABM)

**Actor principal**: Administrador  
**Descripción**: Gestión individual de becarios, padrinos y mediadores, incluyendo asignación de relaciones.  
**Flujo principal**:

1. Ingresar a la sección correspondiente (becarios, padrinos, mediadores).
2. Listar registros existentes.
3. Realizar Alta / Baja / Modificación.
4. Asignar relaciones (becario ↔ padrino, mediador ↔ becario).  
   **Postcondición**: Datos actualizados en la base del sistema.

---

#### CU-04 – Acceso al Repositorio de Cartas

**Actor principal**: Administrador  
**Descripción**: Acceder al total de cartas del sistema, sin restricción por tipo o estado.  
**Flujo principal**:

1. Ingresar al repositorio.
2. Visualizar listado de cartas.
3. Descargar o ver detalles.  
   **Postcondición**: Acceso de solo lectura al repositorio completo.

---

#### CU-06 – Gestión de Cartas

**Actor principal**: Administrador  
**Descripción**: El Administrador puede modificar cualquier aspecto de cualquier carta en el sistema, incluyendo estado, observaciones, imágenes, orden de archivos, y metadatos (becario, padrino, mediador), sin restricciones de asignación o estado.  
**Precondiciones**:

-   El usuario debe estar autenticado como Administrador.  
    **Estados permitidos**:
-   Subida
-   Asignada
-   Aprobada / Desaprobada
-   Enviada / Pendiente de envío
-   Subida fuera de término / Enviada fuera de término  
    **Flujo principal**:

1. Acceder al repositorio de cartas (CU-04).
2. Seleccionar una carta para gestionar.
3. Acceder al detalle de la carta.
4. Realizar una o más de las siguientes acciones:  
   a. Modificar el estado de la carta seleccionando una opción disponible.  
   b. Agregar una observación en el campo correspondiente.  
   c. Editar metadatos (becario, padrino, voluntario, mediador asociados).  
   d. Subir nuevas imágenes originales, sobrescribiendo cualquier imagen original existente.
   f. Subir nuevas versiones de imágenes, sobrescribiendo cualquier nueva versión existente.
   e. Reordenar los archivos de la versión utilizada (nueva o original).
5. Confirmar los cambios.  
   **Reglas de negocio**:

-   El Administrador puede modificar cualquier carta, independientemente de su estado o asignación.
-   Los formatos de archivo permitidos son PDF, JPG, PNG, con un máximo de 3 imágenes por versión.
-   Subir una nueva versión sobrescribe cualquier nueva versión existente; no se acumulan múltiples nuevas versiones.
-   Eliminar una nueva versión revierte al uso de la versión original para visualización y envío.
-   El sistema usa la nueva versión para visualización y envío si está presente; de lo contrario, usa la versión original.
-   El orden de los archivos definido se respeta en la visualización y envío.
-   Todas las modificaciones (estado, observaciones, imágenes, metadatos) se registran en el historial de la carta, incluyendo el Administrador y la fecha/hora.  
    **Postcondición**: Los cambios realizados (estado, observaciones, metadatos, imágenes, orden) se actualizan en el sistema, la versión original se conserva, y el historial refleja las modificaciones.

---

#### CU-06 – Filtrado de Cartas por Criterios

**Actor principal**: Administrador  
**Descripción**: Permite aplicar filtros para encontrar cartas específicas.  
**Criterios disponibles**:

-   Mediador
-   Becario
-   Padrino
-   Fecha de subida o envío
-   Estado de carta  
    **Flujo principal**:

1. Aplicar uno o más filtros.
2. Visualizar resultados.
3. Realizar acciones sobre las cartas (descargar, observar estado).  
   **Postcondición**: Cartas filtradas correctamente.

---

#### CU-07 – Configuración de Fechas de Subida Límite

**Actor principal**: Administrador  
**Descripción**: Definir fechas límite para la carga de cartas en el sistema.  
**Flujo principal**:

1. Ingresar a configuración de fechas.
2. Seleccionar fecha de corte.
3. Guardar cambios.  
   **Reglas de negocio**: Las cartas cargadas después de la fecha se marcan como “Fuera de término”.  
   **Postcondición**: Fecha límite registrada.

---

#### CU-08 – Configuración de Fecha de Envío

**Actor principal**: Administrador  
**Descripción**: Determinar la fecha objetivo para el envío de cartas a los padrinos.  
**Flujo principal**:

1. Ingresar a configuración.
2. Establecer fecha de envío.
3. Confirmar.  
   **Postcondición**: Fecha registrada; usada para marcar envíos “Fuera de término” si se incumple.

---

#### CU-09 – Administración de Formato de Correo

**Actor principal**: Administrador  
**Descripción**: Define la plantilla y contenido del correo que se enviará con las cartas.  
**Elementos configurables**:

-   Imagen de cabecera
-   Asunto
-   Título (dirigido al padrino)
-   Cuerpo del texto (personalizado por tipo de destinatario)
-   Despedida
-   Mensaje de agradecimiento
-   Imagen al pie
-   Enlaces (redes sociales, sitio)
-   Firma (datos de contacto de Fonbec)  
    **Flujo principal**:

1. Ingresar a la sección “Formato de correo”.
2. Configurar los campos editables.
3. Previsualizar.
4. Guardar.  
   **Postcondición**: Plantilla de correo actualizada para envíos.

---

#### CU-10 – Envío Programado de Cartas por Correo Electrónico

**Actor principal**: Sistema (automático)  
**Actor secundario**: Administrador (configura fecha y formato)  
**Descripción**: El sistema envía automáticamente las cartas aprobadas a los padrinos correspondientes por correo electrónico en la fecha configurada, utilizando el formato de correo definido.  
**Precondiciones**:

-   La fecha de envío está configurada (CU-07).
-   El formato de correo está definido (CU-08).
-   Las cartas están en estado “Aprobada” y “Pendiente de envío”.
-   Los padrinos tienen direcciones de correo válidas.
-   Un proveedor de correo está configurado en el sistema.  
    **Flujo principal**:

1. En la fecha de envío configurada, el sistema inicia un trabajo programado.
2. Recupera todas las cartas en estado “Aprobada” y “Pendiente de envío”.
3. Para cada carta:  
   a. Identifica al padrino asociado y su correo electrónico.  
   b. Genera un correo con el formato configurado (incluye imagen de cabecera, asunto, título, cuerpo, despedida, agradecimiento, imagen al pie, enlaces y firma).  
   c. Adjunta los archivos de la carta en el orden definido.  
   d. Envía el correo al correo del padrino.  
   e. Actualiza el estado de la carta a “Enviada” y registra la fecha/hora de envío.
4. Si la fecha de envío ha pasado, marca las cartas enviadas como “Enviada fuera de término”.
5. Notifica al Administrador con un resumen del proceso, incluyendo correos enviados y fallidos (si los hay).  
   **Reglas de negocio**:

-   Solo se envían correos a padrinos con direcciones válidas.
-   Las cartas deben estar en estado “Aprobada” para enviarse.
-   Los archivos adjuntos respetan el orden definido por el Mediador o Voluntario.
-   Los envíos fallidos (ej. correo inválido) se registran, y la carta permanece en “Pendiente de envío” para resolución manual.  
    **Postcondición**: Las cartas aprobadas se envían a los padrinos, sus estados se actualizan a “Enviada” (o “Enviada fuera de término” si aplica), y el Administrador recibe un informe del proceso.

---

### 3.2. Voluntario

#### CU-V01 – Acceso al Repositorio de Cartas

**Actor principal**: Voluntario  
**Descripción**: Permite al Voluntario acceder al listado de cartas disponibles para revisión, excluyendo las de becarios universitarios y las asignadas a otros Voluntarios. También muestra las cartas asignadas al Voluntario actual.  
**Precondiciones**:

-   El usuario debe estar autenticado como Voluntario.  
    **Flujo principal**:

1. Ingresar a la sección “Cartas”.
2. Visualizar el listado de cartas disponibles (en estado “Subida” y no asignadas) y las asignadas al Voluntario.
3. Opcional: Filtrar por estado, padrino, becario, mediador o fechas.  
   **Postcondición**: Se muestra el listado de cartas disponibles y asignadas, excluyendo las de becarios universitarios y las asignadas a otros Voluntarios.

---

#### CU-V02 – Seleccionar Carta para Revisión

**Actor principal**: Voluntario  
**Descripción**: El Voluntario selecciona una carta disponible en el repositorio para revisarla, asignándola exclusivamente a sí mismo. La carta deja de mostrarse en el repositorio principal para otros Voluntarios, pero sigue visible para los Administradores.  
**Precondiciones**:

-   El usuario debe estar autenticado como Voluntario.
-   La carta debe estar en estado “Subida”, no asignada a otro Voluntario, y no pertenecer a un becario universitario.  
    **Flujo principal**:

1. Acceder a la sección “Cartas” (CU-V01).
2. Visualizar el listado de cartas disponibles.
3. Seleccionar una carta para revisión.
4. Confirmar la selección.  
   **Reglas de negocio**:

-   Al seleccionar, la carta cambia su estado a “Asignada” y se asocia al Voluntario.
-   La carta ya no aparece en el repositorio principal para otros Voluntarios.
-   Los Administradores pueden seguir viendo la carta en su repositorio completo (CU-04).  
    **Postcondición**: La carta queda asignada al Voluntario, su estado cambia a “Asignada”, and se oculta del repositorio principal para otros Voluntarios.

---

#### CU-V03 – Revisar Carta Asignada

**Actor principal**: Voluntario  
**Descripción**: El Voluntario revisa una carta asignada, modificando su estado según la evaluación realizada y, opcionalmente, agregando observaciones para documentar la revisión, aprobación o rechazo.  
**Precondiciones**:

-   El usuario debe estar autenticado como Voluntario.
-   La carta debe estar asignada al Voluntario (ver CU-V02).  
    **Estados permitidos**:
-   Asignada
-   Aprobada / Desaprobada
-   Enviada / Pendiente de envío  
    **Flujo principal**:

1. Acceder al repositorio de cartas (CU-V01).
2. Seleccionar una carta asignada al Voluntario.
3. Acceder al detalle de la carta.
4. Elegir un nuevo estado desde las opciones disponibles.
5. Opcional: Ingresar un comentario u observación en el campo correspondiente.
6. Confirmar los cambios.  
   **Reglas de negocio**:

-   El cambio de estado es obligatorio para completar la revisión.
-   Las observaciones, si se ingresan, registran el nombre del Voluntario y la fecha/hora de carga.
-   Las observaciones son visibles en el historial de la carta.  
    **Postcondición**: El estado de la carta queda modificado en el sistema, y cualquier observación ingresada se registra en el historial.

---

#### CU-V04 – Descargar Carta

**Actor principal**: Voluntario  
**Descripción**: El Voluntario puede descargar una carta asignada o disponible para revisión en el repositorio, excluyendo cartas de becarios universitarios.  
**Precondiciones**:

-   El usuario debe estar autenticado como Voluntario.
-   La carta debe estar visible en el repositorio (disponible o asignada al Voluntario) and no pertenecer a un becario universitario.  
    **Flujo principal**:

1. Acceder a la sección “Cartas” (CU-V01).
2. Seleccionar una carta.
3. Hacer clic en la opción “Descargar”.
4. Confirmar la descarga del archivo.  
   **Postcondición**: La carta (conjunto de imágenes) se descarga en el dispositivo local del Voluntario.

---

#### CU-V05 – Subir Nueva Versión de la Imagen de la Carta

**Actor principal**: Voluntario  
**Descripción**: El Voluntario puede subir una única nueva versión de las imágenes de una carta asignada (PDF, JPG, PNG), sobrescribiendo cualquier nueva versión existente pero conservando siempre la versión original. El sistema utiliza la nueva versión para visualización y envío, con fallback a la versión original si no existe una nueva versión o si esta se elimina. El Voluntario también puede eliminar la nueva versión, revirtiendo al uso de la original.  
**Precondiciones**:

-   El usuario debe estar autenticado como Voluntario.
-   La carta debe estar asignada al Voluntario (ver CU-V02).
-   La carta debe estar en estado editable (Asignada, Aprobada, Desaprobada, Pendiente de envío).  
    **Flujo principal**:

1. Acceder al detalle de una carta asignada.
2. Seleccionar la opción “Agregar versión” o “Eliminar versión” si ya existe una nueva versión.
3. Si selecciona “Agregar versión”:  
   a. Subir un conjunto de imágenes (máx. 3; PDF, JPG, PNG).  
   b. Establecer el orden de las imágenes si son múltiples.  
   c. Guardar y confirmar.
4. Si selecciona “Eliminar versión”:  
    a. Confirmar la eliminación de la nueva versión.  
    b. El sistema revierte al uso de la versión original.  
   **Reglas de negocio**:

-   Los formatos permitidos son PDF, JPG, PNG.
-   Máximo 3 imágenes por versión.
-   La versión original (cargada por el Mediador o Becario Universitario) siempre se conserva y nunca se modifica.
-   Si ya existe una nueva versión, subir una nueva la sobrescribe; no se acumulan múltiples nuevas versiones.
-   El Voluntario puede eliminar la nueva versión, en cuyo caso el sistema utiliza la versión original para visualización y envío.
-   El sistema usa la nueva versión para visualización y envío si está presente; de lo contrario, usa la versión original.
-   Se registra el Voluntario y la fecha de carga o eliminación en el historial de versiones.  
    **Postcondición**: La nueva versión de imágenes, si se subió, está disponible y se utiliza por defecto; la versión anterior (si existía) se sobrescribe; la versión original se conserva. Si la nueva versión se elimina, el sistema revierte a la versión original.

---

#### CU-V06 – Ordenar Archivos de una Carta

**Actor principal**: Voluntario  
**Descripción**: Permite al Voluntario definir el orden de las imágenes asociadas a una carta asignada, ya sea para la versión original o la nueva versión (si existe).  
**Precondiciones**:

-   El usuario debe estar autenticado como Voluntario.
-   La carta debe estar asignada al Voluntario and tener más de un archivo adjunto en la versión utilizada (original o nueva).  
    **Flujo principal**:

1. Acceder al detalle de una carta asignada.
2. Visualizar los archivos de la versión utilizada (nueva si existe, original en caso contrario).
3. Reordenar los archivos (mediante controles de arrastre o botones).
4. Guardar el nuevo orden.  
   **Reglas de negocio**:

-   El orden definido se aplica a la versión utilizada por el sistema (nueva o original).
-   El orden se respeta en la visualización and en el envío del correo electrónico.  
    **Postcondición**: El orden de las imágenes se registra and se respeta en la visualización and envío.

---

### 3.3. Mediador

#### CU-M01 – Visualización de Becarios Asignados y sus Datos

**Actor principal**: Mediador  
**Descripción**: El Mediador puede visualizar la lista de becarios asignados, sus datos personales y los datos de sus padrinos.  
**Precondiciones**: El usuario debe estar autenticado como Mediador.  
**Flujo principal**:

1. Ingresar a la sección “Mis Casos”.
2. Visualizar el listado de becarios asignados.
3. Acceder al detalle de cada becario.
4. Consultar información del becario y sus padrinos.  
   **Postcondición**: Información consultada correctamente.

#### CU-M02 – Alta de Carta

**Actor principal**: Mediador  
**Descripción**: El Mediador puede subir nuevas cartas al sistema, seleccionando el becario y los padrinos destinatarios.  
**Precondiciones**: El Mediador debe tener asignado al becario.  
**Flujo principal**:

1. Ingresar a “Subir Carta”.
2. Seleccionar becario asignado.
3. Seleccionar padrinos/s destinatario/s.
4. Adjuntar archivos (máx. 3; tipos: PDF, JPG, PNG).
5. Establecer orden de los archivos si son múltiples.
6. Confirmar subida.  
   **Reglas de negocio**:

-   Tipos de archivo válidos: PDF, JPG, PNG.
-   Máximo 3 archivos por carta.
-   Se registra la fecha de carga y el usuario.  
    **Postcondición**: Carta cargada correctamente, asociada al becario y al/los padrino/s.

---

#### CU-M03 – Modificación de Carta Existente

**Actor principal**: Mediador  
**Descripción**: Permite modificar una carta previamente cargada por el Mediador, dentro del período habilitado.  
**Precondiciones**:

-   La carta debe haber sido cargada por el mismo Mediador.
-   La carta no debe estar aprobada ni enviada.  
    **Flujo principal**:

1. Acceder al listado de cartas propias.
2. Seleccionar carta a modificar.
3. Editar becario o padrinos si es necesario.
4. Reemplazar archivos (respetando reglas de formato y peso).
5. Establecer nuevo orden si corresponde.
6. Confirmar modificación.  
   **Postcondición**: Carta actualizada.

---

#### CU-M04 – Eliminación de Carta

**Actor principal**: Mediador  
**Descripción**: Elimina una carta cargada por el Mediador si no ha sido procesada o aprobada.  
**Precondiciones**: Carta en estado editable (no enviada ni aprobada).  
**Flujo principal**:

1. Ingresar al listado de cartas propias.
2. Seleccionar carta a eliminar.
3. Confirmar la eliminación.  
   **Postcondición**: Carta eliminada del sistema.

#### CU-M05 – Ordenar Archivos de una Carta

**Actor principal**: Mediador  
**Descripción**: Define el orden de los archivos adjuntos de una carta (ej. imágenes escaneadas).  
**Precondiciones**: Carta con más de un archivo cargado.  
**Flujo principal**:

1. Acceder al detalle de la carta.
2. Visualizar archivos adjuntos.
3. Reordenar usando controles.
4. Guardar nuevo orden.  
   **Postcondición**: El orden de los archivos queda registrado.

---

### 3.4. Becario Universitario

#### CU-BU01 – Visualización de su Información y Padrinos

**Actor principal**: Becario Universitario  
**Descripción**: Permite visualizar sus propios datos personales y los datos de sus padrinos asignados.  
**Precondiciones**: El usuario debe estar autenticado como Becario Universitario.  
**Flujo principal**:

1. Ingresar a la sección “Mis Datos”.
2. Visualizar información personal.
3. Visualizar listado de padrinos asociados.  
   **Postcondición**: Información consultada correctamente.

---

#### CU-BU02 – Alta de Carta Personal

**Actor principal**: Becario Universitario  
**Descripción**: Permite cargar cartas propias, dirigiéndolas a sus padrinos.  
**Precondiciones**: El usuario debe tener al menos un padrino asignado.  
**Flujo principal**:

1. Ingresar a “Subir Carta”.
2. Seleccionar uno o más padrinos destinatarios.
3. Adjuntar archivos (máx. 3; tipos: PDF, JPG, PNG).
4. Establecer orden si hay múltiples archivos.
5. Confirmar subida.  
   **Reglas de negocio**:

-   Tipos de archivo válidos: PDF, JPG, PNG.
-   Máximo 3 archivos por carta.
-   Solo puede gestionar su propia información.  
    **Postcondición**: Carta cargada correctamente, asociada al becario universitario.

---

#### CU-BU03 – Modificación de Carta Propia

**Actor principal**: Becario Universitario  
**Descripción**: Edita una carta previamente subida, si no ha sido enviada o aprobada.  
**Precondiciones**:

-   La carta debe pertenecer al becario.
-   Debe estar en estado editable.  
    **Flujo principal**:

1. Ingresar al listado de sus cartas.
2. Seleccionar carta a modificar.
3. Cambiar padrinos destinatarios si es necesario.
4. Reemplazar archivos (respetando límites).
5. Ajustar orden si aplica.
6. Confirmar cambios.  
   **Postcondición**: Carta modificada correctamente.

---

#### CU-BU04 – Eliminación de Carta Propia

**Actor principal**: Becario Universitario  
**Descripción**: Elimina una carta propia si no fue aprobada ni enviada.  
**Precondiciones**: Carta en estado editable.  
**Flujo principal**:

1. Ingresar al listado de cartas.
2. Seleccionar carta a eliminar.
3. Confirmar eliminación.  
   **Postcondición**: Carta eliminada del sistema.

---

#### CU-BU05 – Ordenar Archivos de la Carta

**Actor principal**: Becario Universitario  
**Descripción**: Establece el orden de los archivos de una carta personal.  
**Precondiciones**: Carta con más de un archivo cargado.  
**Flujo principal**:

1. Ingresar al detalle de la carta.
2. Visualizar archivos.
3. Reordenar manualmente.
4. Confirmar nuevo orden.  
   **Postcondición**: Orden de archivos actualizados.

