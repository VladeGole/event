🎟️ Event – an event management service

Event is the backend of an application for organising events: conferences, concerts, workshops and similar gatherings. It lets organisers create and publish events, lets visitors sign up for them, and groups events by country.

This part of the application has no screen of its own. It is the "engine" that works behind the scenes, and a website or mobile app (the frontend) talks to it by sending and receiving data in JSON format.

Author: Vlade Gole

📖 A short glossary (for non-programmers)
Term	What it means
API	The application's "service counter". Other programs send it requests (e.g. "give me all events") and get answers back.
Endpoint	One address on that counter for one action, e.g. /api/v1/events for events.
JSON	The format in which data is exchanged. It is easy to read, as a list of "field: value" pairs.
Token	A digital pass the user receives after signing in. It is valid for 1 hour and is sent with every request that requires sign-in.
Role	What a user is allowed to do in the system (administrator, organiser, participant).
🎯 What is it for?

Imagine a website for buying tickets to events. This service does the following:

An organiser signs in and publishes an event: name, description, price, dates and the country where it takes place.
A visitor browses events (no sign-in needed), filters them by country, price or name, then signs in and joins an event as a participant.
An administrator maintains the list of countries.
The system keeps track of who may do what: someone else's event cannot be edited or deleted, and data is checked before it is saved.
👥 Roles and permissions
Role	Who they are	What they can do
Signed-out visitor	Anyone	View only: events, countries and participants. Can register and sign in.
ADMIN	Event organiser	Creates events, edits and deletes only their own events
PARTICIPANT	Attendee	Signs up for an event, edits and deletes their participant profile
ADMINISTRATOR	System manager	Adds, edits and deletes countries
⚙️ How the system works
  Website / app  --->  Controller  --->  Service  --->  Repository  --->  MySQL database
  (sends a JSON        (receives the     (business      (reads and        (stores data
   request)             request)          rules)         writes data)      permanently)
Part of the code	Its role in plain words
Controller (controller/)	The "reception desk": receives a request and returns a response
Service (service/)	The "manager": checks the rules (who may do what, whether something already exists)
Repository (repository/)	The "archivist": reads from and writes to the database
Model (model/)	A description of the stored data (event, participant, country, user)
DTO (dto/)	The "form" a user fills in or receives back
Mapper (mapper/)	The "translator" between the database and the form
Security / JWT (core/config/)	The "security guard": checks the pass (token) and the role
Exception handler (core/handler/)	The "error desk": returns understandable messages
🧩 Main functions of the code
Sign-up and sign-in (AuthService)
Function	What it does
register	Creates a new account, stores the password in a protected (hashed) form and immediately returns a token
authenticate	Checks the e-mail and password and returns a token if they are correct
Events (EventServiceImpl)
Function	What it does
create	Validates the data, allows only the ADMIN role, does not allow two events with the same name, and adds the country automatically if it does not exist yet
update	Edits an event, but only its owner can
deleteEvent	Deletes an event, but only its owner can
getEventById / findAllEvent	Returns one event or all events
findEventByName	Finds an event by name (letter case does not matter)
findEventByCountry	All events in the chosen country
findEventByParticipantId	All events a given participant has signed up for
findEventByPrivate	Only private or only public events
findEventByPriceFilter	Events by price: equal, greater, less, greater or equal, less or equal
Participants (ParticipantServiceImpl)
Function	What it does
create	Signs the logged-in user (role PARTICIPANT) up for an event, based on the event name
update	Edits participant data, but only the profile owner can; the e-mail must be unique
deleteById	Deletes a participant and removes them from all events
getById	Returns participant data
Countries (CountryServiceImpl)
Function	What it does
createCountry	Adds a country (ADMINISTRATOR only, the name must be unique)
updateCountry	Changes a country's name
deleteById	Deletes a country
findById / findAllCountry	Returns one country or all of them
Helper functions
Function	What it does
JwtService.generateToken	Creates a token that is valid for 1 hour
JwtAuthentacationFilter	On every request, checks that the token is valid and who sent it
ValidationObject.validate	Checks that data is filled in correctly before it is saved
GlobalExceptionHandler	Turns errors into clear JSON messages
📏 Data rules
Name, description and currency must not be empty.
Price must be a whole number of at least 50.
Dates are written as YYYY-MM-DD (e.g. 2026-11-20).
Two events cannot have the same name (upper and lower case are treated as the same).
Two countries cannot have the same name.
🔌 Endpoint overview

Base address: http://localhost:8080 (the default port; adjust as needed).
🔒 = requires sign-in and the appropriate role. The token is sent in the header: Authorization: Bearer <token>.

