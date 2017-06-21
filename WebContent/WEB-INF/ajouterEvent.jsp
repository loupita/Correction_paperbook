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
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Enregistrer un événement</title>
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
		String isConf = (String) request.getAttribute("isConf");
		boolean b =(isConf == null ||isConf.equals("cf"));
			
	%>
	<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/mondial" user="root"
		password="toto" />

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
	<div class="container">
		<div class="main">
			<form action="Main" name="sign up" role="form"
				class="form-horizontal" method="post" accept-charset="utf-8"
				enctype="multipart/form-data">
				<input type="hidden" name="op" value="ajouterEvt" />
				<div class="row">


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
					<div class="col-xs-4">

						<h3>Informations sur l'événement</h3>
						<div class="form-group">
							<div class="col-md-8">
								<br> <br> 
								<h6>Titre :</h6>
								<label for="titre" class="sr-only">Titre</label> <input
									type="text" id="titre" name="titre" class="form-control"
									placeholder="Titre" required autofocus value="">
							</div>
						</div>
						<div class="form-group" id="acr">
							<div class="col-md-8">
								<br> 
								<h6>Acronym :</h6>
								<label for="titre" class="sr-only">Acronym</label> <input
									type="text" id="acr" name="acr" class="form-control"
									placeholder="Acronym" autofocus value="">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<br> <br>
								<h6>Site de l'événement :</h6>
								<label for="site" class="sr-only">Site</label> <input type="url"
									id="site" name="site" class="form-control"
									placeholder="Site de l'événement" autofocus value="">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Rang :</h6>
								<label for="rang" class="sr-only">Rang</label> <select
									class="selectpicker" name=rang>
									<option>A*</option>
									<option>A+</option>
									<option>A</option>
									<option>A-</option>
									<option>B+</option>
									<option>B</option>
									<option>B-</option>
									<option>C+</option>
									<option>C</option>
									<option>C-</option>
									<option>Not ranked</option>
								</select>
							</div>
						</div>
						<div class="form-group" id="lieu">
							<div class="col-md-8">
								<br> 
								<h6>Lieu événement :</h6>
								<label for="pays" class="sr-only">Lieu événement</label> <input
									type="text" id="pays" name="pays" class="form-control"
									placeholder="Lieu événement" autofocus value="">
									<p class="help-block">Pays-Ville</p>
							</div>
						</div>
						
					</div>

					<!-------------------------------------- 2eme COLONNE --------------------------------------------------->
					<div class="col-xs-4 ">
						<br> <br> <br> <br> 
						<div class="form-group" id="dateEvt">
							<div class="col-md-8">
								<br>
								<h6>
									Date de l'événement :
								</h6>

								<label for="date1" class="sr-only">date de l'événement</label> <input
									type="text" id="date1" name="date1" class="form-control"
									placeholder="date de l'événement"  autofocus value="">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Date soumission résumé :</h6>
								<label for="date2" class="sr-only">date soumission
									résumé</label> <input type="text" id="date2" name="date2"
									class="form-control" placeholder="date soumission résumé"
									required autofocus value="">
									<p class="help-block">Sous format : dd/mm/yyyy.</p>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<h6>Date soumission publication :</h6>
								<label for="date3" class="sr-only">date soumission
									publication</label> <input type="text" id="date3" name="date3"
									class="form-control" placeholder="date soumission publication"
									required autofocus value="">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Date rebuttal(facultatif) :</h6>
								<label for="date3" class="sr-only">Date rebuttal</label> <input
									type="text" id="dateR" name="dateR" class="form-control"
									placeholder="date rebuttal" autofocus value="">
							</div>
						</div>
						<div class="form-group" id="dateNotif">
							<div class="col-md-8">
								<br>
								<h6>Date notification acceptance :</h6>
								<label for="date4" class="sr-only">date notification
									acceptance</label> <input type="text" id="date4" name="date4"
									class="form-control" placeholder="date notification acceptance"
									 autofocus value="">
							</div>
						</div>
					</div>
					<!-------------------------------------- 3eme COLONNE --------------------------------------------------->

					<div class="col-xs-4 ">
						<br> <br> <br> <br> <br>
						<h6>Type de l'événement :</h6>
						<div class="form-group">
							<div class="col-md-8">
								<div class="radio">
									<input id="rs" type="radio" name="conf" value="cf"
										<%if(b){ %>checked="checked"<%} %> onclick="typeChanged('cf');" /> <label
										for="rs">Conférence</label>
								</div>
								<div class="radio">
									<input id="rk" type="radio" name="conf" value="k"
										<%if(!b){ %>checked="checked"<%} %> onclick="typeChanged('k');" /> <label for="rk">Journal</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Information sur la publication :</h6>
								<textarea id=info name="info" rows="8" cols="40"
									placeholder="saisir la description ici..." maxlength="200"
									value=""></textarea>
								<p class="help-block">200 caractères max.</p>

							</div>
						</div>
						<br> 
						<div class="form-group">
							<div class="col-md-8">
								<label for="InputFile">Styles, police .... </label> <input
									type="file" name="source" id="exampleInputFile">
							</div>
						</div>
					</div>
				</div>



				<div class="form-group">
					<div class="col-md-offset-0 col-md-8">
						<hr>

						<p></p>
						<br> <input class="btn btn-success" type="submit"
							value="Ajouter événement" />
					</div>
				</div>
				<p></p>
			</form>
		</div>
	</div>

	<script type="text/javascript">
		$('document').ready(function() {

			$('#rk').on('click', function(ev) {
				if (this.checked) {
					$('#acr').hide();
					$('#lieu').hide();
					$('#dateEvt').hide();
					$('#dateNotif').hide();
					console.log('the checkbox1');
				}
			});

			$('#rs').on('click', function(ev) {
				if (this.checked) {
					$('#acr').show();
					$('#lieu').show();
					$('#dateEvt').show();
					$('#dateNotif').show();
					console.log('the checkbox1');
				}
			});
		});
	</script>
</body>
</html>