<%-- 
    Document   : MakePayment
    Created on : Mar 25, 2015, 11:19:28 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../js/libs/twitter-bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../js/libs/twitter-bootstrap/css/index.css" rel="stylesheet" media="screen">
        <title>FBS | Payment Details</title>
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
        
      <form class="form-signin" action="confirmedPayment" method="POST">
        <h2 class="form-signin-heading text-center">Payment Details</h2>
        <label for="inputCardHolderName" class="sr-only">Card Holder Name</label>
        <input type="text" name="cardHolderName" id="inputCardHolderName" class="form-control" placeholder="Card Holder Name" required autofocus>
        <label for="inputCardType" class="sr-only">Card Type</label>
        <input type="text" name="cardType" id="inputCardType" class="form-control" placeholder="Card Type" required>
        <label for="inputCardNumber" class="sr-only">Card Number</label>
        <input type="text" name="cardNumber" id="inputCardNumber" class="form-control" placeholder="Card Number" required>
        <input type="hidden" name="bookingNum" value="<%= request.getParameter("bookingNum") %>" />
        <button class="btn btn-lg btn-primary btn-block" type="submit">Confirm Payment</button>
      </form>
      
    </body>
</html>