Action	Method and address	Who can
Register	POST /api/v1/auth/register	everyone
Sign in	POST /api/v1/auth/login	everyone
New event	POST /api/v1/events/ 🔒	ADMIN
Edit event	PATCH /api/v1/events/{id} 🔒	ADMIN (owner)
Delete event	DELETE /api/v1/events/{id} 🔒	ADMIN (owner)
One event	GET /api/v1/events/{id}	everyone
All events	GET /api/v1/events/	everyone
By country	GET /api/v1/events/by-country/{country}	everyone
By participant	GET /api/v1/events/by-participant/{participantId}	everyone
By name	GET /api/v1/events/by-name/{name}	everyone
Public / private	GET /api/v1/events/by-private/{true|false}	everyone
By price	GET /api/v1/events/by-price?price=100&operator=gte	everyone
Join an event	POST /api/v1/participants/{eventName} 🔒	PARTICIPANT
Edit participant	PATCH /api/v1/participants/{id} 🔒	PARTICIPANT
Delete participant	DELETE /api/v1/participants/{id} 🔒	PARTICIPANT
One participant	GET /api/v1/participants/{id}	everyone
New country	POST /api/v1/countries/ 🔒	ADMINISTRATOR
Edit country	PATCH /api/v1/countries/{id} 🔒	ADMINISTRATOR
Delete country	DELETE /api/v1/countries/{id} 🔒	ADMINISTRATOR
One country	GET /api/v1/countries/{id}	everyone
All countries	GET /api/v1/countries/	everyone

Most responses have the same shape: a short message (message) and the data itself (obj).

📨 JSON examples per endpoint
1. Register – POST /api/v1/auth/register

Request:

json
{
  "firstName": "Ana",
  "lastName": "Jovanović",
  "email": "ana@example.com",
  "password": "Lozinka123",
  "role": "ADMIN"
}

Possible values for role: ADMIN, PARTICIPANT, ADMINISTRATOR.

Response (200 OK):

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
2. Sign in – POST /api/v1/auth/login

Request:

json
{
  "email": "ana@example.com",
  "password": "Lozinka123"
}

Response (200 OK):

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
3. Create an event – POST /api/v1/events/ 🔒 (ADMIN)

Request:

json
{
  "name": "Tech Summit Beograd 2026",
  "description": "A two-day software development conference.",
  "price": 120,
  "currency": "EUR",
  "startDate": "2026-11-20",
  "endDate": "2026-11-21",
  "country": "Srbija",
  "eventPrivate": false
}

Response (201 Created):

json
{
  "message": "Event is create",
  "obj": {
    "id": 1,
    "name": "Tech Summit Beograd 2026",
    "price": 120,
    "currency": "EUR",
    "startDate": "2026-11-20",
    "endDate": "2026-11-21",
    "country": "Srbija",
    "owner": "Ana Jovanović"
  }
}
4. Edit an event – PATCH /api/v1/events/1 🔒 (ADMIN, owner)

All fields are sent (name, description, price, currency, dates and whether the event is private). The country is not changed here.

Request:

json
{
  "name": "Tech Summit Beograd 2026",
  "description": "A two-day conference on software development and artificial intelligence.",
  "price": 150,
  "currency": "EUR",
  "startDate": "2026-11-20",
  "endDate": "2026-11-22",
  "eventPrivate": false
}

Response (200 OK):

json
{
  "message": "Event is update",
  "obj": {
    "id": 1,
    "name": "Tech Summit Beograd 2026",
    "price": 150,
    "currency": "EUR",
    "startDate": "2026-11-20",
    "endDate": "2026-11-22",
    "country": "Srbija",
    "owner": "Ana Jovanović"
  }
}
5. Delete an event – DELETE /api/v1/events/1 🔒 (ADMIN, owner)

No request body. Response: 204 No Content (no content – it means it worked).

6. Viewing events – GET (no sign-in)

GET /api/v1/events/ – all events (200 OK):

json
{
  "message": "Events are found",
  "obj": [
    {
      "id": 1,
      "name": "Tech Summit Beograd 2026",
      "price": 150,
      "currency": "EUR",
      "startDate": "2026-11-20",
      "endDate": "2026-11-22",
      "country": "Srbija",
      "owner": "Ana Jovanović"
    },
    {
      "id": 2,
      "name": "Jazz Evenings",
      "price": 60,
      "currency": "EUR",
      "startDate": "2026-12-05",
      "endDate": "2026-12-05",
      "country": "Srbija",
      "owner": "Ana Jovanović"
    }
  ]
}

GET /api/v1/events/1 – one event. The message is "Event is found" and obj is a single event (same shape as above).

GET /api/v1/events/by-name/Jazz Evenings – one event by name (message: "Events are found").

The other filters return a list in the same shape as "all events":

Example address	What it returns
/api/v1/events/by-country/Srbija	Events in Serbia
/api/v1/events/by-participant/3	Events the participant with ID 3 has signed up for
/api/v1/events/by-private/true	Private events only
/api/v1/events/by-price?price=100&operator=gte	Events priced at 100 or more

Values for operator: eq (equal, the default), gt (greater), lt (less), gte (greater or equal), lte (less or equal). Any other value returns a 400 error.

7. Join an event – POST /api/v1/participants/Tech Summit Beograd 2026 🔒 (PARTICIPANT)

No request body. The event name is in the address, and the participant's name and e-mail are taken by the system from the signed-in user's account.

Response (200 OK):

