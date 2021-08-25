<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="platos">
	<h3><span class="message-span" id="message"></span></h3>
    <h2>Plato <c:out value="${plato.name}"/></h2>

    <table class="table table-striped">
       
        <tr>
            <th>Precio</th>
            <td>
            	<c:out value="${plato.precio} "/><span class="glyphicon glyphicon-euro" aria-hidden="true"></span>
            </td>
        </tr>
       
    </table>
 		
    <spring:url value="{platoId}/edit" var="platoUrl">
        <spring:param name="platoId" value="${plato.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(platoUrl)}" class="btn btn-default">Editar Plato</a>

    <spring:url value="/platos/{platoId}/ingrediente/new" var="addUrl">
        <spring:param name="platoId" value="${plato.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Agregar Ingrediente</a>
    <br/>
    <br/>
    <br/>
    <h2>Ingredientes:</h2>
	

    <table class="table table-striped">
        <c:forEach var="ing" items="${ingredientes}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${ing.producto.name}"/></dd>
                        
                        <dt>Cantidad</dt>
                        <dd><c:out value="${ing.cantidadUsualPP}"/></dd>
                        
                        <dt><spring:url value="/platos/deleteIng/${ing.id}" var="deleteIngURL">
                   		  <spring:param name="platoId" value="${plato.id}"/>
                   		</spring:url>
                   		<a href="${fn:escapeXml(deleteIngURL)}"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                   		</dt>
                       
                    </dl>
                </td>
            </tr>

        </c:forEach>
    </table>

</foorder:layout>
<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>

