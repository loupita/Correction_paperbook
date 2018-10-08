package home;
import static java.nio.charset.StandardCharsets.*;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ejb.*;


/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
@MultipartConfig
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private Facade f=new Facade();
	private HttpSession session;
	private String chemin = "WEB-INF/monFichier/";
	private String chemin2 = "WEB-INF/ex_fichiers_excels/";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Main() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sop = request.getParameter("op");
		session = request.getSession();
		f.InitErreurs();
		session.setAttribute("mur", f.getVersionsPubliques());

		switch(sop){
		case "index" :

			this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			break;

////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************INSCRIPTION/CONNEXION*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////			
		case "inscription":	

			String nom = request.getParameter("nom");

			String prenom = request.getParameter("prenom");

			String email = request.getParameter("email");

			String ident = request.getParameter("identifiant");

			String mdp = request.getParameter("mdp");

			String confMdp = request.getParameter("confirmationMdp");

			f.inscrire(nom, prenom, email, ident, mdp , confMdp);

			if(f.getErreurs().isEmpty()){
				session.setAttribute("Personne",f.getPersonne(ident));
				session.setAttribute("ident",ident);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else{
				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());
				request.setAttribute("nom", nom);
				request.setAttribute("prenom", prenom);
				request.setAttribute("identifiant", ident);
				request.setAttribute("email", email);
				this.getServletContext().getRequestDispatcher("/inscription.jsp").forward(request, response);
				f.InitErreurs();
			}
			break;

		case "connexion":

			String ident1 = request.getParameter("identifiant");
			String mdp1 = request.getParameter("mdp");
			f.connecter(ident1, mdp1);
			if(f.getErreurs().isEmpty()) {
				session.setAttribute("Personne",f.getPersonne(ident1));
				session.setAttribute("ident",ident1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else{
				request.setAttribute("erreurs", f.getErreurs());
				this.getServletContext().getRequestDispatcher("/connexion.jsp").forward(request, response);
				f.InitErreurs();
			}

			break;




////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************ESPACE PERSO TRAVAUX EN COURS*************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "espacePerso" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("travaux_non_soumis", f.getMesTravauxNonSoumis(f.listeTravauxNonSoumis(idPers)));
				request.setAttribute("travaux_soumis", f.getMesTravauxSoumis(f.listeTravauxSoumisNoRep(idPers)));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/espacePerso.jsp").forward(request, response);		
			}
			break;

////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************ESPACE PERSO MUR*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "mur" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);		
			}
			break;



		case "detailsVersionEspPerso" : 
			if( f.getPersonne((String) session.getAttribute("ident")) == null ){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getVersion((String) request.getParameter("idV")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				String idVersion = request.getParameter("idV");
				Version version = f.getVersion(idVersion);
				session.setAttribute("version", version);
				session.setAttribute("idV",idVersion);
				request.setAttribute("reponses", f.getReponses(version));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Version/detailVersionEspPerso.jsp").forward(request, response);
			}
			break;


		case "reponsesEspPerso" : 
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getVersion((String) session.getAttribute("idV")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version = (Version) session.getAttribute("version");
				request.setAttribute("reponses", f.getReponses(version));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Version/reponsesEspPerso.jsp").forward(request, response);
			}

			break;





////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************ESPACE PERSO CONFERENCES*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "mesConferences" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("mesConfs",f.getMesConfs(idPers,1));
				request.setAttribute("nbPagesC", f.getNbPagesC(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesConferences.jsp").forward(request, response);		
			}
			break;

		case "mesConferencesPages" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int page=1;
				if( (String) request.getParameter("page") != null){
					page=Integer.parseInt((String) request.getParameter("page"));
				}
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("mesConfs",f.getMesConfs(idPers,page));
				request.setAttribute("nbPagesC", f.getNbPagesC(idPers));
				request.setAttribute("page", page);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesConferences.jsp").forward(request, response);		
			}
			break;
			
		case "exporterConf":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				f.exporterExcelConf(idPers,this,response);
			}
			break;
			
			
		case "importerConf":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				f.importerExcelConf(idPers, request);
				request.setAttribute("mesConfs",f.getMesConfs(idPers,1));
				request.setAttribute("nbPagesC", f.getNbPagesC(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesConferences.jsp").forward(request, response);		
			}
			break;
			
			
		case "supprimerConferences" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				String[] idConfs = request.getParameterValues("conferencesCheck");
				f.supprimerConferences(idConfs);
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("mesConfs",f.getMesConfs(idPers,1));
				request.setAttribute("nbPagesC", f.getNbPagesC(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesConferences.jsp").forward(request, response);		
			}
			break;
		case "detailConfEspPerso" :
			
			System.out.println("#############################################");
			System.out.println("le resultat de la recherche est:"+ session.getAttribute("ident"));
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			System.out.println("le resultat de la recherche est:"+request.getParameter("id") );
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			
			
			//else if( f.getConference((String) request.getParameter("id")) == null){
			else if( request.getParameter("id")!=null && f.getConference((String) request.getParameter("id")) == null){
				
				
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				
				String sidConf = request.getParameter("id");
				Conference conf = f.getConference(sidConf);
				session.setAttribute("conf", conf);
				session.setAttribute("idConf",Integer.parseInt(sidConf));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/detailConfEspPerso.jsp").forward(request, response);
			}
			break;


		case "RmodifierConfEspPerso":
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/modifierConfEspPerso.jsp").forward(request, response);
			}
			break;

		case "modifierConfEspPerso":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				int idConf = (int) session.getAttribute("idConf");

				String titre = request.getParameter("titre");
				byte ptext[] = titre.getBytes();
				titre = new String(ptext, "UTF-8");

				String acr = request.getParameter("acr");

				String site = request.getParameter("site");

				String rang = request.getParameter("rang");

				String info = request.getParameter("info");

				String lieu = request.getParameter("pays");

				String date1 = request.getParameter("date1");

				String date2 = request.getParameter("date2");

				String date3 = request.getParameter("date3");

				String dateR = request.getParameter("dateR");

				String date4 = request.getParameter("date4");

				f.modifierConf(request,idConf, titre,acr, site, rang, info, lieu, date1, date2, date3, dateR, date4);
				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					Conference conf = f.getConference(idConf);
					session.setAttribute("conf", conf);
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/detailConfEspPerso.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("rang", rang);
					request.setAttribute("site", site);
					request.setAttribute("acr", acr);
					request.setAttribute("lieu", lieu);
					request.setAttribute("date1", date1);
					request.setAttribute("date2", date2);
					request.setAttribute("date3", date3);
					request.setAttribute("date4", date4);
					request.setAttribute("info", info);
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/modifierConfEspPerso.jsp").forward(request, response);
					f.InitErreurs();
				}
			}
			break;



		case "annulerConfEspPerso" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/detailConfEspPerso.jsp").forward(request, response);
			}
			break;


		case "supprimerConfEspPerso" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				int idConf = (int) session.getAttribute("idConf");
				f.supprimerConf(idConf);
				request.setAttribute("mesConfs",f.getMesConfs(idPers,1));
				request.setAttribute("nbPagesC", f.getNbPagesC(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesConferences.jsp").forward(request, response);
			}
			break;


		case "travauxAyantSoumisC":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idConf = (int) session.getAttribute("idConf");
				request.setAttribute("versions", f.getConferenceTravauxSoumis(idConf));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/travauxAyantSoumisC.jsp").forward(request, response);
			}
			break;
			
			
		case "travauxCiblantsC":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if(f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idConf = (int) session.getAttribute("idConf");
				request.setAttribute("travaux", f.getConferenceTravauxCiblant(idConf));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Conference/travauxCiblantsC.jsp").forward(request, response);
			}
			break;

			
		
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			//***********************************ESPACE PERSO JOURNAUX*******************************************
			////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "mesJournaux" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("mesJournaux",f.getMesJournaux(idPers,1));
				request.setAttribute("nbPagesJ", f.getNbPagesJ(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesJournaux.jsp").forward(request, response);		
			}
			break;

			
		case "mesJournauxPages" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int page=1;
				if( (String) request.getParameter("page") != null){
					page=Integer.parseInt((String) request.getParameter("page"));
				}
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("mesJournaux",f.getMesJournaux(idPers,page));
				request.setAttribute("nbPagesJ", f.getNbPagesJ(idPers));
				request.setAttribute("page", page);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesJournaux.jsp").forward(request, response);		
			}
			break;
			
		case "exporterJournaux":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				f.exporterExcelJournaux(idPers,this,response);
			}
			break;
			
		case "importerJournaux":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				f.importerExcelJournaux(idPers, request);
				request.setAttribute("mesJournaux",f.getMesJournaux(idPers,1));
				request.setAttribute("nbPagesJ", f.getNbPagesJ(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesJournaux.jsp").forward(request, response);		
			}
			break;
			
			
		case "supprimerJournaux" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				String[] idJournaux = request.getParameterValues("journauxCheck");
				
				f.supprimerJournaux(idJournaux);
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("mesJournaux",f.getMesJournaux(idPers,1));
				request.setAttribute("nbPagesJ", f.getNbPagesJ(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesJournaux.jsp").forward(request, response);
			}
			break;
			
			
		case "detailJournalEspPerso" :
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((String) request.getParameter("id")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				String sidJournal = request.getParameter("id");
				
				System.out.println("l'id est :" + sidJournal);
				Journal journal = f.getJournal(sidJournal);
				session.setAttribute("journal", journal);
				session.setAttribute("idJournal",Integer.parseInt(sidJournal));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/detailJournalEspPerso.jsp").forward(request, response);
			}

			break;



		case "RmodifierJournalEspPerso":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/modifierJournalEspPerso.jsp").forward(request, response);
			}
			break;

		case "modifierJournalEspPerso":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				int idJournal = (int) session.getAttribute("idJournal");

				String titre = request.getParameter("titre");
				byte ptext[] = titre.getBytes();
				titre = new String(ptext, "UTF-8");


				String site = request.getParameter("site");

				String rang = request.getParameter("rang");

				String info = request.getParameter("info");

				String date2 = request.getParameter("date2");

				String date3 = request.getParameter("date3");

				String dateR = request.getParameter("dateR");


				f.modifierJournal(request,idJournal, titre, site, rang, info, date2, date3, dateR);
				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					Journal journal = f.getJournal(idJournal);
					session.setAttribute("journal", journal);
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/detailJournalEspPerso.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("rang", rang);
					request.setAttribute("site", site);
					request.setAttribute("date2", date2);
					request.setAttribute("date3", date3);
					request.setAttribute("info", info);
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/modifierJournalEspPerso.jsp").forward(request, response);
					f.InitErreurs();
				}
			}
			break;

		case "annulerJournalEspPerso" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/detailJournalEspPerso.jsp").forward(request, response);
			}
			break;



		case "supprimerJournalEspPerso" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				int idJournal = (int) session.getAttribute("idJournal");
				f.supprimerJournal(idJournal);
				request.setAttribute("mesJournaux",f.getMesJournaux(idPers,1));
				request.setAttribute("nbPagesJ", f.getNbPagesJ(idPers));
				request.setAttribute("page", 1);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mesJournaux.jsp").forward(request, response);
			}
			break;


		case "travauxAyantSoumisJ":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idJournal = (int) session.getAttribute("idJournal");
				request.setAttribute("versions", f.getJournalTravauxSoumis(idJournal));
				request.setAttribute("id", idJournal);
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/travauxAyantSoumisJ.jsp").forward(request, response);
			}
			break;
			
			
		case "travauxCiblantsJ":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idJournal = (int) session.getAttribute("idJournal");
				request.setAttribute("travaux", f.getJournalTravauxCiblant(idJournal));
				request.setAttribute("id", ""+idJournal);
				
				System.out.println("###################idJournal : "+request.getAttribute("id"));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/Journal/travauxCiblantsJ.jsp").forward(request, response);
			}
			break;
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			//***********************************ESPACE PERSO TRAVAUX*******************************************
			////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "listeTravaux" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				request.setAttribute("travaux_non_soumis", f.getMesTravauxNonSoumis(f.listeTravauxNonSoumis(idPers)));
				request.setAttribute("travaux_soumis", f.getMesTravauxSoumis(f.listeTravauxSoumisNoRep(idPers)));
				request.setAttribute("travaux_soumis_rep", f.getMesTravauxSoumis(f.listeTravauxSoumisAvecRep(idPers)));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/listeTravaux.jsp").forward(request, response);		
			}
			break;


