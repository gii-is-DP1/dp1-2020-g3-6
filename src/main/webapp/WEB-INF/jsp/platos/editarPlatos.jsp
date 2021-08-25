<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="platos">
    <h2>Plato</h2>
    <form:form modelAttribute="plato" class="form-horizontal" id="add-plato-form"  action="/platos/edit/${plato.id}">
        <div class="form-group has-feedback">
            <foorder:inputField label="nombre" name="name"/>
            <foorder:inputField label="precio" name="precio"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">     
            	<button class="btn btn-default" type="submit">Actualizar</button>
 			</div>
        </div>
    </form:form>
</foorder:layout>
