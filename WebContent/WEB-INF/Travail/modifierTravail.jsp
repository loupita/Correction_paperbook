<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*,ejb.*, home.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Modifier les informations sur le travail</title>
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
<link rel="stylesheet" type="text/css" href="css/app.css" media="all" />
<!-- ------------------------------------------------------------------------ -->
<script type="text/javascript"
	src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script type="text/javascript"
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>

<body>
	<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/mondial" user="root"
		password="toto" />
	<%
		Personne proprio = (Personne) session.getAttribute("Personne");
		Travail travail = (Travail) session.getAttribute("travail");
		String titre = travail.getTitre();
		String resume = travail.getAbstr();
	%>
	<!-- Header -->
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
				<a class="navbar-brand" href="Main?op=espacePerso">Accueil</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><%=proprio.getIdentifiant()%> <b
							class="caret"></b></a>
						<ul class="dropdown-menu">
							<li class="dropdown-header">Actions</li>
							<li><a href="Main?op=RajouterEvt">Ajouter un événement</a></li>
							<li><a href="Main?op=RajouterTravail">Ajouter un travail</a></li>
							<li><a href="Main?op=rechercher">Rechercher</a></li>
							<li class="dropdown-header">Actualités & profil</li>
							<li><a href="Main?op=espacePerso">Mon espace personnel</a></li>
							<li><a href="Main?op=listeTravaux">Mes travaux</a></li>
							<li><a href="Main?op=mesConferences">Mes Conférences</a></li>
							<li><a href="Main?op=mesJournaux">Mes Journaux</a></li>
							<li><a href="Main?op=mesStatistiques">Mes statistiques</a></li>
							<li><a href="Main?op=deconnexion">Déconnexion</a></li>
						</ul></li>

				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<div class="col-md-2 offset2">
		<ul class="nav nav-pills nav-stacked">
			<li><a href="Main?op=annulerT">Details sur le travail</a></li>
			<li class="active"><a href="Main?op=RmodifierT">Modifier/Supprimer</a></li>
			<li><a href="Main?op=Rsoumettre">Soumettre à un événement</a></li>
			<li><a href="Main?op=Rcibler">Cibler un événement</a></li>
		</ul>
	</div>
	
	
	<div class=container>

		<div class="row col-sm-10">
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
				%>
				<ul>
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
			<form action="Main" role="form" class="form-horizontal" method="post"
				accept-charset="utf-8" enctype="multipart/form-data">

				<div class=row>
					<div class="col-xs-8">

						<h3>Modifier/Supprimer le travail :</h3>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Titre :</h6>
								<label for="titre" class="sr-only">Titre</label> <input
									type="text" id="titre" name="titre" class="form-control"
									placeholder="Titre" required autofocus value="<%=titre%>">
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-8">
						<h6>Abstract :</h6>
						<textarea id=abstr name="abstr" rows="10" cols="100"
							placeholder="Résumé..." maxlength="500" value=""><%=resume%></textarea>

						<br>
					</div>
				</div>

				<hr>
				<input type="hidden" name="op" />
				<div class=row>
					<div class="col-xs-4">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								<div class="btn-group btn-group-lg">
									<br> <input class="btn btn-success" type="button"
										value="Modifier"
										onclick="this.form.op.value='modifierT'; this.form.submit();" />
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								<div class="btn-group btn-group-lg">
									<br> <input class="btn btn-danger" type="button"
										value="Supprimer"
										onclick="this.form.op.value='supprimerT'; this.form.submit();" />

								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-4 ">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								<div class="btn-group btn-group-lg">
									<br> <input class="btn btn-default" type="button"
										value="Annuler"
										onclick="this.form.op.value='annulerT'; this.form.submit();" />
								</div>
							</div>
						</div>
					</div>

				</div>
			</form>
		</div>
	</div>

</body>
</html>