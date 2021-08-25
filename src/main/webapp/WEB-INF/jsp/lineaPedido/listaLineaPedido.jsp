<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="lineaPedido">
	<h3><span class="message-span" id="message"></span></h3>
    <h2>LineaPedido</h2>

    <table id="lineaPedidoTable" class="table table-striped">
        <thead>
        <tr>
            <th>Producto</th>
            <th>Cantidad</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${lineaPedido}" var="lineaPedido">
            <tr>
                <td>
                    <c:out value="${lineaPedido.producto.name}"/>
                </td>
                <td>
                    <c:out value="${lineaPedido.cantidad}"/>
                </td>
            </tr>
        </c:forEach>
         
        </tbody>
    </table>

</foorder:layout>

<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>