////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************ESPACE PERSO STATS*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "mesStatistiques" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/statistiques.jsp").forward(request, response);		
			}
			break;
			
			
		case "rechercherStats" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				String isConf = request.getParameter("isConf");
				String type = request.getParameter("type");
				String texte = request.getParameter("rechercher");
				request.setAttribute("events", f.getResultatStats(idPers,texte, isConf, type));
				request.setAttribute("eventParticulier", f.moyenneParRang(idPers,texte, isConf, type));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/resultatStats.jsp").forward(request, response);		
				
			}
			break;
			
			
			
////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************ESPACE PERSO RECHERCHER*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "rechercher" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
			this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/rechercher.jsp").forward(request, response);		
			}
		break;
		
		
		case "rechercherResults" :
			//verifier s'il y a quelqu'un connecte ou pas
			if(f.getPersonne((String) (session.getAttribute("ident"))) == null){
			this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else{
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				String duree = request.getParameter("duree");
				String isMois = request.getParameter("isMois");
				String rang = request.getParameter("rang");
				String isConf = request.getParameter("isConf");
				String date = request.getParameter("date");
				System.out.println("Main "+date);
				if(isConf.matches("Conference")){
					request.setAttribute("confs", f.getResultatRechercheConf(idPers,duree, isMois,rang,
							date));
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/resultatRechercherConf.jsp").forward(request, response);		
				} 
				else {
					request.setAttribute("journaux", f.getResultatRechercheJournal(idPers,duree, isMois,rang,
							date));
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/resultatRechercherJournal.jsp").forward(request, response);
				}
			}
		break;
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////


			////////////////////////////////////////////////////////////////////////////////////////////////////////
			//***********************************ACTIONS GENERALES*******************************************
			////////////////////////////////////////////////////////////////////////////////////////////////////////

			////////////////////////////////////////////////////////////////////
			//***********************************AJOUTER EVENT**************
			////////////////////////////////////////////////////////////////////
		case "RajouterEvt" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/ajouterEvent.jsp").forward(request, response);
			}
			break;
		case "ajouterEvt" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				String titre = request.getParameter("titre");
				byte ptext[] = titre.getBytes();
				titre = new String(ptext, "UTF-8");


				String acr = request.getParameter("acr");

				String site = request.getParameter("site");

				String rang = request.getParameter("rang");

				String info = request.getParameter("info");

				String lieu = request.getParameter("pays");

				String date1 = request.getParameter("date1");

				String date2 = request.getParameter("date2");

				String date3 = request.getParameter("date3");

				String dateR = request.getParameter("dateR");

				String date4 = request.getParameter("date4");

				String isConf= request.getParameter("conf");


				if (isConf!=null && isConf.equals("cf")){
					f.ajouterConf(request, titre,acr, site, rang, info, lieu, date1, date2, 
							date3,dateR, date4);
				}
				else {
					f.ajouterJournal(request, titre, site, rang, info, date2, date3,
							dateR);
				}


				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					int idPers = ((Personne) session.getAttribute("Personne")).getId();
					request.setAttribute("travaux_non_soumis", f.getMesTravauxNonSoumis(f.listeTravauxNonSoumis(idPers)));
					request.setAttribute("travaux_soumis", f.getMesTravauxSoumis(f.listeTravauxSoumisNoRep(idPers)));
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/espacePerso.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("rang", rang);
					request.setAttribute("site", site);
					request.setAttribute("lieu", lieu);
					request.setAttribute("date1", date1);
					request.setAttribute("date2", date2);
					request.setAttribute("date3", date3);
					request.setAttribute("date4", date4);
					request.setAttribute("isConf", isConf);
					request.setAttribute("info", info);
					this.getServletContext().getRequestDispatcher("/WEB-INF/ajouterEvent.jsp").forward(request, response);
					f.InitErreurs();
				}
			}
			break;






		////////////////////////////////////////////////////////////////////
		//***********************************AJOUTER TRAVAIL**************
		////////////////////////////////////////////////////////////////////

		case "RajouterTravail" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/ajouterTravail.jsp").forward(request, response);
			}
			break;



		case "ajouterTravail" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				String titre = request.getParameter("titre");
				
				System.out.println("le titre sans l'ensodage est:" + titre);
				

				byte[] ptext = titre.getBytes(ISO_8859_1);
				titre = new String(ptext, "UTF-8");
				String abstr = request.getParameter("abstr");
				byte text[] = abstr.getBytes(ISO_8859_1);
				abstr = new String(text, "UTF-8");

				String[] confs = request.getParameterValues("confCible");

				String[] journaux = request.getParameterValues("journalCible");

				String confChoisi = request.getParameter("confChoisi");

				String journalChoisi = request.getParameter("journalChoisi");

				boolean soumis=(request.getParameter("soumis")!=null);

				String sisConf= request.getParameter("conf");

				boolean isConf=false;
				if (sisConf!=null && sisConf.equals("cf")){
					isConf = true;
				}
				
				f.ajouterTravail(request,titre,abstr,soumis,confs, journaux,
						confChoisi,journalChoisi,isConf);

				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					int idPers = ((Personne) session.getAttribute("Personne")).getId();
					request.setAttribute("travaux_non_soumis", f.getMesTravauxNonSoumis(f.listeTravauxNonSoumis(idPers)));
					request.setAttribute("travaux_soumis", f.getMesTravauxSoumis(f.listeTravauxSoumisNoRep(idPers)));
					this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("abstr", abstr);
					this.getServletContext().getRequestDispatcher("/WEB-INF/ajouterTravail.jsp").forward(request, response);
					f.InitErreurs();
				}
			}
			break;




