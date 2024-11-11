<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
</head>
<body>
<%
    // Check if the user is already logged in
// retrieve the current session without creating a new one. If there is no existing session, null is returned.  
	HttpSession sessionObj = request.getSession(false);
	out.println(sessionObj.getAttribute("is_loggedin"));
	// Checks if the session exists and the "is_loggedin" attribute is not null.
    if (sessionObj != null && sessionObj.getAttribute("is_loggedin") != null) {
    	
        // User is already logged in, redirect to the dashboard
        response.sendRedirect("dashboard.jsp");
        return; // Stop further execution of the JSP
    }

    // Display error message from session or cookies
    //Retrieves the "error" parameter from the request, if it exists.
    String error = request.getParameter("error");
    if (error != null) {//Checks if the "error" parameter is not null
        out.println("<p style='color:red;'>" + error + "</p>");
    }
//Retrieves an array of cookies from the request.
    Cookie[] cookies = request.getCookies();
    String loginError = null;// Initializes the loginError variable to null

    if (cookies != null) {// Checks if the cookies array is not null
        for (Cookie cookie : cookies) {
            if ("error".equals(cookie.getName())) {
                loginError = cookie.getValue();
                break;
            }
        }

        if (loginError != null) {
            out.println("<div class='center error-message'><p>Invalid credential</p></div>");
        }
    }
%>
<div align="center">
    <h1>User Login</h1>
</div>
<form action="SubmitServelet" method="post">
    <div align="center">
        <table>
            <tr>
                <td>Enter name:</td>
                <td><input type="text" name="user_name" required></td>
            </tr>
            <tr>
                <td>Enter password:</td>
                <td><input type="password" name="password" required></td>
            </tr>
        </table>
        <div>
            <input type="submit" value="Login">
            <input type="reset" value="Reset">
            <br>
            <a href="registration.jsp">Register</a> | <a href="password.jsp">Forgot Password</a>
        </div>
    </div>
</form>
</body>
</html>
