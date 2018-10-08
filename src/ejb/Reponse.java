package ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Reponse implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String reviewer;
	private String jugement;
	
	@Column(columnDefinition="TEXT")
	private String detailReponse;
	
	@ManyToOne
	Version version;
	
	public Reponse(){}
	
	
	public Reponse(String reviewer, String jug,String detailReponse){
		this.reviewer=reviewer;
		this.jugement = jug;
		this.detailReponse = detailReponse;
	}


	public String getReviewer() {
		return reviewer;
	}


	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}


	public String getJugement() {
		return jugement;
	}


	public void setJugement(String jugement) {
		this.jugement = jugement;
	}


	public Version getVersion() {
		return version;
	}


	public void setVersion(Version version) {
		this.version = version;
	}


	public String getDetailReponse() {
		return detailReponse;
	}


	public void setDetailReponse(String detailReponse) {
		this.detailReponse = detailReponse;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
