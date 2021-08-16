<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="platopedido">

    <h2><c:out value="${platopedido.plato.name}"/></h2>

    <h2>Ingredientes:</h2>
	

    <table class="table table-striped">
        <c:forEach var="ing" items="${ingredientespedido}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${ing.ingrediente.producto.name}"/></dd>
                        
                        <dt>Cantidad</dt>
                        <dd><c:out value="${ing.cantidadPedida}"/></dd>
                    </dl>
                </td>
            </tr>

        </c:forEach>
    </table>

</foorder:layout>
