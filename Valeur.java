package application;

public enum Valeur {
	ROI("Roi"), REINE("Reine"), VALET("Valet"), DIX("Dix"), NEUF("Neuf"), HUIT("Huit"), SEPT("Sept"), SIX("Six"), CINQ("Cinq"), QUATRE("Quatre"), TROIS("Trois"), DEUX("Deux"), AS("As");
	
	private String nom;
	
	Valeur(String nom){
		this.nom=nom;
	}
	public String toString(){
	    return nom;
	  }
	

}