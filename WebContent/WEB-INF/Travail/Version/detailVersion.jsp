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
<title>Détail version</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Connexion à mon application">

<!-- --------------------CSS-------------------------------------------------- -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link
	href="http://netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.css"
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
		Version version = (Version) session.getAttribute("version");
		Journal j = version.getJournal();
		Conference c = version.getConference();
		String fichier = version.getVersionFichier();
		String sources = version.getVersionArchive();
		String date = version.getDate().toLocaleString();
		Travail t = version.getTravail();
		Collection<Reponse> reponses = (Collection<Reponse>) session
				.getAttribute("reponses");
		boolean accepte = (version.isAccepte());
		boolean refuse = (version.isRefuse());
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

		<div class="col-md-9">
			<ul class="nav nav-tabs">
				<li class="active"><a
					href="Main?op=detailVersion&id=<%=version.getId()%>"> Détail
						Version</a></li>
				<li><a href="Main?op=reponses">Réviewers/Résultats</a></li>
				<li><a href="Main?op=RajouterReponse">Ajouter résultat</a></li>
				<%if(isConf && accepte) { %>
				<li><a href="Main?op=RchoisirPresentateur">Choisir
				présentateur</a></li>
				<%}%>
			</ul>
			<h1>
				<p style="color: grey;">Version :</p>
				<%=t.getTitre()%> <%if(accepte){ %> 
					<p style="color:green;">Accéptée</p>
					<%} else if(refuse){ %>
					<p style="color:red;">Refusée</p>
					<%} else { %>
					<p style="color:grey;">(Pas de réponse pour l'instant)</p>
					<%}%></h1>
			<h2>
				<p style="color: grey;">
					Postée le :
					<%=date%>
				</p>
			</h2>
			<br> <br> <br /> <br>
			
			<%
				if (c != null) {
			%>
			<h4>
				<p style="color: grey;">Conférence :</p>
			</h4>
			<div class="table-responsive">
				<table class="table table-bordered">
					<tbody>
						<tr>
							<th scope="row">Titre</th>
							<td><%=c.getTitre()%></td>
						</tr>
						<tr>
							<th scope="row">Acronym</th>
							<td><%=c.getAcronym()%></td>
						</tr>
						<tr>
							<th scope="row">Rang</th>
							<td><%=c.getRang()%></td>
						</tr>
						<tr>
							<th scope="row">Site</th>
							<td><a href="<%=c.getSite()%>"><%=c.getSite()%></a></td>
						</tr>
						<tr>
							<th scope="row">Lieu de la conférence</th>
							<td><%=c.getLieuEvt()%></td>
						</tr>
						<tr>
							<th scope="row">Date de la conférence</th>
							<td><%=c.getDateEvt()%></td>
						</tr>
						<tr>
							<th scope="row">Date soumission de l'Abstract</th>
							<td><%=c.getDateAbstractSubmission()%></td>
						</tr>
						<tr>
							<th scope="row">Date soumission Papier</th>
							<td><%=c.getDatePaperSubmission()%></td>
						</tr>
						<%
							if (c.getDateRebuttal() != null
										&& !c.getDateRebuttal().isEmpty()) {
						%>
						<tr>
							<th scope="row">Date Rebuttal</th>
							<td><%=c.getDateRebuttal()%></td>
						</tr>
						<%
							}
						%>
						<tr>
							<th scope="row">Date Notification acceptance</th>
							<td><%=c.getDateNotifAcceptance()%></td>
						</tr>
						<%if(isConf && accepte) { 
						String nom = version.getNomPresentateur();
						String prenom = version.getPrenomPresentateur();%>
						<tr>
							<th scope="row">Présentateur</th>
							<td><%if(nom==null || prenom==null) {%>
							 Présentateur pas encore choisie <a href="Main?op=RchoisirPresentateur">Choisir</a>
							 <%} else { %>
							 <%=nom%> <%=prenom%>
							 <%}%>
							 </td>
						</tr>
						<%} %>
					</tbody>
				</table>
			</div>
			<%
				} else {
			%>
			<h4>
				<p style="color: grey;">Journal :</p>
			</h4>
			<div class="table-responsive">
				<table class="table table-bordered">
					<tbody>
						<tr>
							<th scope="row">Titre</th>
							<td><%=j.getTitre()%></td>
						</tr>
						<tr>
							<th scope="row">Rang</th>
							<td><%=j.getRang()%></td>
						</tr>
						<tr>
							<th scope="row">Site</th>
							<td><a href="<%=j.getSite()%>"><%=j.getSite()%></a></td>
						</tr>
						<tr>
							<th scope="row">Date soumission de l'Abstract</th>
							<td><%=j.getDateAbstractSubmission()%></td>
						</tr>
						<tr>
							<th scope="row">Date soumission Papier</th>
							<td><%=j.getDatePaperSubmission()%></td>
						</tr>
						<%
							if (j.getDateRebuttal() != null
										&& !j.getDateRebuttal().isEmpty()) {
						%>
						<tr>
							<th scope="row">Date Rebuttal</th>
							<td><%=j.getDateRebuttal()%></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>
			<%
				}
			%>

			<br> <br> <br>
			<p class="text-left">
				<i class="fa fa-file-pdf-o fa-2x"></i><a
					href="Main?op=fichier&
							nomFichier=<%=fichier%>"> papier</a>
				<i class="fa fa-file-archive-o fa-2x"></i><a
					href="Main?op=fichier&
							nomFichier=<%=sources%>"> sources</a>
			</p>
			<hr>
			<form Action=Main>
				<input type="hidden" name="op" /> <br>
				<div class=row>
					<div class="col-xs-4">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								<div class="btn-group btn-group-lg">
									<br> <input class="btn btn-primary" type="button"
										value="Rendre Publique" id="rendrePublique"
										<%if (reponses == null || reponses.isEmpty() || version.isPublique()) {%>
										disabled <%}%>
										onclick="this.form.op.value='rendrePublique'; this.form.submit();" />
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								<div class="btn-group btn-group-lg">
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-4 ">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								<div class="btn-group btn-group-lg">
									<br>
									<%
										if (version.isPublique()) {
												%>
									<input class="btn btn-danger" type="button"
										value="Ne plus Rendre Publique" id="rendrePublique"
										onclick="this.form.op.value='NePlusRendrePublique'; this.form.submit();" />
									<%}	%>
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