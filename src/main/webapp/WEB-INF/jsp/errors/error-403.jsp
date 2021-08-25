<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="error">
	<spring:url value="/resources/images/ForbiddenAccess.png" var="ForbiddenAccess"/>
	<p class="text-center"><img src="${ForbiddenAccess}" style="width:40%"/></p>
	<h2 style="text-align:center;">ERROR 403!</h2>
	<p style="text-align:center;">Lo sentimos, pero no est�s autorizado para acceder a esta p�gina
		<br>o puede que est�s intentando acceder a una p�gina inexistente.</p>
</foorder:layout>