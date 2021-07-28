<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags"%>

<script>
$(function(){
	var mayus = new RegExp("^(?=.*[A-Z])");
	var special = new RegExp("^(?=.*[!@#$&*])");
	var numbers = new RegExp("^(?=.*[0-9])");
	var lower =  new RegExp("^(?=.*[a-z])");
	var len =  new RegExp("^(?=.*{5,})");
	
	$("#contrasena").on("keyup",function(){
		var pass = $("#contrasena").val();
		var seguridad =0;
		if( mayus.test(pass)){
			seguridad=1+seguridad;
		}
		if( special.test(pass)){
			seguridad=1+seguridad;
		}
		if( numbers.test(pass)){
			seguridad=1+seguridad;
		}
		if( lower.test(pass)){
			seguridad=1+seguridad;
		}
		if( len.test(pass)){
			seguridad=1+seguridad;
		}
		if(seguridad==0){
			$("#mensaje").text("No segura");
		
		}
		if(seguridad==1){
			$("#mensaje").text("Muy poco segura");
	
			}
		if(seguridad==2){
			$("#mensaje").text("poco segura");

			}
		if(seguridad==3){
			$("#mensaje").text("Segura");
		
			}
		if(seguridad==4){
			$("#mensaje").text("Muy segura");
		}
			});
	});
function validarsize(){
	var name = document.forms["camarero"]["name"].value;
	var apellido = document.forms["camarero"]["apellido"].value;
	var gmail = document.forms["camarero"]["gmail"].value;
	var usuario = document.forms["camarero"]["usuario"].value;
	var contrasena = document.forms["camarero"]["contrasena"].value;
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
<foorder:layout pageName="camareros">
	<h2>Editar Camarero</h2>
	<form:form name="camarero" modelAttribute="camarero"
		class="form-horizontal" id="edit-camareros-form"
		action="/camareros/edit" onsubmit="return validarsize();">
		<div class="form-group has-feedback">
			<foorder:inputField label="nombre" name="name" />
			<foorder:inputField label="apellido" name="apellido" />
			<foorder:inputField label="gmail" name="gmail" />
			<foorder:inputField label="telefono" name="telefono" />
			<foorder:inputField label="usuario" name="usuario" />
			<foorder:inputField label="contrasena" name="contrasena" />
			<input type="password" name="contra" id="contra">
			<span id="mensaje"></span>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<input type="hidden" name="id" value="${camarero.id}">
				<button class="btn btn-default" type="submit">Actualizar</button>
			</div>
		</div>
	</form:form>
</foorder:layout>
