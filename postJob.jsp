<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*"%>
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
%>
<%
Map<String, Object> job = null;
// Retrieve job list from the request attributes
List<Map<String, Object>> jobList = (List<Map<String, Object>>) request.getAttribute("jobList");
if (jobList != null && !jobList.isEmpty()) {
    job = jobList.get(0); // Assuming there's only one job in the list
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

</head>
<body>
	<div align="center">
		
	</div>
	<form action="PostJobServlet" method="post">
		<input type="hidden" name="job_id" value="<%= (job != null) ? request.getParameter("job_id") : ""%>">
		<div align="center">
			<table>
				<tr>
					<td>Job Title:</td>
                <td><input type="text" name="job_title" value="<%= (job != null) ? job.get("job_title") : "" %>"></td>
				</tr>
				<tr>
					<td>Description:</td>
                <td><textarea name="description"><%= (job != null) ? job.get("description") : "" %></textarea></td>
				</tr>
				<tr>
					<td>Duration:</td>
                <td><input type="text" name="duration" value="<%= (job != null) ? job.get("duration") : "" %>"></td>
				</tr>
				<tr>
					<td>Timeline:</td>
                <td><input type="date" name="timeline" value="<%= (job != null) ? job.get("timeline") : "" %>"></td>
				</tr>
				<tr>
					<td>Job Ends Before:</td>
                <td><input type="date" name="job_ends_before" value="<%= (job != null) ? job.get("job_ends_before") : "" %>"></td>
				</tr>
				<tr>
					<td>Max Cost:</td>
                <td><input type="text" name="max_cost" value="<%= (job != null) ? job.get("max_cost") : "" %>"></td>
				</tr>
				<tr>
					<td>Min Cost:</td>
                <td><input type="text" name="min_cost" value="<%= (job != null) ? job.get("min_cost") : "" %>"></td>
				</tr>
				<tr>
					<td>Bid Ends On:</td>
                <td><input type="date" name="bid_end_on" value="<%= (job != null) ? job.get("bid_end_on") : "" %>"></td>
				</tr>
				<tr>
					<td>final bid amount:</td>
                <td><input type="text" name="final_bid_amount" value="<%= (job != null) ? job.get("final_bid_amount") : "" %>"></td>
				</tr>
				<tr>
					<td>Location:</td>
                <td><input type="text" name="location" value="<%= (job != null) ? job.get("location") : "" %>"></td>
				</tr>
			</table>
			<div>
				<input type="submit" value="Submit" href="postJob.jsp">
			</div>
		</div>
	</form>
	
	 <%
        String message = (String) request.getAttribute("message");
        String messageType = (String) request.getAttribute("messageType");
        if (message != null) {
    %>
        <div class="<%= "success".equals(messageType) ? "message-success" : "message-error" %>">
            <%= message %>
        </div>
    <%
        }
    %>
	<a href="LogoutServlet">Logout</a> 
	<a href="JobList_Servlet">list</a>
	<a href="dashboard.jsp">dashboard</a>
	<br>
	<a href="FindJobServlet">Find a job</a>
</body>
</html>
