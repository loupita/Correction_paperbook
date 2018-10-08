package ejb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Singleton
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class Facade {

	@PersistenceContext
	private EntityManager em;

	private Personne persConnecte=null;
	private static final int    taille   = 10240; //10 ko
	//resultat pour verification.
	private String resultat;
	private HashSet<String> erreurs = new HashSet<String>();

	private String format = "dd/MM/yyyy";
	private SimpleDateFormat formater = new SimpleDateFormat( format );
	private long CONST_DURATION_OF_DAY = 1000l * 60 * 60 * 24;

	//chemins pour ecrire les fichiers (publication + sources)
	private String chemin = "WEB-INF/monFichier/";
	private String chemin2 = "WEB-INF/ex_fichiers_excels/";
	//pour ecrire fichiers Excel
	private String cheminExcel = chemin2;





	//***************************************************getters***************************************************

	/**Renvoie resultat. */
	public String getResultat() {
		return resultat;
	}

	/**Retourne les erreurs. */
	public Collection<String> getErreurs() {
		return erreurs;
	}

	/**Renvoie la personne connecte. */
	public Personne getPersonneConnecte() {
		return persConnecte;
	}

	/**Ajouter un message d'erreur dans la liste erreurs. */
	public void setErreur(String message ) {
		erreurs.add(message );
	}

	/**Vider la liste des erreurs.*/
	public void InitErreurs(){
		erreurs.clear();
	}


	/**renvoie la Personne a partir de son id.*/
	public Personne getPersonne(int i) {
		return em.find(Personne.class, i);
	}

	/**renvoie la Personne a partir de son identifiant.*/
	public Personne getPersonne(String identifiant) {
		Personne p=null;
		Collection<Personne> personnes = em.createQuery("select p from Personne p where p.identifiant Like :identifiant").
				setParameter("identifiant",identifiant ).getResultList();
		Iterator<Personne> it = personnes.iterator();
		if(it.hasNext())
			p=it.next();
		return p;
	}

	/**Renvoie la conference à partir de son id(int). */
	public Conference getConference(int i) {
		Conference c = em.find(Conference.class, i);
		if(c!=null && c.getParticipant().getId()==persConnecte.getId()){
			return c;
		}
		return null;
	}

	/**Renvoie la conference à partir de son id(String). */
	public Conference getConference(String si) {
		return getConference(Integer.parseInt(si));
	}

	/**Renvoie le journal à partir de son id(int). */
	public Journal getJournal(int i) {
		Journal j = em.find(Journal.class, i);
		if(j!=null && j.getParticipant().getId()==persConnecte.getId()){
			return j;
		}
		return null;
	}

	/**Renvoie le journal à partir de son id(String). */
	public Journal getJournal(String si) {
		return getJournal(Integer.parseInt(si));
	}

	/**Renvoie le travail à partir de son id(int). */
	public Travail getTravail(int id) {
		Travail t = em.find(Travail.class,id);
		if(t!=null && t.getProprio().getId()==persConnecte.getId()){
			return t;
		}
		return null;
	}

	/**Renvoie le travail à partir de son id(String). */
	public Travail getTravail(String id) {
		return getTravail(Integer.parseInt(id));
	}

	/**Renvoie la version à partir de son id(int) et verifier que le travail est le meme. */
	public Version getVersion(int id, int idTravail){
		Version v = em.find(Version.class, id);
		if(v!= null && v.getTravail().getId()==idTravail){
			return v;
		}
		return null;

	}

	/**Renvoie la version à partir de son id(int) verifier que la version est publique. */
	public Version getVersion(int id){
		Version v = em.find(Version.class, id);
		if(v!= null && v.isPublique())
			return v;
		return null;
	}
	/**Renvoie la version à partir de son id(String) verifier que la version est publique. */
	public Version getVersion(String id){
		return getVersion(Integer.parseInt(id));
	}

	/**Renvoie la reponse à partir de son id(int). */
	public Reponse getReponse(int id, int idVersion){
		Reponse r = em.find(Reponse.class, id);
		if(r!=null && r.getVersion().getId() == idVersion)
			return r;
		return null;
	}

	/**Renvoie la reponse à partir de son id(String). */
	public Reponse getReponse(String id, int idVersion){
		return getReponse(Integer.parseInt(id),idVersion);
	}

	//*************************************************Validations***********************************************

	/** valider un titre
	 * 
	 * @param titre titre à valider.
	 * @throws Exception
	 */
	private void validationTitre(String titre) throws Exception {
		if (titre.equals("") || titre==null) {
			throw new Exception("Veuillez donner un titre");
		}
		else if ( titre.contains("\"")) {
			throw new Exception( "Caractère spécial \" interdit" );
		}
	}

	/** valider un texte
	 * 
	 * @param texte texte à valider.
	 * @throws Exception
	 */
	private void validationTexte(String texte) throws Exception {
		if ( texte.contains("\"")) {
			throw new Exception( "Caractère spécial \" interdit" );
		}
	}

	/** Valider un email
	 * 
	 * @param email
	 * @throws Exception
	 */
	private void validationEmail( String email ) throws Exception {
		if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
			throw new Exception( "Merci de saisir une adresse mail valide." );
		}
	}

	/** Valider un identifiant
	 * 
	 * @param ident
	 * @throws Exception
	 */
	private void validationIdent( String ident ) throws Exception {
		if(ident==null){
			throw new Exception("Veuillez saisir un identifiant.");
		}
		else {
			if ( !ident.matches("[a-zA-Z0-9 ]*")) {
				throw new Exception( "Caractères spéciaux interdits" );
			}
			Collection<Personne> personnes = em.createQuery("select p from Personne p where p.identifiant Like :identUtil").
					setParameter("identUtil",ident ).getResultList();
			if(!personnes.isEmpty()){
				throw new Exception("Identifiant deja utilise saisissez un autre. ");
			}
		}
	}

	/** Validation d'un mot de passe.
	 * @param mdp le mot de passe
	 * @param conf confirmation du mot de passe
	 * @throws Exception
	 */
	private void validationMdp( String mdp , String conf ) throws Exception {
		if(mdp==null || conf==null){
			throw new Exception("Veuillez saisir un mot de passe.");
		}
		else {
			if ( !mdp.matches("[a-zA-Z0-9 ]*")) {
				throw new Exception( "Caractères spéciaux interdits" );
			}
			if(mdp.length()<3){
				throw new Exception("il doit y avoir au moins 3 caracteres. ");
			}
			if(!mdp.equals(conf)){
				throw new Exception("Erreur dans la saisie du mot de passe. ");
			}
		}
	}

	/** Vérifier si une chaîne de caractères est un chiffre.
	 * 
	 * @param str 
	 * @throws Exception
	 */
	private void validationIsNumeric(String str) throws Exception{
		for (char c : str.toCharArray()){
			if (!Character.isDigit(c)) throw new Exception("durée doit être un chiffre");
		}
	}

	/** Validation de l'authentification d'un utilisateur
	 * 
	 * @param ident identifiant 
	 * @param mdp le mot de passe
	 * @return la personne qui s'est authentifié
	 * @throws Exception
	 */
	private Personne validationAuth(String ident,String mdp) throws Exception {
		Personne p = null;
		if ( ident != null ) {
			Collection<Personne> personnes = em.createQuery("select p from Personne "
					+ "p where p.identifiant Like :identUtil").
					setParameter("identUtil",ident ).getResultList();
			Iterator<Personne> it = personnes.iterator();
			if(it.hasNext())
				p=it.next();

			if(p==null){
				throw new Exception("Identifiant incorrect. ");
			}
			else {
				if ( mdp != null ) {
					if(!p.getMdp().equals(mdp)){
						throw new Exception( "mot de passe incorrect." );
					}
				}

				else {
					throw new Exception( "Merci de saisir votre mot de passe." );
				}
			}
		}
		else{
			throw new Exception( "Merci de saisir votre identifiant." );
		}
		return p;
	}


	/** Validation du nom et prenom lors de l'inscription.
	 * 
	 * @param nom
	 * @param prenom
	 * @throws Exception
	 */
	private void validationNomPrenom( String nom,String prenom) throws Exception {
		if ( nom.equals("") || prenom.equals("")) {
			throw new Exception( "Merci de renseigner tous les champs." );
		}
		if ( !nom.matches("[a-zA-Z0-9 ]*") || !prenom.matches("[a-zA-Z0-9 ]*")) {
			throw new Exception( "Caractères spéciaux interdits" );
		}
	}

	/** Validation de la date (dd/mm/yyyy) et doit être supérieur ou égale à la date
	 * d'aujourd'hui
	 * @param date
	 * @throws Exception
	 */
	int cpt=0;
	private void validationDate( final String date) throws Exception {
		formater.setLenient(false);
		cpt++;
		if (date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")){
			try {
				System.out.println("####################################");
				Date d = formater.parse(date);
				System.out.println("la date entrer est :"+ d);
				Date today = new Date();
				System.out.println("la date d'aujourd'hui est :" +today);
				/*if (d.compareTo(today ) == -1) {
					// traitement du cas d < today
					throw new Exception("date invalide, nous sommes le : "+formater.format(today));
				} */
			} catch (ParseException e) {
				
				throw new Exception( "date invalide" );
			}
			catch(Exception e){
				throw new Exception( "date invalide");
			}
		}

		else {
			throw new Exception( "date invalide. la date doit être sous format (dd/mm/yyyy)");
		}
	}


	//*************************************************************************************************************


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************INSCRIPTION/CONNEXION*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** S'inscrire en fournissant les informations (ci-dessous)
	 * 
	 * @param nom
	 * @param prenom
	 * @param email
	 * @param ident
	 * @param mdp
	 * @param confmdp
	 */
	public void inscrire(String nom,String prenom, String email,String ident,
			String mdp ,String confmdp){
		try{
			validationNomPrenom(nom,prenom);
		} catch(Exception e){
			setErreur(e.getMessage());
			System.out.println("validationNomPrenom erreur");
		}

		try {
			validationEmail(email);
		} catch (Exception e) {
			setErreur(e.getMessage());
			System.out.println("validationEmail erreur");
		}
		try {
			validationIdent(ident);
		} catch (Exception e) {
			setErreur(e.getMessage());
			System.out.println("validationIdent erreur");
		}
		try {
			validationMdp(mdp,confmdp);
		} catch (Exception e) {
			setErreur(e.getMessage());
			System.out.println("validationMdp erreur");
		}

		if(erreurs.isEmpty()){
			Personne p = new Personne(nom,prenom,email,ident,mdp);
			em.persist(p);
			persConnecte = p;
		}
	}



	/** Se connecter en fournissant identifiant et mot de passe.
	 * 
	 * @param ident
	 * @param motDePasse
	 */
	public void connecter( String ident , String motDePasse ) {
		/* Validation de l'authentification. */
		Personne p = null;
		try {
			p=validationAuth( ident, motDePasse );
		} catch ( Exception e ) {
			setErreur(e.getMessage() );
			System.out.println("erreur dans validationAuth");
		}

		/* Initialisation du resultat global de la validation. */
		if ( erreurs.isEmpty() ) {
			resultat = "Succes de la connexion.";
			if(p!=null) 
				persConnecte=p;
		} 
		else {
			resultat = "echec de la connexion.";
		}
	}



	//	/**changer mot de passe.*/
	//	public void updatePassword(String oldpwd, String newpwd, String confpwd) {
	//		if(!persConnecte.getMdp().equals(oldpwd)){
	//			erreurs.add("mot de passe incorrect." );
	//		}
	//		else{
	//			try {
	//				this.validationMdp(newpwd, confpwd);
	//			} catch (Exception e) {
	//				setErreur(e.getMessage() );
	//			}
	//			if ( erreurs.isEmpty() ) {
	//				resultat = "Succes de la connexion.";
	//				getPersonne(persConnecte.getId()).setMdp(newpwd);//pour modifier dans la base de donner
	//				persConnecte=getPersonne(persConnecte.getId());//mettre a jour le personne connecter
	//			}
	//		}
	//	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO TRAVAUX EN COURS*************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** renvoyer la liste des travaux non soumis d'une personne.
	 * @param idPers l'id de la personne. 
	 * @return liste des travaux non soumis de la personne (idPers)
	 */
	public Collection<Travail> listeTravauxNonSoumis(int idPers){
		Collection<Travail> travaux = new LinkedList<Travail>();
		for(Travail t : getMesTravaux(idPers)){
			if(getVersions(t.getId()).isEmpty())
				t.setSoumis(false);
			if(!t.getSoumis())
				travaux.add(t);
		}
		return travaux;
	}

	/** Associe à chaque travail le nb de jours restant (le minimum) pour l'abstract d'un 
	 * événement et renvoie la liste des travaux
	 * @param travaux pour lesquels on associe le nb de jour restant
	 * @return renvoie les travaux non soumis
	 */
	public Collection<Travail> getMesTravauxNonSoumis(Collection<Travail> travaux){
		if(travaux==null || travaux.isEmpty()){
			return new LinkedList<Travail>();
		}
		Date today = new Date();
		for(Travail t :travaux){

			Collection<Conference> confsCibles = getConferencesCibles(t.getId());
			Collection<Journal> journauxCibles = getJournauxCibles(t.getId());
			List<Date> dates = new LinkedList<Date>();
			for(Conference c : confsCibles){
				Date d;
				try {
					if(!c.getDateAbstractSubmission().equals("None")){
						d = formater.parse(c.getDateAbstractSubmission());
						dates.add(d);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			for(Journal j : journauxCibles){
				Date d;
				try {
					if(!j.getDateAbstractSubmission().equals("None")){
						d = formater.parse(j.getDateAbstractSubmission());
						dates.add(d);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(dates.isEmpty()){
				t.setJourRestant(-1000000);
			}
			else {
				Collections.sort(dates);
				Date datePlusProche = dates.get(0);
				long diff = datePlusProche.getTime() - today.getTime();
				long nb = (long)diff/CONST_DURATION_OF_DAY;
				t.setJourRestant(nb);
				em.createNativeQuery("update Travail SET jourRestant='"+nb+"' where id="+t.getId()+";").executeUpdate();
			}

		}
		return travaux;
	}

	/** renvoyer la liste des travaux soumis d'une personne.
	 * 
	 * @param idPers l'id de la personne. 
	 * @return liste des travaux soumis de la personne (idPers)
	 */
	public Collection<Travail> listeTravauxSoumisNoRep(int idPers){
		Collection<Travail> travaux = new LinkedList<Travail>();
		for(Travail t : getMesTravaux(idPers)){
			if(getVersions(t.getId()).isEmpty())
				t.setSoumis(false);
			if(t.getSoumis() && !t.isHasResponse())
				travaux.add(t);


		}
		return travaux;
	}

	/** Associe à chaque travail le nb de jours restant (le minimum) pour recevoir le résultat 
	 * d'un événement et renvoie la liste des travaux
	 * @param travaux pour lesquels on associe le nb de jour restant
	 * @return renvoie les travaux soumis
	 */
	public Collection<Travail> getMesTravauxSoumis(Collection<Travail> travaux){
		if(travaux==null || travaux.isEmpty())
			return new LinkedList<Travail>();
		Date today = new Date();
		for(Travail t :travaux){

			Collection<Conference> confsSoumis = getConferencesSoumis(t.getId());
			List<Date> dates = new LinkedList<Date>();
			for(Conference c : confsSoumis){
				Date d;
				try {
					if(!c.getDateNotifAcceptance().equals("None")){
						d = formater.parse(c.getDateNotifAcceptance());
						dates.add(d);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			if(dates.isEmpty())
				t.setJourRestant(-1000000);
			else {
				Collections.sort(dates);
				Date datePlusProche = dates.get(0);
				long diff = datePlusProche.getTime() - today.getTime();
				long nb = (long)diff/CONST_DURATION_OF_DAY;
				t.setJourRestant(nb);
				em.createNativeQuery("update Travail SET jourRestant='"+nb+"' where id="+t.getId()+";").executeUpdate();
			}
		}
		return travaux;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO MUR*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Renvoie la liste des publications rendues publiques
	 * 
	 * @return la liste des publications rendues publiques
	 */
	public Collection<Version> getVersionsPubliques(){
		Collection<Version> versions = em.createQuery("select v from "
				+ "Version v where v.publique Like :publique").
				setParameter("publique",true).getResultList();
		return versions;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO CONFERENCES*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Renvoie la liste des conférences d'une personne(idPers).
	 * @param idPers
	 * @return la liste des conférences d'une personne(idPers).
	 */
	public Collection<Conference> getMesConfs(int idPers){
		Collection<Conference> events = new LinkedList<Conference>();
		events = em.createQuery("select e from Conference e "
				+ "where e.participant Like :Pers ").setParameter("Pers",getPersonne(idPers))
				.getResultList();
		return events;
	}


	/** Renvoie la liste des conférences d'une personne(idPers) à partir d'une certaine page.
	 * @param idPers
	 * @param page la page chargee
	 * @return la liste des conférences d'une personne(idPers).
	 */
	public Collection<Conference> getMesConfs(int idPers,int page){
		LinkedList<Conference> inter = new LinkedList<Conference>();
		Collection<Conference> results = new LinkedList<Conference>();
		Collection<Conference> events = em.createQuery("select e from Conference e "
				+ "where e.participant Like :Pers").setParameter("Pers",getPersonne(idPers))
				.getResultList();
		inter.addAll(events);
		int cpt=(page-1)*10;
		while(cpt<(page*10) && cpt<events.size()){
			results.add(inter.get(cpt));
			cpt++;
		}
		return results;
	}


	/** Modifier les informations sur une conference en fournissant les informations ci-dessous.
	 * 
	 * @param request requete
	 * @param idEvt id de la conference
	 * @param titrerec titre
	 * @param acr acronym
	 * @param siterec le lien de la conference
	 * @param rangrec le rang
	 * @param inforec info sur le style
	 * @param paysrec le lieu
	 * @param date1rec date de la conférence
	 * @param date2rec date de l'abstract 
	 * @param date3rec date de la soumission du papier
	 * @param dateRrec date rebuttal
	 * @param date4rec date notification acceptance
	 */
	public void modifierConf(HttpServletRequest request,int idEvt,String titrerec,String acr, String siterec, String rangrec,String inforec, String paysrec, 
			String date1rec, String date2rec, String date3rec, String dateRrec, String date4rec){
		try {
			validationDate(date1rec);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationDate(date2rec);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationDate(date3rec);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationDate(date4rec);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}


		try {
			validationDate(dateRrec);
		} catch(Exception e) {
			dateRrec="";
		}

		try {
			validationTitre(titrerec);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationTitre(acr);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationTexte(inforec);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		if(erreurs.isEmpty()){
			int n= em.createNativeQuery("update Conference SET acronym='"+acr+"', rang = '"+rangrec+"', titre='" 
					+titrerec+"', site='"+siterec+"', dateAbstractSubmission='"+date2rec+"', dateEvt='"+date1rec+"',"
					+ "dateNotifAcceptance='"+date4rec+"', datePaperSubmission='"+date3rec+"',"
					+ "info ='"+inforec+"', lieuEvt ='"+paysrec+"', dateRebuttal='"+dateRrec+"' where id="+idEvt+";").executeUpdate();


			Conference e = em.find(Conference.class, idEvt);
			String nomFichierStyle = persConnecte.getId()+"_"+e.getId()+"_Conf.zip";
			String fichier = enregistrerFichierZipStyle( request,nomFichierStyle,chemin2);
			
			if(fichier!=null && !fichier.isEmpty()){
				System.out.println("fichiers"+fichier);
				e.setNomFichierStyle(nomFichierStyle);
			}
			if(!erreurs.isEmpty()){
				System.out.println("erreurs non vide, on supprime la conference !");
				em.remove(e);
			}
		}

	}

	/** Supprimer une conference
	 * 
	 * @param id id de la conference à supprimer
	 */
	public void supprimerConf(int id){
		Conference c = getConference(id);
		em.createNativeQuery("delete from Travail_ConfsCibles where conference_id='"+id+"'").executeUpdate();
		em.createNativeQuery("delete from Travail_ConfsSoumis where confS_id='"+id+"'").executeUpdate();
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "conference_id="+id+";").getResultList();
		for(Integer i : ids){
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}
		int n= em.createNativeQuery("delete from Conference where id='"+id+"'").executeUpdate();
		File style = new File(chemin2+c.getNomFichierStyle());
		if(style!=null)
			style.delete();
		System.out.println("n : "+n);
	}


	/** 
	 * @param idConf id de la conference
	 * @return liste des travaux(versions) qui ont été soumis pour cette conférence.
	 */
	public Collection<Version> getConferenceTravauxSoumis(int idConf){
		Set<Version> versions = new HashSet<Version>();
		Collection<Integer> ids = em.createNativeQuery("select id from "
				+ "Version where conference_id='"+idConf+"'").getResultList();
		for(int i : ids){
			Version c = em.find(Version.class, i);
			if(c!=null)
				versions.add(c);
		}
		return versions;
	}

	/** 
	 * @param idConf id de la conference
	 * @return liste des travaux(versions) qui ciblent cette conférence.
	 */
	public Collection<Travail> getConferenceTravauxCiblant(int idConf){
		Set<Travail> travaux = new HashSet<Travail>();
		//Ne pas inclure les travaux ayant soumis avec les travaux ciblants
		Set<Travail> travauxSoumis = new HashSet<Travail>();
		Collection<Version> versions = getConferenceTravauxSoumis(idConf);
		for(Version v : versions ){
			travauxSoumis.add(v.getTravail());
		}
		Collection<Integer> ids = em.createNativeQuery("select travail_id from "
				+ "Travail_ConfsCibles where conference_id='"+idConf+"'").getResultList();
		for(int i : ids){
			Travail c = em.find(Travail.class, i);
			if(c!=null && !travauxSoumis.contains(c) )
				travaux.add(c);
		}
		return travaux;
	}


	/** Extrait les conferences dans un fichier Excel.
	 * @param idPers
	 * @param servlet
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	public void exporterExcelConf(int idPers,HttpServlet servlet, HttpServletResponse response){
		HSSFWorkbook wb = new HSSFWorkbook();
		//feuille excel
		HSSFSheet sheet = wb.createSheet("Conferences");
		//Titres des colonnes.
		// Aqua background
		HSSFRow row = sheet.createRow(0);
		CellStyle style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.BIG_SPOTS);
		//Titre
		Cell cell0 = row.createCell((short) 0);
		cell0.setCellValue("Titre");
		cell0.setCellStyle(style);
		//Acronym
		Cell cell1 = row.createCell((short) 1);
		cell1.setCellValue("Acronym");
		cell1.setCellStyle(style);
		//Rang
		Cell cell2 = row.createCell((short) 2);
		cell2.setCellValue("Rang");
		cell2.setCellStyle(style);
		//Lieu
		Cell cell3 = row.createCell((short) 3);
		cell3.setCellValue("Lieu");
		cell3.setCellStyle(style);
		//Date conference
		Cell cell4 = row.createCell((short) 4);
		cell4.setCellValue("Date conference");
		cell4.setCellStyle(style);
		//Date Abstract
		Cell cell5 = row.createCell((short) 5);
		cell5.setCellValue("Date Abstract");
		cell5.setCellStyle(style);
		//Date Soumission
		Cell cell6 = row.createCell((short) 6);
		cell6.setCellValue("Date Soumission");
		cell6.setCellStyle(style);
		//Date Rebuttal
		Cell cell7 = row.createCell((short) 7);
		cell7.setCellValue("Date Rebuttal");
		cell7.setCellStyle(style);
		//Date Notification
		Cell cell8 = row.createCell((short) 8);
		cell8.setCellValue("Date Notification");
		cell8.setCellStyle(style);

		int cpt=1;
		for(Conference c : getMesConfs(idPers)){
			row = sheet.createRow(cpt);
			//titre
			row.createCell((short)0,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getTitre());
			//Acronym
			row.createCell((short)1,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getAcronym());
			//Rang
			row.createCell((short)2,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getRang());
			//Lieu
			row.createCell((short)3,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getLieuEvt());
			//Date Conference
			row.createCell((short)4,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDateEvt());
			//Date Abstract
			row.createCell((short)5,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDateAbstractSubmission());
			//Date Soumission
			row.createCell((short)6,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDatePaperSubmission());
			//Date Rebuttal
			if(c.getDateRebuttal()==null || c.getDateRebuttal().isEmpty())
				row.createCell((short)7,HSSFCell.CELL_TYPE_STRING).setCellValue("None");
			else
				row.createCell((short)7,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDateRebuttal());
			//Date Notification
			row.createCell((short)8,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDateNotifAcceptance());
			cpt++;
		}

		FileOutputStream fileOut;
		try {
			String fichier = servlet.getServletContext().getRealPath(cheminExcel)+idPers+"_"+"conferences.xls";
			fileOut = new FileOutputStream(fichier);
			wb.write(fileOut);
			fileOut.close();

			telechergerFichier(servlet, idPers+"_"+"conferences.xls", response, cheminExcel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Importer une feuille Excel
	 * */
	public void importerExcelConf(int idPers, HttpServletRequest request){

		//Récupération du contenu du champ fichier du formulaire.
		String nomFichier = null;
		InputStream contenuFichier = null;
		try {
			Part part = request.getPart("feuille");
			//recuperer nom fichier juste pour la validation.
			nomFichier = getNomFichier( part );
			try {
				ValidationFichierExcel(nomFichier);
				contenuFichier = part.getInputStream();
			} catch (Exception e) {
				setErreur(e.getMessage());
			}
		}
		catch ( IllegalStateException e ) {
			e.printStackTrace();
			setErreur("Les données envoyées sont trop volumineuses." );
		} catch ( IOException e ) {
			e.printStackTrace();
			setErreur("Erreur de configuration du serveur." );
		} catch ( ServletException e ) {
			e.printStackTrace();
			setErreur("Ce type de requete n'est pas supporte, merci d'utiliser "
					+ "le formulaire prevu pour envoyer votre fichier." );
		}

		// Si aucune erreur n'est survenue.
		if ( erreurs.isEmpty() ) {
			// Validation du fichier.
			try {
				validationFichier( contenuFichier );
			} catch ( Exception e1 ) {
				setErreur(e1.getMessage() );
			}
		}

		// Si aucune erreur n'est survenue jusqu'à présent */
		if ( erreurs.isEmpty() ) {
			try {
				Workbook wb = WorkbookFactory.create(contenuFichier);
				org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
				int rowsCount = sheet.getLastRowNum();
				System.out.println("Total Number of Rows: " + (rowsCount + 1));
				//				//l'indice de chaque colonne (titre(j), acronym(k), rang(z)). 
				//				Row row0 = sheet.getRow(0);
				//				int j = 0;
				//				while(!row0.getCell(j).equals("Title") && j<10){
				//					j++;
				//				}
				//				int k = 0;
				//				while(!row0.getCell(k).equals("Acronym") && j<10){
				//					j++;
				//				}
				//				int z = 0;
				//				while(!row0.getCell(z).equals("Rank") && j<10){
				//					j++;
				//				}
				if(rowsCount <= 0)
					return ;
				for (int i = 1; i <= rowsCount; i++) {
					Row row = sheet.getRow(i);
					int colCounts = row.getLastCellNum();
					//					System.out.println("Total Number of Cols: " + colCounts);
					if(colCounts <= 0)
						return ;
					Cell cellTitre = row.getCell(1);
					String titre = cellTitre.getStringCellValue();

					Cell cellAcr = row.getCell(2);
					String acr = cellAcr.getStringCellValue();

					Cell cellRang = row.getCell(3);
					String rang = cellRang.getStringCellValue();

					//					System.out.println("[CONFERENCE : ] "+titre+" Acr : "+acr+" Rang :"+rang+";");
					try {
						validationTitre(titre);
					} catch (Exception e) {
						setErreur(e.getMessage());
					}

					if(erreurs.isEmpty() && persConnecte != null){
						Conference e = new Conference(titre,acr, "None", rang, "None", "None", "None", "None", "None","None");
						e.setParticipant(persConnecte);
						em.persist(e);
					}
				}
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	/**
	 * @param idPers
	 * @return le nb de page de liste de journaux
	 */
	public int getNbPagesC(int idPers){
		int quotient = (getMesConfs(idPers).size())/10;
		int reste = getMesConfs(idPers).size()-10*quotient;
		if (reste!=0)
			return quotient+1;
		else 
			return quotient;
	}

	/** Supprimer les conferences cochees
	 * @param idConfs les id des conferences cochees
	 */
	public void supprimerConferences(String[] idConfs){
		if(idConfs==null)
			return;
		for(String id : idConfs){
			supprimerConf(Integer.parseInt(id));
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO JOURNAUX*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Renvoie la liste des journaux d'une personne(idPers).
	 * @param idPers id de la personne
	 * @return renvoie la liste des journaux enregistrée d'une personne
	 */
	public Collection<Journal> getMesJournaux(int idPers){
		Collection<Journal> events = new LinkedList<Journal>();
		events = em.createQuery("select e from Journal e "
				+ "where e.participant Like :Pers ").setParameter("Pers",getPersonne(idPers))
				.getResultList();
		return events;
	}

	/** Renvoie la liste des journaux d'une personne(idPers) à partir d'une certaine page.
	 * @param idPers id de la personne
	 * @param page la page chargee
	 * @return renvoie la liste des journaux enregistrée d'une personne
	 */
	public Collection<Journal> getMesJournaux(int idPers, int page){
		LinkedList<Journal> inter = new LinkedList<Journal>();
		Collection<Journal> results = new LinkedList<Journal>();
		Collection<Journal> events = em.createQuery("select e from Journal e "
				+ "where e.participant Like :Pers").setParameter("Pers",getPersonne(idPers))
				.getResultList();
		inter.addAll(events);
		int cpt=(page-1)*10;
		while(cpt<(page*10) && cpt<events.size()){
			results.add(inter.get(cpt));
			cpt++;
		}
		return results;
	}

	/** Modifier les informations d'un journal un en fournissant les informations ci-dessous.
	 * 
	 * @param request requete
	 * @param idEvt id de la conference
	 * @param titrerec titre
	 * @param siterec le lien du journal
	 * @param rangrec le rang
	 * @param inforec info sur le style
	 * @param date2rec date de l'abstract 
	 * @param date3rec date de la soumission du papier
	 * @param dateRrec date rebuttal
	 */
	public void modifierJournal(HttpServletRequest request, int idEvt,String titrerec, String siterec, String rangrec,String inforec, 
			String date2rec, String date3rec, String dateRrec){
		try {
			validationDate(date2rec);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationDate(date3rec);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationDate(dateRrec);
		} catch(Exception e) {
			dateRrec="";
		}

		try {
			validationTitre(titrerec);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationTexte(inforec);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		if(erreurs.isEmpty()){
			int n= em.createNativeQuery("update Journal SET  rang = '"+rangrec+"', titre='" 
					+titrerec+"', site='"+siterec+"', dateAbstractSubmission='"+date2rec+"', "
					+ " datePaperSubmission='"+date3rec+"', dateRebuttal='"+dateRrec+"',"
					+ "info ='"+inforec+"' where id="+idEvt+";").executeUpdate();

			Journal e = em.find(Journal.class, idEvt);
			String nomFichierStyle = persConnecte.getId()+"_"+e.getId()+"_Journal.zip";
			String fichier = enregistrerFichierZipStyle( request,nomFichierStyle,chemin2);
			if(fichier!=null && !fichier.isEmpty()){
				
				e.setNomFichierStyle(nomFichierStyle);
			}
			if(!erreurs.isEmpty()){
				System.out.println("erreurs non vide, on supprime la conference !");
				em.remove(e);
			}
		}

	}

	/** supprimer un journal
	 * @param id id du journal à supprimer
	 */
	public void supprimerJournal(int id){
		Journal j = getJournal(id);
		em.createNativeQuery("delete from Travail_JournalCibles where journal_id='"+id+"'").executeUpdate();
		em.createNativeQuery("delete from Travail_JournalSoumis where journalS_id='"+id+"'").executeUpdate();
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "journal_id="+id+";").getResultList();
		for(Integer i : ids){
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}
		em.createNativeQuery("delete from Journal where id='"+id+"'").executeUpdate();
		File style = new File(chemin2+j.getNomFichierStyle());
		if(style!=null)
			style.delete();
	}

	/**
	 * @param idJournal
	 * @return liste des travaux soumis pour ce journal
	 */
	public Collection<Version> getJournalTravauxSoumis(int idJournal){
		Set<Version> versions = new HashSet<Version>();
		Collection<Integer> ids = em.createNativeQuery("select id from "
				+ "Version where journal_id='"+idJournal+"'").getResultList();
		for(int i : ids){
			Version c = em.find(Version.class, i);
			if(c!=null)
				versions.add(c);
		}
		return versions;
	}


	/**
	 * @param idJournal
	 * @return liste des travaux(versions) qui ciblent ce journal
	 */
	public Collection<Travail> getJournalTravauxCiblant(int idJournal){
		Set<Travail> travaux = new HashSet<Travail>();
		//Ne pas inclure les travaux ayant soumis avec les travaux ciblants
		Set<Travail> travauxSoumis = new HashSet<Travail>();
		Collection<Version> versions = getJournalTravauxSoumis(idJournal);
		for(Version v : versions ){
			travauxSoumis.add(v.getTravail());
		}
		Collection<Integer> ids = em.createNativeQuery("select travail_id from "
				+ "Travail_JournalCibles where journal_id='"+idJournal+"'").getResultList();
		for(int i : ids){
			Travail c = em.find(Travail.class, i);
			if(c!=null && !travauxSoumis.contains(c))
				travaux.add(c);
		}
		return travaux;
	}



	/** Extrait les conferences dans un fichier Excel.
	 * @param idPers
	 * @param servlet
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	public void exporterExcelJournaux(int idPers,HttpServlet servlet, HttpServletResponse response){
		HSSFWorkbook wb = new HSSFWorkbook();
		//feuille excel
		HSSFSheet sheet = wb.createSheet("Journaux");
		//Titres des colonnes.
		// Aqua background
		HSSFRow row = sheet.createRow(0);
		CellStyle style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.BIG_SPOTS);
		//Titre
		Cell cell0 = row.createCell((short) 0);
		cell0.setCellValue("Titre");
		cell0.setCellStyle(style);
		//Rang
		Cell cell1 = row.createCell((short) 1);
		cell1.setCellValue("Rang");
		cell1.setCellStyle(style);
		//Date Abstract
		Cell cell2 = row.createCell((short) 2);
		cell2.setCellValue("Date Abstract");
		cell2.setCellStyle(style);
		//Date Soumission
		Cell cell3 = row.createCell((short) 3);
		cell3.setCellValue("Date Soumission");
		cell3.setCellStyle(style);
		//Date Rebuttal
		Cell cell4 = row.createCell((short) 4);
		cell4.setCellValue("Date Rebuttal");
		cell4.setCellStyle(style);

		int cpt=1;
		for(Journal c : getMesJournaux(idPers)){
			row = sheet.createRow(cpt);
			//titre
			row.createCell((short)0,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getTitre());
			//Rang
			row.createCell((short)1,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getRang());
			//Date Abstract
			row.createCell((short)2,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDateAbstractSubmission());
			//Date Soumission
			row.createCell((short)3,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDatePaperSubmission());
			//Date Rebuttal
			if(c.getDateRebuttal()==null || c.getDateRebuttal().isEmpty())
				row.createCell((short)4,HSSFCell.CELL_TYPE_STRING).setCellValue("None");
			else
				row.createCell((short)4,HSSFCell.CELL_TYPE_STRING).setCellValue(c.getDateRebuttal());
			cpt++;
		}

		FileOutputStream fileOut;
		try {
			String fichier = servlet.getServletContext().getRealPath(cheminExcel)+idPers+"_"+"journaux.xls";
			fileOut = new FileOutputStream(fichier);
			wb.write(fileOut);
			fileOut.close();

			telechergerFichier(servlet, idPers+"_"+"journaux.xls", response, cheminExcel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 * Importer une feuille Excel
	 * */
	public void importerExcelJournaux(int idPers, HttpServletRequest request){

		//Récupération du contenu du champ fichier du formulaire.
		String nomFichier = null;
		InputStream contenuFichier = null;
		try {
			Part part = request.getPart("feuille");
			//recuperer nom fichier juste pour la validation.
			nomFichier = getNomFichier( part );
			try {
				ValidationFichierExcel(nomFichier);
				contenuFichier = part.getInputStream();
			} catch (Exception e) {
				setErreur(e.getMessage());
			}
		}
		catch ( IllegalStateException e ) {
			e.printStackTrace();
			setErreur("Les données envoyées sont trop volumineuses." );
		} catch ( IOException e ) {
			e.printStackTrace();
			setErreur("Erreur de configuration du serveur." );
		} catch ( ServletException e ) {
			e.printStackTrace();
			setErreur("Ce type de requete n'est pas supporte, merci d'utiliser "
					+ "le formulaire prevu pour envoyer votre fichier." );
		}

		// Si aucune erreur n'est survenue.
		if ( erreurs.isEmpty() ) {
			// Validation du fichier.
			try {
				validationFichier( contenuFichier );
			} catch ( Exception e1 ) {
				setErreur(e1.getMessage() );
			}
		}

		// Si aucune erreur n'est survenue jusqu'à présent */
		if ( erreurs.isEmpty() ) {
			try {
				Workbook wb = WorkbookFactory.create(contenuFichier);
				org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
				int rowsCount = sheet.getLastRowNum();
				if(rowsCount <= 0)
					return ;
				for (int i = 1; i <= rowsCount; i++) {
					Row row = sheet.getRow(i);
					int colCounts = row.getLastCellNum();
					if(colCounts <= 0)
						return ;

					Cell cellTitre = row.getCell(2);
					String titre = cellTitre.getStringCellValue();

					Cell cellRang = row.getCell(1);
					String rang = cellRang.getStringCellValue();

					try {
						validationTitre(titre);
					} catch (Exception e) {
						setErreur(e.getMessage());
					}

					if(erreurs.isEmpty() && persConnecte != null){
						Journal e = new Journal(titre, "None", rang, "None", "None", "None");
						e.setParticipant(persConnecte);
						em.persist(e);
					}
				}
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	/**
	 * @param idPers
	 * @return le nb de page de liste de journaux
	 */
	public int getNbPagesJ(int idPers){
		int quotient = (getMesJournaux(idPers).size())/10;
		int reste = getMesJournaux(idPers).size()-10*quotient;
		if (reste!=0)
			return quotient+1;
		else 
			return quotient;
	}



	/** Supprimer les journaux coches
	 * @param idConfs les id des journaux coches
	 */
	public void supprimerJournaux(String[] idJournaux){
		if(idJournaux==null)
			return;
		for(String id : idJournaux){
			supprimerJournal(Integer.parseInt(id));
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO TRAVAUX*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 
	 * @param IdPers
	 * @return liste des travaux d'une personne(IdPers)
	 */
	public Collection<Travail> getMesTravaux(int IdPers){
		Collection<Travail> travaux = em.createQuery("select p from "
				+ "Travail p where p.proprio Like :Pers").
				setParameter("Pers",getPersonne(IdPers)).getResultList();
		return travaux; 
	}

	/**
	 * @param idPers
	 * @return liste des travaux qui ont reçu une réponse.
	 */
	public Collection<Travail> listeTravauxSoumisAvecRep(int idPers){
		Collection<Travail> travaux = new LinkedList<Travail>();
		for(Travail t : getMesTravaux(idPers)){
			if(t.getSoumis() && t.isHasResponse())
				travaux.add(t);
		}
		return travaux;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO STATS*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Calcule pour chaque Conference/Journal le nb de papier, accepté, refusé.
	 * @param events
	 */
	private void calculPapierResult(Collection<Evenement> events){
		Collection<Object> nbPapier = new HashSet<Object>();
		Collection<Object> nbPapierA=new HashSet<Object>();
		Collection<Object> nbPapierR=new HashSet<Object>();
		for(Evenement evt : events){
			int idEvt = evt.getId();
			if(evt instanceof Conference){
				nbPapier = em.createNativeQuery("select * from Version "
						+ "where conference_id='"+idEvt+"';").getResultList();
				nbPapierA = em.createNativeQuery("select * from Version "
						+ "where conference_id='"+idEvt+"' and accepte='1';").getResultList();
				nbPapierR = em.createNativeQuery("select * from Version "
						+ "where conference_id='"+idEvt+"' and refuse='1';").getResultList();
			} else {
				nbPapier = em.createNativeQuery("select * from Version "
						+ "where journal_id='"+idEvt+"';").getResultList();
				nbPapierA = em.createNativeQuery("select * from Version "
						+ "where journal_id='"+idEvt+"' and accepte='1';").getResultList();
				nbPapierR = em.createNativeQuery("select * from Version "
						+ "where journal_id='"+idEvt+"' and refuse='1';").getResultList();
			}
			evt.setNbPapier(nbPapier.size());
			evt.setNbPapierAccepte(nbPapierA.size());
			evt.setNbPapierRefuse(nbPapierR.size());
		}

	}


	/** 
	 * @param idPers
	 * @param texte le champ à remplir
	 * @param isConf conference ou journal
	 * @param type le type de recherche (titre,acronym,rang...)
	 * @return le resultat de recherche : la liste des evenements qui répondent au critère.
	 */
	public Collection<Evenement> getResultatStats(int idPers,String texte,String isConf, String type){
		Collection<Evenement> events= new HashSet<Evenement>();

		if(isConf.matches("Conference")){
			switch(type){
			case "1" : 
				events = em.createQuery("select c from "
						+ "Conference c where c.titre Like :titre and c.participant Like:Pers").
						setParameter("titre",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
				break;
			case "0" : 
				events = em.createQuery("select c from "
						+ "Conference c where c.acronym Like :acr and c.participant Like:Pers").
						setParameter("acr",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
				break;
			case "Rang" : 
				events = em.createQuery("select c from "
						+ "Conference c where c.rang Like :rang and c.participant Like:Pers").
						setParameter("rang",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
				break;
			}
		}
		else if(isConf.matches("Journal")){
			switch(type){
			case "1" : 
				events = em.createQuery("select c from "
						+ "Journal c where c.titre Like :titre and c.participant Like:Pers").
						setParameter("titre",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
				break;
			case "Rang" : 
				events = em.createQuery("select c from "
						+ "Journal c where c.rang Like :rang and c.participant Like:Pers").
						setParameter("rang",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
				break;

			}
		}
		//calculer les nombres de papiers accepter, refuse et le total.
		calculPapierResult(events);
		return events;
	} 

	/**
	 * @param idPers
	 * @param texte le champ à remplir
	 * @param isConf conference ou journal
	 * @param type le type de recherche (titre,acronym,rang...)
	 * @return un evenement representant la moyenne de tous tes conferences ou journaux
	 * par ex : pour le rang A* on renvoie la moyenne pour tous les confernces ou journaux 
	 * de rang A* i.e un event dont le nb de papier est la moyenne de tous les events de rang A*
	 */
	public Evenement moyenneParRang(int idPers,String texte,String isConf,String type){
		Collection<Object> nbPapier = new HashSet<Object>();
		Collection<Object> nbPapierA=new HashSet<Object>();
		Collection<Object> nbPapierR=new HashSet<Object>();
		Collection<Evenement> events= new HashSet<Evenement>();
		Evenement eventParticulier = null;
		if(type.matches("Rang")){
			String inter="journal_id";
			if(isConf.matches("Conference")){
				eventParticulier=new Conference();
				eventParticulier.setTitre("Le taux de succès pour les conférences "
						+ "de rang : "+texte);
				inter="conference_id";
				events  = em.createQuery("select c from "
						+ "Conference c where c.rang Like :rang and c.participant Like:Pers").
						setParameter("rang",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
			} else {
				eventParticulier=new Journal();
				eventParticulier.setTitre("Le taux de succès pour les journaux"
						+ " de rang : "+texte);
				events  = em.createQuery("select c from "
						+ "Journal c where c.rang Like :rang and c.participant Like:Pers").
						setParameter("rang",texte).setParameter("Pers",getPersonne(idPers)).getResultList();
			}
			for(Evenement evt : events){
				int idEvt = evt.getId();
				nbPapier.addAll(em.createNativeQuery("select * from Version "
						+ "where "+inter+"='"+idEvt+"';").getResultList());
				nbPapierA.addAll(em.createNativeQuery("select * from Version "
						+ "where "+inter+"='"+idEvt+"' and accepte='1';").getResultList());
				nbPapierR.addAll(em.createNativeQuery("select * from Version "
						+ "where "+inter+"='"+idEvt+"' and refuse='1';").getResultList());
			}

			eventParticulier.setNbPapier(nbPapier.size());
			eventParticulier.setNbPapierAccepte(nbPapierA.size());
			eventParticulier.setNbPapierRefuse(nbPapierR.size());
		}
		return eventParticulier;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************ESPACE PERSO RECHERCHER*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param idPers id de la personne
	 * @param sduree le nombre saisi.
	 * @param isMois mois ou jour
	 * @param rang le rang
	 * @param date critere de recherche : date abstract, date soumission...
	 * @return le résultat de recherche i.e : liste des conférences qui répondent au critère.
	 */
	public Collection<Conference> getResultatRechercheConf(int idPers,String sduree, String isMois, String rang,
			String date){
		Collection<Conference> events= new HashSet<Conference>();
		Date today = new Date();
		try{
			validationIsNumeric(sduree);
		} catch(Exception e){
			setErreur(e.getMessage());
		}
		if(erreurs.isEmpty()){
			int duree = Integer.parseInt(sduree);
			int marge = 8;
			//calculer duree fournie
			if(isMois.matches("Mois")){
				duree = duree*30;
				marge=15;
			}
			Collection<Conference> eventsRang= new HashSet<Conference>();

			eventsRang = em.createQuery("select c from "
					+ "Conference c where c.rang Like :rang and c.participant Like:Pers").
					setParameter("rang",rang).setParameter("Pers",getPersonne(idPers)).getResultList();
			for(Conference evt : eventsRang){
				Date dEvt; 
				long diff;
				long nbJour;
				try {
					switch(date){

					case "0":
						dEvt =formater.parse(evt.getDateEvt());
						//calculer le nombre de jour restant pour la conférence
						diff = dEvt.getTime() - today.getTime();
						nbJour = (long)diff/CONST_DURATION_OF_DAY;
						//remplir la liste résultante.
						for(int i=0;i<marge;i++){
							if(duree == nbJour+i || nbJour ==duree+i){
								events.add(evt);
							}
						}

						break;
					case "1":
						dEvt =formater.parse(evt.getDateAbstractSubmission());
						//calculer le nombre de jour restant pour la conférence
						diff = dEvt.getTime() - today.getTime();
						nbJour = (long)diff/CONST_DURATION_OF_DAY;
						//remplir la liste résultante.
						for(int i=0;i<marge;i++){
							if(duree == nbJour+i || nbJour ==duree+i){
								events.add(evt);
							}
						}
						break;

					default :
						//calculer le nombre de jour restant pour la conférence
						dEvt =formater.parse(evt.getDatePaperSubmission());
						diff = dEvt.getTime() - today.getTime();
						nbJour = (long)diff/CONST_DURATION_OF_DAY;
						//remplir la liste résultante.
						for(int i=0;i<marge;i++){
							if(duree == nbJour+i || nbJour ==duree+i){
								events.add(evt);
							}
						}
						break;

					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return events;
	}


	/**
	 * @param idPers id de la personne
	 * @param sduree le nombre saisi.
	 * @param isMois mois ou jour
	 * @param rang le rang
	 * @param date critere de recherche : date abstract, date soumission
	 * @return le résultat de recherche i.e : liste des journaux qui répondent au critère.
	 */
	public Collection<Journal> getResultatRechercheJournal(int idPers,String sduree, String isMois, String rang,
			String date){
		Collection<Journal> events= new HashSet<Journal>();
		Date today = new Date();
		try{
			validationIsNumeric(sduree);
		} catch(Exception e){
			setErreur(e.getMessage());
		}
		if(erreurs.isEmpty()){
			int duree = Integer.parseInt(sduree);
			int marge = 8;
			//calculer duree fournie
			if(isMois.matches("Mois")){
				duree = duree*30;
				marge = 31;
			}
			Collection<Journal> eventsRang= new HashSet<Journal>();

			eventsRang = em.createQuery("select c from "
					+ "Journal c where c.rang Like :rang and c.participant Like:Pers").
					setParameter("rang",rang).setParameter("Pers",getPersonne(idPers)).getResultList();
			for(Journal evt : eventsRang){
				Date dEvt; 
				long diff;
				long nbJour;
				try {
					switch(date){

					case "1":
						dEvt =formater.parse(evt.getDateAbstractSubmission());
						//calculer le nombre de jour restant pour la conférence
						diff = dEvt.getTime() - today.getTime();
						nbJour = (long)diff/CONST_DURATION_OF_DAY;
						//remplir la liste résultante.
						for(int i=0;i<marge;i++){
							if(duree == nbJour+i || nbJour ==duree+i){
								events.add(evt);
							}
						}
						break;

					default :
						//calculer le nombre de jour restant pour la conférence
						dEvt =formater.parse(evt.getDatePaperSubmission());
						diff = dEvt.getTime() - today.getTime();
						nbJour = (long)diff/CONST_DURATION_OF_DAY;
						//remplir la liste résultante.
						for(int i=0;i<marge;i++){
							if(duree == nbJour+i || nbJour ==duree+i){
								events.add(evt);
							}
						}
						break;

					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return events;
	}


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
	/** Ajouter une conference 
	 * @param request
	 * @param titre
	 * @param acr
	 * @param site
	 * @param rang
	 * @param info
	 * @param pays
	 * @param date1 date de la conference
	 * @param date2 date de l'abstract	
	 * @param date3 date de soumission papier
	 * @param dateR date Rebuttal
	 * @param date4 date notification acceptance.
	 */
	public void ajouterConf(HttpServletRequest request, String titre,String acr, String site, String rang,String info, String pays, 
			String date1, String date2, String date3, String dateR,String date4){
		String nomFichierStyle;

		try {
			validationDate(date1);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationDate(date2);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationDate(date3);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationDate(date4);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}

		if(dateR!=null && !dateR.equals("")){
			try {
				validationDate(dateR);
			} catch(Exception e) {
				setErreur(e.getMessage());
			}
		}

		try {
			validationTitre(titre);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationTexte(info);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationTitre(acr);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		if(erreurs.isEmpty() && persConnecte != null){
			Conference e = new Conference(titre,acr, site, rang, info, date1, date2, date3, date4,pays);
			e.setParticipant(persConnecte);
			if(dateR!=null)
				e.setDateRebuttal(dateR);
			em.persist(e);
			nomFichierStyle = persConnecte.getId()+"_"+e.getId()+"_Conf.zip";
			String fichier = enregistrerFichierZipStyle( request,nomFichierStyle,chemin2);
			if(fichier!=null && !fichier.isEmpty()){
				e.setNomFichierStyle(nomFichierStyle);
			}
			if(!erreurs.isEmpty()){
				System.out.println("erreurs non vide, on supprime la conference !");
				em.remove(e);
			}
		}
	}


	/** Ajouter un journal
	 * @param request
	 * @param titre
	 * @param acr
	 * @param site
	 * @param rang
	 * @param info
	 * @param pays
	 * @param date2 date de l'abstract	
	 * @param date3 date de soumission papier
	 * @param dateR date Rebuttal
	 */
	public void ajouterJournal(HttpServletRequest request, String titre, String site, String rang,String info,
			String date1, String date2, String dateR){
		String nomFichierStyle;
		try {
			validationDate(date1);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationDate(date2);
		} catch(Exception e) {
			setErreur(e.getMessage());
		}
		if(dateR!=null && !dateR.equals("")){
			try {
				validationDate(dateR);
			} catch(Exception e) {
				setErreur(e.getMessage());
			}
		}
		try {
			validationTitre(titre);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		try {
			validationTexte(info);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		if(erreurs.isEmpty() && persConnecte != null){
			Journal e = new Journal(titre, site, rang, info, date1, date2);
			e.setParticipant(persConnecte);
			if(dateR!=null)
				e.setDateRebuttal(dateR);
			em.persist(e);
			nomFichierStyle = persConnecte.getId()+"_"+e.getId()+"_Journal.zip";
			String fichier = enregistrerFichierZipStyle( request,nomFichierStyle,chemin2);
			if(fichier!=null && !fichier.isEmpty()){
				e.setNomFichierStyle(nomFichierStyle);
			}
			if(!erreurs.isEmpty()){
				System.out.println("erreurs non vide, on supprime le journal !");
				em.remove(e);
			}
		}
	}




	////////////////////////////////////////////////////////////////////
	//***********************************AJOUTER TRAVAIL**************
	////////////////////////////////////////////////////////////////////

	/** Ajouter un travail
	 * @param request
	 * @param titre
	 * @param abstr l'abstract
	 * @param soumis soumis ou pas
	 * @param idConfCibles les conférences ciblées
	 * @param idJournalCibles les journaux ciblés
	 * @param idConfChoisi la conference à laquelle il a soumis
	 * @param idJournalChoisi le journal auquel il a soumis
	 * @param isConf conference/journal
	 */
	public void ajouterTravail(HttpServletRequest request,String titre, String abstr, boolean soumis, 
			String[] idConfCibles,String[] idJournalCibles, String idConfChoisi,String idJournalChoisi,
			boolean isConf){
		String fichier;
		String archive;
		Boolean soumisEffect=false;
		
		System.out.println("le titre est: " + titre);
		
		try {
			validationTitre(titre);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationTexte(abstr);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}
		if(erreurs.isEmpty() && persConnecte != null){
			Travail t = new Travail(titre, abstr,soumis);
			t.setProprio(persConnecte);
			int idPers = persConnecte.getId();
			em.persist(t);
			int idTravail = t.getId();
			System.out.println(" Persistance reussie : idTravail = "+idTravail);
			//Si soumis.
			if(soumis) {
				Date d = new Date();
				Version v = new Version(d);
				//choisir parmi les conferences choisi.
				//penser a tout archiver pour cycle de vie.
				if(idConfChoisi!=null && isConf){
					Conference evtSoumis = em.find(Conference.class, Integer.parseInt(idConfChoisi));
					if(evtSoumis!=null){
						v.setConference(evtSoumis);
						soumisEffect=true;
						Set<Conference> confs = new HashSet<Conference>();
						confs.add(evtSoumis);
						t.setConfSoumis(confs);
					}
					
					System.out.println("Conference bien ajouté");

				}
				else {
					if(idJournalChoisi!=null && !isConf){
						Journal evtSoumis = em.find(Journal.class, Integer.parseInt(idJournalChoisi));
						if(evtSoumis!=null){
							v.setJournal(evtSoumis);
							soumisEffect=true;
							Set<Journal> journaux = new HashSet<Journal>();
							journaux.add(evtSoumis);
							t.setJournalSoumis(journaux);
						}
						
						System.out.println("Journal bien ajouté");

					}
				}
				if(soumisEffect){
					System.out.println("idTravail = "+idTravail);
					em.createNativeQuery("update Travail SET soumis=1 where id="+idTravail+"").executeUpdate();
					
					
					System.out.println("Requête reussi");
					//System.out.println(" travail : "+v.getTravail().getId());
					v.setTravail(t);
					System.out.println(" travail : "+v.getTravail().getId());
					//créer dans la bdd.
					
					
						em.persist(v);
					
					
					//enregistrer le pdf : idPers+idTravail+date.pdf
					fichier = idPers+"_"+idTravail+"_"+d.toString()+".pdf";
					archive = idPers+"_"+idTravail+"_"+d.toString()+".zip";
					System.out.println("Le nom du fichier est:"+ fichier);

					enregistrerFichierPdf(request,fichier,chemin);
					//enregistrer le pdf : idPers+idTravail+date.zip
					enregistrerFichierZip( request,archive,chemin);
					t.setNomFichier(fichier);
					t.setNomSource(archive);
					//enregister le fichier dans cette version.
					v.setVersionFichier(fichier);
					v.setVersionArchive(archive);
					System.out.println("Je suis passé");

				}

			} 

			//Si pas soumis
			else {
				if(idConfCibles!=null) {
					Set<Conference> confCibles = new HashSet<Conference>();
					for(String idEvtCible : idConfCibles){

						Conference evtCible = em.find(Conference.class, Integer.parseInt(idEvtCible));
						if(evtCible!=null){
							confCibles.add(evtCible);
						}

					}
					t.setConfCibles(confCibles);
				}
				if(idJournalCibles!=null) {

					Set<Journal> journalCibles = new HashSet<Journal>();
					for(String idEvtCible : idJournalCibles){
						Journal evtCible = em.find(Journal.class, Integer.parseInt(idEvtCible));
						if(evtCible!=null){
							journalCibles.add(evtCible);
						}
					}
					t.setJournalCibles(journalCibles);
				}


				fichier = idPers+"_"+idTravail+".pdf";
				archive = idPers+"_"+idTravail+".zip";
				//enregistrer le pdf : idPers+idTravail.pdf
				enregistrerFichierPdf(request,fichier,chemin);
				//enregistrer le pdf : idPers+idTravail.zip
				enregistrerFichierZip(request,archive,chemin);
				t.setNomFichier(fichier);
				t.setNomSource(archive);
			} 
			//Verifier qu il n'y a pas d'erreurs dans l'enregistrement de fichier.
			

			
			if(!erreurs.isEmpty()){
				System.out.println("Probleme");
				//em.remove(t);
				supprimerTravail(idTravail);
			}
		}

	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL DETAIL*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param id id du travail
	 * @return liste des conférence auquelles un travail a soumis
	 */
	public Set<Conference> getConferencesSoumis(int id){
		Set<Conference> confs = new HashSet<Conference>();
		Collection<Integer> ids = em.createNativeQuery("select confS_id from "
				+ "Travail_ConfsSoumis where travailS_id='"+id+"'").getResultList();
		for(int i : ids){
			Conference c = em.find(Conference.class, i);
			if(c!=null)
				confs.add(c);
		}
		return confs;
	}

	/**
	 * 
	 * @param id id du travail
	 * @return liste des journaux auquels un travail a soumis
	 */
	public Set<Journal> getJournauxSoumisTravail(int id){
		Set<Journal> journaux = new HashSet<Journal>();
		Collection<Integer> ids = em.createNativeQuery("select journalS_id from "
				+ "Travail_JournalSoumis where travailS_id='"+id+"'").getResultList();
		for(int i : ids){
			Journal c = em.find(Journal.class, i);
			if(c!=null)
				journaux.add(c);
		}
		return journaux;
	}


	/**
	 * @param id id du travail
	 * @return  liste des conferences que le travail cible.
	 */
	public Set<Conference> getConferencesCibles(int id){
		Set<Conference> confs = new HashSet<Conference>();
		Collection<Conference> confsSoumis = getConferencesSoumis(id);
		Collection<Integer> ids = em.createNativeQuery("select Conference_id from "
				+ "Travail_ConfsCibles where travail_id='"+id+"'").getResultList();
		for(int i : ids){
			Conference c = em.find(Conference.class, i);
			if(c!=null && !confsSoumis.contains(c))
				confs.add(c);
		}
		return confs;
	}

	/**
	 * @param id id du travail
	 * @return liste des journaux que le travail cible.
	 */
	public Set<Journal> getJournauxCibles(int id){
		Set<Journal> journaux = new HashSet<Journal>();
		Collection<Journal> journauxSoumis = getJournauxSoumisTravail(id);
		Collection<Integer> ids = em.createNativeQuery("select journal_id from "
				+ "Travail_JournalCibles where travail_id='"+id+"'").getResultList();
		for(int i : ids){
			Journal c = em.find(Journal.class, i);
			if(c!=null && !journauxSoumis.contains(c) )
				journaux.add(c);
		}
		return journaux;
	}


	/**
	 * @param idTravail
	 * @return liste des versions d'un travail
	 */
	public Collection<Version> getVersions(int idTravail){
		Collection<Version> versions = em.createQuery("select v from Version v "
				+ "where v.travail Like :Travail ").setParameter("Travail",getTravail(idTravail))
				.getResultList();
		List<Version> list = new ArrayList<Version>(versions);
		Collections.sort(list);
		return list;
	}


	/** supprimer une conference de la liste (soumis) pour un travail
	 * @param idTravail travail 
	 * @param idConf la conference à supprimer
	 */
	public void supprimerConfListe(int idTravail, int idConf){
		em.createNativeQuery("delete from Travail_ConfsSoumis "
				+ "where confS_id='"+idConf+"';").executeUpdate();
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "conference_id='"+idConf+"' and travail_id="+idTravail+";").getResultList();
		for(Integer i : ids){
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}
		if(getConferencesSoumis(idTravail).isEmpty() && getJournauxSoumisTravail(idTravail).isEmpty()){
			em.createNativeQuery("update Travail SET soumis='"+0+"' where id='"+idTravail+"';").executeUpdate();
		}
	}


	/** supprimer une conference de la liste (cibles) pour un travail
	 * @param idTravail travail 
	 * @param idConf la conference à supprimer
	 */
	public void supprimerConfListeCibles(int idTravail, int idConf){
		em.createNativeQuery("delete from Travail_ConfsCibles "
				+ "where conference_id='"+idConf+"';").executeUpdate();
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "conference_id='"+idConf+"' and travail_id="+idTravail+";").getResultList();
		for(Integer i : ids){
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}
		if(getConferencesSoumis(idTravail).isEmpty() && getJournauxSoumisTravail(idTravail).isEmpty()){
			em.createNativeQuery("update Travail SET soumis='"+0+"' where id='"+idTravail+"';").executeUpdate();
		}
	}



	/** supprimer un journal de la liste (soumis) pour un travail
	 * @param idTravail travail
	 * @param idJournal le journal à supprimer
	 */
	public void supprimerJournalListe(int idTravail, int idJournal){
		em.createNativeQuery("delete from Travail_JournalSoumis "
				+ "where journalS_id='"+idJournal+"';").executeUpdate();
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "journal_id='"+idJournal+"' and travail_id="+idTravail+";").getResultList();
		for(Integer i : ids){
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}

		if(getConferencesSoumis(idTravail).isEmpty() && getJournauxSoumisTravail(idTravail).isEmpty()){
			em.createNativeQuery("update Travail SET soumis='"+0+"' where id='"+idTravail+"';").executeUpdate();
		}
	}

	/** supprimer un journal de la liste (cibles) pour un travail
	 * @param idTravail travail
	 * @param idJournal le journal à supprimer
	 */
	public void supprimerJournalListeCibles(int idTravail, int idJournal){
		em.createNativeQuery("delete from Travail_JournalCibles "
				+ "where journal_id='"+idJournal+"';").executeUpdate();
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "journal_id='"+idJournal+"' and travail_id="+idTravail+";").getResultList();
		for(Integer i : ids){
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL CONFERENCE*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Collection<Conference> getPropostionsConfs(int idConf,String marge, String rang, Collection<Conference> confs){
		
		Collection<Conference> result= new HashSet<Conference>();
		String dateNotif = getConference(idConf).getDateNotifAcceptance();
		try{
			validationIsNumeric(marge);
		} catch(Exception e){
			setErreur(e.getMessage());
		}
		try{
			validationDate(dateNotif);
		} catch(Exception e){
			setErreur(e.getMessage());
		}
		if(erreurs.isEmpty()){
			
			Date dateN;
			int margeN = Integer.parseInt(marge);
			try {
				dateN = formater.parse(dateNotif);
				for(Conference c : confs){
					if(c.getRang().equals(rang)){
						Date dateAbstr;
						try {
							dateAbstr = formater.parse(c.getDateAbstractSubmission());
							long diff = Math.abs(dateAbstr.getTime()- dateN.getTime());
							long nbJours = (long)diff/CONST_DURATION_OF_DAY;
							if(nbJours>=0 && nbJours<=margeN)
								result.add(c);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL JOURNAL*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL MODIFIER*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** modifier le titre ou l'abstract d'un travail.
	 * @param idTravail travail à modifier
	 * @param titre nouveau titre
	 * @param abstr nouveau abstract
	 */
	public void modifierTravail(int idTravail ,String titre, String abstr){
		try {
			validationTitre(titre);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}
		try {
			validationTexte(abstr);
		} catch (Exception e) {
			setErreur(e.getMessage());
		}

		if(erreurs.isEmpty()){
			em.createNativeQuery("update Travail SET titre='" 
					+titre+"', abstr='"+abstr+"' where id="+idTravail+";").executeUpdate();
		}

	}

	/** supprimer un travail
	 * @param id travail à supprimer
	 */
	public void supprimerTravail(int id){

		Travail t = getTravail(id);
		//supprimer les fichiers
		if(t!=null){
			File pub = new File(chemin+t.getNomFichier());
			if(pub!= null)
				pub.delete();
			File src = new File(chemin+t.getNomSource());
			if(src!=null)
				src.delete();
		}
		
		//supprimer les versions associés.
		Collection<Integer> ids = em.createNativeQuery("select id from Version where "
				+ "travail_id="+id+";").getResultList();
		for(Integer i : ids){
			System.out.println("I am entering here");
			em.createNativeQuery("delete from Reponse where version_id='"+i+"';").executeUpdate();
			//supprimer les versions
			em.createNativeQuery("delete from Version where id='"+i+"';").executeUpdate();
		}
		em.createNativeQuery("delete from Travail_ConfsCibles where travail_id='"+id+"'").executeUpdate();
		em.createNativeQuery("delete from Travail_JournalCibles where travail_id='"+id+"'").executeUpdate();
		em.createNativeQuery("delete from Travail_ConfsSoumis where travailS_id='"+id+"'").executeUpdate();
		em.createNativeQuery("delete from Travail_JournalSoumis where travailS_id='"+id+"'").executeUpdate();
		em.createNativeQuery("delete from Travail where id='"+id+"'").executeUpdate();
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL SOUMETTRE*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Soumettre un travail à une conférence.
	 * @param request
	 * @param idTravail
	 * @param idConfChoisi
	 */
	public void soumettreConference(HttpServletRequest request,int idTravail,String idConfChoisi){
		String fichier;
		String archive;
		if(persConnecte != null){
			int idPers = persConnecte.getId();
			Travail t = getTravail(idTravail);
			Date d = new Date();
			Version v = new Version(d);
			//enregistrer le pdf : idPers+idTravail+date.pdf
			fichier = idPers+"_"+idTravail+"_"+d.toString()+".pdf";
			archive = idPers+"_"+idTravail+"_"+d.toString()+".zip";
			enregistrerFichierPdf(request,fichier,chemin);
			//enregistrer le zip : idPers+idTravail+date.zip
			enregistrerFichierZip( request,archive,chemin);
			//enregistrer les fichiers dans la version.
			v.setVersionFichier(fichier);
			v.setVersionArchive(archive);

			//Verifier qu il n'y a pas d'erreurs dans l'enregistrement de fichier.
			if(erreurs.isEmpty()){
				if(idConfChoisi!=null){
					Conference evtSoumis = em.find(Conference.class, Integer.parseInt(idConfChoisi));
					if(evtSoumis!=null){
						v.setConference(evtSoumis);
						Set<Conference> confs = new HashSet<Conference>(getConferencesSoumis(idTravail));
						confs.add(evtSoumis);
						t.setConfSoumis(confs);
						em.createNativeQuery("update Travail SET soumis='"+1+"' where id="+idTravail+";").executeUpdate();
						t.setSoumis(true);
						//evtSoumis.setSoumis(true);  //A revoir
						v.setTravail(t);
						em.persist(v);
					}
				}
			}

		}
	}


	/** Soumettre un travail à un journal.
	 * @param request
	 * @param idTravail
	 * @param idJournalChoisi
	 */
	public void soumettreJournal(HttpServletRequest request,int idTravail,String idJournalChoisi){
		String fichier;
		String archive;
		if(persConnecte != null){
			int idPers = persConnecte.getId();
			Travail t = getTravail(idTravail);
			Date d = new Date();
			Version v = new Version(d);
			//enregistrer le pdf : idPers+idTravail+date.pdf
			fichier = idPers+"_"+idTravail+"_"+d.toString()+".pdf";
			archive = idPers+"_"+idTravail+"_"+d.toString()+".zip";
			enregistrerFichierPdf(request,fichier,chemin);
			//enregistrer le zip : idPers+idTravail+date.zip
			enregistrerFichierZip( request,archive,chemin);
			//enregistrer les fichiers dans la version.
			v.setVersionFichier(fichier);
			v.setVersionArchive(archive);

			//Verifier qu il n'y a pas d'erreurs dans l'enregistrement de fichier.
			if(erreurs.isEmpty()){
				if(idJournalChoisi!=null){
					Journal evtSoumis = em.find(Journal.class, Integer.parseInt(idJournalChoisi));
					if(evtSoumis!=null){
						v.setJournal(evtSoumis);
						Set<Journal> journaux = new HashSet<Journal>(getJournauxSoumisTravail(idTravail));
						journaux.add(evtSoumis);
						t.setJournalSoumis(journaux);
						em.createNativeQuery("update Travail SET soumis='"+1+"' where id="+idTravail+";").executeUpdate();
						t.setSoumis(true);
						//evtSoumis.setSoumis(true);  //A revoir
						v.setTravail(t);
						em.persist(v);
					}
				}
			}

		}
	}




	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL CIBLER*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 
	 * @param idPerso
	 * @return liste des conferences d'une personne.
	 */
	public Collection<Conference> getConfPerso(int idPerso){
		Collection<Conference> confs = em.createQuery("SELECT c from Conference c where "
				+ "c.participant Like:Perso").setParameter("Perso", getPersonne(idPerso))
				.getResultList();
		return confs;
	}


	/**
	 * @param idPerso
	 * @return liste des journaux d'une personne.
	 */
	public Collection<Journal> getJournalPerso(int idPerso){
		Collection<Journal> journaux = em.createQuery("SELECT j from Journal j where "
				+ "j.participant Like:Perso").setParameter("Perso", getPersonne(idPerso))
				.getResultList();
		return journaux;
	}


	/**
	 * @param idTravail
	 * @param idPerso
	 * @return liste des conferences auquelles un travail n'a pas encore soumis
	 */
	public Collection<Conference> getConfPotentiel(int idTravail, int idPerso){
		Collection<Conference> confs = getConfPerso(idPerso);
		Collection<Conference> confsCibles = getConferencesCibles(idTravail);
		Collection<Conference> confsP = confs;
		confsP.removeAll(confsCibles);
		return confsP;
	}


	/**
	 * @param idTravail
	 * @param idPerso
	 * @return  liste des journaux auquels un travail n'a pas encore soumis
	 */
	public Collection<Journal> getJournalPotentiel(int idTravail, int idPerso){
		Collection<Journal> journaux = getJournalPerso(idPerso);
		Collection<Journal> journauxCibles = getJournauxCibles(idTravail);
		Collection<Journal> journauxP = journaux;
		journauxP.removeAll(journauxCibles);
		return journauxP;
	}



	/** cibler des conférences et des journaux
	 * @param idTravail travail
	 * @param idConfCibles conferences ciblees
	 * @param idJournalCibles journaux cibles
	 */
	public void cibler(int idTravail, String[] idConfCibles, String[] idJournalCibles ){
		Travail t = getTravail(idTravail);
		if(idConfCibles!=null) {
			Set<Conference> confCibles = new HashSet<Conference>();
			for(String idEvtCible : idConfCibles){

				Conference evtCible = em.find(Conference.class, Integer.parseInt(idEvtCible));
				if(evtCible!=null){
					confCibles.add(evtCible);
				}


			}
			t.setConfCibles(confCibles);
		}
		if(idJournalCibles!=null) {

			Set<Journal> journalCibles = new HashSet<Journal>();
			for(String idEvtCible : idJournalCibles){
				Journal evtCible = em.find(Journal.class, Integer.parseInt(idEvtCible));
				if(evtCible!=null){
					journalCibles.add(evtCible);
				}

			}
			t.setJournalCibles(journalCibles);
		}
	}





	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TRAVAIL VERSION*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Rendre une version d'un travail publique.
	 * @param version la version à rendre publique
	 */ 
	public void rendrePublique(Version version){
		if(version!=null){
			em.createNativeQuery("update Version SET publique='1' where id="+version.getId()+";")
			.executeUpdate();
			version.setPublique(true);
		}

	}


	/** ne plus rendre publique une version d'un travail
	 * @param version la version à ne plus rendre publique
	 */
	public void nePlusRendrePublique(Version version){
		if(version!=null){
			em.createNativeQuery("update Version SET publique='0' where id="+version.getId()+";")
			.executeUpdate();
			version.setPublique(false);
		}
	}


	/** Ajouter une réponse à une version
	 * @param version
	 * @param reviewer le nom du reviewer
	 * @param jugement 
	 * @param detail
	 * @param accepter accepter/refuse
	 */
	public void ajouterReponse(Version version,String reviewer, String jugement, 
			String detail,boolean accepter){

		try{
			validationTitre(reviewer);
		} catch(Exception e){
			setErreur(e.getMessage());
		}
		try{
			validationTitre(jugement);
		} catch(Exception e){
			setErreur(e.getMessage()); 
		}
		try{ 
			validationTitre(detail);
		} catch(Exception e){
			setErreur(e.getMessage());
		}

		Reponse r = new Reponse(reviewer,jugement,detail);
		em.persist(r);
		r.setVersion(version);
		Travail travail = version.getTravail();
		em.createNativeQuery("update Travail SET hasResponse='1' where id="+travail.getId()+";").executeUpdate();
		setAccepter(version, accepter);
	}


	/** Rendre une version accepter
	 * @param version
	 * @param accepter
	 */
	public void setAccepter(Version version, boolean accepter){
		int i=0;
		version.setRefuse(true);
		version.setAccepte(false);
		if(accepter){
			i=1;
			version.setAccepte(true);
			version.setRefuse(false);
		}
		int j=1-i;
		em.createNativeQuery("update Version SET accepte='"+i+"', refuse='"+j+"'"
				+ " where id="+version.getId()+";").executeUpdate();
	}

	/** 
	 * @param version
	 * @return liste de réponses d'une version
	 */
	public Collection<Reponse> getReponses(Version version){
		Collection<Reponse> reponses = em.createQuery("select r from "
				+ "Reponse r where r.version Like :Version").
				setParameter("Version",version).getResultList();
		return reponses;
	}


	/**
	 * @param idTravail
	 * @return booléen pour savoir si une version a eu des retours ou pas
	 */
	private boolean pas_de_Reponse(int idTravail){
		for(Version v : getVersions(idTravail)){
			if(!getReponses(v).isEmpty()){
				return false;
			}
		}
		return true;
	}

	/** supprimer une réponse 
	 * @param idTravail
	 * @param version
	 * @param idRep
	 */
	public void supprimerReponse(int idTravail,Version version,int idRep){
		em.createNativeQuery("delete from Reponse where id='"+idRep+"';").executeUpdate();
		if(pas_de_Reponse(idTravail))
			em.createNativeQuery("update Travail SET hasResponse='0' where id="+version.getTravail().getId()+";").executeUpdate();
	}


	/** Choisir un presentateur pour une version de travail accépté déstiné à une conférence
	 * @param v version
	 * @param nom nom du présentateur
	 * @param prenom prenom du présentateur
	 */
	public void choisirPresentateur(Version v, String nom, String prenom){
		//validation du nom prenom utf-8.
		v.setNomPresentateur(nom);
		v.setPrenomPresentateur(prenom);
		em.createNativeQuery("update Version SET nomPresentateur='"+nom+"' , "
				+ "prenomPresentateur='"+prenom+"' where id='"+v.getId()+"';").executeUpdate();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************TELECHARGER FICHIERS*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////


	/** Valider un fichier uploadé pdf
	 * @param nom nom du fichier.
	 * @throws Exception
	 */
	private void ValidationFichierPdf(String nom) throws Exception{
		if(nom ==null || nom.isEmpty()){
			throw new Exception("Merci d'inserer un fichier (.pdf)");
		}
		else {
			nom = nom.substring( nom.lastIndexOf( '/' ) + 1 )
					.substring( nom.lastIndexOf( '\\' ) + 1 );
			String[] parts = nom.split(Pattern.quote("."));
			if(parts.length<2){
				throw new Exception("Merci d'insérer un fichier");
			}
			else {
				if(!parts[1].matches("pdf")){
					throw new Exception("Le fichier publication doit être un pdf.");
				}
			}

		}
	}

	private void ValidationFichierExcel(String nom) throws Exception{
		if(nom ==null || nom.isEmpty()){
			throw new Exception("Merci d'inserer un fichier (.xls)");
		}
		else {
			nom = nom.substring( nom.lastIndexOf( '/' ) + 1 )
					.substring( nom.lastIndexOf( '\\' ) + 1 );
			String[] parts = nom.split(Pattern.quote("."));
			if(parts.length<2){
				throw new Exception("Merci d'insérer un fichier");
			}
			else {
				if(!parts[1].matches("xls")){
					throw new Exception("Le fichier doit être une feuille excel (.xls)");
				}
			}

		}
	}

	/**  Valider un fichier uploadé archive(zip)
	 * @param nom nom de l'archive
	 * @throws Exception
	 */
	private void ValidationFichierZip(String nom) throws Exception{
		if(nom ==null || nom.isEmpty()){
			throw new Exception("Merci d'inserer un fichier archive(.zip)");
		}
		else {
			nom = nom.substring( nom.lastIndexOf( '/' ) + 1 )
					.substring( nom.lastIndexOf( '\\' ) + 1 );
			String[] parts = nom.split(Pattern.quote("."));
			if(parts.length<2){
				throw new Exception("Merci d'insérer un fichier");
			}
			else {
				if(!parts[1].matches("zip")){
					throw new Exception("Le fichier sources doit être une archive (.zip).");
				}
			}

		}
	}


	/** Valider un fichier archive (contient des templates latex par exemple).
	 * @param nom nom de l'archive
	 * @throws Exception
	 */
	private void ValidationFichierZipStyle(String nom) throws Exception{
		if(nom ==null || nom.isEmpty()){
			return;
		}
		else {
			nom = nom.substring( nom.lastIndexOf( '/' ) + 1 )
					.substring( nom.lastIndexOf( '\\' ) + 1 );
			String[] parts = nom.split(Pattern.quote("."));
			if(parts.length<2){
				throw new Exception("Merci d'insérer un fichier");
			}
			else {
				if(!parts[1].matches("zip")){
					throw new Exception("Le fichier sources doit être une archive (.zip).");
				}
			}

		}
	}


	/** Valider le contenu d'un fichier
	 * @param contenuFichier
	 * @throws Exception
	 */
	private void validationFichier( InputStream contenuFichier ) throws Exception {
		if (  contenuFichier == null ) {
			throw new Exception( "Merci de sélectionner un fichier à envoyer." );
		}
	}

	/** Ecrire le fichier sur le disque.
	 * @param contenu le contenu du fichier
	 * @param nomFichier nom du fichier
	 * @param chemin chemin dans lequel on écrit le fichier
	 * @throws Exception
	 */
	private void ecrireFichier( InputStream contenu, String nomFichier,String chemin ) throws Exception {
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
			//Ouvre les flux.
			entree = new BufferedInputStream(contenu, taille);
			sortie = new BufferedOutputStream( new FileOutputStream( new File( chemin+'/'+nomFichier ),false ),
					taille );
			//copier le contenu.
			byte[] tampon = new byte[taille];
			int longueur = 0;
			while ( ( longueur = entree.read( tampon ) ) > 0 ) {
				sortie.write( tampon, 0, longueur );
			}
		} finally {
			try {
				sortie.close();
			} catch ( IOException ignore ) {
				System.out.println("probleme dans sortie.close()");
			}
			try {
				entree.close();
			} catch ( IOException ignore ) {
				System.out.println("probleme dans entree.close()");
			}
		}
	}

	/** Renvoie le nom d'un fichier.
	 */
	private static String getNomFichier( Part part ) {
		//Boucle sur chacun des paramètres de l'en-tête "content-disposition". 
		for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
			//Recherche de l'éventuelle présence du paramètre "filename". 
			if ( contentDisposition.trim().startsWith( "filename" ) ) {
				//renvoyer le nom du fichier.
				return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
			}
		}
		return null;
	}


	/** Enregistrer un fichier pdf uploadé sur le disque
	 * @param request
	 * @param nomFichierVoulu nom fichier voulu
	 * @param chemin le chemin où l'on enregistre le fichier
	 */
	public String enregistrerFichierPdf( HttpServletRequest request,String nomFichierVoulu,String chemin ) {
		String fichier=new String();

		//Récupération du contenu du champ fichier du formulaire.
		String nomFichier = null;
		InputStream contenuFichier = null;
		try {
			Part part = request.getPart("publication");
			//recuperer nom fichier juste pour la validation.
			nomFichier = getNomFichier( part );
			try {
				ValidationFichierPdf(nomFichier);
				contenuFichier = part.getInputStream();
			} catch (Exception e) {
				setErreur(e.getMessage());
			}
		}
		catch ( IllegalStateException e ) {
			e.printStackTrace();
			setErreur("Les données envoyées sont trop volumineuses." );
		} catch ( IOException e ) {
			e.printStackTrace();
			setErreur("Erreur de configuration du serveur." );
		} catch ( ServletException e ) {
			e.printStackTrace();
			setErreur("Ce type de requete n'est pas supporte, merci d'utiliser "
					+ "le formulaire prevu pour envoyer votre fichier." );
		}

		// Si aucune erreur n'est survenue.
		if ( erreurs.isEmpty() ) {
			// Validation du fichier.
			try {
				validationFichier( contenuFichier );
			} catch ( Exception e1 ) {
				setErreur(e1.getMessage() );
			}
			fichier=nomFichier;
		}

		// Si aucune erreur n'est survenue jusqu'à présent */
		if ( erreurs.isEmpty() ) {
			/* Écriture du fichier sur le disque */
			try {
				String absoluteFilePath= request.getServletContext().getRealPath(chemin);
				System.out.println(absoluteFilePath);
				ecrireFichier( contenuFichier, nomFichierVoulu, absoluteFilePath);
			} catch ( Exception e ) {
				setErreur("Erreur lors de l'ecriture du fichier sur le disque." );
			}
		}

		// Initialisation du résultat global de la validation pour déboggage.
		if ( erreurs.isEmpty() ) {
			resultat = "Succès de l'envoi du fichier.";
		} else {
			resultat = "Échec de l'envoi du fichier.";
		}

		return fichier;
	}


	/** Enregistrer une archive zip uploadé sur le disque
	 * @param request
	 * @param nomFichierVoulu nom fichier voulu
	 * @param chemin le chemin où l'on enregistre le fichier
	 */
	public String enregistrerFichierZip( HttpServletRequest request,String nomFichierVoulu,String chemin ) {
		String fichier=new String();

		//Récupération du contenu du champ fichier du formulaire.
		String nomFichier = null;
		InputStream contenuFichier = null;
		try {
			Part part = request.getPart("source");
			//recuperer nom fichier juste pour la validation.
			nomFichier = getNomFichier( part );
			try {
				ValidationFichierZip(nomFichier);
				contenuFichier = part.getInputStream();
			} catch (Exception e) {
				setErreur(e.getMessage());
			}
		}
		catch ( IllegalStateException e ) {
			e.printStackTrace();
			setErreur("Les données envoyées sont trop volumineuses." );
		} catch ( IOException e ) {
			e.printStackTrace();
			setErreur("Erreur de configuration du serveur." );
		} catch ( ServletException e ) {
			e.printStackTrace();
			setErreur("Ce type de requete n'est pas supporte, merci d'utiliser "
					+ "le formulaire prevu pour envoyer votre fichier." );
		}

		// Si aucune erreur n'est survenue.
		if ( erreurs.isEmpty() ) {
			// Validation du fichier.
			try {
				validationFichier( contenuFichier );
			} catch ( Exception e1 ) {
				setErreur(e1.getMessage() );
			}
			fichier=nomFichier;
		}

		// Si aucune erreur n'est survenue jusqu'à présent 
		if ( erreurs.isEmpty() ) {
			/* Écriture du fichier sur le disque */
			try {
				String absoluteFilePath= request.getServletContext().getRealPath(chemin);
				System.out.println(absoluteFilePath);
				ecrireFichier( contenuFichier, nomFichierVoulu, absoluteFilePath);
			} catch ( Exception e ) {
				setErreur("Erreur lors de l'ecriture du fichier sur le disque." );
			}
		}

		// Initialisation du résultat global de la validation pour déboggage.
		if ( erreurs.isEmpty() ) {
			resultat = "Succès de l'envoi du fichier.";
		} else {
			resultat = "Échec de l'envoi du fichier.";
		}

		return fichier;
	}




	/** Enregistrer une archive zip uploadé sur le disque
	 * @param request
	 * @param nomFichierVoulu nom fichier voulu
	 * @param chemin le chemin où l'on enregistre le fichier
	 */
	public String enregistrerFichierZipStyle( HttpServletRequest request,String nomFichierVoulu,String chemin ) {
		String fichier=new String();

		//Récupération du contenu du champ fichier du formulaire.
		String nomFichier = null;
		InputStream contenuFichier = null;
		try {
			Part part = request.getPart("source");
			//recuperer nom fichier juste pour la validation.
			nomFichier = getNomFichier( part );
			try {
				ValidationFichierZipStyle(nomFichier);
				contenuFichier = part.getInputStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch ( IllegalStateException e ) {
			e.printStackTrace();
			setErreur("Les données envoyées sont trop volumineuses." );
		} catch ( IOException e ) {
			e.printStackTrace();
			setErreur("Erreur de configuration du serveur." );
		} catch ( ServletException e ) {
			e.printStackTrace();
			setErreur("Ce type de requete n'est pas supporte, merci d'utiliser "
					+ "le formulaire prevu pour envoyer votre fichier." );
		}

		// Si aucune erreur n'est survenue.
		if ( erreurs.isEmpty() ) {
			// Validation du fichier.
			try {
				validationFichier( contenuFichier );
			} catch ( Exception e1 ) {
				setErreur(e1.getMessage() );
			}
			fichier=nomFichier;
			if(fichier==null || fichier.isEmpty())
				return fichier;
		}

		// Si aucune erreur n'est survenue jusqu'à présent */
		if ( erreurs.isEmpty() ) {
			/* Écriture du fichier sur le disque */
			try {
				String absoluteFilePath= request.getServletContext().getRealPath(chemin);
				System.out.println(absoluteFilePath);
				ecrireFichier( contenuFichier, nomFichierVoulu, absoluteFilePath);
				} catch ( Exception e ) {
				setErreur("Erreur lors de l'ecriture du fichier sur le disque." );
			}
		}

		return fichier;
	}

	/** Télécharger un fichier depuis le site
	 * @param servlet
	 * @param fichierRequis fichier à télécharger
	 * @param response
	 * @param chemin le chemin où se trouve le fichier
	 */
	public void telechergerFichier( HttpServlet servlet,String fichierRequis, HttpServletResponse response,String chemin){

		/*
		 * Décode le nom de fichier récupéré, susceptible de contenir des
		 * espaces et autres caractères spéciaux, et prépare l'objet File
		 */
		System.out.println("########################################   "+ fichierRequis);
//		try {
//			fichierRequis = URLDecoder.decode( fichierRequis, "UTF-8" );
//			System.out.println("Nous soes entrez");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			
//		}
		String absoluteFilePath= servlet.getServletContext().getRealPath(chemin);
		File fichier = new File( absoluteFilePath, fichierRequis );
		System.out.println("Chemin crée : "+chemin+" vue par la machine : "+absoluteFilePath);
		

		/* Vérifie que le fichier existe bien */
		if ( !fichier.exists() ) {
			/*
			 * Si non, alors on envoie une erreur 404, qui signifie que la
			 * ressource demandée n'existe pas
			 */
			try {
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				response.sendError( HttpServletResponse.SC_NOT_FOUND );
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		/* Récupère le type du fichier */

		String type = servlet.getServletContext().getMimeType( fichier.getName() );

		/* Si le type de fichier est inconnu, alors on initialise un type par défaut */

		if ( type == null ) {

			type = "application/octet-stream";

		}
		/* Initialise la réponse HTTP */
		response.reset();
		response.setBufferSize( taille );
		response.setContentType( type );
		response.setHeader( "Content-Length", String.valueOf( fichier.length() ) );
		response.setHeader( "Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"" );

		/* Prépare les flux */
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
			/* Ouvre les flux */
			try {
				entree = new BufferedInputStream( new FileInputStream( fichier ), taille );
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				sortie = new BufferedOutputStream( response.getOutputStream(), taille );
			} catch (IOException e) {
				e.printStackTrace();
			}

			/* Lit le fichier et écrit son contenu dans la réponse HTTP */
			byte[] tampon = new byte[taille];
			int longueur;
			try {
				while ( ( longueur = entree.read( tampon ) ) > 0 ) {
					sortie.write( tampon, 0, longueur );
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				sortie.close();
				entree.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//***********************************DECONNEXION*******************************************
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Se déconnecter
	 */
	public void deconnecter() {
		this.persConnecte=null;
	}

}
