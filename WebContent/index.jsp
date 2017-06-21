<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*,ejb.*, home.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html>
<html lang="fr">

<head>

<meta charset="utf-8">
<title>Bienvenue sur Paperbook</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Connexion à mon application">

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

<script type="text/javascript"
	src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script type="text/javascript"
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>
<body>
	<!-- Fixed navbar -->
	<div class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button class="navbar-toggle" type="button" data-toggle="collapse"
					data-target="#navbar-main">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><font size="+3">Paperbook</font></a>
			</div>
			<div class="navbar-collapse collapse" id="navbar-main">
				<form class="navbar-form navbar-right" action=Main method="post"
				 role="search">
					<input type=hidden name=op value="connexion">
					<div class="form-group">
						<input type="text" class="form-control" name="identifiant"
							placeholder="Identifiant" required autofocus>
					</div>
					<div class="form-group">
						<input type="password" class="form-control" name="mdp"
							placeholder="mot de passe" required>
					</div>
					<button type="submit" class="btn btn-default">Se connecter</button>
				</form>
			</div>
		</div>
	</div>
	<!-- 	Navbar FIN -->
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
					<div class="offset2 col-xs-6">
					<br><br><br><br>
					<h4><p style="color:grey;">Pour bien commencer il est conseillé de commencer par lire la démo : </p> </h4>
					<a href="Main?op=demo&nomFichier=demo.pdf">télecharger la démo</a>
						<object type="application/pdf" name="PDF" id="PDF">
							<param name="src" value="http://195.154.68.69:8080/paperbook/Main?op=fichier&nomFichier=demo.pdf" />
						</object> 
					</div>
					<div class="offset2 col-xs-6">

						<h1>Inscription</h1>
						<h3><p style="color:grey;">Informations personnelles</p></h3>
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
						<hr>
					


					<div class="form-group">
						<div class="col-md-offset-0 col-md-8">
							
							<br> <input class="btn btn-success btn btn-success"
								type="submit" value="Inscription" />
						</div>
					</div>
					</div>
					<br>
				</form>
			</div>
		</div>
	</div>
	<!-- Fixed footer -->
	<div class="navbar navbar-inverse navbar-fixed-bottom"
		role="navigation">
		<div class="container">
			<div class="navbar-text pull-left">
				<p>© 2015 Oussama Markad-Alain Tchana</p>
			</div>
			<div class="navbar-text pull-center">
				<p>Contact : Alain.Tchana@enseeiht.fr</p>
			</div>
			<div class="navbar-text pull-right">
				<a href="http://sd-41336.dedibox.fr/tchana/"><i class="fa fa-external-link fa-2x"></i>SEPIA-IRIT</a>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		PDF.SetShowToolBar("true"); //--- barre d'outils true(visible) false(non visible) ---//
		PDF.SetShowScrollbar("true"); //--- barre de scroll true(visible) false(non visible) ---//
		PDF.SetPageMode("none"); //--- cache les signets ---//
		PDF.setZoom(80); //--- Zoom le document à 80% ---//
	</script>
	
</body>
</html>