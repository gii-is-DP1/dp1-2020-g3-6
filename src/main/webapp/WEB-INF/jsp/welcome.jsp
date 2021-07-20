<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  


<petclinic:layout pageName="home">


    <sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default" href='<spring:url value="/owners/new" htmlEscape="true"/>'>Add Owner</a>
	</sec:authorize>
	
	<sec:authorize access="!isAuthenticated()">
	
	<H1 class="welcome" align="center">Bienvenido a foorder</H1>
	
	<br>
	<br>
	
	<h1  align="center"> <img  src="resources/images/restaurante.jpg"> </h1>
	
	<br>
	
	
	<H1 align="center"> Por favor identif�cate y disfruta de la aplicaci�n </H1>
	
	<br>

	</sec:authorize>

	<sec:authorize access="isAuthenticated()">
	<H1 class="welcome" align="center" >Bienvenido <sec:authentication property="name" /> </H1>
	
	<br>
	<h2 align="center"> Busca arriba todas las funcionalidades pertenecientes a tu rol! </h2>
	<br>
	</sec:authorize>

</petclinic:layout>
