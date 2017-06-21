package ejb;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Version implements Serializable, Comparable<Version> {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private Date date;
	private String versionFichier;
	private String versionArchive;
	private boolean publique;
	private boolean accepte;
	private boolean refuse;
	private String nomPresentateur;  	//par défault le propriétaire.
	private String prenomPresentateur;
	@ManyToOne
	Travail travail;
	
	
	@OneToOne
	Conference conference;
	
	@OneToOne
	Journal journal;
	
	@OneToMany(mappedBy="version",fetch=FetchType.EAGER)
	Set<Reponse> reponses;
	
	public Version(){}
	public Version(Date date){
		this.date = date;
	}


	public Travail getTravail() {
		return travail;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTravail(Travail travail) {
		this.travail = travail;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Conference getConference() {
		return conference;
	}
	public void setConference(Conference conference) {
		this.conference = conference;
	}
	public Journal getJournal() {
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
	public String getVersionFichier() {
		return versionFichier;
	}
	public void setVersionFichier(String versionFichier) {
		this.versionFichier = versionFichier;
	}
	public String getVersionArchive() {
		return versionArchive;
	}
	public void setVersionArchive(String versionArchive) {
		this.versionArchive = versionArchive;
	}
	
	@Override
	public int compareTo(Version o) {
			Version arg = (Version) o;
			Date darg = arg.getDate();
			if(darg.compareTo(date)==-1)
				return -1;
			else
				return 1;
	}
	
	public boolean isPublique() {
		return publique;
	}
	
	public void setPublique(boolean publique) {
		this.publique = publique;
	}
	
	public boolean isAccepte() {
		return accepte;
	}
	
	public void setAccepte(boolean accepte) {
		this.accepte = accepte;
	}
	
	public String getNomPresentateur() {
		return nomPresentateur;
	}
	
	public void setNomPresentateur(String nomPresentateur) {
		this.nomPresentateur = nomPresentateur;
	}
	public String getPrenomPresentateur() {
		return prenomPresentateur;
	}
	public void setPrenomPresentateur(String prenomPresentateur) {
		this.prenomPresentateur = prenomPresentateur;
	}
	public boolean isRefuse() {
		return refuse;
	}
	public void setRefuse(boolean refuse) {
		this.refuse = refuse;
	}
	
	
}
