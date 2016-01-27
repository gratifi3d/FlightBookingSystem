<%-- 
    Document   : Register
    Created on : Mar 11, 2015, 8:44:41 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Register</title>
        <link href="../js/libs/twitter-bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../js/libs/twitter-bootstrap/css/index.css" rel="stylesheet" media="screen">
    </head>
    <body>
        
      <form class="form-signin" action="registerStatus" method="POST">
        <h2 class="form-signin-heading text-center">Register</h2>
        <label for="inputUserName" class="sr-only">Username</label>
        <input type="text" name="userName" id="inputUserName" class="form-control" placeholder="Username" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="text" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <label for="inputConfirmPassword" class="sr-only">Confirm Password</label>
        <input type="text" name="confirmPassword" id="inputConfirmPassword" class="form-control" placeholder="Confirm Password" required>
        <label for="inputContactNumber" class="sr-only">Contact Number</label>
        <input type="text" name="contactNumber" id="inputContactNumber" class="form-control" placeholder="Contact No." required>
        <label for="inputEmail" class="sr-only">Email</label>
        <input type="email" name="email" id="inputEmail" class="form-control" placeholder="Email" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Confirm</button>
        <p class="text-center"><a href="index">Already a user? Log in</a></p>
      </form>
          
    </body>
</html>
