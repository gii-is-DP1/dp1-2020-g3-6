<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="propietarios">
	<h3><span class="message-span" id="message"></span></h3>
    <h2>Propietario</h2>

    <table id="propietariosTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Telefono</th>
            <th>Gmail</th>
            <th>Usuarios</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${propietarios}" var="propietario">
            <tr>
                <td>
                    <c:out value="${propietario.name}"/>
                </td>
                <td>
                    <c:out value="${propietario.apellido}"/>
                </td>
                <td>
                    <c:out value="${propietario.telefono}"/>
                </td>
                <td>
                   	<c:out value="${propietario.gmail}"/>
                </td>
                 <td>
                   	<c:out value="${propietario.usuario}"/>
                </td>
                <td>
                   <spring:url value="/propietarios/delete/{propietarioId}" var="propietarioURL">
                   		  <spring:param name="propietarioId" value="${propietario.id}"/>
                   </spring:url>
                   <a href="${fn:escapeXml(propietarioURL)}"><span class="glyphicon glyphicon-trash" aria-hidden="true">&nbsp;</a>
                
           	    	<spring:url value="/propietarios/edit/{propietarioId}" var="propietarioEditUrl">
    		  			<spring:param name="propietarioId" value="${propietario.id}"/>
  					</spring:url>
                	<a href="${fn:escapeXml(propietarioEditUrl)}"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></a>
                </td>
             
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form method="get" action="/propietarios/new">
      	<button class="btn btn-default" type="submit">Crear propietario</button>
  	</form>
</foorder:layout>

<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>