////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL DETAIL*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////

			
		case "detailTravail" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if(f.getTravail((String) request.getParameter("id")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				String sidTravail = request.getParameter("id");
				int idTravail = Integer.parseInt(sidTravail);
				Travail travail = f.getTravail(sidTravail);
				session.setAttribute("travail", travail);
				//id pour verification.
				session.setAttribute("idTravail",idTravail);
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}

			break;
			
		case "supprimerConfListe":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((String) request.getParameter("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idTravail = (int) session.getAttribute("idTravail");
				String sidConf = request.getParameter("idConf");
				
				if(sidConf!=null){
					int idConf = Integer.parseInt(sidConf);
					f.supprimerConfListe(idTravail,idConf);
				}
					
				Travail travail = f.getTravail(idTravail);
				session.setAttribute("travail", travail);
				//id pour verification.
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}

			break;
			
			
		case "supprimerJournalListe":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((String) request.getParameter("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idTravail = (int) session.getAttribute("idTravail");
				String sidJournal = request.getParameter("idJournal");
				
				if(sidJournal!=null){
					int idJournal = Integer.parseInt(sidJournal);
					f.supprimerJournalListe(idTravail,idJournal);
				}
				
				Travail travail = f.getTravail(idTravail);
				session.setAttribute("travail", travail);
				//id pour verification.
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}

			break;
			
			
			
			
		case "supprimerConfListeCibles":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((String) request.getParameter("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idTravail = (int) session.getAttribute("idTravail");
				String sidConf = request.getParameter("idConf");
				
				if(sidConf!=null){
					int idConf = Integer.parseInt(sidConf);
					f.supprimerConfListeCibles(idTravail,idConf);
				}
					
				Travail travail = f.getTravail(idTravail);
				session.setAttribute("travail", travail);
				//id pour verification.
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}

			break;
			
			
		case "supprimerJournalListeCibles":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((String) request.getParameter("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idTravail = (int) session.getAttribute("idTravail");
				String sidJournal = request.getParameter("idJournal");
				
				if(sidJournal!=null){
					int idJournal = Integer.parseInt(sidJournal);
					f.supprimerJournalListeCibles(idTravail,idJournal);
				}
				
				Travail travail = f.getTravail(idTravail);
				session.setAttribute("travail", travail);
				//id pour verification.
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}

			break;
		
////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL CONFERENCE*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
		case "detailConf" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((String) request.getParameter("id")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				String sidConf = request.getParameter("id");
				Conference conf = f.getConference(sidConf);
				session.setAttribute("conf", conf);
				session.setAttribute("idConf",Integer.parseInt(sidConf));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/detailConf.jsp").forward(request, response);
			}

			break;
			
		case "propositions" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/propositions.jsp").forward(request, response);
			}
			break;
			
		case "resultatPropositions" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idConf = (int) session.getAttribute("idConf");
				String rang = request.getParameter("rang");
				String marge = request.getParameter("marge");
				//Collection des conférences qui ont la date de l'abstract à +/- 15 jours de la date de notification.
				//les conférences ciblées.
				int idTravail = (int) session.getAttribute("idTravail");
				session.setAttribute("conferences", f.getPropostionsConfs(idConf,marge,rang,f.getConferencesCibles(idTravail)));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/resultatPropositions.jsp").forward(request, response);
			}
			break;
			

		case "RmodifierConf":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/modifierConf.jsp").forward(request, response);
			}
			break;

		case "modifierConf":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				int idConf = (int) session.getAttribute("idConf");
				String titre = request.getParameter("titre");
				byte ptext[] = titre.getBytes();
				titre = new String(ptext, "UTF-8");

				String acr = request.getParameter("acr");

				String site = request.getParameter("site");

				String rang = request.getParameter("rang");

				String info = request.getParameter("info");

				String lieu = request.getParameter("pays");

				String date1 = request.getParameter("date1");

				String date2 = request.getParameter("date2");

				String date3 = request.getParameter("date3");

				String dateR = request.getParameter("dateR");

				String date4 = request.getParameter("date4");
				System.out.println("date1 "+date1);
				System.out.println("date2 "+date2);
				System.out.println("date3 "+date3);
				System.out.println("dateR "+dateR);
				System.out.println("date4 "+date4);

				f.modifierConf(request,idConf, titre,acr, site, rang, info, lieu, date1, date2, date3, dateR, date4);
				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					Conference conf = f.getConference(idConf);
					session.setAttribute("conf", conf);
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/detailConf.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("rang", rang);
					request.setAttribute("site", site);
					request.setAttribute("lieu", lieu);
					request.setAttribute("date1", date1);
					request.setAttribute("date2", date2);
					request.setAttribute("date3", date3);
					request.setAttribute("date4", date4);
					request.setAttribute("info", info);
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/modifierConf.jsp").forward(request, response);

					f.InitErreurs();
				}
			}
			break;
		

		case "annulerConf" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Conference/detailConf.jsp").forward(request, response);
			}
			break;

			
		case "supprimerConf" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getConference((int) session.getAttribute("idConf")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idConf = (int) session.getAttribute("idConf");
				f.supprimerConf(idConf);
				//prendre en compte les changements.
				int idTravail = (int) session.getAttribute("idTravail");
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}
			break;

