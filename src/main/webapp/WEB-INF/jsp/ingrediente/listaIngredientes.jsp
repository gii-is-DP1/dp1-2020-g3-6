<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="ingrediente">
    <h2>Ingrediente</h2>

    <table id="ingTable" class="table table-striped">
        <thead>
        <tr>
   			<th>Producto</th>
   			<th>Cantidad</th>		
   			<th>Acciones</th>	
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${ingrediente}" var="ingrediente">
            <tr>
                <td>
                    <c:out value="${ingrediente.producto.name}"/>
                </td>
                <td>
                    <c:out value="${ingrediente.cantidadUsualPP}"/>
                </td>
             	 <td>
                   <spring:url value="/ingrediente/delete/{ingId}" var="ingURL">
                   		  <spring:param name="ingId" value="${ingrediente.id}"/>
                   </spring:url>
                   <a href="${fn:escapeXml(ingURL)}"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                 <td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>

</foorder:layout>
