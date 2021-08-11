<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="error">
	<spring:url value="/resources/images/ForbiddenAccess.png" var="ForbiddenAccess"/>
	<p class="text-center"><img src="${ForbiddenAccess}" style="width:40%"/></p>
	<br>
	<h2 style="text-align:center;">ERROR 500!</h2>
	<p style="text-align:center;">Lo sentimos, nuestros ingenieros están trabajando en ello,
		<br>aunque puede ser un problema local, intente recargar la página.</p>
</foorder:layout>