////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL JOURNAL*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////

		
		case "detailJournal" :
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((String) request.getParameter("id")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				String sidJournal = request.getParameter("id");
				Journal journal = f.getJournal(sidJournal);
				session.setAttribute("journal", journal);
				session.setAttribute("idJournal",Integer.parseInt(sidJournal));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Journal/detailJournal.jsp").forward(request, response);
			}

			break;

		case "RmodifierJournal":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Journal/modifierJournal.jsp").forward(request, response);
			}
			break;

		case "modifierJournal":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				int idJournal = (int) session.getAttribute("idJournal");
				String titre = request.getParameter("titre");
				byte ptext[] = titre.getBytes();
				titre = new String(ptext, "UTF-8");
				System.out.println("le titre est : " +titre);

				String site = request.getParameter("site");

				String rang = request.getParameter("rang");

				String info = request.getParameter("info");

				String date2 = request.getParameter("date2");

				String date3 = request.getParameter("date3");

				String dateR = request.getParameter("dateR");


				f.modifierJournal(request,idJournal, titre, site, rang, info, date2, date3, dateR);
				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					Journal journal = f.getJournal(idJournal);
					session.setAttribute("journal", journal);
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Journal/detailJournal.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("rang", rang);
					request.setAttribute("site", site);
					request.setAttribute("date2", date2);
					request.setAttribute("date3", date3);
					request.setAttribute("info", info);
					if(request.getParameter("esp")==null)
						this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Journal/modifierJournal.jsp").forward(request, response);
					f.InitErreurs();
				}
			}
			break;

		
		case "annulerJournal" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Journal/detailJournal.jsp").forward(request, response);
			}
			break;

		
		case "supprimerJournal" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getJournal((int) session.getAttribute("idJournal")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idJournal = (int) session.getAttribute("idJournal");
				f.supprimerJournal(idJournal);
				//prendre en compte les changements.
				int idTravail = (int) session.getAttribute("idTravail");
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}
			break;




