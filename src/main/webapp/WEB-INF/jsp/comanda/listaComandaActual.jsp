<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags"%>

<foorder:layout pageName="Comanda Actual">
	<h3><span class="message-span" id="message"></span></h3>
	<h2>Comandas</h2>

	<spring:url value="/comanda/listaComandaActual/new" var="newURL">
	</spring:url>
	<table id="comandaActualTable" class="table table-striped">
		<thead>
			<tr>
				<th>Mesa</th>
				<th>Camarero</th>
				<th>Precio total</th>
				<th>Acciones</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list_comanda}" var="comanda">
				<tr>
					<td><c:out value="${comanda.mesa}" /></td>
					<td><c:out value="${comanda.camarero.name}" /></td>
					<td><c:out value="${comanda.precioTotal} " /><span
						class="glyphicon glyphicon-euro" aria-hidden="true"></span></td>
					<td><spring:url
							value="/comanda/listaComandaActual/{comandaID}" var="infoURL">
							<spring:param name="comandaID" value="${comanda.id}" />
						</spring:url>
						<form class="btn-line" action="${fn:escapeXml(infoURL)}">
							<button class="btn btn-default" type="submit">Info</button>
						</form> <spring:url
							value="/comanda/listaComandaActual/finalizarComanda/{comandaID}"
							var="closeURL">
							<spring:param name="comandaID" value="${comanda.id}" />
						</spring:url>
						<form class="btn-line" action="${fn:escapeXml(closeURL)}">
							<button class="btn btn-default" type="submit">Finalizar
								comanda</button>
						</form></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="btn-line-comanda">
		<form:form name="new_comanda" modelAttribute="new_comanda"
			id="add-comanda-form" class="btn-line"
			action="${fn:escapeXml(newURL)}">
			<foorder:inputField label="Mesa" name="mesa" />
			<hr>
			<button class="btn btn-default" type="submit">Nueva comanda</button>
			<input type="hidden" name="lista_comanda" value="${comanda} }">
		</form:form>
	</div>
</foorder:layout>

<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>