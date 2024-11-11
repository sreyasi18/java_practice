<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Registration Page</title>
</head>
<body>
<%
    // Check if the user is already logged in
    HttpSession sessionObj = request.getSession(false);
	out.println(sessionObj.getAttribute("is_loggedin"));
    if (sessionObj != null && sessionObj.getAttribute("is_loggedin") != null) {
    	
        // User is already logged in, redirect to the dashboard
        response.sendRedirect("dashboard.jsp");
        return; // Stop further execution of the JSP
    }
    %>

	<form action=RegistrationServlet method = post>
		Enter name:<input type = "text" name="name"/><br>
		<br>
		Enter User Type:<input type = "text" name="user_type"/><br>
		<br>
		Enter User Name:<input type ="text"name="user_name"/><br>
		<br>
	  	Enter Email :<input type = "text" name="email"/><br>
	  	<br>
	  	Enter Password:<input type = "text" name="password"/><br>
	  	<br>
	  	Enter Confirm Password:<input type = "text" name="password"/><br>
	  	<br>
	  	enter is_active:<input type="number" name="is_active"/><br>
	    <input type="submit" value="Submit" <a href="login.jsp">login</a> >
	    
	   
	 </form>
</body>
</html>