////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL MODIFIER*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////

		case "RmodifierT":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/modifierTravail.jsp").forward(request, response);
			}
			break;

		case "modifierT":
			//verifier s'il y a quelqu'un connecte ou pas.
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un travail choisi.
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {

				int idTravail = (int) session.getAttribute("idTravail");

				String titre = request.getParameter("titre");
				byte ptext[] = titre.getBytes();
				titre= new String(ptext, "UTF-8");

				String abstr = request.getParameter("abstr");
				byte ptex[] = abstr.getBytes();
				abstr= new String(ptex, "UTF-8");

				f.modifierTravail(idTravail, titre, abstr);

				//erreurs pour les afficher.
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					//tenir compte du changement.
					session.setAttribute("travail", f.getTravail((int) session.getAttribute("idTravail")));
					request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
					request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
					request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
					request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
					request.setAttribute("versions",f.getVersions(idTravail));
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
				}
				else{
					request.setAttribute("titre", titre);
					request.setAttribute("abstr", abstr);
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/modifierTravail.jsp").forward(request, response);
					f.InitErreurs();
				}
			}
			break;

		case "annulerT" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un travail choisi.
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idTravail = (int) session.getAttribute("idTravail");
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}
			break;


		case "supprimerT" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un evenement choisi.
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				int idTravail = (int) session.getAttribute("idTravail");
				f.supprimerTravail(idTravail);
				request.setAttribute("travaux_non_soumis", f.getMesTravauxNonSoumis(f.listeTravauxNonSoumis(idPers)));
				request.setAttribute("travaux_soumis", f.getMesTravauxSoumis(f.listeTravauxSoumisNoRep(idPers)));
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/espacePerso.jsp").forward(request, response);
			}
			break;

