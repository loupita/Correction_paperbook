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
			String rang ="A*";
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
			<li class="active"><a href="Main?op=Rsoumettre">Soumettre à un événement</a></li>
			<li><a href="Main?op=Rcibler">Cibler un événement</a></li>
		</ul>
	</div>
	
	
	<div class=container>

		<form action="Main" role="form" class="form-horizontal" method="post"
			accept-charset="utf-8" enctype="multipart/form-data">
			<input type="hidden" name="op" value="soumettre" />

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
				<div class="col-xs-4">

					<br> <br> <br> <br>
					<div class="form-group">
						<div class="col-md-8">
							<label for="InputFile">Publication</label> <input type="file"
								name="publication" id="exampleInputFile">
						</div>
					</div>
					<br> <br>
					<div class="form-group">
						<div class="col-md-8">
							<label for="InputFile">Sources</label> <input type="file"
								name="source" id="exampleInputFile">
						</div>
					</div>
					<br> <br>

				</div>
				<!-- ------------------------------------------SOUMIS------------------------------------------------------ -->
				<div class="col-xs-4">
					<br>
					<br>
					<br>
					<br>
					<h6>Conférence/Journal :</h6>
					<div class="form-group">
						<div class="col-md-8">
							<div class="radio">
								<input id="rs" type="radio" name="conf" value="cf"
									checked="checked" onclick="typeChanged('cf');" /> <label
									for="rs">Conférence</label>
							</div>
							<div class="radio">
								<input id="rk" type="radio" name="conf" value="k"
									onclick="typeChanged('k');" /> <label for="rk">Journal</label>
							</div>
						</div>
					</div>
					<br> <br>
					<div class="form-group" id=conf2>
						<div class="col-md-8">
							<h6>Choisir Conférence où il est soumis:</h6>
							<select class="selectpicker" name=rangConf2 id="rangConf2">
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
							<sql:query dataSource="${snapshot}" var="evts3">
							SELECT * from Conference where participant_id='<%=proprio.getId()%>'; 
							</sql:query>
							<select id="confChoisi" name="confChoisi" style="width: 300px;">
								<optgroup label="Choisir Conférence">
								<c:forEach var="row" items="${evts3.rows}">
									<option value="${row.id}" rang="${row.rang}">${row.titre}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="form-group" id=journal2>
						<div class="col-md-8">
							<h6>Choisir Journal où il est soumis:</h6>
							<select class="selectpicker" name=rangJournal2 id="rangJournal2">
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
							<sql:query dataSource="${snapshot}" var="evts4">
							SELECT * from Journal where participant_id='<%=proprio.getId()%>';
							</sql:query>
							<select id="journalChoisi" name="journalChoisi"
								style="width: 300px">
								<optgroup label="Choisir Journal">
								<c:forEach var="row" items="${evts4.rows}">
									<option value="${row.id}" rang="${row.rang}">${row.titre}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-0 col-md-8">
						<hr>
						<p></p>
						<br> <input id="soumettreTravail" class="btn btn-success"
							type="submit" value="Soumettre Travail" />
					</div>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript">
		$('document').ready(function() {
			$('#journal2').hide();
			$('#rk').on('click', function(ev) {
				if (this.checked) {
					$('#conf2').hide();
					$('#journal2').show();
				}
			});

			$('#rs').on('click', function(ev) {
				if (this.checked) {
					$('#conf2').show();
					$('#journal2').hide();
				}
			});
		});
	</script>
			<script type="text/javascript">
	$('document').ready(function() {
		//initialement le rang vaut A*.
		$('#confChoisi option').each(function(){
			if($(this).attr('rang')!="A*"){
            	$(this).hide();
            	$(this).prop('selected', false);
            	} else{
            		$(this).show();
            		$(this).prop('selected', true);
            	}
			if( !$('#confChoisi option:selected').is(":visible")){
            	$('#confChoisi option:selected').prop('selected', true);
            	$("#confChoisi").prop("selectedIndex", -1);
                }
            });
		$('#rangConf2').on('click', function(ev) {
		          var nom;
		          $("select option:selected").each(function () {
		                nom= $('#rangConf2 option:selected').text();
		                //boucler sur les options.
		                $('#confChoisi option').each(function(){
		                	if($(this).attr('rang')!=nom){
			                	$(this).hide();
			                	$(this).prop('selected', false);
			                	} else{
			                		$(this).show();
			                		$(this).prop('selected', true);
				                } 
			            });
		                if( !$('#confChoisi option:selected').is(":visible")){
		                	$('#confChoisi option:selected').prop('selected', true);
		                	$("#confChoisi").prop("selectedIndex", -1);
			                }
		              });
		});
	});
	</script>
	<script type="text/javascript">
	$('document').ready(function() {
		//initialement le rang vaut A*.
		$('#journalChoisi option').each(function(){
			if($(this).attr('rang')!="A*"){
            	$(this).hide();
            	$(this).prop('selected', false);
            	} else{
            		$(this).show();
            		$(this).prop('selected', true);
            	}
			if( !$('#journalChoisi option:selected').is(":visible")){
            	$('#journalChoisi option:selected').prop('selected', true);
            	$("#journalChoisi").prop("selectedIndex", -1);
                }
            });
		$('#rangJournal2').on('click', function(ev) {
		          var nom;
		          $("select option:selected").each(function () {
		                nom= $('#rangJournal2 option:selected').text();
		                //boucler sur les options.
		                $('#journalChoisi option').each(function(){
		                	if($(this).attr('rang')!=nom){
			                	$(this).hide();
			                	$(this).prop('selected', false);
			                	} else{
			                		$(this).show();
			                		$(this).prop('selected', true);
				                } 
		                	
			             });
		                if( !$('#journalChoisi option:selected').is(":visible")){
		                	$('#journalChoisi option:selected').prop('selected', true);
		                	$("#journalChoisi").prop("selectedIndex", -1);
			                }
		              });
		});
	});
	</script>
</body>
</html>