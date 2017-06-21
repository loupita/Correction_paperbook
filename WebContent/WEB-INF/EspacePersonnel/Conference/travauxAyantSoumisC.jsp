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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Mon Espace Personnel</title>
<!-- Bootstrap -->
<!-- --------------------CSS-------------------------------------------------- -->
<link href="css/app.css" rel="stylesheet">
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
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->


</head>
<body>
	<%
		Personne proprio = (Personne) session.getAttribute("Personne");
		Collection<Version> versions = (Collection<Version>) request
				.getAttribute("versions");
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
						data-toggle="dropdown"><i class="fa fa-user fa-fw"></i><%=proprio.getIdentifiant()%> <b
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
		<div class="list-group">
			<a class="list-group-item" href="Main?op=mur"><i
				class="fa fa-home fa-fw"></i>&nbsp; Publications mises en ligne</a> <a
				class="list-group-item" href="Main?op=espacePerso"><i
				class="fa fa-tasks fa-fw"></i>&nbsp; Travaux en cours</a> 
				<a class="list-group-item"
				href="Main?op=listeTravaux"><i class="fa fa-archive fa-fw"></i>&nbsp;
				Mes Travaux</a> 
				<a class="list-group-item" href="Main?op=mesConferences"><i
				class="fa fa-folder-open fa-fw"></i>&nbsp; Mes conférences</a>
				<a class="list-group-item active" href="Main?op=detailConfEspPerso"><i
				class="fa fa-pencil fa-fw"></i>&nbsp; Détail conférence</a> <a
				class="list-group-item" href="Main?op=mesJournaux"><i class="fa fa-folder-open fa-fw"></i>&nbsp;
				Mes journaux</a>
				<a class="list-group-item" href="Main?op=mesStatistiques"><i
				class="fa  fa-bar-chart-o fa-fw"></i>&nbsp; Mes statistiques</a>
				<a class="list-group-item" href="Main?op=rechercher"><i
				class="fa fa-calendar fa-fw"></i>&nbsp; Rechercher</a>
		</div>
	</div>

	<div class=container>

		<div class="col-md-9">
		<ul class="nav nav-tabs">
				<li><a href="Main?op=annulerConfEspPerso">Détail
						Conférence</a></li>
				<li><a href="Main?op=RmodifierConfEspPerso">Modifier/Supprimer</a></li>
				<li  class="active"><a href="Main?op=travauxAyantSoumisC">Travaux ayant soumis</a></li>
				<li><a href="Main?op=travauxCiblantsC">Travaux Ciblant</a></li>
			</ul>
			<h3>Liste des travaux ayant soumis à la conférence</h3>

			<%
				if (versions.isEmpty()) {
			%>
			<p>Vous n'avez aucun travail ayant soumis à cette conférence. <a href="Main?op=RajouterTravail">ajoutez un travail</a></p>
			<%
				} else {
			%>
			<div class="table-responsive">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>#</th>
							<th width=60%>Titre</th>
							<th width=40%>Résultat</th>
						</tr>
					</thead>
					<%
						int cpt = 1;
							for (Version v : versions) {
								Travail t = v.getTravail();
								String titre = t.getTitre();
								int id = t.getId();
					%>
					<tbody>
						<tr>
							<td><%=cpt%></td>
							<td><a id="myToolTip<%=cpt%>"
								href="Main?op=detailTravail
							&id=<%=id%>"
								title="oussama"><%=titre%></a></td>
								<td><%if(v.isAccepte()){ %>
									<p style="color:green;">Accéptée</p>
									<%} else if(v.isRefuse()) { %>
									<p style="color:red;">Refusée</p>
									<%} else { %>
									<p style="color:grey;">Pas de réponse</p>
									<%}%>
								</td>
						</tr>
					</tbody>
					<%
						cpt++;
							}
					%>
				</table>
			</div>
			<%
				}
			%>

		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.min.js"></script>
	<script src="js/skel.min.js"></script>
	<script src="js/init.js"></script>
</body>


</html>