////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL SOUMETTRE*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "Rsoumettre":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/soumettre.jsp").forward(request, response);
			}
			break;


		case "soumettre" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un travail choisi.
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				String idConfChoisi = request.getParameter("confChoisi");
				String idJournalChoisi = request.getParameter("journalChoisi");

				int idTravail = (int) session.getAttribute("idTravail");
				
				String isConf= request.getParameter("conf");


				if (isConf!=null && isConf.equals("cf")){
					f.soumettreConference(request, idTravail, idConfChoisi);
				}
				else {
					f.soumettreJournal(request, idTravail, idJournalChoisi);
				}
				

				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					session.setAttribute("travail", f.getTravail(idTravail));
					request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
					request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
					request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
					request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
					request.setAttribute("versions",f.getVersions(idTravail));
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
				}
				else{
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/soumettre.jsp").forward(request, response);
				}
			}
			break;

			
			
////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL CIBLER*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "Rcibler":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				int idTravail = (int) session.getAttribute("idTravail");
				request.setAttribute("confsPotentiel",f.getConfPotentiel(idTravail, idPers));
				request.setAttribute("journauxPotentiel",f.getJournalPotentiel(idTravail, idPers));
				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/cibler.jsp").forward(request, response);
			}
			break;


		case "cibler" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un travail choisi.
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				String[] confs = request.getParameterValues("confCible");

				String[] journaux = request.getParameterValues("journalCible");

				int idTravail = (int) session.getAttribute("idTravail");
				f.cibler(idTravail, confs, journaux);

				request.setAttribute("confsCibles",f.getConferencesCibles(idTravail));
				request.setAttribute("journauxCibles",f.getJournauxCibles(idTravail));
				request.setAttribute("confsSoumis",f.getConferencesSoumis(idTravail));
				request.setAttribute("journauxSoumis",f.getJournauxSoumisTravail(idTravail));
				request.setAttribute("versions",f.getVersions(idTravail));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/detailTravail.jsp").forward(request, response);
			}
			break;



