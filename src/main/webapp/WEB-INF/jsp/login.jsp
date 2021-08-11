<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!------ Include the above in your HEAD tag ---------->

<!DOCTYPE html>
<html>
<head>
	<title>Login Page</title>
   <!--Made with love by Mutiullah Samim -->
   
	<!--Bootsrap 4 CDN-->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    
    <!--Fontawesome CDN-->
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

	<style>
	body{
	background: 	linear-gradient(217deg, #8cbeca, rgba(255,0,0,0) 70.71%),
                    linear-gradient(127deg, #969696, rgba(0,255,0,0) 70.71%),
                    linear-gradient(336deg, #c4c4c4, rgba(0,0,255,0) 70.71%);
	}
	
	#contenedor_fooder{
    background-image: radial-gradient(circle, #9aa6b2, #86909b, #727b85, #5f6770, #4c535b, #43494f, 
    #3a3f44, #313539, #2f3134, #2c2d2f, #29292b, #262626);   
	}
	
	#contenedor_fooder h3, p{
	color: white;
	
	}
	
	.containerbienvenido {
	background: #29292b; 
	}
	
	h1 {
	font-weight: bold;
	color: white;
	}
	
	</style>
</head>
<body class="body-login">
	<div class="containerbienvenido pt-4 pb-4">
        <div class="row text-center">
            <div class="col-md">
                <h1> BIENVENIDO A FOORDER </h1>
                
            </div>
        </div>
    </div>
    <div class="container py-3 mt-5">
        <div id="errors"></div>
        <div class="card card0">
            <div class="d-flex flex-lg-row flex-column-reverse">
                <!--Login-->
                <div class="col-md justify-content-center card" id="contenedor_login">
                    <form action="login" method="POST">
                     
   						 <c:out value="${error}"></c:out>
					
                        <div class="row justify-content-center my-auto">
                            <div class="col-md-8 col-10 my-5">
                                <h3 class="mb-5 text-center heading">LOG IN</h3>

                                <div class="input-group form-group">
									<div class="input-group-prepend">
										<span class="input-group-text"><i class="fas fa-user"></i></span>
									</div>
									<input name="username" type="text" class="form-control" placeholder="usuario" required autofocus>
								</div>
								
								<div class="input-group form-group">
									<div class="input-group-prepend">
										<span class="input-group-text"><i class="fas fa-key"></i></span>
									</div>
									<input name="password" type="password" class="form-control" placeholder="contrasena" required autofocus>
								</div>
						
                                <div class="row justify-content-center my-3 px-3">
                                    <div class="col-md text-center">
                                        <button type="submit" class="btn btn-dark btn-lg" id="btn-register"> Iniciar Sesion
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>


                <!--Register-->
                <div class="col-md justify-content-center card" id="contenedor_fooder">
                      	<div class="row justify-content-center my-auto">
                            <div class="col-md-8 col-10 my-5">
                                <h3 class="mb-5 text-center heading">SOMOS FOORDER</h3>
                                <p class="mb-5 text-center"> Para poder iniciar sesion debe estar dado de alta como un trabajador
                                de la empresa.</p>
                                <p class="mb-5 text-center"> Si ya estas dado de alta como trabajador y no puede iniciar sesion,
                                debe ponerse en contacto con el manager de su empresa, y si no es posible, con el dueño.  </p>
                            </div>
                        </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>