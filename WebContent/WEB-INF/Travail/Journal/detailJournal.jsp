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
<title>Détail journal</title>
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
	<%
		Personne proprio = (Personne) session.getAttribute("Personne");
		Journal evt = (Journal) session.getAttribute("journal");
		String site = evt.getSite();
		String titre = evt.getTitre();

		String dateAbstract = evt.getDateAbstractSubmission();
		String datePaper = evt.getDatePaperSubmission();
		String dateRebuttal = evt.getDateRebuttal();
		String info = evt.getInfo();
		String rang = evt.getRang();
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
			<li class="active"><a href="Main?op=annulerJournal">Détails
					journal</li>
			<li><a href="Main?op=RmodifierT">Modifier/Supprimer</a></li>
			<li><a href="Main?op=Rsoumettre">Soumettre à un événement</a></li>
			<li><a href="Main?op=Rcibler">Cibler un événement</a></li>
		</ul>
	</div>

	<div class=container>

		<div class="col-md-9">
		<ul class="nav nav-tabs">
				<li class="active"><a href="Main?op=annulerJournal">Détail
						Journal</a></li>
				<li><a href="Main?op=RmodifierJournal">Modifier/Supprimer</a></li>
			</ul>
			<h1>
				<p style="color: grey;">Journal : <%=rang%></p>
				<%=titre%>
				<small><a align=center href="<%=site%>"> <br> lien
						vers site.
				</a></small>
			</h1>
			<br /> <br>
			<br /> <br /> <br /> <br />
			<h4>Dates importantes :</h4>
			<ul>
				<li>Le résumé devra être envoyé avant le : <mark> <%=dateAbstract%></mark></li>
				<li>Le papier doit être soumis avant le : <mark><%=datePaper%></mark></li>
				<%if(dateRebuttal!=null && !dateRebuttal.equals("")){ %>
				<li>La date rebuttal est le : <mark><%=dateRebuttal%></mark></li>
				<%} %>
			</ul>
			<br> <br>
			<div class="panel panel-info">
				<div class="panel-heading">La publication doit être conforme à
					ce qui a était demandé par les organisateurs :</div>
				<div class="panel-body">
					<%
						if (info.isEmpty()) {
					%>
					<p>Aucun style ou modele particulier demandé.</p>
					<%
						} else {
					%>
					<%=info%>
					<%
						}
					%>
					<%if(evt.getNomFichierStyle()!=null){ %>
					<a href="Main?op=fichierStyleJournal">fichier</a>
					<%}%>
				</div>
			</div>
			<br />
		</div>
	</div>
</body>
</html>