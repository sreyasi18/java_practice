<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>find a job </title>
</head>
<body>
<div align = "center">
<a href="LogoutServlet">logout</a>
<a href="postJob.jsp">post a job</a>
<a href="dashboard.jsp">dashboard</a>
<a href="wishlist.jsp">wish list</a>
</div>

<%
	HttpSession sessionObj = request.getSession(false);
	if(sessionObj == null){
		out.println("Session is null.Redirecting to login page.");
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
    <h1>find job</h1>
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
    List<Map<String, Object>> findJob = (List<Map<String, Object>>) request.getAttribute("findJob");
	    if (findJob != null) {
	    	
	  
	        for (Map<String, Object> job : findJob) {
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
				
	        </td>
	         <td>
                <form action="JobDetailsServlet" method="get">
                    <input type="hidden" name="job_id" value="<%= job.get("id") %>">
                    <input type="submit" value="Details">
                </form>
            </td>
        </tr>
       </table>
<%
    // Retrieve message and messageType from request attributes
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");

    // Display message if available
    if (message != null) {
%>
    <div class="<%= "success".equals(messageType) ? "message-success" : "message-error" %>">
        <%= message %>
    </div>
<%
    }
	        }
	    }
%>

<script>
function updateJobStatus(job_id, status) {
    // Redirect to FindJobServlet with job_id and status parameters
    window.location.href = "FindJobServlet?job_id=" + job_id + "&status=" + status;
}
</script>

    

</body>
</html>