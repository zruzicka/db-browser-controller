# db-browser-controller

Basic MySQL database browser with REST API.

## How to run 

* Application can be started as SpringBoot application via `cz.zr.browser.Application`.
* Starts with default port `8080` and `context-path: /db-browser-controller`

## REST API

When started locally, Swagger API contract is available at http://localhost:8080/db-browser-controller/swagger-ui/

### Connection Controller 
* Provides CRUD operations for DB connections details.
* Locally available at http://localhost:8080/db-browser-controller/swagger-ui/#/connection-controller

HTTP| Endpoint | Description
--|--|--
GET | ​/db-browser-controller​/v1​/connections | Returns all available connections. (Pagination, sorting and filtering is not supported yet.)
POST | ​/db-browser-controller​/v1​/connections | Create connection
PUT | ​/db-browser-controller​/v1​/connections​/{id} | Update connection
DELETE | ​/db-browser-controller​/v1​/connections​/{id} | Delete connection

### DB Browser Controller
* Provides REST API for browsing structure and data using database connections stored via Connection Controller above.
* Locally available at http://localhost:8080/db-browser-controller/swagger-ui/#/db-browser-controller

HTTP| Endpoint | Description
--|--|--
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables | Returns all available tables of selected DB connection.
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables​/{tableName}​/columns | Returns all available columns of selected DB connection and DB table.
GET | ​/db-browser-controller​/v1​/connections​/{id}​/tables​/{tableName}​/preview | Returns all available rows of selected DB connection and DB table.
