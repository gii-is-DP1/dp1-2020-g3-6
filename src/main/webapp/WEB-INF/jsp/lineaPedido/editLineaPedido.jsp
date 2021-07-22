<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>


<foorder:layout pageName="lineaPedido">
    <h2>LineaPedido</h2>
    <form:form modelAttribute="lineaPedido" class="form-horizontal" id="add-proveedor-form" action="/lineaPedido/save">
        <div class="form-group has-feedback">
            <foorder:inputField label="producto" name="producto"/>
            <foorder:inputField label="cantidad" name="cantidad"/>
            <foorder:inputField label="pedido" name="pedido"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="hidden" name="id" value="${lineaPedido.id}">  
                <button class="btn btn-default" type="submit">A�adir lineaPedido</button>
                  
            </div>
        </div>
    </form:form>
</foorder:layout>
