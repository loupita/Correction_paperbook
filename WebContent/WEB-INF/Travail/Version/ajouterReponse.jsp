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
<title>Ajouter réponse d'un reviewer</title>
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
			Collection<Reponse> reponses = (Collection<Reponse>) session
					.getAttribute("reponses");
			boolean b = (version.isAccepte());
			boolean isConf = false;
			if (version.getConference() != null)
				isConf = true;
			
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
			<li class="active"><a
				href="Main?op=detailVersion&id=<%=version.getId()%>">Details sur
					la version</a></li>
			<li><a href="Main?op=RmodifierT">Modifier/Supprimer</a></li>
			<li><a href="Main?op=Rsoumettre">Soumettre à un événement</a></li>
			<li><a href="Main?op=Rcibler">Cibler un événement</a></li>
		</ul>
	</div>


	<div class=container>

		<div class="row col-sm-10">
			<ul class="nav nav-tabs">
				<li><a href="Main?op=detailVersion&id=<%=version.getId()%>">
						Détail Version</a></li>
				<li><a href="Main?op=reponses">Réviewers/Résultats</a></li>
				<li class="active"><a href="Main?op=RajouterReponse">Ajouter
						résultat</a></li>
				<%if(isConf && b) { %>
				<li><a href="Main?op=RchoisirPresentateur">Choisir
				présentateur</a></li>
				<%}%>
			</ul>
			<%
				if (request.getAttribute("erreurs") != null) {
						Collection<String> erreurs = (Collection<String>) request
								.getAttribute("erreurs");
						if (!erreurs.isEmpty()) {
			%>
			<br><br>
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
				<input type="hidden" name="op" value="ajouterReponse" />
				<div class=row>
					<div class="col-xs-8">
						<h3>Résultat :</h3>
						<div class="form-group">
							<div class="col-md-8">
								<div class="radio">
									<input id="rs" type="radio" name="resultat" value="oui"
										<%if(b){%> checked="checked" <%}%>
										onclick="typeChanged('cf');" /> <label for="rs">Accéptée</label>
								</div>
								<div class="radio">
									<input id="rk" type="radio" name="resultat" value="non"
										<%if(!b){%> checked="checked" <%}%>
										onclick="typeChanged('k');" /> <label for="rk">Refusée</label>
								</div>
							</div>
						</div>
						
						<hr>
						<h3>Ajouter une réponse d'un reviewer :</h3>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Reviewer :</h6>
								<label for="reviewer" class="sr-only">Reviewer</label> <input
									type="text" id="reviewer" name="reviewer" class="form-control"
									placeholder="Reviewer" required autofocus value="">
							</div>
						</div>

						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Jugement :</h6>
								<label for="jugement" class="sr-only">Titre</label> <input
									type="text" id="jugement" name="jugement" class="form-control"
									placeholder="Jugement" required autofocus value="">
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-8">
						<h6>Commentaire reviewer :</h6>
						<textarea id=detail name="detail" rows="10" cols="100"
							placeholder="Commentaire reviewer..." required maxlength="1000"
							value=""></textarea>

						<br>
					</div>
				</div>

				<hr>
				<div class="form-group">
					<div class="col-md-offset-0 col-md-8">
						<br> <input class="btn btn-success" type="submit"
							value="Ajouter réponse" />
					</div>
				</div>
			</form>
		</div>
	</div>


 	<script type="text/javascript"> 
// 		$('document').ready(function() {
<%-- 	<%if(b){%> --%>
// 		$('#presentateur').show();
<%-- 	<%} else {%> --%>
// 		$('#presentateur').hide();
<%-- 	<%}%> --%>
// 		$('#rk').on('click', function(ev) {
// 				if (this.checked) {
// 					$('#presentateur').hide();
// 					console.log('the checkbox1');
// 				}
// 			});

// 			$('#rs').on('click', function(ev) {
// 				if (this.checked) {
// 					$('#presentateur').show();
// 					console.log('the checkbox1');
// 				}
// 			});
// 		});
	</script>
</body>
</html>