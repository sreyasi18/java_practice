<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Wishlist</title>
</head>
<body>
<div align="center">
    <a href="LogoutServlet">Logout</a>
    <a href="postJob.jsp">Post a Job</a>
    <a href="dashboard.jsp">Dashboard</a>
</div>
<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null) {
        response.sendRedirect("login.jsp");
        return;
    } else if (sessionObj.getAttribute("is_loggedin") == null) {
        response.sendRedirect("login.jsp");
        return;
    } else if (!sessionObj.getAttribute("is_loggedin").equals(1)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<div align="center">
    <h1>Job Wishlist</h1>
    <form action="WishlistServlet" method="get">
        <label for="selectedDate">Select Date:</label>
        <input type="date" id="selectedDate" name="selectedDate" required>
        <input type="submit" value="Submit">
    </form>
</div>

<%
    List<Map<String, Object>> availableJobs = (List<Map<String, Object>>) request.getAttribute("available_jobs");
    if (availableJobs != null) {
%>
    <div align="center">
        <h2>Jobs till Date</h2>
    </div>
    <table border="1" align="center" style="width:80%">
        <tr>
            <th>Job Title</th>
            <th>Description</th>
            <th>Created At</th>
            <th>Status</th>
            <th>Final Bid Amount</th>
        </tr>
        <%
            for (Map<String, Object> job : availableJobs) {
        %>
            <tr>
                <td><%= job.get("job_title") %></td>
                <td><%= job.get("description") %></td>
                <td><%= job.get("created_at") %></td>
                <td><%= job.get("status") %></td>
                <td><%= job.get("final_bid_amount") %></td>
            </tr>
        <%
            }
        %>
    </table>
<%
    }
%>
</body>
</html>
