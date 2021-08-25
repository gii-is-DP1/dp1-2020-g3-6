<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="platopedido">
    <h2>platopedido</h2>
    <form:form modelAttribute="platopedido" class="form-horizontal" id="add-platopedido-form" action="/platopedido/save">
        <div class="form-group has-feedback">
            <div class="control-group">
				<foorder:selectField name="platodto" label="platos" names="${listaPlatos}" size="6"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="hidden" name="id" value="${platopedido.id}">
                <input type="hidden" name=estadoplatodto value="ENCOLA"> 
                <button class="btn btn-default" type="submit">Anadir plato pedido</button>
            </div>
        </div>
    </form:form>
</foorder:layout>
