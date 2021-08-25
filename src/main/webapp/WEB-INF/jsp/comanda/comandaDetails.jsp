<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags"%>

<foorder:layout pageName="Info comanda">
<h3><span class="message-span" id="message"></span></h3>
	<h2>
		Comanda mesa
		<c:out value="${comanda.mesa}"></c:out>
	</h2>
	<div class="comandaContainer">
		<div class="tabla1">
			<table id="editarComandaTable" class="table table-striped">
				<thead>
					<tr>
						<th>Plato</th>
						<th>Estado del plato</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${platop}" var="platop">
						<tr>
							<td><c:out value="${platop.plato.name}" /></td>
							<td><c:choose>
									<c:when test="${platop.estadoplato == 'ENCOLA'}">
										<c:out value="En cola" />
									</c:when>
									<c:when test="${platop.estadoplato == 'ENPROCESO'}">
										<c:out value="En proceso" />
									</c:when>
									<c:when test="${platop.estadoplato == 'FINALIZADO'}">
										<c:out value="Finalizado" />
									</c:when>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="tabla2">
			<form:form modelAttribute="platopedido" class="form-horizontal"
				id="add-platopedido-form" action="/platopedido/${comanda.id}/save">

				<div class="form-group has-feedback">
					<div class="control-group">
						<foorder:selectField name="platodto" label="platos"
							names="${listaPlatos}" size="6" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<input type="hidden" name="id" value="${platopedido.id}">
						<input type="hidden" name=estadoplatodto value="ENCOLA">
						<button class="btn btn-default" type="submit">Anadir plato</button>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</foorder:layout>
<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>