////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TRAVAIL VERSION*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "detailVersion":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			//verifier si un travail choisi.
			else if( f.getVersion(Integer.parseInt(request.getParameter("id"))
					,(int) session.getAttribute("idTravail")) == null ){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				String sidVersion = request.getParameter("id");
				int idVersion = Integer.parseInt(sidVersion);
				int idTravail = (int) session.getAttribute("idTravail");
				Version version = f.getVersion(idVersion,idTravail);
				session.setAttribute("version", version);
				session.setAttribute("reponses", f.getReponses(version));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/detailVersion.jsp").forward(request, response);
			}


			break;


		case "RajouterReponse":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/ajouterReponse.jsp").forward(request, response);
			}
			break;


		case "ajouterReponse":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				String reviewer = request.getParameter("reviewer");

				String jugement = request.getParameter("jugement");

				String detail = request.getParameter("detail");

				byte[] ptext = detail.getBytes(ISO_8859_1);
				detail= new String(ptext, "UTF-8");
				

				Version version = (Version) session.getAttribute("version");
				
				String resultat= request.getParameter("resultat");
				boolean accepter=false;
				if (resultat!=null && resultat.equals("oui")){
					accepter=true;
				}
					
				f.ajouterReponse(version, reviewer, jugement, detail,accepter);
				
				request.setAttribute("erreurs", f.getErreurs());

				if(f.getErreurs().isEmpty()){
					session.setAttribute("reponses", f.getReponses(version));
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/detailVersion.jsp").forward(request, response);
				}
				else{
					this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/ajouterReponse.jsp").forward(request, response);
				}
			}


			break;


		case "reponses":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version =(Version) session.getAttribute("version");
				session.setAttribute("reponses", f.getReponses(version));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/reponses.jsp").forward(request, response);
			}


			break;


		case "rendrePublique":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version = (Version) session.getAttribute("version");
				f.rendrePublique(version);
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/detailVersion.jsp").forward(request, response);
			}


			break;

		case "NePlusRendrePublique":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version = (Version) session.getAttribute("version");
				f.nePlusRendrePublique(version);
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/detailVersion.jsp").forward(request, response);
			}


			break;

			
		case "supprimerReponse" :
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getReponse((String) request.getParameter("idRep"),
					((Version) session.getAttribute("version")).getId()) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version =(Version) session.getAttribute("version");
				int idTravail = (int) session.getAttribute("idTravail");
				String sidReponse = request.getParameter("idRep");
				int idReponse = Integer.parseInt(sidReponse);
				f.supprimerReponse(idTravail,version,idReponse);
				
				session.setAttribute("reponses", f.getReponses(version));
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/reponses.jsp").forward(request, response);
			}


			break;

			
		case "RchoisirPresentateur":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version =(Version) session.getAttribute("version");
				String nomPresentateur = version.getNomPresentateur();
				String prenomPresentateur = version.getPrenomPresentateur();
				if(nomPresentateur==null || prenomPresentateur==null){
					Personne pers = (Personne) session.getAttribute("Personne");
					nomPresentateur=pers.getNom();
					prenomPresentateur = pers.getPrenom();
				}
				request.setAttribute("nomPresentateur", nomPresentateur);
				request.setAttribute("prenomPresentateur", prenomPresentateur);
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/choisirPresentateur.jsp").forward(request, response);
			}
			break;
			
		case "choisirPresentateur":
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else if( f.getTravail((int) session.getAttribute("idTravail")) == null){
				this.getServletContext().getRequestDispatcher("/WEB-INF/EspacePersonnel/mur.jsp").forward(request, response);
			}
			else {
				Version version =(Version) session.getAttribute("version");
				String nomPresentateur = request.getParameter("nomPresentateur");
				String prenomPresentateur = request.getParameter("prenomPresentateur");
				if(nomPresentateur==null || prenomPresentateur==null){
					Personne pers = (Personne) session.getAttribute("Personne");
					nomPresentateur=pers.getNom();
					prenomPresentateur = pers.getPrenom();
				}
				f.choisirPresentateur(version,nomPresentateur,prenomPresentateur);
				this.getServletContext().getRequestDispatcher("/WEB-INF/Travail/Version/detailVersion.jsp").forward(request, response);
			}
			break;

