package application;

public enum Couleur {
	ROUGE("Couleur rouge"), NOIRE("Couleur noire");
	
	private String nom;
	
	Couleur(String nom){
		this.nom=nom;
	}
	
	public String toString(){
	    return nom;
	  }

}
