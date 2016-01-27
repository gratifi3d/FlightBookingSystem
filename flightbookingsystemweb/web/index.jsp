<%-- 
    Document   : index
    Created on : Mar 11, 2015, 6:11:27 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Home</title>
        <link href="../js/libs/twitter-bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../js/libs/twitter-bootstrap/css/index.css" rel="stylesheet" media="screen">
    </head>
    <body>
        
    <div class="container">
        
      <form class="form-signin" action="loginUser" method="POST">
        <h2 class="form-signin-heading text-center">Flight Booking System</h2>
        <label for="inputUserName" class="sr-only">Email address</label>
        <input type="text" name="userName" id="inputUserName" class="form-control" placeholder="Username" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
        <p class="text-center"><a href="register">Not a user? Register</a></p>
      </form>
        
    </div> <!-- /container -->
    

    </body>
</html>
