package application;

import java.util.ArrayList;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pile extends Pane{	
	
	protected ArrayList<Carte> pile= new ArrayList<Carte>();
	protected PileType pt;
	protected Rectangle r=new Rectangle();
	
	Pile(PileType pt){
		this.pt=pt;
		switch(pt) {
		case PLACE:
			this.r.setFill(Color.rgb(125,0,22));
			break;
		case LIGNEE:
			this.r.setFill(Color.rgb(0,105,70));
			break;
		case MAIN:
			this.r.setFill(Color.rgb(20,0,20));
			break;
		}
		this.r.setHeight(96);
		this.r.setWidth(71);
	}
	
	int taille() {
		return pile.size();
	}
	
	boolean estVide() {
		if (pile.isEmpty())	return true;
		return false;
	}
	
	Carte getC(int i) {
		if (pile.isEmpty())	return null;
		return pile.get(i);
	}
	
	ArrayList<Carte> getPile(){
		return pile;
	}
	
	void addC(Carte c) {
		this.pile.add(c);
	}
	
	void clear() {
		this.pile.clear();
	}
	
	void retirer(int i) {
		this.pile.remove(i);
	}
	
	int indexOf(Carte c) {
		return this.pile.indexOf(c);
	}
	
	PileType getType() {
		return this.pt;
	}
	
	void dragRetour(DraggedCartes d) {
		Carte c=this.pile.get(this.taille()-d.taille()-1);
		if(!this.estVide())	c.Retour();
		c.addMouseEventHandler();
	}
	
	Carte sommet() {
		return this.pile.get(this.pile.size()-1);
	}
	
	
	Rectangle getRec() {
		return this.r;
	}
	
	
	public enum PileType {
		MAIN, PLACE, LIGNEE
	}
	
	@Override	
	public String toString() {
		String r="";
		for(int i=0;i<this.taille();i++) {
			r+=this.getC(i).toString();
		}
		return r;
	}
	
}
