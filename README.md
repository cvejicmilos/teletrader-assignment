
# teletrader-assignment

A RESTful API built with Spring Boot for order processing and storing.

The API allows users to create, manage, and track orders.

## Setup and Installation
1. Clone the repository
``clone https://github.com/cvejicmilos/teletrader-assignment``
2. Navigate into the project directory
``cd teletrader-assignment``
``cd teletrader``
3. Start the application using Docker Compose
``docker compose up``

## Endpoints
### Don't require authorization
#### Register
``
curl --location 'http://localhost:8080/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "firstname": "firstname",
    "lastname": "lastname",
    "username": "username",
    "password": "password"
}'
``

``
curl --location 'http://localhost:8080/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "firstname": "admin",
    "lastname": "admin",
    "username": "admin",
    "password": "adminpassword"
}'
``
#### Login
``
curl --location 'http://localhost:8080/auth/login' \
--data '{
    "username": "username",
    "password": "password"
}'
``
#### Get All Active Orders
``
curl --location 'http://localhost:8080/orders'
``
#### Get Last 10 Active BUY/SELL orders
``
curl --location 'http://localhost:8080/orders/latest?type=BUY'
``

``
curl --location 'http://localhost:8080/orders/latest?type=SELL'
``
### Require authorization

**These endpoints require the user to be authenticated and provide a valid Bearer Token in the `Authorization` header.**

#### Get "My" Orders
``
curl --location 'http://localhost:8080/orders/my' \
--header 'Authorization: Bearer <Your Bearer Token>'
``
#### Create A New Order
``
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <Your Bearer Token>' \
--data '{
  "stockPrice": 123,
  "stockAmount": 456,
  "stockSymbol": "MSFT",
  "type": "BUY"
}'
``
#### Cancel Order You Created
``
curl --location --request PATCH 'http://localhost:8080/orders/cancel/1' \
--header 'Authorization: Bearer <Your Bearer Token>'
``
#### Get Top 10 Active BUY/SELL Orders For A Stock
``
curl --location 'http://localhost:8080/orders/top?type=SELL&stockSymbol=AAPL' \
--header 'Authorization: Bearer <Your Bearer Token>'
``

``
curl --location 'http://localhost:8080/orders/top?type=BUY&stockSymbol=MSFT' \
--header 'Authorization: Bearer <Your Bearer Token>'
``
#### Get All Orders -> Authenticated User Must Have ADMIN Role!
``
curl --location 'http://localhost:8080/orders/all' \
--header 'Authorization: Bearer <Your Bearer Token>'
``
#### Accept Other Users Active Order
``
curl --location --request PATCH 'http://localhost:8080/orders/accept/1' \
--header 'Authorization: Bearer <Your Bearer Token>'
``


## Suggestions for New Features

Here are some potential features to improve the functionality of the application:

-   **Order Filtering**: Add support for filtering orders by price, date, or status.
    
-   **Email Service**: Integrate email notifications for actions like order confirmations or cancellations.
    
-   **Push Notifications**: Implement push notifications for order status updates (e.g., using WebSockets or third-party services).
    
-   **Rate Limiting**: Implement rate limiting for API requests to prevent abuse.
    
-   **Change Password**: Add functionality to allow users to change their password.
    
-   **Delete Account**: Allow users to delete their account.
    
-   **Testing Code**: Write unit and integration tests for the API to improve reliability and ensure that features are working as expected.
