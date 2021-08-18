<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="producto">
	<h3><span class="message-span" id="message"></span></h3>
    <h2>Producto</h2>
  	
    <table id="productoTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Tipo Producto</th>
            <th>Cantidad Minima</th>
            <th>Cantidad Actual</th>
            <th>Cantidad Maxima</th>
            <th>Proveedor</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="producto">
            <tr>
                <td>
                    <c:out value="${producto.name}"/>
                </td>
                <td>
                    <c:out value="${producto.tipoProducto}"/>
                </td>
                <td>
                    <c:out value="${producto.cantMin}"/>
                </td>
                <td>
                    <c:out value="${producto.cantAct}"/>
                </td>
                <td>
                   	<c:out value="${producto.cantMax}"/>
                </td>
                <td>
                   	<c:out value="${producto.proveedor.name}"/>
                </td>
                <td>
                   <spring:url value="/producto/edit/{productoId}" var="productoURL">
                   		  <spring:param name="productoId" value="${producto.id}"/>
                   </spring:url>
                   <a href="${fn:escapeXml(productoURL)}"><span class="glyphicon glyphicon-pencil" aria-hidden="true">&nbsp;</a>
                   
                   <spring:url value="/producto/delete/{productoId}" var="productoURL">
                   		  <spring:param name="productoId" value="${producto.id}"/>
                   </spring:url>
                   <a href="${fn:escapeXml(productoURL)}"><span class="glyphicon glyphicon-trash" aria-hidden="true">&nbsp;</a>
                   
                   <spring:url value="/producto/savePedido/{productoId}" var="productoURL">
                   		  <spring:param name="productoId" value="${producto.id}"/>
                   </spring:url>
                   
                   <c:choose>
  						<c:when test="${producto.cantMin >= producto.cantAct && producto.proveedor.activo}">
                			<a href="${fn:escapeXml(productoURL)}"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></a>  
 				   		</c:when>
					</c:choose> 
                </td>                
            </tr>
        </c:forEach>
        
        </tbody>
    </table>
    
    <nav aria-label="Pagination">
    	<ul class="pagination justify-content-center">
			<c:forEach items="${pages}" var="page">
				<a href="/producto?page=${page}"><c:out value="${page}"></c:out></a>
			</c:forEach>    	
    	
    	</ul>
  	</nav>
  	
  	<form method="get" action="/producto/new">
      	<button class="btn btn-default" type="submit">Crear Producto</button>
  	</form>
</foorder:layout>
<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>
