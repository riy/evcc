## Build a Client API: Backend Coding Challenge

### Purpose

The purpose of this project is to build a REST-based API that handles Client data. With this application it should be possible to create new clients. A client is identified by the client ID. We should be able to edit first name, last name, telephone number, email address and postal address (providing street, postal code, city and country).

The application was built on Spring Boot and builds with Java 17. It utilizes a couple of Gradle plugins that help with maintaining clean code and following best practices in regard to coding style, code quality and code formatting.

### Compiling

Code quality is ensured, so make sure you fix all issues reported by Checkstyle, PMD or Spotless. The `etc/config/` folder contains the settings for the IDEA Eclipse code formatter plugin, as well as the import order rules. Both are applied to your code when running
```bash
./gradlew spotlessApply
```

If your code is not properly formatted the build will fail, so this command is your friend.

### Code coverage

I've added the Gradle JaCoCo plugin to ensure 100% code coverage with tests. If you make changes and your build fails you need to look into the JaCoCo report and fix where it says that the code is not fully covered by tests.

Note: full code coverage has not been achieved yet, so I've prevented the build from failing for now. See [gradle/jacoco.gradle:33](gradle/jacoco.gradle#L33) & [:43](gradle/jacoco.gradle#L43) to change that.

### Run build and tests
```bash
./gradlew build
```

### Run application
```bash
./gradlew bootRun
```

The application will launch headless, with no web frontend, listen on port `8081` (`8080` is usually reserved for web frontends). For the sake of simplicity the `populate` Spring profile will automatically be included, which will trigger the `ClientTestDataCreator` bean and insert 250 randomly generated client records. The REST-based API will be up and listen at a root of `http://localhost:8081/api`.

### REST requests

This is a list of REST requests you may execute against the API. Please note that for this simple case no versioning has been implemented. One option would be to version by URL, e.g. `/api/v1/`, the other by supplying version information in a `RequestHeader`. 

#### GET
Returns a paged list of all clients 
```bash
curl -X GET -H "Accept: application/json" "http://localhost:8081/api/clients?page=0&size=2"
```

#### GET
Returns one client. Enter an unknown ID to see an error message:
```bash
curl -X GET -H "Accept: application/json" "http://localhost:8081/api/client/<UUID>"
```

#### POST
Searches for active clients, and provides a paged list of search results. Each search attribute is optional and may be skipped. Whatever has been provided is ANDed in the search query.
```bash
curl -X POST -H "Accept: application/json" "http://localhost:8081/api/clients?page=0&size=5" \
  -H "Content-Type: application/json; charset=utf-8" \
  -d $'{ \
    "lastname": "Hedda", \
    "zipCode": "Blunt", \
    "city": "Hamburg", \
    "country": "Germany" \
  }'
```

#### POST
Creates a new client. The example includes mandatory fields only - remove any of them to see an error message:
```bash
curl -X "POST" "http://localhost:8081/api/client" \
  -H "Content-Type: application/json; charset=utf-8" \
  -d $'{ \
    "firstname": "Hedda", \
    "lastname": "Blunt", \
    "email": "hedda.blunt2020@gmail.com", \
    "status": "active"
  }'
```

#### PUT
Updates an existing client. The example includes all mandatory fields, other fields will contain `null` when updating the record:
```bash
curl -X "PUT" "http://localhost:8081/api/client" \
  -H "Content-Type: application/json; charset=utf-8" \
  -d $'{ \
    "id": "<UUID>", \
    "firstname": "Hedda", \
    "lastname": "Blunt", \
    "email": "hedda.blunt2020@gmail.com", \
    "status": "active"
  }'
```

#### PUT
Updates the status of an existing client to `ACTIVE` or `INACTIVE`.
```bash
curl -X "PUT" "http://localhost:8081/api/client/status/inactive/<UUID>"
```
There are multiple ways how to implement such simple methods without the overhead of the associated Resource object. Using a `POST` with the full client information (but using only the `status` here), a `PUT` with a `@RequestBody` that contains some JSON, setting the new status via a request parameter, or, as implemented here, just making it a part of the endpoint URL.

#### DELETE
Deletes a client. Enter an unknown ID to see an error message:
```bash
curl -X DELETE -H "Accept: application/json" "http://localhost:8081/api/client/<UUID>"
```

### Implementation

#### REST API

