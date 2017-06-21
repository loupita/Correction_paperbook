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
<title>Mes journaux</title>
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
			Collection<Journal> mesJournaux = (Collection<Journal>) request
			.getAttribute("mesJournaux");
		int nbPages = (Integer) request.getAttribute("nbPagesJ");
		int p  = (Integer) request.getAttribute("page");
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
				class="list-group-item" href="Main?op=espacePerso"><i
				class="fa fa-tasks fa-fw"></i>&nbsp; Travaux en cours</a> <a
				class="list-group-item" href="Main?op=listeTravaux"><i
				class="fa fa-archive fa-fw"></i>&nbsp; Mes Travaux</a> <a
				class="list-group-item" href="Main?op=mesConferences"><i
				class="fa fa-folder-open fa-fw"></i>&nbsp; Mes conférences</a> <a
				class="list-group-item active" href="Main?op=mesJournaux"><i
				class="fa fa-folder-open fa-fw"></i>&nbsp; Mes journaux</a> <a
				class="list-group-item" href="Main?op=mesStatistiques"><i
				class="fa  fa-bar-chart-o fa-fw"></i>&nbsp; Mes statistiques</a> <a
				class="list-group-item" href="Main?op=rechercher"><i
				class="fa fa-calendar fa-fw"></i>&nbsp; Rechercher</a>
		</div>
	</div>


	<div class=container>
		<div class="col-md-9">
			<form action="Main" name="sign up" class="form-horizontal"
				method="post" accept-charset="utf-8" enctype="multipart/form-data">
				<input type="hidden" name="op" />
				<h1>Liste journaux</h1>
				<br>
				<%
					if (mesJournaux.isEmpty()) {
				%>
				<p>
					Vous n'avez enregistré aucun journal <a href="Main?op=RajouterEvt">ajoutez
						un journal.</a>
				</p>
				<%
					} else {
				%>
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>#</th>
								<th>Titre</th>
								<th>Rang</th>
								<th>Date de l'abstract</th>
								<!-- 							<th>cas actuel</th> -->
							</tr>
						</thead>
						<%
						int cpt = (p-1)*10+1;
						for (Journal evt : mesJournaux) {
						%>
						<tbody>
							<tr>
								<td><%=cpt%></td>
								<td><a
									href="Main?op=detailJournalEspPerso&id=<%=evt.getId()%>"><%=evt.getTitre()%></td>
								<td><%=evt.getRang()%></td>
								<td><%=evt.getDateAbstractSubmission()%></td>
								<td><INPUT type="checkbox" class="choix" name="journauxCheck"
									value="<%=evt.getId()%>"></td>
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
					if (!mesJournaux.isEmpty()) {
				%>
				<center>
					<ul id="pagination-demo" class="pagination-sm"></ul>
				</center>
				
				<div class="form-group">
					<div class="col-md-offset-0 col-md-8">
						<br> <input class="btn btn-danger btn-sm" type="button"
							value="Supprimer"
							onclick="this.form.op.value='supprimerJournaux'; this.form.submit();" />
						<input type='checkbox' id='cocheTout' /> <span id='cocheText'>Cocher
							tout</span>
					</div>
				</div>
				<%} %>
				<hr>

				<div class=row>
					<div class="col-xs-4">
						<div class="form-group">
							<div class="col-md-offset-0 col-md-8">
								 <label for="Exporter">Exporter</label> <br> <input class="btn btn-primary btn-sm"
									type="button" value="Exporter"
									onclick="this.form.op.value='exporterJournaux'; this.form.submit();" />
							</div>
						</div>
					</div>
					<div class="col-xs-4 ">
						<div class="form-group">
							<div class="col-md-8">
								<label for="InputFile">Importer</label> <input type="file"
									name="feuille" id="exampleInputFile">
								<div class="col-md-8">
									<input class="btn btn-primary btn-sm" type="button"
										value="Importer"
										onclick="this.form.op.value='importerJournaux'; this.form.submit();" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>

		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.twbsPagination.min.js"></script>
	<script src="js/jquery.min.js"></script>
	<script src="js/skel.min.js"></script>
	<script src="js/init.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var cases = $('.choix');
			$('#cocheTout').on('click', function(ev) { // clic sur la case cocher/decocher
				if (this.checked) { // si 'cocheTout' est coché
					cases.prop('checked', true); // on coche les cases
					$('#cocheText').html('Tout decocher'); // mise à jour du texte de cocheText
				} else { // si on décoche 'cocheTout'
					cases.prop('checked', false);// on coche les cases
					$('#cocheText').html('Cocher tout');// mise à jour du texte de cocheText
				}

			});

		});
	</script>
	<script type="text/javascript">
		$('#pagination-demo').twbsPagination({
			totalPages : <%=nbPages%> ,
			visiblePages : 10,
			href : 'Main?op=mesJournauxPages&page={{number}}',
			onPageClick : function(event, page) {
				$('#page-content').text('Page ' + page);
			}
		});
	</script>
</body>


</html>
