<%-- 
    Document   : ViewRequests
    Created on : Mar 11, 2015, 9:07:22 PM
    Author     : User
--%>

<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | View Requests</title>
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
        <h2 style="text-align: center; margin-top: 40px;">All Requests</h2>
        <%
            if(request.getAttribute("requests") != null) {
            ArrayList<ArrayList<String>> allRequests = (ArrayList<ArrayList<String>>)request.getAttribute("requests");
            int counter = 1;
            for(Object o: allRequests) {
            ArrayList<String> im = (ArrayList<String>)o;
            
            %> 
            
            <h3 style="text-align: center">* Request # <%=counter%> *</h3>
            <div class="form-signin">
            <p>Content: <%= im.get(0) %></p>
            <p>Status: <%= im.get(1) %></p>
            <p>Comments: <%= im.get(2) %></p>
            </br>
            </div>
            <%
            counter++;
            }
            }
            %>        
            
    </body>
</html>
