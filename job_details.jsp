<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Job Details</title>
</head>
<body>
<div align="center">
    <a href="LogoutServlet">Logout</a>
    <a href="postJob.jsp">Post a Job</a>
    <a href="dashboard.jsp">Dashboard</a>
    <a href="JobList_Servlet">Back to Job List</a>
    <br>
<a href="FindJobServlet">Find a job</a>
</div>

<%
    Map<String, Object> jobDetails = (Map<String, Object>) request.getAttribute("job_details");
    if (jobDetails != null) {
%>
    <div align="center">
        <h1>Job Details</h1>
        <p><strong>Job Title:</strong> <%= jobDetails.get("job_title") %></p>
        <p><strong>Description:</strong> <%= jobDetails.get("description") %></p>
        <p><strong>Created At:</strong> <%= jobDetails.get("created_at") %></p>
        <p><strong>Status:</strong> <%= jobDetails.get("status") %></p>
        <p><strong>Final Bid Amount:</strong> <%= jobDetails.get("final_bid_amount") %></p>
    </div>
    
     <div align="center">
        <h2>Submit Your Bid</h2>
        <form action="BidSubmit" method="post">
            <input type="hidden" name="job_id" value="<%= jobDetails.get("job_id") %>">
            <label for="bid_amount">Bid Amount:</label>
            <input type="text" id="bid_amount" name="bid_amount" required>
            <input type="submit" value="Submit Bid">
        </form>
    </div>
<%
    } else {
%>
    <div align="center">
        <p>No job details available.</p>
    </div>
<%
    }
%>
</body>
</html>
