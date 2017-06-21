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
<title>Ajouter travail</title>
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
		password="toto" />
	<%
		Personne proprio = (Personne) session.getAttribute("Personne");
			Collection<Conference> confsP = (Collection<Conference>) 
					request.getAttribute("confsPotentiel");
			Collection<Journal> journauxP = (Collection<Journal>) request.getAttribute("journauxPotentiel");
			Collection<Conference> confsCibles = (Collection<Conference>) request.getAttribute("confsCibles");
			Collection<Journal> journauxCibles = (Collection<Journal>) 
					request.getAttribute("journauxCibles");
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
			<li><a href="Main?op=RmodifierT">Modifier/Supprimer</a></li>
			<li><a href="Main?op=Rsoumettre">Soumettre à un événement</a></li>
			<li class="active"><a href="Main?op=Rcibler">Cibler un événement</a></li>
		</ul>
	</div>
	
	
	
	<div class=container>


		<form action="Main" role="form" class="form-horizontal" method="post"
			accept-charset="utf-8" enctype="multipart/form-data">
			<input type="hidden" name="op" value="cibler" />

			<div class="row col-xs-10">
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


				<h3>Cibler Conférences/Journaux</h3>

				<!-------------------------------------- CONFERENCES --------------------------------------------------->
				<div class="form-group">
					<div class="col-xs-4">
						<h6>Choisir confèrences ciblées:<select class="selectpicker"
										name="rangConf1" id="rangConf1">
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
									</select></h6>

						<select id="confS" name="confCibleInter"
							style="width: 300px; height: 31px;">
							<optgroup label="Choisir conférences">
								<%
										for(Conference confP : confsP ){
									%>
									<option value="<%=confP.getId()%>" rang="<%=confP.getRang()%>"><%=confP.getTitre()%></option>
									<%
										}
									%>
						</select> <input id="plus" class="btn btn-default btn-xs"
							style="width: 63px;" value="Ajouter"><input id="moins"
							class="btn  btn-default btn-xs" style="width: 63px;"
							value="Enlever">
					</div>
					<br> <br>
				</div>
				<br>
				<div class="form-group">
					<div class="col-xs-4">
						<div
							style="overflow-x: auto; overflow_y: auto; width: 418px; height: 138px;">
							<select id="confCible" name="confCible" multiple
								style="height: 125px; width: 400px;">
								<optgroup label="Conférences Ciblées">
								<%
										for(Conference confC : confsCibles ){
									%>
									<option value="<%=confC.getId()%>" rang="<%=confC.getRang()%>"><%=confC.getTitre()%></option>
									<%
										}
									%>
							</select>
						</div>
					</div>

				</div>
				<!-------------------------------------- CONFERENCES FIN --------------------------------------------------->
				<div class="row col-xs-10">
					<div class="form-group">
						<div class="col-xs-4">
							<h6>Choisir journaux ciblées:<select class="selectpicker"
										name="rangJournal1" id="rangJournal1" style="width: 100px;">
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
									</select></h6>

							<select id="journalS" name="journalCibleInter"
								style="width: 300px; height: 31px;">
								<optgroup label="Choisir Journaux">
									<%
										for(Journal journalP : journauxP ){
									%>
									<option value="<%=journalP.getId()%>" rang="<%=journalP.getRang()%>"><%=journalP.getTitre()%></option>
									<%
										}
									%>
								
							</select> <input id="plusJ" class="btn btn-default btn-xs"
								style="width: 63px;" value="Ajouter"><input id="moinsJ"
								class="btn  btn-default btn-xs" style="width: 63px;"
								value="Enlever">
						</div>
						<br> <br>
					</div>

					<br>
					<div class="form-group">
						<div class="col-xs-4">

							<div
								style="overflow-x: auto; overflow_y: auto; width: 418px; height: 138px;">
								<select id="journalCible" name="journalCible" multiple
									style="height: 125px; width: 400px;">
									<optgroup label="Journaux Ciblés">
									<%
										for(Journal journalC : journauxCibles){
									%>
									<option value="<%=journalC.getId()%>" rang="<%=journalC.getRang()%>"
									><%=journalC.getTitre()%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>

					</div>
				</div>
				<!-------------------------------------- JOURNAUX FIN--------------------------------------------------->




				<br>
				<hr>
				<div class="form-group">
					<div class="col-md-offset-0 col-md-8">
						<hr>

						<p></p>
						<br> <input id="ajouterCibles" class="btn btn-success"
							type="submit" value="Cibler événements" />
					</div>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript">
		$('document').ready(function() {
			$('#plus').on('click', function(ev) {
				$("select[id='confS'] option:selected").map(function() {
					var value = $('#confS option:selected').val();
					var name = $('#confS option:selected').text();
					var rang = $('#confS option:selected').attr('rang');
					$('#confCible').append($('<option>', { 
				        value: value,
				        rang: rang,
				        text : name 
				    }));
					$('#confS option:selected').remove();
					$('#rangConf1').click();
				});
			});
		});
	</script>
	<script type="text/javascript">
		$('document').ready(function() {
			var name;
			var value;
			$('#moins').on('click', function(ev) {
				$("select[id='confCible'] option:selected").each(function() {
					value = $('#confCible option:selected').val();
					name = $('#confCible option:selected').text();
					var rang = $('#confCible option:selected').attr('rang');
					$('#confS').append($('<option>', { 
				        value: value,
				        rang: rang,
				        text : name 
				    }));
					$('#confCible option:selected').remove();
					$('#rangConf1').click();
				});
			});
		});
	</script>
	<script type="text/javascript">
		$('document').ready(function() {
			$('#plusJ').on('click', function(ev) {
				$("select[id='journalS'] option:selected").map(function() {
					var value = $('#journalS option:selected').val();
					var name = $('#journalS option:selected').text();
					var rang = $('#journalS option:selected').attr('rang');
					$('#journalCible').append($('<option>', { 
				        value: value,
				        rang: rang,
				        text : name 
				    }));
					$('#journalS option:selected').remove();
					$('#rangJournal1').click();
				});
			});
		});
	</script>
	<script type="text/javascript">
		$('document').ready(function() {
			$('#moinsJ').on('click', function(ev) {
				$("select[id='journalCible'] option:selected").each(function() {
					var value = $('#journalCible option:selected').val();
					var name = $('#journalCible option:selected').text();
					var rang = $('#journalCible option:selected').attr('rang');
					$('#journalS').append($('<option>', { 
				        value: value,
				        rang: rang,
				        text : name 
				    }));
					$('#journalCible option:selected').remove();
					$('#rangJournal1').click();
				});
			});
		});
	</script>
	<script type="text/javascript">
		$('document').ready(function() {
			$('#ajouterCibles').on('click', function(ev) {
				$('#confCible option').prop('selected', true);
				$('#journalCible option').prop('selected', true);
			});
		});
	</script>
	
		<script type="text/javascript">
	$('document').ready(function() {
		//initialement le rang vaut A*.
		$('#confS option').each(function(){
			if($(this).attr('rang')!="A*"){
            	$(this).hide();
            	$(this).prop('selected', false);
            	} else{
            		$(this).show();
            		$(this).prop('selected', true);
            	}
			//verifier si l'élément selectionne est visible.
            if( !$('#confS option:selected').is(":visible")){
            	$('#confS option:selected').prop('selected', true);
            	$("#confS").prop("selectedIndex", -1);
                }
            });
		$('#rangConf1').on('click', function(ev) {
		          var nom;
		          $("select option:selected").each(function () {
		                nom= $('#rangConf1 option:selected').text();
		                //boucler sur les options.
		                $('#confS option').each(function(){
		                	if($(this).attr('rang')!=nom){
			                	$(this).hide();
			                	$(this).prop('selected', false);
			                	} else{
			                		$(this).show();
			                		$(this).prop('selected', true);
				                } 
			            });
		                //verifier si l'élément selectionne est visible.
		                if( !$('#confS option:selected').is(":visible")){
		                	$('#confS option:selected').prop('selected', true);
		                	$("#confS").prop("selectedIndex", -1);
			                }

                 });
		});
	});
	</script>
		<script type="text/javascript">
	$('document').ready(function() {
		//initialement le rang vaut A*.
		$('#journalS option').each(function(){
			if($(this).attr('rang')!="A*"){
            	$(this).hide();
            	$(this).prop('selected', false);
            	} else {
            		$(this).show();
            		$(this).prop('selected', true);
            	}
			//verifier si l'élément selectionne est visible.
            if( !$('#journalS option:selected').is(":visible")){
            	$('#journalS option:selected').prop('selected', true);
            	$("#journalS").prop("selectedIndex", -1);
                }
            });
		$('#rangJournal1').on('click', function(ev) {
		          var nom;
		          $("select option:selected").each(function () {
		                nom= $('#rangJournal1 option:selected').text();
		                //boucler sur les options.
		                $('#journalS option').each(function(){
		                	if($(this).attr('rang')!=nom){
			                	$(this).hide();
			                	$(this).prop('selected', false);
			                	} else {
			                		$(this).show();
			                		$(this).prop('selected', true);
				                } 
			             });
		              //verifier si l'élément selectionne est visible.
		                if( !$('#journalS option:selected').is(":visible")){
		                	$('#journalS option:selected').prop('selected', true);
		                	$("#journalS").prop("selectedIndex", -1);
			                }
		              });
		});
	});
	</script>
	<script type="text/javascript"> 
	$('#rangJournal1').on('click', function(ev) {
        $("select option:selected").each(function () {
        });
	});

	</script>
</body>
</html>