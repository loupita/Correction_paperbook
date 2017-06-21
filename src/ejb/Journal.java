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
import javax.persistence.OneToOne;



@Entity
public class Journal implements Serializable, Evenement{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String titre;
	private String site;
	private String rang;
	private String info;  //style, police, 2colonnes...
	private String dateAbstractSubmission;
	private String datePaperSubmission;
	private String dateRebuttal;
	private String nomFichierStyle;
	private int nbPapier;
	private int nbPapierAccepte;
	private int nbPapierRefuse;
	
	@ManyToOne
	Personne participant;
	
	@OneToOne(mappedBy="journal",fetch=FetchType.EAGER)
	Version version;

	@ManyToMany(mappedBy="journalSoumis",fetch=FetchType.EAGER)
	Set<Travail> travauxSoumisJ;
	
	@ManyToMany(mappedBy="journalCibles",fetch=FetchType.EAGER)
	Set<Travail> travauxCiblant;
	
	public Journal(){}
	public Journal(String titre,String site, String rang,String info, 
			 String date1, String date2){
		
		this.titre = titre;
		this.site=site;
		this.rang = rang;
		this.info = info;
		this.dateAbstractSubmission = date1;
		this.datePaperSubmission = date2;
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
	public Set<Travail> getTravauxSoumisJ() {
		return travauxSoumisJ;
	}
	public void setTravauxSoumisJ(Set<Travail> travauxSoumisJ) {
		this.travauxSoumisJ = travauxSoumisJ;
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

	public void incrementerNbPapier(){
		setNbPapier(getNbPapier()+1);
	}
	
	public void incrementerNbPapierAccepte(){
		setNbPapierAccepte(getNbPapierAccepte()+1);
	}
	
	public void incrementerNbPapierRefuse(){
		setNbPapierRefuse(getNbPapierRefuse()+1);
	}
}
