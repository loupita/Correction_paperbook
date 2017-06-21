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
<title>Détail travail</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Connexion à mon application">

<!-- --------------------CSS-------------------------------------------------- -->
<link rel="stylesheet" href="css/app.css"/>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link
	href="http://netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.css"
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
	<%
		Personne proprio = (Personne) session.getAttribute("Personne");
		Travail t = (Travail) session.getAttribute("travail");
		Collection<Journal> journaux = (Collection<Journal>) request
		.getAttribute("journauxCibles");
		Collection<Conference> confs = (Collection<Conference>) request
		.getAttribute("confsCibles");
		Collection<Journal> journauxSoumis = (Collection<Journal>) request
		.getAttribute("journauxSoumis");
		Collection<Conference> confsSoumis = (Collection<Conference>) request
		.getAttribute("confsSoumis");
		String titre = t.getTitre();
		Boolean soumis = t.getSoumis();
		String abstr = t.getAbstr();
		//lien vers ses fichiers
		String chemin = "/home/omarkad/test/stage/WebContent/fichiers/";
		String papierLastVersion = chemin + t.getNomFichier();
		String srcLastVersion = chemin + t.getNomSource();
		Collection<Version> versions =(Collection<Version>) request
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
			<li class="active"><a href="Main?op=annulerT">Details sur
					le travail</a></li>
			<li><a href="Main?op=RmodifierT">Modifier/Supprimer</a></li>
			<li><a href="Main?op=Rsoumettre">Soumettre à un événement</a></li>
			<li><a href="Main?op=Rcibler">Cibler un événement</a></li>
		</ul>
	</div>

	<div class=container>

		<div class="col-md-9">
			<h1>
				<p style="color: grey;">Travail :</p><%=titre%></h1>


			<br /> <br>
			<div class="panel panel-default">
				<div class="panel-heading">L'Abstract</div>
				<div class="panel-body">
					<%
						if (abstr.isEmpty()) {
					%>
					<p>
						Aucun résumé pour ce travail, veuillez le compléter en allant <a
							href="Main?op=RmodifierT">ici</a>
					</p>
					<%
						} else {
					%>
					<%=abstr%>
					<%
						}
					%>
				</div>
			</div>
			<br /> <br />
			<%if(soumis && !versions.isEmpty()){
				%>
				<fieldset>
			<p style="color: green;">
				<strong>Soumis</strong>
			</p>
			<%
					if (confsSoumis!=null && !confsSoumis.isEmpty()) {
				%>
				<p style="color:grey;">Conférences :</p>
			<div class="table-responsive">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>#</th>
							<th>Titre</th>
							<th>Rang</th>
							<th>Lieu</th>
							<th>Date(Notification)</th>
						</tr>
					</thead>
					<%
							int cpt = 1;
								for (Conference evt : confsSoumis) {
						%>
					<tbody>
						<tr>
							<td><%=cpt%></td>
							<td><a href="Main?op=detailConf&id=<%=evt.getId()%>"><%=evt.getTitre()%></a></td>
							<td><%=evt.getRang()%></td>
							<td><%=evt.getLieuEvt()%></td>
							<td><%=evt.getDateNotifAcceptance()%></td>
							<td><a href="Main?op=supprimerConfListe&idConf=<%=evt.getId()%>">
							<img src="img/cross.png" alt="supprimer"></a></td>
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

			<%
					if (journauxSoumis!=null && !journauxSoumis.isEmpty()) {
				%>
				<p style="color:grey;">Journaux :</p>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>#</th>
						<th>Titre</th>
						<th>Rang</th>
						<th>Date(Abstract)</th>
					</tr>
				</thead>
				<% 
						int cpt = 1;
							for (Journal evt : journauxSoumis) {
					%>
				<tbody>
					<tr>
						<td><%=cpt%></td>
						<td><a href="Main?op=detailJournal&id=<%=evt.getId()%>"><%=evt.getTitre()%></a></td>
						<td><%=evt.getRang()%></td>
						<td><%=evt.getDateAbstractSubmission()%></td>
						<td><a href="Main?op=supprimerJournalListe&idJournal=<%=evt.getId()%>">
							<img src="img/cross.png" alt="supprimer"></a></td>
					</tr>
				</tbody>
				<%
						cpt++;
							}
					}
					%>
			</table>
			<br />
			<div class="panel panel-info">
				<div class="panel-heading">Les versions du travail soumis (de
					la plus récente à la plus ancienne)</div>
				<div class="panel-body">
					<ul>
						<%
						for(Version v : versions) { 
							String nom;
							if(v.getConference()!=null){
								nom = v.getConference().getAcronym();
							}
							else {
								nom = v.getJournal().getTitre();
							}
						%>
						
						<li><a href="Main?op=detailVersion&id=<%=v.getId()%>">version</a>
							de : <%=nom%>
							<p class="text-right">
								<i class="fa fa-file-pdf-o"></i><a
									href="Main?op=fichier&
							nomFichier=<%=v.getVersionFichier()%>">papier</a>
								<i class="fa fa-file-archive-o"></i><a
									href="Main?op=fichier&
							nomFichier=<%=v.getVersionArchive()%>">sources</a>
							</p></li>
						<% }%>
					</ul>
				</div>
			</div>
			</fieldset>
			<%
			}else {
				%><p style="color: red;">
				<strong>Non Soumis</strong>
			</p>
			<%} %>

			<br />
			<h4>Liste des conférences ciblées</h4>
			<br>
			<%
					if (confs.isEmpty()) {
				%>
			<p>Vous ne ciblez aucune conférence avec ce travail.</p>
			<%
					} else {
				%>
			<div class="table-responsive">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>#</th>
							<th>Titre</th>
							<th>Rang</th>
							<th>Lieu</th>
							<th>Date(Abstract)</th>
						</tr>
					</thead>
					<%
							int cpt = 1;
								for (Conference evt : confs) {
						%>
					<tbody>
						<tr>
							<td><%=cpt%></td>
							<td width=50%><a href="Main?op=detailConf&id=<%=evt.getId()%>"><%=evt.getTitre()%></a></td>
							<td><%=evt.getRang()%></td>
							<td><%=evt.getLieuEvt()%></td>
							<td><%=evt.getDateAbstractSubmission()%></td>
							<td><a href="Main?op=supprimerConfListeCibles&idConf=<%=evt.getId()%>">
							<img src="img/cross.png" alt="supprimer"></a></td>
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

			<h4>Liste des journaux ciblés</h4>

			<%
					if (journaux.isEmpty()) {
				%>
			<br>
			<p>Vous ne ciblez aucun journal avec ce travail.</p>
			<%
					} else {
				%>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>#</th>
						<th>Titre</th>
						<th>Rang</th>
						<th>Date(Abstract)</th>
					</tr>
				</thead>
				<%
						int cpt = 1;
							for (Journal evt : journaux) {
					%>
				<tbody>
					<tr>
						<td><%=cpt%></td>
						<td width=50%><a href="Main?op=detailJournal&id=<%=evt.getId()%>"><%=evt.getTitre()%></a></td>
						<td><%=evt.getRang()%></td>
						<td><%=evt.getDateAbstractSubmission()%></td>
						<td><a href="Main?op=supprimerJournalListeCibles&idJournal=<%=evt.getId()%>">
							<img src="img/cross.png" alt="supprimer"></a></td>
					</tr>
				</tbody>
				<%
						cpt++;
							}
					%>
			</table>
			<%
					}
				%>
			<br> <br>

		</div>
	</div>
</body>
</html>