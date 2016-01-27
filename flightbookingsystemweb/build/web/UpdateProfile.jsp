<%-- 
    Document   : UpdateProfile
    Created on : Mar 11, 2015, 9:03:30 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Update Profile</title>
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
        
      <form class="form-signin" action="changeProfile" method="POST">
        <h2 class="form-signin-heading text-center">Update Profile</h2>
        <label for="inputNewEmail" class="sr-only">New Email </label>
        <input type="email" name="newEmail" id="inputNewEmail" class="form-control" placeholder="New Email" required autofocus>
        <label for="inputNewContact" class="sr-only">New Contact</label>
        <input type="text" name="newContactNumber" id="inputNewContact" class="form-control" placeholder="New Contact" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Confirm</button>
      </form>
        
    </body>
</html>
