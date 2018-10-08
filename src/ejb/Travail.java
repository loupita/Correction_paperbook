package ejb;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
//import javax.validation.constraints.Size;

@Entity
public class Travail {




	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String titre;
	
	@Column(columnDefinition="TEXT")
	private String abstr;
	
	private String nomFichier;
	private String nomSource;
	private Boolean soumis;
	private long jourRestant;
	private boolean hasResponse;


	@ManyToOne
	Personne proprio;

	//*****************************LES EVENEMENTS SOUMIS*********************************
	@ManyToMany
	@JoinTable(
			name="Travail_ConfsSoumis",
			joinColumns=
			@JoinColumn(name="travailS_id", referencedColumnName="ID"),
			inverseJoinColumns=
			@JoinColumn(name="confS_id", referencedColumnName="ID")
			)
	Set<Conference> confSoumis;

	@ManyToMany
	@JoinTable(
			name="Travail_JournalSoumis",
			joinColumns=
			@JoinColumn(name="travailS_id", referencedColumnName="ID"),
			inverseJoinColumns=
			@JoinColumn(name="journalS_id", referencedColumnName="ID")
			)
	Set<Journal> journalSoumis;

	//*****************************LES EVENEMENTS CIBLES*********************************
	@ManyToMany
	@JoinTable(
			name="Travail_ConfsCibles",
			joinColumns=
			@JoinColumn(name="travail_id", referencedColumnName="ID"),
			inverseJoinColumns=
			@JoinColumn(name="conference_id")
			)
	Set<Conference> confCibles = new HashSet<Conference>();

	@ManyToMany
	@JoinTable(
			name="Travail_JournalCibles",
			joinColumns=
			@JoinColumn(name="travail_id", referencedColumnName="ID"),
			inverseJoinColumns=
			@JoinColumn(name="journal_id")
			)
	Set<Journal> journalCibles = new HashSet<Journal>();

	@OneToMany(mappedBy="travail",fetch=FetchType.EAGER)
	Set<Version> archives ;

	public Travail(){}

	public Travail(String titre, String abstr,
			boolean soumis){

		this.titre =titre;
		this.abstr= abstr;
		this.soumis = soumis;
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


	public String getAbstr() {
		return abstr;
	}

	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	public String getNomSource() {
		return nomSource;
	}

	public void setNomSource(String nomSource) {
		this.nomSource = nomSource;
	}

	public Boolean getSoumis() {
		return soumis;
	}

	public void setSoumis(Boolean soumis) {
		this.soumis = soumis;
	}

	public Personne getProprio() {
		return proprio;
	}

	public void setProprio(Personne proprio) {
		this.proprio = proprio;
	}

	public void ajouterArchive(Version archive) {
		this.archives.add(archive);
	}


	public Set<Conference> getConfCibles() {
		return confCibles;
	}

	public void setConfCibles(Set<Conference> confCibles) {
		this.confCibles = confCibles;
	}

	public Set<Journal> getJournalCibles() {
		return journalCibles;
	}

	public void setJournalCibles(Set<Journal> journalCibles) {
		this.journalCibles = journalCibles;
	}

	public Set<Conference> getConfSoumis() {
		return confSoumis;
	}

	public void setConfSoumis(Set<Conference> confSoumis) {
		this.confSoumis = confSoumis;
	}

	public Set<Journal> getJournalSoumis() {
		return journalSoumis;
	}

	public void setJournalSoumis(Set<Journal> journalSoumis) {
		this.journalSoumis = journalSoumis;
	}

	public long getJourRestant() {
		return jourRestant;
	}

	public void setJourRestant(long jourRestant) {
		this.jourRestant = jourRestant;
	}

	public boolean isHasResponse() {
		return hasResponse;
	}

	public void setHasResponse(boolean hasResponse) {
		this.hasResponse = hasResponse;
	}
	

}
