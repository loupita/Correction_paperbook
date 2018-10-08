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
<title>Bienvenue sur site</title>
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
					<li class="active"><a href="inscription.jsp">Inscription</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-xs-12">

				<div class="main">

					<div class="row">
						<div class="col-xs-12 col-sm-6 col-sm-offset-1">
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
							<h1>Se connecter</h1>
							<h3>Bienvenue</h3>

							<form action="Main" name="login" role="form"
								class="form-horizontal" method="post" accept-charset="utf-8">
								<input type="hidden" name="op" value="connexion" />
								<div class="form-group">
									<div class="col-md-8">
										<input name="identifiant" placeholder="Idenfiant"
											class="form-control" type="text" id="UserUsername" required autofocus">
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-8">
										<input name="mdp" placeholder="Mot de passe"
											class="form-control" type="password" id="UserPassword"
											required >
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-offset-0 col-md-8">
										<input class="btn btn-success btn btn-success" type="submit"
											value="Connexion" />
									</div>
								</div>

							</form>
							<p class="credits">
								Vous n'avez pas encore de compte ?<a href="inscription.jsp"
									target="_blank">Inscrivez-vous</a>.
							</p>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<!-- Fixed footer -->
	<div class="navbar navbar-inverse navbar-fixed-bottom"
		role="navigation">
		<div class="container">
			<div class="navbar-text pull-left">
				<p>© 2015 Oussama Markad.</p>
			</div>
			<div class="navbar-text pull-right">
				<a href="#"><i class="fa fa-facebook-square fa-2x"></i></a> <a
					href="#"><i class="fa fa-twitter fa-2x"></i></a> <a href="#"><i
					class="fa fa-google-plus fa-2x"></i></a>
			</div>
		</div>
	</div>
</body>
</html>