<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/saveperson" method="post">
		<table>
			<tr>
				<td>Person Name: </td>
				<td><input type="text" name="personname"/></td>
			</tr>
			<tr>
				<td>Person Salary</td>
				<td><input type="number" pattern="(\d{3})([\.])(\d{2})" name="salary"/></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Add"/></td>
			</tr>
		</table>
	</form>
	
	${response}
</body>
</html>