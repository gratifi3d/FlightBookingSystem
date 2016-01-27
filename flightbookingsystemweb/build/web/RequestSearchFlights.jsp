<%-- 
    Document   : RequestSearchFlights
    Created on : Mar 19, 2015, 4:03:24 PM
    Author     : User
--%>

<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../js/libs/twitter-bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../js/libs/twitter-bootstrap/css/index.css" rel="stylesheet" media="screen">        
        <title>FBS | Search Flights</title>
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
                        
        <form class="form-signin" action="passengerInfo" method="POST">
            <h3 style="text-align: center">Direct Flights</h3>
        
            <%
            ArrayList<Vector> flightList = (ArrayList<Vector>)request.getAttribute("flights");
            for(Object o: flightList) {
                Vector im = (Vector)o;
                // Just get the schedule Ids here
                String id = im.get(6).toString();
                %>
        <div>
            <input type="radio" name="flightData" value="<%= id %>">
            <p>Flight No.: <%= im.get(0) %></p>
            <p>Departure: <%= im.get(1) %></p>
            <p>Arrival: <%= im.get(2) %></p>
            <p>Price: <%= im.get(3) %></p>
            <br/>
        </div>
        <%
            }
        %>
        
        <h3 style="text-align: center">One Stop Flights</h3>
        <%
            ArrayList<Vector> oneStopList = (ArrayList<Vector>)request.getAttribute("oneStops");
            for(Object o: oneStopList) {
                Vector im = (Vector)o;
                int totalPrice = 0;
                // Just get the schedule Ids here
                String ids = "";
                for (int i=0; i<im.size(); i++) {
                    Vector a = (Vector)im.get(i);
                    ids = ids + a.get(7).toString();
                    ids += " ";
                }
                %>
            <input type="radio" name="flightData" value="<%= ids %>">
                <%
                
                for(int i=0; i<im.size(); i++) {
                    Vector k = (Vector)im.get(i);
        %>
        <div>
            <p>Flight No.: <%= k.get(0) %></p>
            <p>Departure: <%= k.get(8) %></p>
            <p>Arrival: <%= k.get(9) %></p>
            </br>
        </div>
        <%
        totalPrice += Integer.parseInt(k.get(5).toString());
                }
              %>  
            <p>Price: SGD <%= totalPrice %> / passenger</p>
            </br>
              <%
            }
        %>
        
        <input type="hidden" name="numberPassengers" value="<%= request.getParameter("numberPassengers") %>" />
        <button class="btn btn-primary btn-block" type="submit">Book Flight</button>
        </form>
    </body>
</html>
