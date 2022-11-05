<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

<title>Lobbies</title>
</head>
<body>
	<h2>Lobbies:</h2>
	<div class="container">
		<br />
		<c:if test="${message != null}">
		<div class="alert alert-${messageType}">
			<c:out value="${message}"></c:out>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">�</a>
		</div>
		</c:if>
	</div>
	<a href="/lobbies/create"><span class="glyphicon glyphicon-plus sucess" aria-hidden="true"></span>Create Lobby</a>
	<table class="table table-striped">
		<tr>			
			<th>LobbyID</th>
			<th>Game</th>
			<th>Host</th>
			<th>Actions</th>
		</tr>
		<c:forEach items="${lobbies}" var="lobby">
			<tr>				
				<td><c:out value="${lobby.id}"/></td>				
				<td><c:out value="${lobby.game}"/></td>		
				<td><c:out value="${lobby.host.username}"/></td>		
				<td><a href="/lobbies/edit/${lobby.id}" ><span class="glyphicon glyphicon-pencil warning" aria-hidden="true"></span></a>
					&nbsp;<a href="/lobbies/delete/${lobby.id}"><span class="glyphicon glyphicon-trash alert" aria-hidden="true"></a> </td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>