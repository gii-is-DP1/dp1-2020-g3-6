<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="platos">
    <h2>Plato</h2>

    <table id="camarerosTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Precio</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${platos}" var="plato">
            <tr>
            	<td>
                  <spring:url value="/platos/{platoId}" var="platoUrl">
                        <spring:param name="platoId" value="${plato.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(platoUrl)}"><c:out value="${plato.name}"/></a>
                <td>
                    <c:out value="${plato.precio} "/><span class="glyphicon glyphicon-euro" aria-hidden="true"></span>
                </td>
              
                <td>
                   <spring:url value="/platos/delete/{platoId}" var="platoURL">
                   		  <spring:param name="platoId" value="${plato.id}"/>
                   </spring:url>
                   <a href="${fn:escapeXml(platoURL)}"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <form method="get" action="/platos/new">
      	<button class="btn btn-default" type="submit">Crear Plato</button>
  	</form>
</foorder:layout>