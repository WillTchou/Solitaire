package application;

public enum Signe {
	COEUR("Coeur"), TREFLE("Trefle"), CARREAU("Carreau"), PIQUE("Pique");
	
	private String nom;
	
	Signe(String nom){
		this.nom=nom;
	}
	
	public String toString(){
	    return nom;
	  }
	
	Couleur couleurDe(Signe s) {
		if(s==Signe.COEUR || s==Signe.CARREAU) {
			return Couleur.ROUGE;
		}
		else
			return Couleur.NOIRE;
	}

}
