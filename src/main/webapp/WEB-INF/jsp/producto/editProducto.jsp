<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>
<script>
function validarsize(){
	var name = document.forms["producto"]["name"].value;
	var tipoproductodto = document.forms["producto"]["tipoproductodto"].value;
	var cantMin = document.forms["producto"]["cantMin"].value;
	var cantAct = document.forms["producto"]["cantAct"].value;
	var cantMax = document.forms["producto"]["cantMax"].value;
	var proveedor = document.forms["producto"]["proveedor"].value;
	
	if(!(cantMin-cantMin == 0.0)){
        alert("La cantidad m�nima tiene que ser un n�mero");
        return false;
	}
	if(!(cantMax-cantMax == 0.0)){
        alert("La cantidad m�xima tiene que ser un n�mero");
        return false;
	}
	if(!(cantAct-cantAct == 0.0)){
        alert("La cantidad actual tiene que ser un n�mero");
        return false;
	}
	
	return true;
} 
</script>
<foorder:layout pageName="producto">
    <h2>Nuevo producto</h2>
    <form:form name="producto" modelAttribute="producto" class="form-horizontal" id="edit-producto-form"  action="/producto/save" onsubmit="return validarsize();">
        <div class="form-group has-feedback">
            <foorder:inputField label="Nombre" name="name"/>
            <div class="control-group">
				<foorder:selectField name="tipoproductodto" label="Tipo Producto " names="${listaTipos}" size="6"/>
            </div>
            <foorder:inputField label="Cantidad minima" name="cantMin"/>
            <foorder:inputField label="Cantidad actual" name="cantAct"/>
            <foorder:inputField label="Cantidad maxima" name="cantMax"/>
            <div class="control-group">
				<foorder:selectField name="proveedor" label="Proveedor" names="${listaProveedores}" size="6"/>
            </div>
 
        </div>
      	<div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="hidden" name="id" value="${producto.id}">  
                <button class="btn btn-default" type="submit">Anadir producto</button>
            </div>
        </div>
    </form:form>
</foorder:layout>
