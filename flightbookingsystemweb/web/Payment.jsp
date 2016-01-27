<%-- 
    Document   : Payment
    Created on : Mar 11, 2015, 9:06:18 PM
    Author     : User
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Payment</title>
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
        
        <form class="form-signin" action="makePayment" method="POST">
            <h2 style="text-align: center">Make Payment</h2>
        
            <%
            ArrayList<ArrayList<String>> userBookings = (ArrayList<ArrayList<String>>)request.getAttribute("userBookings");
            int bookingNum = 0;
            for(Object o: userBookings) {
                ArrayList<String> im = (ArrayList<String>)o;
                if(im.get(5).equals("unpaid")) {
                %>
        <div>
            <h3 style="text-align: center">* Booking # <%=bookingNum+1%> *</h3>
            <input type="radio" name="bookingNum" value="<%= im.get(0).toString() %>">
            <p>Booking No.: <%= im.get(0) %></p>
            <p>Booking Time: <%= im.get(1) %></p>
            <p>Route: <%= im.get(2) %></p>
            <p>No. Passengers: <%= im.get(3) %></p>
            <p>Total Amount: <%= im.get(4) %></p>
            <br/>
        </div>
        <%
        bookingNum++;
            }
        }
        %>
        
        <%
            if(bookingNum>0) {
        %>
        <button class="btn btn-primary btn-block" type="submit">Make Payment</button>
<%
                }
%>
        </form>
        
    </body>
</html>
