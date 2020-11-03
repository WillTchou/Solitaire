package application;

import java.util.Collections;
import java.util.Stack;

import application.Pile.PileType;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class Carte extends ImageView{
	
	protected Signe s;
	protected Valeur v;	
	protected boolean o;
	protected Image imageV;
	protected Image imageR;
	protected ImageView imv;
	protected Pile saPile;
	private static final ClipboardContent CLIPBOARD_CONTENT = new ClipboardContent();
	protected boolean place;
	
	private	EventHandler<MouseEvent> DragDetectedHandler(final Carte c){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final Dragboard db = c.startDragAndDrop(TransferMode.MOVE);
				DraggedCartes d=new DraggedCartes(c);
				CLIPBOARD_CONTENT.putString(d.getPile().toString());
				for(int i=0;i<d.taille();i++)	{
					db.setDragView(d.getC(i).getImage());
				}
				db.setContent(CLIPBOARD_CONTENT);
				System.out.println("DnD detecté."); 
				event.consume();  	
			} 
			 
		};
	}
	
	private EventHandler<DragEvent> DragOverHandler(Carte v){
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) { 
				Carte c=(Carte) event.getGestureSource();
				DraggedCartes d=new DraggedCartes(c);
				final Dragboard dragBroard = event.getDragboard(); 
				if (event.getGestureSource()!=v && dragBroard.hasString()) {
					if(opC(d.getBottomC(),v) && estSup(d.getBottomC(),v) && v.getPile().getType()!=PileType.MAIN)	event.acceptTransferModes(TransferMode.MOVE); 
					else if((d.taille()==1)&&(v.getPile().getType()==PileType.PLACE)&&(d.getBottomC().getSigne()==v.getSigne())&&(estSup(v,d.getBottomC()))) {
						d.getBottomC().changePlace();
						event.acceptTransferModes(TransferMode.MOVE);
					}
				}
				System.out.println("DnD drag."); 
				event.consume();
			}
		};
	}
	
	
	private EventHandler<DragEvent> DragDroppedHandler(Carte v){
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				boolean success = false;
				final Dragboard dragBroard = event.getDragboard(); 
		        Carte c=(Carte) event.getGestureSource();
		        DraggedCartes d=new DraggedCartes(c);
		        if(d.getBottomC().getPlace()) {
		        	try {
						d.getBottomC().toFront();
				        d.getBottomC().setLayoutX(v.getLayoutX());
				        d.getBottomC().setLayoutY(v.getLayoutY()+0.1);
				        if(d.getBottomC().getPile().getType()==PileType.LIGNEE && !d.getBottomC().getPile().getC(d.getBottomC().getPile().taille()-2).getRecto())	d.getBottomC().getPile().dragRetour(d);
				        d.getBottomC().getPile().retirer(c.getPile().indexOf(c));
				        v.getPile().addC(d.getBottomC());
				        d.getBottomC().setPile(v.getPile());
				        d.getBottomC().removeMouseEventHandler();
					}catch (Exception ex) {}
					finally { 
				        event.setDropCompleted(success); 
				        event.consume(); 
				    }
		        }
		        
		        else{
			        	try { 			        
			        
					        d.getBottomC().toFront();
					        d.getBottomC().setLayoutX(v.getLayoutX());
					        d.getBottomC().setLayoutY(v.getLayoutY()+20);
					        if(d.getBottomC().getPile().getType()==PileType.LIGNEE && d.getBottomC()!=d.getBottomC().getPile().getC(0))	d.getBottomC().getPile().dragRetour(d);
					        d.getBottomC().getPile().retirer(c.getPile().indexOf(c));
					        v.getPile().addC(d.getBottomC());
					        d.getBottomC().setPile(v.getPile());
							for(int i=1;i<d.taille();i++) {
								d.getC(i).toFront();
								d.getC(i).setLayoutX(d.getC(i-1).getLayoutX());
								d.getC(i).setLayoutY(d.getC(i-1).getLayoutY()+20);
								d.getC(i).getPile().retirer(i);
								v.getPile().addC(d.getC(i));
								d.getC(i).setPile(v.getPile());
							}
					        success=true;
						}catch (Exception ex) {}
						finally { 
					        event.setDropCompleted(success); 
					        event.consume(); 
						}
					}
			}
		};
	}
	
	Carte(Signe s,Valeur v,boolean o){
		this.s=s;
		this.v=v;
		this.o=o;
		this.place=false;
		imageV=new Image("cartesimage/CardBack.jpg");
		imageR=new Image("cartesimage/"+this.getVal()+this.getSigne()+".jpg");
		this.setImage(o ? imageR :imageV);
	}
	
	Signe getSigne() {
		return this.s;
	}
	
	Valeur getVal() {
		return this.v;
	}
	
	Couleur getCouleur() {
		if(this.getSigne()==Signe.COEUR || this.getSigne()==Signe.CARREAU)
			return Couleur.ROUGE;
		else
			return Couleur.NOIRE;
	}
	
	static boolean opC(Carte a,Carte b) {
		if(a.getCouleur()!=b.getCouleur())
			return true;
		return false;
	}
	
	static boolean estSup(Carte a,Carte b) {
		if(a.getVal().ordinal()==b.getVal().ordinal()+1)
			return true;
		return false;
	}
	
	static boolean memeSigne(Carte a,Carte b) {
		if(a.getSigne()==b.getSigne())
			return true;
		return false;
	}
	
	static Stack<Carte> packet(){
		Stack<Carte> p=new Stack<Carte>();
		Signe[] s=Signe.values();
		Valeur[] v=Valeur.values();
		for(int i=0;i<s.length;i++) {
			for(int k=0;k<v.length;k++) {
				p.add(new Carte(s[i],v[k],false));
			}
		}
		Collections.shuffle(p);
		return p;
	}
	
	
	ImageView getImageView() {
		return this.imv;
	}
	
	Pile getPile(){
		return saPile;
	}
	
	boolean getPlace() {
		return this.place;
	}
	void changePlace() {
		place=!place;
	}
	
	void setPile(Pile pile) {
		saPile=pile;
	}
	
	boolean getRecto() {
		return this.o;
	}
	
	void Retour() {
		o =! o;
		setImage(o ? imageR :imageV);
	}
	
	void addMouseEventHandler() {
		setOnDragDetected(DragDetectedHandler(this));
		setOnDragOver(DragOverHandler(this));
		setOnDragDropped(DragDroppedHandler(this));
	}
	
	void removeMouseEventHandler() {
		removeEventHandler(DragEvent.DRAG_OVER, DragOverHandler(this));
		removeEventHandler(DragEvent.DRAG_DROPPED, DragDroppedHandler(this));
	}
	
	
	@Override	
	public String toString() {
		return "Cette carte est "+this.getVal()+" de "+this.getSigne()+"\n";
	}
	

}
