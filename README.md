# MarvelAPI
Prueba técnica

Consiste en consumir un API público securizado, para bajarse una serie de datos a una base de datos embebida en la aplicación, para luego exponer una serie de servicios que permitan realizar diversas acciones sobre ese conjunto de datos.
Se deberá desarrollar con los siguientes requisitos mínimos:
        Springboot
        Hibernate
        Maven
        Java 8 o superior
El candidato deberá entregar el proyecto a través de un repositorio público en github.
El detalle sería el siguiente:
Hacer uso del servicio GET /v1/public/characters del API de Marvel, https://developer.marvel.com, para descargarse los datos de los personajes de Marvel y los comics en los que aparecen. 
Es un API securizado, por lo que se tendrá que componer el apiKey de la forma indicada en la documentación de dicho API. Como el apikey tiene un componente privado, preparar el proyecto para que dicho apikey se reciba como argumento en la ejecución.
Los datos a almacenar, (en una base de datos H2), serán los siguientes:
        Id del personaje. No el del API, un secuencial interno.
        Nombre del personaje
        Descripción del personaje
        Url de la imagen del personaje
        Id del comic en el que aparece
        Nombre del comic en el que aparece
Exponer una serie de servicios que se alimenten de los datos almacenados de forma local:
CRUD de los datos generales del personaje. 
        Consulta de los personajes. Sólo su info, id, nombre, descripción y url de la imagen del personaje.
        Modificación de los datos del personaje. Nombre y/o descripción.
        Creación de nuevo personaje.
        Borrado de un personaje. 
Consulta del detalle de un personaje que incluya los comics en los que aparece. Recibirá el id del personaje.
Consulta de los n comics con más personajes, siendo n un parámetro de entrada del servicio. 
Estará ordenado de mayor a menor y devolverá los personajes (id y nombre) y el número de personajes que aparecen en cada uno de ellos.
