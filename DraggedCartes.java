package application;


import java.util.ArrayList;

public class DraggedCartes {
	
	protected ArrayList<Carte> dc=new ArrayList<Carte>();
	
	public DraggedCartes(Carte c) {		
		if(c.getPile().indexOf(c)<=c.getPile().taille()) {
			dc.add(c);
			for(int i=c.getPile().indexOf(c)+1;i<c.getPile().taille();i++) {
				dc.add(c.getPile().getC(i));
				c.getPile().getC(i).toFront();
			}
		}
	}
	
	Carte getBottomC() {
		return this.dc.get(0);
	}
	
	Carte getC(int i) {
		if (dc.isEmpty())	return null;
		return dc.get(i);
	}
	
	ArrayList<Carte> getPile() {
		return this.dc;
	}
	
	
	int taille() {
		return this.dc.size();
	}
}
