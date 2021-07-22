<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="foorder" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default bg-primary" role="navigation">
	<div class="container-navbar">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<sec:authorize access="hasAuthority('propietario')">
				<foorder:menuItem active="${name eq 'empleados'}" url="/empleados"
					title="empleados">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Empleados</span>
				</foorder:menuItem>
				<foorder:menuItem active="${name eq 'producto'}" url="/producto"
					title="producto">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Producto</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'platos'}" url="/platos"
					title="platos">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Platos</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'Total Comandas'}" url="/comanda/listaComandaTotal"
					title="comanda">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Comandas Realizadas</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'proveedor'}" url="/proveedor"
					title="proveedor">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Proveedor</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'pedidos'}" url="/pedidos"
					title="pedidos">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Pedidos</span>
				</foorder:menuItem>
 				</sec:authorize>
 				
 				<!-- parte de propietarios -->
 				
 				<sec:authorize access="hasAuthority('camarero')">
				<foorder:menuItem active="${name eq 'Comanda Actual'}" url="/comanda/listaComandaActual"
					title="comanda">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Comandas Actuales</span>
				</foorder:menuItem>
 				</sec:authorize>
 				
 				
 				<sec:authorize access="hasAuthority('cocinero')">
				<foorder:menuItem active="${name eq 'platos'}" url="/platopedido"
					title="platos">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Platos Pedidos</span>
				</foorder:menuItem>
 				</sec:authorize>
 				
 				
 				<sec:authorize access="hasAuthority('manager')">
 				
				<foorder:menuItem active="${name eq 'pedidos'}" url="/pedidos"
					title="pedidos">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Pedidos</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'Total Comandas'}" url="/comanda/listaComandaTotal"
					title="comanda">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Comandas Realizadas</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'producto'}" url="/producto"
					title="producto">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Producto</span>
				</foorder:menuItem>
				
				<foorder:menuItem active="${name eq 'Notificaciones Producto'}" url="/producto/notificaciones"
					title="Notificaciones">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Notificaciones</span>
				</foorder:menuItem>
				
 				</sec:authorize>


			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
		
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span> 
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
