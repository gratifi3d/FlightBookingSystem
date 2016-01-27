<%-- 
    Document   : SearchFlights
    Created on : Mar 11, 2015, 9:04:38 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Search Flights</title>
        <link href="../js/libs/twitter-bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../js/libs/twitter-bootstrap/css/index.css" rel="stylesheet" media="screen">
    </head>
    <body>
        <nav class="nav">
            <button type="button" class="btn btn-default" onClick="location.href='updatePassword'">Update Password</button>
            <button type="button" class="btn btn-default" onClick="location.href='updateProfile'">Update Profile</button>
            <button type="button" class="btn btn-default" onClick="location.href='searchFlights'">Search Flights</button>
            <button type="button" class="btn btn-default" onClick="location.href='payment'">Payment</button>
            <button type="button" class="btn btn-default" onClick="location.href='viewBookings'">View Bookings</button>
            <button type="button" class="btn btn-default" onClick="location.href='viewRequests'">View Requests</button>
            <button type="button" class="btn btn-default" onClick="location.href='logout'">Logout</button>
        </nav>
        
      <form class="form-signin" action="requestSearchFlights" method="POST">
        <h2 class="form-signin-heading text-center">Search Flights</h2>
        <label for="inputDepartureDate" class="sr-only">Departure Date</label>
        <input type="text" name="departureDate" id="inputDepartureDate" class="form-control" placeholder="Departure Date (DD/MM/YYYY)" required autofocus>
        <label for="inputDepartureCity" class="sr-only">Departure City</label>
        <input type="text" name="departureCity" id="inputDepartureCity" class="form-control" placeholder="Departure City" required>
        <label for="inputArrivalCity" class="sr-only">Arrival City</label>
        <input type="text" name="arrivalCity" id="inputArrivalCity" class="form-control" placeholder="Arrival City" required>
        <label for="inputNumberPassengers" class="sr-only">No. Passengers</label>
        <input type="text" name="numberPassengers" id="inputNumberPassengers" class="form-control" placeholder="No. Passengers" required>
        
        <button class="btn btn-lg btn-primary btn-block" type="submit">Search</button>
      </form>
    </body>
</html>
