<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="error">
	<spring:url value="/resources/images/NotFound.png" var="NotFound"/>
	<p class="text-center"><img src="${NotFound}" style="width:40%"/></p>
	<h2 style="text-align:center;">ERROR 404!</h2>
	<p style="text-align:center;">Lo sentimos, pero la página que solicita no está en Foorder.</p>
</foorder:layout>
