<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, java.util.Map" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Job List</title>
</head>
<body>
<div align="center">
 <a href="LogoutServlet">Logout</a> 
<a href="postJob.jsp">Post a Job</a>
<a href="dashboard.jsp">dashboard</a>
<a href="wishlist.jsp">Wish list</a>
<br>
<a href="FindJobServlet">Find a job</a>

</div>
         

<%
    // Check if the user is already logged in
    
    // retrieves the current session without creating a new one. null is returned if no session exists.
    HttpSession sessionObj = request.getSession(false);

//The if statement checks if the session does not exist, if the "is_loggedin" attribute is not present,
//or if the "is_loggedin" attribute does not equal 1 (indicating the user is not logged in).
//If any condition is true:
	
    if (sessionObj == null) {
        out.println("Session is null. Redirecting to login page.");
        response.sendRedirect("login.jsp");
        return;
    } else if (sessionObj.getAttribute("is_loggedin") == null) {
        out.println("Session attribute 'is_loggedin' is null. Redirecting to login page.");
        response.sendRedirect("login.jsp");
        return;
    } else if (!sessionObj.getAttribute("is_loggedin").equals(1)) {
        out.println("User is not logged in. Redirecting to login page.");
        response.sendRedirect("login.jsp");
        return;
    }

%>

<div align="center">
    <h1>Your Job List</h1>
</div>

<table border="1" align="center" style="width:80%">
    <tr>
        <th>Job Title</th>
        <th>Created At</th>
        <th>Status</th>
        <th>Final Bid Amount</th>
        <th>Actions</th>
    </tr>
    
    <%
    List<Map<String, Object>> jobList = (List<Map<String, Object>>) request.getAttribute("job_list");
	    if (jobList != null) {
	    	
	  
	        for (Map<String, Object> job : jobList) {
    %>
        <tr>
            <td><%= job.get("job_title") %></td>
	        <td><%= job.get("created_at") %></td>
	        <td><%= job.get("status") %></td>
	        <td><%= job.get("final_bid_amount") %></td>
	        <td>
                <select name="action" id="action" onchange="updateJobStatus(<%= job.get("id") %>,this.value)">
                    <option value="closed" <%= (job.get("status").equals("closed") ? "selected" : "") %>>Close</option>
                    <option value="hold" <%= (job.get("status").equals("hold") ? "selected" : "") %>>Hold</option>
                    <option value="assigned" <%= (job.get("status").equals("assigned") ? "selected" : "") %>>Assigned</option>
                </select>
				<button onclick="location.href='PostJobServlet?job_id=<%= job.get("id") %>'">Edit</button>
	        </td>
	         <td>
                <form action="JobDetailsServlet" method="get">
                    <input type="hidden" name="job_id" value="<%= job.get("id") %>">
                    <input type="submit" value="Details">
                </form>
            </td>
        </tr>
        
        <%
            }
        }
    %>
</table>

<script>
function updateJobStatus(job_id, status) {
	//alert("PostJobServlet?job_id="+job_id+"&status=" + status);
    window.location.href = "PostJobServlet?job_id="+job_id+"&status=" + status;
}
</script>

</body>
</html>
