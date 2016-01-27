<%-- 
    Document   : UpdatePassword
    Created on : Mar 12, 2015, 1:20:27 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Change Password</title>
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
        
      <form class="form-signin" action="changePassword" method="POST">
        <h2 class="form-signin-heading text-center">Update Password</h2>
        <label for="inputOldPassword" class="sr-only">Old Password</label>
        <input type="password" name="oldPassword" id="inputOldPassword" class="form-control" placeholder="Old Password" required autofocus>
        <label for="inputNewPassword" class="sr-only">New Password</label>
        <input type="password" name="newPassword" id="inputNewPassword" class="form-control" placeholder="New Password" required>
        <label for="inputConfirmNewPassword" class="sr-only">Confirm New Password</label>
        <input type="password" name="confirmNewPassword" id="inputConfirmNewPassword" class="form-control" placeholder="Confirm New Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Confirm</button>
      </form>
        
    </body>
</html>
