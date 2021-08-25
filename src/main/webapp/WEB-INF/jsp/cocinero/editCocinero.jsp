<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags" %>
<script>
function validarsize(){
	var name = document.forms["cocinero"]["name"].value;
	var apellido = document.forms["cocinero"]["apellido"].value;
	var gmail = document.forms["cocinero"]["gmail"].value;
	var usuario = document.forms["cocinero"]["usuario"].value;
	var contrasena = document.forms["cocinero"]["contrasena"].value;
	if(name.length < 3 || name.length > 75){
        alert("La longitud del nombre debe ser mayor de 3 o menor de 75");
        return false;
    }
	if(apellido.length < 3 || apellido.length > 75){
        alert("La longitud del apellido debe ser mayor de 3 o menor de 75");
        return false;
    }
	if(gmail.length < 3 || gmail.length > 75){
        alert("La longitud del gmail debe ser mayor de 3 o menor de 75");
        return false;
    }
	if(usuario.length < 3 || usuario.length > 75){
        alert("La longitud del usuario debe ser mayor de 3 o menor de 75");
        return false;
    }
	if(contrasena.length < 3 || contrasena.length > 75){
        alert("La longitud del contrasena debe ser mayor de 3 o menor de 75");
        return false;
    }
	return true;
} 
</script>
<foorder:layout pageName="cocinero">
    <h2>Cocinero</h2>
    <form:form name="cocinero" modelAttribute="cocinero" class="form-horizontal" id="add-cocinero-form" action="/cocinero/save">
        <div class="form-group has-feedback">
            <foorder:inputField label="name" name="name"/>
            <foorder:inputField label="apellido" name="apellido"/>
            <foorder:inputField label="gmail" name="gmail"/>
            <foorder:inputField label="telefono" name="telefono"/>
            <foorder:inputField label="usuario" name="usuario"/>
            <foorder:inputField label="contrasena" name="contrasena"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="hidden" name="id" value="${cocinero.id}">  
                <button class="btn btn-default" type="submit">Anadir cocinero</button> 
            </div>
        </div>
    </form:form>
</foorder:layout>

