package ejb;

import java.util.Set;

public interface Evenement {

	
	public int getId();
	public String getTitre() ;
	public void setTitre(String titre);
	public String getRang();
	public void setRang(String rang) ;
	public String getInfo() ;
	public void setInfo(String info) ;
	public String getSite() ;
	public void setSite(String site) ;
	public Personne getParticipant() ;
	public void setParticipant(Personne participant) ;
	public String getDateAbstractSubmission() ;
	public void setDateAbstractSubmission(String dateAbstractSubmission);
	public String getDatePaperSubmission() ;
	public void setDatePaperSubmission(String datePaperSubmission) ;
	public Set<Travail> getTravauxCiblant() ;
	public void setTravauxCiblant(Set<Travail> travauxCiblant) ;
	public String getDateRebuttal();
	public void setDateRebuttal(String dateRebuttal);
	public int getNbPapier();
	public void setNbPapier(int nbPapier);
	public int getNbPapierAccepte() ;
	public void setNbPapierAccepte(int nbPapierAccepte);
	public int getNbPapierRefuse();
	public void setNbPapierRefuse(int nbPapierRefuse);
	public float getTauxDeSucces() ;
}
