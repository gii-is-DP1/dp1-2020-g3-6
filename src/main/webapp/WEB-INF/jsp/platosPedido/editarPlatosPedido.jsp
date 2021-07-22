<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>

<foorder:layout pageName="platopedido">
    <h2>Plato Pedido</h2>
    <form:form modelAttribute="platopedido" class="form-horizontal" id="add-platopedido-form" action="/platopedido/edit">
        <div class="form-group has-feedback">
            <div class="control-group">
				<foorder:selectField name="estadoplatodto" label="Selecciona Nuevo Estado" names="${estadosPlato}" size="6"/>
            </div>
      
             <div class="control-group">
				<foorder:selectField name="platodto" label="Confirma Plato a Modificar" names="${listaPlatos}" size="6"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="hidden" name="id" value="${platopedido.id}">  
                <div>
                <button class="btn btn-default" type="submit">Anadir plato pedido</button>
                </div>
            </div>
        </div>
    </form:form>
</foorder:layout>
