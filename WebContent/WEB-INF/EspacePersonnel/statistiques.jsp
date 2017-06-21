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
<title>Mes statistiques</title>
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
				class="fa fa-home fa-fw"></i>&nbsp; Publications mises en ligne</a> 
				<a
				class="list-group-item" href="Main?op=espacePerso"><i
				class="fa fa-tasks fa-fw"></i>&nbsp; Travaux en cours</a> 
				<a class="list-group-item"
				href="Main?op=listeTravaux"><i class="fa fa-archive fa-fw"></i>&nbsp;
				Mes Travaux</a> 
				<a class="list-group-item" href="Main?op=mesConferences"><i
				class="fa fa-folder-open fa-fw"></i>&nbsp; Mes conférences</a>
				 <a
				class="list-group-item" href="Main?op=mesJournaux"><i class="fa fa-folder-open fa-fw"></i>&nbsp;
				Mes journaux</a>
				<a class="list-group-item active" href="Main?op=mesStatistiques"><i
				class="fa  fa-bar-chart-o fa-fw"></i>&nbsp; Mes statistiques</a>
				<a class="list-group-item" href="Main?op=rechercher"><i
				class="fa fa-calendar fa-fw"></i>&nbsp; Rechercher</a>
		</div>
	</div>

	<!-- Main -->
	<div id="main" class=container>
		<div class="col-md-10">
			<!-- recherche -->
			<h2>Chercher les statistiques pour un(e) Conférence/Journal</h2>
			<br> 
			<form method="post" action="Main">
				<input type="hidden" name="op" value="rechercherStats">
				<div class="row">
				
					<div class="col-xs-6 offset2">
						<div class="form-group">
							<div class="col-md-8">
							<h6>Saisir critère :</h6>
								<input type="text" id="Rechercher" name="rechercher"
									class="form-control" placeholder="Rechercher" required=""
									autofocus="" style="width:420px;">
							</div>
						</div>
					</div>
					<div class="col-xs-2 offset2">
								<h6>Chercher par :</h6>
								<select class="selectpicker" id="type" name="type">
									<option value="1">Titre</option>
									<option value="0">Acronym</option>
									<option>Rang</option>
								</select>
					</div>
					<div class="col-xs-2 offset2">
						<h6>Conférence/Journal :</h6>
						<select class="selectpicker" id="isConf" name="isConf">
							<option>Conference</option>
							<option>Journal</option>
						</select>
					</div>

				</div>
				<hr>
				<div class="form-group">
					<div class="col-md-offset-0 col-md-8">
						<input class="btn btn-primary" type="submit"
							value="Rechercher" />
					</div>
				</div>
			</form>
		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.min.js"></script>
	<script src="js/skel.min.js"></script>
	<script src="js/init.js"></script>
	
	<script type="text/javascript">
		$('document').ready(function() {
			$('#isConf').on('change', function(ev) {
				var nom = $('#isConf option:selected').text();
					if (nom!="Conference") {
						$('#type option[value=0]').hide();
						$('#type option[value=1]').prop('selected',true);
					} else {
						$('#type option[value=0]').show();
					}
			});
		});
	</script>
</body>


</html>
