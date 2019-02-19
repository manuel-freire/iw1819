# iw1819

Plantilla para Ingeniería Web UCM, edición 2018-19

## La plantilla

Esta plantilla está pensada para ser usada como la base de los proyectos a realizar en la edición 2018-19 de la asignatura. Está basada en el resultado de crear un proyecto en STS 4.1.1 vía `new spring starter project > security + jpa + hsqldb + thymeleaf + web + websocket`, pero además incorpora

+ una aplicación de ejemplo completa, que usa WebSockets para medir el grado de seguimiento de una clase, y que permite la subida y descarga de ficheros
+ clases de modelo que, vía JPA, se persisten como tablas en la BD HSQLDB, incluyendo para autenticación.
+ pruebas unitarias
+ un fichero "leeme.html" de ejemplo

## La aplicación: el Karmómetro

+ Lee su "leeme.html" para ver tanto el proyecto como su estructura a nivel de BD y vistas.
+ Lánzala ejecutando la aplicación y entrando vía http://localhost:8080

## Cómo funciona

Hay 3 tipos de usuarios: el administrador, que puede dar de alta profesores. Los profesores, que pueden gestionar grupos y activar / desactivar encuestas; y los alumnos, que para hacer login usan el código asignado en el momento de crear el grupo (y que les dice el profe), y que votan para hacer saber su opinión.

En realidad, los alumnos se crean dinámicamente, en el momento de suministrar el código.

Cuando entran en la aplicación, pueden votar sobre una serie de preguntas estándar (configuradas por el profesor), y hacer y votar sus propias preguntas. En todo momento, pueden ver qué han votado ellos y sus compañeros (en forma de agregado: la aplicación es muy anónima, para que no haya miedo a participar).

## Los fuentes: partes principales

Como todo proyecto maven, los fuentes se encuentran bajo la carpeta src/ (y los metadatos del proyecto, incluyendo la forma de construirlo y sus dependencias, están en el pom.xml). Siguiendo con el esquema maven, en src/ hay varios subdirectorios:

- src/main: para los fuentes que forman parte de la distribución
    + java: para fuentes java que forman la aplicación en sí
        * [es.ucm.fdi.iw](https://github.com/manuel-freire/iw1819/tree/master/src/main/java/es/ucm/fdi/iw): configuración de la aplicación web Spring. Distintos ficheros se encargan de distintos apartados:
            - AppConfig: expone algunos "beans" para i18n (internacionalización) y gestión de rutas para ficheros cargados por los usuarios, en colaboración con LocalData.
            - SecurityConfig: configura los accesos a la aplicación, en colaboración con IwUserDetailsService, que especifica cómo verificar permisos en las tablas generadas con JPA. Hay usuarios pre-creados en el import.sql.
            - WebSocketConfig configura el uso de WebSockets, y IwApplication especifica cómo lanzar la aplicación desde spring boot.
        * [es.ucm.fdi.iw.control](https://github.com/manuel-freire/iw1819/tree/master/src/main/java/es/ucm/fdi/iw/control): controladores; gestión de peticiones a la aplicación. Las anotadas con `@Controller` permiten especificar mapeos petición-manejador (con `@GetMapping` y familia)
        * [es.ucm.fdi.iw.model](https://github.com/manuel-freire/iw1819/tree/master/src/main/java/es/ucm/fdi/iw/model): clases de modelo; las anotadas con `@Entity` generan tablas usando JPA
    + [resources](https://github.com/manuel-freire/iw1819/tree/master/src/main/resources): para ficheros de configuración, o recursos que se sirven por la aplicación pero que no forman parte directamente del código de la misma
        * import.sql: contenido inicial de la base de datos. Se ejecuta después de generar la BD a partir de las clases del modelo, y debe ser compatible con lo generado.
        * application*.properties: configuración a alto nivel de la aplicación. El principal es application.properties, y ahí puedes especificar un "perfil", según el cual se cargará o bien application-default.properties (si no lo cambias), que usa una BD que se regenera a cada vez -- o application-externaldb.properties, que usa una BD externa persistente, pero que tienes que haber lanzado antes para que funcione.
        * Message*.properties: por si quieres internacionalizar tu aplicación, lo cual es educativo pero aburrido, y no sube mucho la nota (pero es bueno entender cómo funciona). Ver transparencias sobre thymeleaf para ver cómo incluir mensajes internacionalizados en tu aplicación.
        * [static](https://github.com/manuel-freire/iw1819/tree/master/src/main/resources/static): recursos web no-dinámicos, tipo imágenes, CSSs, JS, ...
        * [templates](https://github.com/manuel-freire/iw1819/tree/master/src/main/resources/templates): templates thymeleaf, y fragmentos de los mismos
- src/test: para los ficheros que se usan sólo en las pruebas
- src/test/java: código de pruebas
- src/test/resources: ficheros de recursos para usar durante las pruebas

## Usando esto como plantilla

+ Clona el repositorio a tu cuenta de grupo
+ Modifica, en el pom.xml, el nombre de proyecto y su grupo
+ Elimina todo lo que sea karmómetro-específico:
    - sustituye la información del leeme
    - sustituye las clases de modelo karmómetro-específicas
+ Ábrelo con tu copia de STS
