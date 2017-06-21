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


	<div class=container>

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
		<form action="Main" role="form" class="form-horizontal" method="post"
			accept-charset="utf-8" enctype="multipart/form-data">
			<input type="hidden" name="op" value="ajouterTravail" />

			<div class=row>
				<div class="col-xs-4">

					<h3>Informations sur le travail</h3>
					<div class="form-group">
						<div class="col-md-8">
							<br>
							<h6>Titre :</h6>
							<label for="titre" class="sr-only">Titre</label> <input
								type="text" id="titre" name="titre" class="form-control"
								placeholder="Titre" required autofocus value="">
						</div>
					</div>

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
					<h6>S'agit-il d'un travail soumis :</h6>
					<div class="form-group">
						<div class="col-sm-8">
							<div class="checkbox">
								<input id="soumis" type="checkbox" name="soumis" value="" /> <label
									for="rs">Soumis</label>
							</div>
						</div>
					</div>
				</div>

				<!-------------------------------------- 2eme COLONNE CONFERENCE --------------------------------------------------->
				<div class="col-sm-8 ">
					<br> <br> <br> <br>
					<div id="conf1">
						<div class="form-group" class="conf1">
							<div class="col-md-8">
								<h6>
									Choisir confèrences ciblées:<select class="selectpicker"
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
									</select>
								</h6>

								<sql:query dataSource="${snapshot}" var="evts1">
									SELECT * from Conference where participant_id='<%=proprio.getId()%>';
								</sql:query>
								<select id="confS" name="confCibleInter"
									style="width: 300px; height: 31px;">
									<optgroup label="Choisir conférence">
										<c:forEach var="row" items="${evts1.rows}">
											<option value="${row.id}" rang="${row.rang}">${row.titre}</option>
										</c:forEach>
								</select><input id="plus" class="btn btn-default btn-xs"
									style="width: 63px;" value="Ajouter"><input id="moins"
									class="btn  btn-default btn-xs" style="width: 63px;"
									value="Enlever">
							</div>
						</div>

						<br>
						<div class="form-group" class="conf1">
							<div class="col-md-8">
								<div
									style="overflow-x: auto; overflow_y: auto; width: 418px; height: 138px;">
									<select id="confCible" name="confCible" multiple
										style="height: 125px; width: 419px;">
										<optgroup label="Conférences Ciblées">
									</select>
								</div>
							</div>

						</div>
					</div>


					<!-------------------------------------- 3eme COLONNE --------------------------------------------------->
					<div id="journal1">
						<div class="form-group">
							<div class="col-md-8">
								<h6>
									Choisir journaux ciblées:<select class="selectpicker"
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
									</select>
								</h6>

								<sql:query dataSource="${snapshot}" var="evts2">
									SELECT * from Journal where participant_id='<%=proprio.getId()%>';
								</sql:query>
								<select id="journalS" name="journalCibleInter"
									style="width: 300px; height: 31px;">
									<optgroup label="Choisir Journal">
										<c:forEach var="row" items="${evts2.rows}">
											<option value="${row.id}" rang="${row.rang}">${row.titre}</option>
										</c:forEach>
								</select><input id="plusJ" class="btn btn-default btn-xs"
									style="width: 63px;" value="Ajouter"><input id="moinsJ"
									class="btn  btn-default btn-xs" style="width: 63px;"
									value="Enlever">
							</div>
						</div>

						<br>
						<div class="form-group" class="journal1">
							<div class="col-md-8">
								<div
									style="overflow-x: auto; overflow_y: auto; width: 418px; height: 138px;">
									<select id="journalCible" name="journalCible" multiple
										style="height: 125px; width: 419px;">
										<optgroup label="Journaux Ciblés">
									</select>
								</div>
							</div>

						</div>
					</div>
					<!-- ------------------------------------------SOUMIS------------------------------------------------------ -->
					<div class="choix">
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
								<br> <br>
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
								<br> <br>
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
				</div>
			</div>

			<hr>
			<h6>Abstract :</h6>
			<textarea id=abstr name="abstr" rows="10" cols="100"
				placeholder="Résumé..." maxlength="1500" value=""></textarea>

			<br>

			<div class="form-group">
				<div class="col-md-offset-0 col-md-8">
					<hr>

					<p></p>
					<br> <input id="ajouterTravail" class="btn btn-success"
						type="submit" value="Ajouter Travail" />
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
			$('.choix').hide();
			$('#soumis').on('click', function(ev) {
				if (this.checked) {
					$('#conf1').hide();
					$('#journal1').hide();
					$('.choix').show();
					console.log('the checkbox1');
				} else {
					$('#conf1').show();
					$('#journal1').show();
					$('.choix').hide();
					console.log('the checkbox2');
				}
			});
		});
	</script>
	<script type="text/javascript">
		$('document').ready(function() {
			$('#plus').on('click', function(ev) {
				$("select[id='confS'] option:selected").map(function() {
					var value = $('#confS option:selected').val();
					var name = $('#confS option:selected').text();
					var rang = $('#confS option:selected').attr('rang');
					$('#confCible').append($('<option>', {
						value : value,
						rang : rang,
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
						value : value,
						rang : rang,
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
						value : value,
						rang : rang,
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
						value : value,
						rang : rang,
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
			$('#ajouterTravail').on('click', function(ev) {
				$('#confCible option').prop('selected', true);
				$('#journalCible option').prop('selected', true);
			});
		});
	</script>


	<script type="text/javascript">
		$('document').ready(function() {
			//initialement le rang vaut A*.
			$('#confS option').each(function() {
				if ($(this).attr('rang') != "A*") {
					$(this).hide();
					$(this).prop('selected', false);
				} else {
					$(this).show();
					$(this).prop('selected', true);
				}
				//verifier si l'élément selectionne est visible.
				if (!$('#confS option:selected').is(":visible")) {
					$('#confS option:selected').prop('selected', true);
					$("#confS").prop("selectedIndex", -1);
				}
			});
			$('#rangConf1').on('click', function(ev) {
				var nom;
				$("select option:selected").each(function() {
					nom = $('#rangConf1 option:selected').text();
					//boucler sur les options.
					$('#confS option').each(function() {
						if ($(this).attr('rang') != nom) {
							$(this).hide();
							$(this).prop('selected', false);
						} else {
							$(this).show();
							$(this).prop('selected', true);
						}
					});
					//verifier si l'élément selectionne est visible.
					if (!$('#confS option:selected').is(":visible")) {
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
			$('#journalS option').each(function() {
				if ($(this).attr('rang') != "A*") {
					$(this).hide();
					$(this).prop('selected', false);
				} else {
					$(this).show();
					$(this).prop('selected', true);
				}
				//verifier si l'élément selectionne est visible.
				if (!$('#journalS option:selected').is(":visible")) {
					$('#journalS option:selected').prop('selected', true);
					$("#journalS").prop("selectedIndex", -1);
				}
			});
			$('#rangJournal1').on('click', function(ev) {
				var nom;
				$("select option:selected").each(function() {
					nom = $('#rangJournal1 option:selected').text();
					//boucler sur les options.
					$('#journalS option').each(function() {
						if ($(this).attr('rang') != nom) {
							$(this).hide();
							$(this).prop('selected', false);
						} else {
							$(this).show();
							$(this).prop('selected', true);
						}
					});
					//verifier si l'élément selectionne est visible.
					if (!$('#journalS option:selected').is(":visible")) {
						$('#journalS option:selected').prop('selected', true);
						$("#journalS").prop("selectedIndex", -1);
					}
				});
			});
		});
	</script>
	
	<script type="text/javascript">
		$('document').ready(function() {
		//initialement le rang vaut A*.
		$('#confChoisi option').each(function() {
			if ($(this).attr('rang') != "A*") {
				$(this).hide();
				$(this).prop('selected', false);
			} else {
				$(this).show();
				$(this).prop('selected', true);
			}
			if (!$('#confChoisi option:selected').is(":visible")) {
				$('#confChoisi option:selected').prop('selected',true);
				$("#confChoisi").prop("selectedIndex",-1);
			}
		});
		$('#rangConf2').on('click',function(ev) {
			var nom;
			$("select option:selected").each(function() {
				nom = $('#rangConf2 option:selected').text();
				//boucler sur les options.
				$('#confChoisi option').each(function() {
					if ($(this)	.attr('rang') != nom) {
						$(this).hide();
						$(this).prop('selected',false);
					} else {
						$(this).show();
						$(this).prop('selected',true);
					}
				});
				if (!$('#confChoisi option:selected').is(":visible")) {
					$('#confChoisi option:selected').prop('selected',true);
					$("#confChoisi").prop("selectedIndex",-1);
				}
			});
		});
	});
	</script>
	<script type="text/javascript">
		$('document').ready(function() {
			//initialement le rang vaut A*.
			$('#journalChoisi option').each(function() {
				if ($(this).attr('rang') != "A*") {
					$(this).hide();
					$(this).prop('selected', false);
				} else {
					$(this).show();
					$(this).prop('selected', true);
				}
				if (!$('#journalChoisi option:selected').is(":visible")) {
					$('#journalChoisi option:selected').prop('selected',true);
					$("#journalChoisi").prop("selectedIndex",-1);
				}
			});
			$('#rangJournal2').on('click',function(ev) {
				var nom;
				$("select option:selected").each(function() {
					nom = $('#rangJournal2 option:selected').text();
					//boucler sur les options.
					$('#journalChoisi option').each(function() {
						if ($(this).attr('rang') != nom) {
							$(this).hide();
							$(this).prop('selected',false);
						} else {
							$(this).show();
							$(this).prop('selected',true);
						}
					});
					if (!$('#journalChoisi option:selected').is(":visible")) {
						$('#journalChoisi option:selected').prop('selected',true);
						$("#journalChoisi").prop("selectedIndex",-1);
					}
				});
			});
		});
	</script>
</body>
</html>