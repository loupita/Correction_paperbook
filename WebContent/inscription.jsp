<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*,ejb.*, home.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>s'inscrire</title>

<!-- --------------------CSS-------------------------------------------------- -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link
	href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"
	rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald'
	rel='stylesheet' type='text/css'>
<!-- ------------------------------------------------------------------------ -->
<link href="css/sticky-footer.css" rel="stylesheet">
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/skel.min.js"></script>
<script src="js/init.js"></script>
</head>
<body>


	<!-- Fixed navbar -->
	<div class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="index.jsp"><font size="+3">Paperbook</font></a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li class="active"><a href="connexion.jsp">Connexion</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<div id="main" class="container">
		<%
			if (request.getAttribute("erreurs") != null) {
				Collection<String> erreurs = (Collection<String>) request
						.getAttribute("erreurs");
				if (!erreurs.isEmpty()) {
		%>
		<div class="alert alert-danger" role="alert">
			<b>Erreur !</b>
			<%
				for (String erreur : erreurs) {
			%><ul>
				<li><%=erreur%></li>
			</ul>
			<%
				}
					}
			%>
		</div>
		<%
			}
		%>



	</div>

	<div class="container">
		<div class="main">

			<div class="row">

				<form action="Main" name="sign up" role="form"
					class="form-horizontal" method="post" accept-charset="utf-8">
					<input type="hidden" name="op" value="inscription" />

					<div class="offset2 col-xs-4">

						<h1>S'inscrire</h1>
						<h3>Informations personnelles</h3>
						<div class="form-group">
							<div class="col-md-8">
								<label for="Nom" class="sr-only">Nom</label> <input type="Nom"
									id="Nom" name="nom" class="form-control" placeholder="Nom"
									required autofocus
									value="<%if (request.getAttribute("nom") != null)
				request.getAttribute("nom");%>">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<label for="Prenom" class="sr-only">Prénom</label> <input
									type="Prenom" id="Prenom" name="prenom" class="form-control"
									placeholder="Prénom" required autofocus
									value="<%if (request.getAttribute("prenom") != null)
				request.getAttribute("prenom");%>">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<label for="inputId" class="sr-only">Identifiant</label> <input
									type="identifiant" id="inputId" name="identifiant"
									class="form-control" placeholder="identifiant" required
									autofocus>
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<label for="inputEmail" class="sr-only">l'adresse mail </label>
								<input type="email" id="inputEmail" name="email"
									class="form-control" placeholder="L'adresse mail" required
									autofocus
									value="<%if (request.getAttribute("email") != null)
				request.getAttribute("email");%>">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<label for="inputPassword" class="sr-only">Mot de passe</label>
								<input type="password" id="inputPassword" name="mdp"
									class="form-control" placeholder="Mot de passe" required>
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<label for="inputConfirmationPassword" class="sr-only">Confirmation
									mot de passe</label> <input type="password"
									id="inputConfirmationPassword" name="confirmationMdp"
									class="form-control" placeholder="Confirmation mot de passe"
									required>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-0 col-md-8">
							<hr>
							<br> <input class="btn btn-success btn btn-success"
								type="submit" value="Inscription" />
						</div>
					</div>
					<p></p>
				</form>
			</div>
		</div>
	</div>
	<!-- 	<footer class="footer" id="footer"> -->
	<!-- 		<div class="container"> -->
	<!-- 			<p class="text-muted">Made by Oussama Markad</p> -->
	<!-- 		</div> -->
	<!-- 	</footer> -->
</body>
</html>
