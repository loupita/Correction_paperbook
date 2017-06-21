<%@page import="javax.swing.JSpinner.DateEditor"%>
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
<title>Modification des informations</title>
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
		String rang = evt.getRang(); 
		String dateAbstract = evt.getDateAbstractSubmission();
		String datePaper = evt.getDatePaperSubmission();
		String dateRebuttal = evt.getDateRebuttal();
		String info = evt.getInfo();
		String fichier1 = "file:///home/omarkad/test/stage/WebContent/styles/"+proprio.getId()+"_"+evt.getId()+
				"_Journal.zip";
		String fichier = "file:///home/omarkad/test/stage/WebContent/styles/1_22_Journal.zip";
	%>
	<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/mondial" user="root"
		password="root" />

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
				<div class="row">
					<ul class="nav nav-tabs">
						<li><a href="Main?op=annulerJournalEspPerso">Détail Journal</a></li>
						<li class="active"><a href="Main?op=RmodifierJournalEspPerso">Modifier/Supprimer</a></li>
						<li><a href="Main?op=travauxAyantSoumisJ">Travaux ayant soumis</a></li>
						<li><a href="Main?op=travauxCiblantsJ">Travaux Ciblant</a></li>
					</ul>

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
									placeholder="Titre" required autofocus value="<%=titre%>">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Site de l'événement :</h6>
								<label for="site" class="sr-only">Site</label> <input type="url"
									id="site" name="site" class="form-control"
									placeholder="Site de l'événement" autofocus value="<%=site%>">
							</div>
						</div>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Rang :</h6>
								<label for="rang" class="sr-only">Rang</label> <select
									class="selectpicker" name=rang>
									<option <%if (rang.equals("A*")) {%> selected="selected" <%}%>>A*</option>
									<option <%if (rang.equals("A+")) {%> selected="selected" <%}%>>A+</option>
									<option <%if (rang.equals("A")) {%> selected="selected" <%}%>>A</option>
									<option <%if (rang.equals("A-")) {%> selected="selected" <%}%>>A-</option>
									<option <%if (rang.equals("B+")) {%> selected="selected" <%}%>>B+</option>
									<option <%if (rang.equals("B")) {%> selected="selected" <%}%>>B</option>
									<option <%if (rang.equals("B-")) {%> selected="selected" <%}%>>B-</option>
									<option <%if (rang.equals("C+")) {%> selected="selected" <%}%>>C+</option>
									<option <%if (rang.equals("C")) {%> selected="selected" <%}%>>C</option>
									<option <%if (rang.equals("C-")) {%> selected="selected" <%}%>>C-</option>
									<option <%if (rang.equals("Not ranked")) {%> selected="selected" <%}%>>Not ranked</option>
								</select>
							</div>
						</div>
						<p></p>

					</div>

					<p></p>
					<!-------------------------------------- 2eme COLONNE --------------------------------------------------->
					<div class="col-xs-4 ">
						<br> <br> <br>
						<p></p>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Date soumission résumé :</h6>
								<label for="date2" class="sr-only">date soumission
									résumé</label> <input type="text" id="date2" name="date2"
									class="form-control" placeholder="date soumission résumé"
									required autofocus value="<%=dateAbstract%>">
								<p class="help-block">Sous format : dd/mm/yyyy.</p>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Date soumission publication :</h6>
								<label for="date3" class="sr-only">date soumission
									publication</label> <input type="text" id="date3" name="date3"
									class="form-control" placeholder="date soumission publication"
									required autofocus value="<%=datePaper%>">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Date rebuttal(facultatif) :</h6>
								<label for="date3" class="sr-only">Date rebuttal</label> <input
									type="text" id="dateR" name="dateR" class="form-control"
									placeholder="date rebuttal" autofocus
									value="<%if (dateRebuttal != null) {%>
									<%=dateRebuttal%>
									<%}%>">
							</div>
						</div>
					</div>
					<!-------------------------------------- 3eme COLONNE --------------------------------------------------->

					<div class="col-xs-4 ">
						<br> <br> <br> <br>
						<div class="form-group">
							<div class="col-md-8">
								<br>
								<h6>Information sur la publication :</h6>
								<textarea id=info name="info" rows="8" cols="40"
									placeholder="saisir la description ici..." maxlength="200"
									value="<%=info%>"></textarea>
								<p class="help-block">200 caractères max.</p>

							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8">
								<label for="InputFile">Styles, police .... </label> <input
									type="file" name="source" id="exampleInputFile">
							</div>
						</div>
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
										onclick="this.form.op.value='modifierJournalEspPerso'; this.form.submit();" />
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
										onclick="this.form.op.value='supprimerJournalEspPerso';this.form.submit();" />
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
										onclick="this.form.op.value='annulerJournalEspPerso';this.form.submit();" />
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