json
{
  "message": "Participant is create",
  "obj": {
    "id": 1,
    "email": "marko@example.com"
  }
}
8. Edit a participant – PATCH /api/v1/participants/1 🔒 (PARTICIPANT, profile owner)

Request:

json
{
  "firstName": "Marko",
  "lastName": "Petrović",
  "email": "marko.petrovic@example.com"
}

Response (200 OK):

json
{
  "message": "Participant update",
  "obj": {
    "id": 1,
    "email": "marko.petrovic@example.com"
  }
}
9. Viewing and deleting a participant
GET /api/v1/participants/1 – returns {"message": "Participant is found", "obj": {"id": 1, "email": "..."}}.
DELETE /api/v1/participants/1 🔒 – responds with 204 No Content. The participant is also removed from every event they had signed up for.
10. Countries

Add – POST /api/v1/countries/ 🔒 (ADMINISTRATOR)

Request:

json
{
  "name": "Srbija"
}

Response (201 Created):

json
{
  "message": "Country is created",
  "obj": {
    "id": 1,
    "name": "Srbija"
  }
}

Edit – PATCH /api/v1/countries/1 🔒

Request:

json
{
  "name": "Republika Srbija"
}

Response (200 OK):

json
{
  "message": "Country is updated",
  "obj": {
    "id": 1,
    "name": "Republika Srbija"
  }
}

View – GET /api/v1/countries/ and GET /api/v1/countries/1

The message is "Country is found". For one country obj is a single object {"id": 1, "name": "Srbija"}, and for all countries it is a list of such objects. These two endpoints return HTTP status 302 (Found), which is unusual, since 200 is normally used for a successful read.

Delete – DELETE /api/v1/countries/1 🔒 – responds with 204 No Content.

⚠️ According to the data model, deleting a country also deletes all events in that country.

❌ Error examples

Errors have a clear message and an HTTP status. The response also contains a technical throwable field with details for developers, omitted here for readability.

Invalid data (400 Bad Request) – e.g. a price below 50:

json
{
  "message": "Price must be at least 50",
  "httpStatus": "BAD_REQUEST"
}

If several fields are wrong, the messages are joined with the | character.

Already exists (409 Conflict) – e.g. an event or country with the same name:

json
{
  "message": "Object already exist",
  "httpStatus": "CONFLICT"
}

Does not exist (404 Not Found):

json
{
  "message": "Event not exist",
  "httpStatus": "NOT_FOUND"
}

Similar messages: "Country not exist" and "Participant not found".

Invalid parameter (400) – e.g. operator=abc in the price filter:

json
{
  "message": "Invalid argument provided",
  "httpStatus": "BAD_REQUEST"
}
🧰 Technologies used
Part	Technology
Language and framework	Java 17, Spring Boot 3.4.4
Database	MySQL (Spring Data JPA / Hibernate)
Security	Spring Security, JWT tokens (jjwt), BCrypt password hashing
Object mapping	MapStruct, Lombok
Data validation	Jakarta Bean Validation
API documentation	Swagger UI (springdoc-openapi)
Tests	JUnit and Spring Boot Test
🗄️ Database

Tables are created automatically on first start-up.

Table	What it contains
user	Users: first name, last name, e-mail, hashed password, role
country	Countries
event	Events: name, description, price, currency, dates, country, owner, privacy
participant	Participants (linked to a user)
event_participant	The link "which participant attends which event"
🚀 Running the project

Requirements: Java 17, MySQL. Maven is not needed because the project includes mvnw.

Create an empty MySQL database, e.g. event_db1.
In src/main/resources/application.properties, enter the database address, username and password, plus the secret key used to sign tokens (secret). You can generate a key with openssl rand -base64 32.
Start the application:
bash
   ./mvnw spring-boot:run
The service is available at http://localhost:8080. The interactive documentation, where every endpoint can be tried out in the browser, is at http://localhost:8080/swagger-ui/index.html.

Tests:

bash
./mvnw test
Quick example from the command line
bash
# 1) Register an organiser
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ana","lastName":"Jovanović","email":"ana@example.com","password":"Lozinka123","role":"ADMIN"}'

# 2) Create an event (replace <TOKEN> with the token from the response)
curl -X POST http://localhost:8080/api/v1/events/ \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Tech Summit","description":"Conference","price":120,"currency":"EUR","startDate":"2026-11-20","endDate":"2026-11-21","country":"Srbija","eventPrivate":false}'

# 3) View all events (no sign-in)
curl http://localhost:8080/api/v1/events/
⚠️ Known limitations and future work
🔐 Role security: when registering, users choose their own role, so in production roles should be assigned in a controlled way (e.g. only PARTICIPANT for public registration).
🔑 Secrets: the database password and the token secret should be kept in environment variables, not in a file published on GitHub.
🚦 Access error statuses: when the service rejects a user because of role or ownership, a dedicated error handler returning 401/403 with a clear message is recommended.
💶 Price: the "at least 50" rule makes free events impossible.
🧪 Tests: unit tests exist for mappers and services, and integration tests could be added.
📄 Next: pagination of the event list, search by date and e-mail confirmation of sign-ups.
