package ejb;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@Entity
public class Personne implements Serializable {
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String nom;
	private String Prenom;
	private String email;
	private String identifiant;
	private String mdp;
	private String session_id;

	
	@OneToMany(mappedBy="participant",fetch=FetchType.EAGER)
	Set<Conference> conferences;
	
	@OneToMany(mappedBy="participant",fetch=FetchType.EAGER)
	Set<Journal> Journaux;
	
	@OneToMany(mappedBy="proprio",fetch=FetchType.EAGER)
	Set<Travail> travaux;
	
	public Personne(){}
	public Personne(String nom,String prenom, String email ,String ident,String mdp){
		this.nom=nom;
		this.Prenom=prenom;
		this.email=email;
		this.identifiant=ident;
		this.mdp=mdp;
	}
	
	
	
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getIdentifiant() {
		return identifiant;
	}
	
	public void setIdentifiant(String indentifiant) {
		this.identifiant = indentifiant;
	}
	
	public String getMdp() {
		return mdp;
	}
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public Set<Conference> getConferences() {
		return conferences;
	}
	public void setConferences(Set<Conference> conferences) {
		this.conferences = conferences;
	}
	public Set<Journal> getJournaux() {
		return Journaux;
	}
	public void setJournaux(Set<Journal> journaux) {
		Journaux = journaux;
	}
	public Set<Travail> getTravaux() {
		return travaux;
	}
	public void setTravaux(Set<Travail> travaux) {
		this.travaux = travaux;
	}
	
	
}
