<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>DashBoard</title>
</head>
<body>
<%
    // Check if the user is already logged in
    
    //retrieves the current session without creating a new one. null is returned if no session exists.
    HttpSession sessionObj = request.getSession(false);
	out.println(sessionObj.getAttribute("is_loggedin"));
    if (sessionObj != null && sessionObj.getAttribute("is_loggedin") == null) {
    	
        // User is already logged in, redirect to the dashboard
        response.sendRedirect("login.jsp");
        return; // Stop further execution of the JSP
    }
    
    // retrieves all cookies from the request.
    Cookie[] cookies = request.getCookies();
    
    // initializes a string variable name to null.
    String name = null;

    //checks if there are any cookies.
    if (cookies != null) {
  //The for loop iterates through all cookies to find the one with the name "user_name".
  //If the "user_name" cookie is found, its value is assigned to the name variable,
  //and the loop breaks.
 
  for (Cookie cookie : cookies) {
            if ("user_name".equals(cookie.getName())) {
                name = cookie.getValue();
                break;
            }
        }
    }
    %>
<div align="center">
<p style="font-size:30px">Welcome <% out.println(name); %></p>
<br>
<a href="postJob.jsp">Post a Job</a>
<a href="LogoutServlet">Logout</a> 
<a href="JobList_Servlet">list</a>
<a href="wishlist.jsp">Wish list</a>
<br>
<a href="FindJobServlet">Find a job</a>

</div>
</body>
</html>