The [Client](src/main/java/com/sherzad/evcc/domain/Client.java) class can be considered a Domain class, according to Domain-driven design principles. There are different schools of thought about whether a Domain class may also act as an `@Entity`. For simplicity purposes this was done here, but in large-scale applications this seldomly works. At some point the Domain class and the Entity will always diverge and need to be separated from one another. Its [ID](src/main/java/com/sherzad/evcc/domain/Client.java#L36) field, which is also exported to the client, has therefore been implemented as a UUID in order to prevent leaking internals out. If using an auto-increment / sequence one could guess the user IDs and... shenanigans could ensue. Due to time constraints HATEOAS has not been implemented, but could easily be added with the help of Spring HATEOAS.

The [ClientResource](src/main/java/com/sherzad/evcc/web/ClientResource.java) acts as the DTO between the front- and the backend. In this case there's just one relevant attribute translation, for the [ClientStatusEnum](src/main/java/com/sherzad/evcc/domain/ClientStatusEnum.java), but it exemplifies that an object transferred to/from the caller may have different needs than the Domain `@Entity` that is persisted. It could contain translated messages, resolved keys, additional attributes for convenience purposes etc..

The [ClientController](src/main/java/com/sherzad/evcc/api/ClientController.java) demonstrates the simplest shape of a  REST-based API. All incoming and outgoing requests are JSON-based, including error messages returned or Exceptions thrown. The translation between the Domain object `Client` and the Resource object `ClientResource` happens there, utilizing the [ClientResourceAssembler](src/main/java/com/sherzad/evcc/web/ClientResourceAssembler.java).

#### Tests
The Spring beans use `@Autowired` instead of constructor-based injection, which Lombok could even help with. The rationale behind that is that it makes testing easier. Since Mockito and JUnit Jupiter have become much more powerful and help with `@MockBean`-ing Spring beans, `@Spy`-ing on them etc. I believe this concept doesn't always apply, so autowiring in tests should be fine nowadays.

### Status

**This is what is done:**
* Develop a RESTful API to handle client data
* We should be able to edit first name, last name, telephone number, email address and postal address fields (street, postal code, city and country)
* It should support all CRUD functionalities
* It should be able to validate input
* It should be backed by a persistence layer
* It should be able to activate and deactivate clients
* The "List Clients" endpoint should be able to be queried by last name, postal code, city and/or country
  * It should not return deactivated clients
* The "List Clients" endpoint should be paginated

**This is what is mostly done:**
* Tests (unit, integration, end-to-end)
  * Tests for the `ClientController` are missing, but it can be tested manually with the `curl` commands listed above. Other than that this class is a typical use case for `MockMvc` tests, which are simple but which I didn't get to do.

**This is missing, due to time constraints:**
* It should have authentication and access control to access the endpoints
  * I'd implement this using JSON Web Token. One endpoint to authenticate the user, then transfer the token for all subsequent REST calls via the Authorization-Field as a Bearer-Token.
  * Spring Security would implicitly handle authentication. Specific endpoints could be annotated appropriately (e.g. `@PreAuthorize`) to ensure only authorized ROLEs have access. 
* It should have the capability to add new users and set their permissions
  * While adding/editing users would come as regular endpoints the roles and permissions should be integrated into the Spring Security mechanisms. That way endpoints, service methods or even Thymeleaf sections could authorize those users against their ROLEs. The method bodies would have to handle only very little to no authorization-related stuff.
* It should be able to provide a history of changes made to each client profile
  * There are multiple way to implement this, namely JPA, Spring (operations on an `@Entity` would be intercepted using `@EntityListeners`) or Hibernate Envers. To my knowledge Hibernate Envers is still the only Auditing framework that helps with detecting `DELETE` operations. An audited `@Entity` would be annotated with `@Audited`, detected operations would be written to a database table created by us with Liquibase. Oh and one final alternative would be Oracle-based triggers, but this is a non-portable and database-dependent mechanism that also spreads the implementation across code and the database, which is not always desirable. 
* Implement additional middlewares and handlers that make an API more stable, secure, ready for a SPA frontend to directly connect to the API from a different domain
  * Spring Retry could help with ensuring calls, that do not require an immediate response, to get written to the database.
  * Spring Metrics could help with measuring traffic on the application, any method, endpoints or a whole business use case. Grafana would help visualizing that information, so we could act on it.
* Make considerations for how we might need to extend this in the future based on your experience with person-specific information
  * Implementing rights & roles and checking for authorization down to method-level is paramount.
  * Generally, all information sent to the frontend should be stripped down to what is absolutely necessary. Internals should be masked, e.g. as done with the Client ID (implemented as a randomly generated `UUID`). Identifiers (e.g. `enum` names) should be masked/aliased in order to hide any and all internals of the implementation.
  * If satisfying GDPR requirements is important auditing data will become an issue, unless it also gets expunged regularly or on request.
  * Endpoint request throttling and API keys should be implemented to understand exactly which clients access the API how much, set rate limits on them or block an API key if it is suspected of being misused.
* Implement a containerized environment
  * Dockerizing this application is simple with less than 10 lines of code, if not using a prepared Docker base image.
  * Since the application is stateless it can easily be deployed into an OpenShift/Kubernetes cluster. Traffic routing would be configured there.
