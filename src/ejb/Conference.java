package ejb;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;



@Entity
public class Conference implements Serializable,Evenement{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String titre;
	private String acronym;
	private String site;
	private String rang;
	private String info;  //style 2colonnes...
	private String dateEvt;
	private String nomFichierStyle;
	private String dateAbstractSubmission;
	private String datePaperSubmission;
	private String dateNotifAcceptance;
	private String lieuEvt;
	private String dateRebuttal;
	private int nbPapier;
	private int nbPapierAccepte;
	private int nbPapierRefuse;
	
	@ManyToOne
	Personne participant;
	
	@OneToOne(mappedBy="conference",fetch=FetchType.EAGER)
	Version version;
	
	@ManyToMany(mappedBy="confSoumis",fetch=FetchType.EAGER)
	Set<Travail> travauxSoumisC;
	
	@ManyToMany(mappedBy="confCibles",fetch=FetchType.EAGER)
	Set<Travail> travauxCiblant;
	
	public Conference(){}
	public Conference(String titre,String acronym,String site, String rang,String info, 
			String date1, String date2, String date3, String date4,String pays){
		this.titre = titre;
		this.acronym=acronym;
		this.site=site;
		this.rang = rang;
		this.info = info;
		this.dateEvt = date1;
		this.dateAbstractSubmission = date2;
		this.datePaperSubmission = date3;
		this.dateNotifAcceptance = date4;
		this.lieuEvt=pays;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}

	public String getdateEvt() {
		return dateEvt;
	}
	public void setdateEvt(String dateEvt) {
		this.dateEvt = dateEvt;
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}


	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getLieuEvt() {
		return lieuEvt;
	}
	public void setLieuEvt(String lieuEvt) {
		this.lieuEvt = lieuEvt;
	}
	public Personne getParticipant() {
		return participant;
	}


	public void setParticipant(Personne participant) {
		this.participant = participant;
	}

	public String getDateAbstractSubmission() {
		return dateAbstractSubmission;
	}
	public void setDateAbstractSubmission(String dateAbstractSubmission) {
		this.dateAbstractSubmission = dateAbstractSubmission;
	}
	public String getDatePaperSubmission() {
		return datePaperSubmission;
	}
	public void setDatePaperSubmission(String datePaperSubmission) {
		this.datePaperSubmission = datePaperSubmission;
	}
	public String getDateNotifAcceptance() {
		return dateNotifAcceptance;
	}
	public void setDateNotifAcceptance(String dateNotifAcceptance) {
		this.dateNotifAcceptance = dateNotifAcceptance;
	}
	public String getDateEvt() {
		return dateEvt;
	}
	public void setDateEvt(String dateEvt) {
		this.dateEvt = dateEvt;
	}
	public Set<Travail> getTravauxSoumisC() {
		return travauxSoumisC;
	}
	public void setTravauxSoumisC(Set<Travail> travauxSoumisC) {
		this.travauxSoumisC = travauxSoumisC;
	}
	public Set<Travail> getTravauxCiblant() {
		return travauxCiblant;
	}
	public void setTravauxCiblant(Set<Travail> travauxCiblant) {
		this.travauxCiblant = travauxCiblant;
	}
	public String getNomFichierStyle() {
		return nomFichierStyle;
	}
	public void setNomFichierStyle(String nomFichierStyle) {
		this.nomFichierStyle = nomFichierStyle;
	}
	public String getDateRebuttal() {
		return dateRebuttal;
	}
	public void setDateRebuttal(String dateRebuttal) {
		this.dateRebuttal = dateRebuttal;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public int getNbPapier() {
		return nbPapier;
	}
	public void setNbPapier(int nbPapier) {
		this.nbPapier = nbPapier;
	}
	public int getNbPapierAccepte() {
		return nbPapierAccepte;
	}
	public void setNbPapierAccepte(int nbPapierAccepte) {
		this.nbPapierAccepte = nbPapierAccepte;
	}
	
	public float getTauxDeSucces() {
		if(nbPapierAccepte+nbPapierRefuse==0)
			return -1;
		return ((float)nbPapierAccepte/(float)(nbPapierAccepte+nbPapierRefuse))*100;
	}
	
	public int getNbPapierRefuse() {
		return nbPapierRefuse;
	}
	public void setNbPapierRefuse(int nbPapierRefuse) {
		this.nbPapierRefuse = nbPapierRefuse;
	}
	
	
}
