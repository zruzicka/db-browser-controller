# db-browser-controller

Basic MySQL database browser with REST API.

## How to run locally

* Application can be started as SpringBoot application via `cz.zr.browser.Application`.
* Starts with default port `8080` and `context-path: /db-browser-controller`.

## REST API

When started locally, Swagger API contract is available at http://localhost:8080/db-browser-controller/swagger-ui/.

### Connection Controller 
* Provides CRUD operations for DB connections details.
* Locally available at http://localhost:8080/db-browser-controller/swagger-ui/#/connection-controller.

HTTP| Endpoint | Description
--|--|--
GET | ​/db-browser-controller​/v1​/connections | Returns all available connections. (Pagination, sorting and filtering is not supported yet.)
POST | ​/db-browser-controller​/v1​/connections | Creates new DB connection record.
PUT | ​/db-browser-controller​/v1​/connections​/{id} | Updates DB connection record.
DELETE | ​/db-browser-controller​/v1​/connections​/{id} | Deletes DB connection record.

### DB Browser Controller
* Provides REST API for browsing structure and data using database connections stored via Connection Controller above.
* Locally available at http://localhost:8080/db-browser-controller/swagger-ui/#/db-browser-controller.
* Current API design does not allow to specify particular DB name or DB schema only via endpoint URL path. Applied `databaseName` is loaded from Connection record peristed via [Connection Controller](https://github.com/zruzicka/db-browser-controller#connection-controller). If you need to select particular `databaseName`, please select related Connection via connection `{id}` `PathVariable`.

HTTP| Endpoint | Description
--|--|--
GET | ​/db-browser-controller​/v1​/connections​/{id}​/schemas | Returns all available schemas of selected DB connection.
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables | Returns all available tables of selected DB connection.
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables​/{tableName}​/columns | Returns all available columns of selected DB connection and DB table.
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables​/{tableName}​/preview | Returns all available rows of selected DB connection and DB table.

### REST API endpoints for statistics
* Statistics endpoints included among [DB Browser Controller endpoints](http://localhost:8080/db-browser-controller/swagger-ui/#/db-browser-controller).

HTTP| Endpoint | Description
--|--|--
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables​/{tableName}​/columns​/statistics | Returns statistics for each column of selected DB table within selected DB connection.
GET | ​/db-browser-controller​/v1​/connections​/{id}​/schemas/{schemaName}/tables​/{tableName}​/statistics | Returns statistics of selected DB table within selected DB connection.

Available Columns Statistics
* min 
* max 
* avg

Available Table Statistics
* recordsCount
* columnsCount