////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************TELECHARGER FICHIERS*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "fichierStyleConf":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				int idConf = (int) session.getAttribute("idConf");
				System.out.println("**********************************");
				String fichierRequis = idPers+"_"+idConf+"_Conf.zip";
				System.out.println("le fichier requis est: "+fichierRequis );
				f.telechergerFichier(this,fichierRequis, response,chemin2);
			}


			break;


		case "fichierStyleJournal":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				int idPers = ((Personne) session.getAttribute("Personne")).getId();
				int idJournal = (int) session.getAttribute("idJournal");
				String fichierRequis = idPers+"_"+idJournal+"_Journal.zip";
				f.telechergerFichier(this,fichierRequis, response,chemin2);
			}


			break;


		case "fichier":
			//verifier s'il y a quelqu'un connecte ou pas
			if( f.getPersonne((String) (session.getAttribute("ident"))) == null){
				this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			}
			else {
				String fichierRequis = request.getParameter("nomFichier");
				f.telechergerFichier(this,fichierRequis, response,chemin);
				System.out.println("indice "+chemin+fichierRequis);
			}


			break;
		
		case "demo":
			String fichierRequis = request.getParameter("nomFichier");
			
			System.out.println("la demo est " + fichierRequis);
			f.telechergerFichier(this,fichierRequis, response,chemin);
			
			break;
			
////////////////////////////////////////////////////////////////////////////////////////////////////////
//***********************************DECONNEXION*******************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////
		case "deconnexion":
			f.deconnecter();
			session.invalidate();
			this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			break;
		}
	}

}
