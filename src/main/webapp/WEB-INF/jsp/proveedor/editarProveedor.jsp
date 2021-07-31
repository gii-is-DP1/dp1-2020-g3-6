<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="proveedor">
<span id="message"></span>
    <h2>Proveedor</h2>
    <form:form modelAttribute="proveedor" class="form-horizontal" id="add-proveedor-form"  action="/proveedor/edit">
        <div class="form-group has-feedback">
            <foorder:inputField label="nombre" name="name"/>
            <foorder:inputField label="gmail" name="gmail"/>
            <foorder:inputField label="telefono" name="telefono"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="hidden" name="id" value="${proveedor.id}">  
                <button class="btn btn-default" type="submit">Actualizar</button>
            </div>
        </div>
    </form:form>
</foorder:layout>
<script>

var queryString = window.location.search;
var urlParams = new URLSearchParams(queryString);
var message = urlParams.get('message');

	
$('#message').text(message).text();


</script>