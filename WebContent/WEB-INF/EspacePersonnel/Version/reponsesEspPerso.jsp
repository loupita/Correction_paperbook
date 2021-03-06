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
<title>Réviewers/Résultats de la publication</title>
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
		password="root" />
	<%
		Personne proprio = (Personne) session.getAttribute("Personne");
		Version version = (Version) session.getAttribute("version");
		Collection<Reponse> reponses = (Collection<Reponse>) request
				.getAttribute("reponses");
		boolean accepte = version.isAccepte();
		boolean refuse = version.isRefuse();
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
						data-toggle="dropdown"><i class="fa fa-user fa-fw"></i><%=proprio.getIdentifiant()%>
							<b class="caret"></b></a>
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
		<div class="list-group">
			<a class="list-group-item" href="Main?op=mur"><i
				class="fa fa-home fa-fw"></i>&nbsp; Publications mises en ligne</a> <a
				class="list-group-item active"
				href="Main?op=detailsVersionEspPerso&idV=<%=version.getId()%>"><i
				class="fa fa-pencil fa-fw"></i>&nbsp;Détails publication</a> <a
				class="list-group-item" href="Main?op=espacePerso"><i
				class="fa fa-tasks fa-fw"></i>&nbsp; Travaux en cours</a> <a
				class="list-group-item" href="Main?op=listeTravaux"><i
				class="fa fa-archive fa-fw"></i>&nbsp; Mes Travaux</a> <a
				class="list-group-item" href="Main?op=mesConferences"><i
				class="fa fa-folder-open fa-fw"></i>&nbsp; Mes conférences</a> <a
				class="list-group-item" href="Main?op=mesJournaux"><i
				class="fa fa-folder-open fa-fw"></i>&nbsp; Mes journaux</a> <a
				class="list-group-item" href="Main?op=mesStatistiques"><i
				class="fa  fa-bar-chart-o fa-fw"></i>&nbsp; Mes statistiques</a> <a
				class="list-group-item" href="Main?op=rechercher"><i
				class="fa fa-calendar fa-fw"></i>&nbsp; Rechercher</a>
		</div>
	</div>
	<div class=container>

		<div class="row col-sm-10">
			<ul class="nav nav-tabs">
				<li><a
					href="Main?op=detailsVersionEspPerso&idV=<%=version.getId()%>">
						Détail Version</a></li>
				<li class="active"><a href="Main?op=reponsesEspPerso">voir
						réponses des Reviewers</a></li>
			</ul>
			<div class=row>
				<div class="col-xs-8">
					<br>
					<h3>
						Reviewer/Réponses :
						<%
						if (accepte) {
					%>
						<p style="color: green;">Accéptée</p>
						<%
							} else if (refuse) {
						%>
						<p style="color: red;">Refusée</p>
						<%
							} else {
						%>
						<p style="color: grey;">Pas de réponse encore</p>

						<%
							}
						%>
					</h3>
					<%
						for (Reponse r : reponses) {
					%>
					<p>
						Reviewer :
						<%=r.getReviewer()%></p>
					<p>
						Jugement :
						<%=r.getJugement()%></p>
					<p>
						Commentaire reviewer :
						<%=r.getDetailReponse()%></p>
					<hr>
					<br>
					<%
						}
					%>
					<br>

				</div>
			</div>
		</div>
	</div>
</body>
</html>