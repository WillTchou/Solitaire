package application;


import java.util.Stack;

import application.Pile.PileType;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class Solitaire extends Pane{
	
	
    public static final int STACK_COUNT = 4;
    public static final int DECK_COUNT = 7;
	
	protected Pile main;
	protected Pile decks[]=new Pile[DECK_COUNT];
	protected Pile[] piles=new Pile[STACK_COUNT];
	protected double dragX,dragY;
	protected Stack<Carte> pioche;
	protected ImageView tv=new ImageView(new Image("cartesimage/TasVide.jpg"));
	
	
	protected EventHandler<MouseEvent> onMouseClickedHandlerV = e -> {
		ImageView iv= (ImageView) e.getSource();
		for(int i=main.taille()-1;i>=0;i--) {
			main.getC(i).Retour();
			main.getC(i).setLayoutX(iv.getLayoutX());
			main.getC(i).setLayoutY(iv.getLayoutY());
			main.getC(i).removeMouseEventHandler();
			pioche.push(main.getC(i));
			main.retirer(i);				
		}
	};
	
	protected EventHandler<MouseEvent> onMouseClickedHandler = e -> {
		Carte c=(Carte) e.getSource();
		c=pioche.peek();
		c.Retour();
		c.toFront();
		c.setLayoutX(955);
		c.setLayoutY(100);
		removeMouseClickedEvent(c);
		c.addMouseEventHandler();
		main.addC(c);
		c.setPile(main);	
		pioche.pop();
		pioche.peek().setLayoutX(1040);
		pioche.peek().setLayoutY(100);
		getChildren().add(pioche.peek());
		addMouseClickedEvent(pioche.peek());
	};
		
	
	
	public Solitaire() {
		pioche= Carte.packet();
		initialisation();
		distribution();
	}
	
	
	void initialisation() {
		
		this.tv.setLayoutX(1040);
		this.tv.setLayoutY(100);
		
		this.getChildren().add(this.tv);
		this.tv.setOnMouseClicked(onMouseClickedHandlerV);
		
		main=new Pile(PileType.MAIN);
		main.getRec().setLayoutX(955);
		main.getRec().setLayoutY(100);
		this.getChildren().add(main.getRec());
		
		Button restartB=new Button("Restart");
		restartB.setStyle("-fx-background-color: #A44340; ");
		getChildren().add(restartB);
		restartB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
                restart();
            }
		});
		
	}
	
	void distribution() {
		
		for(int i=0;i<DECK_COUNT;i++) {
			decks[i]=new Pile(PileType.LIGNEE);
			Pile ds=decks[i];
			ds.getRec().setLayoutX(105+i*110);
			ds.getRec().setLayoutY(110);
			ds.getRec().setOnDragOver(dragEvent -> { 
				Carte c=(Carte) dragEvent.getGestureSource();
				DraggedCartes d=new DraggedCartes(c);
				final Dragboard dragBroard = dragEvent.getDragboard(); 
				if (dragEvent.getGestureSource()!=ds.getRec() && dragBroard.hasString()) {
					if((d.getBottomC().getVal()==Valeur.ROI))	dragEvent.acceptTransferModes(TransferMode.MOVE); 
				} 
				dragEvent.consume();
			});
			decks[i].getRec().setOnDragDropped(dragEvent -> { 
				boolean success = false;
				try { 
			        final Dragboard dragBroard = dragEvent.getDragboard(); 
			        Carte c=(Carte) dragEvent.getGestureSource();
			        DraggedCartes d=new DraggedCartes(c);
			        d.getBottomC().toFront();
			        d.getBottomC().setLayoutX(ds.getRec().getLayoutX());
			        d.getBottomC().setLayoutY(ds.getRec().getLayoutY());
			        if(d.getBottomC().getPile().getType()==PileType.LIGNEE)	d.getBottomC().getPile().dragRetour(d);
			        d.getBottomC().getPile().retirer(c.getPile().indexOf(c));
			        d.getBottomC().setPile(ds);
			        for(int k=1;k<d.taille();k++) {
						d.getC(k).toFront();
						d.getC(k).setLayoutX(d.getC(k-1).getLayoutX());
						d.getC(k).setLayoutY(d.getC(k-1).getLayoutY()+20);
						d.getC(k).getPile().retirer(k);
						ds.addC(d.getC(k));
						d.getC(k).setPile(ds);
					}
			        success=true;
				}catch (Exception ex) {}
				finally { 
					dragEvent.setDropCompleted(success); 
					dragEvent.consume(); 
			    } 
			});
			this.getChildren().add(decks[i].getRec());
			
			for(int k=0;k<=i;k++) {
					
				decks[i].addC(this.pioche.peek());
				this.pioche.pop();
				
				if(decks[i].taille()==1+i) {
					decks[i].getC(k).Retour();
					decks[i].getC(k).setLayoutX(105+i*110);
					decks[i].getC(k).setLayoutY(110+k*20);
					decks[i].getC(k).setPile(decks[i]);
					getChildren().add(decks[i].getC(k));
					decks[i].getC(k).addMouseEventHandler();
					continue;
				}
					
				decks[i].getC(k).setLayoutX(105+i*110);
				decks[i].getC(k).setLayoutY(110+k*20);
				decks[i].getC(k).setPile(decks[i]);
				getChildren().add(decks[i].getC(k));
			}
		}
		
		for(int i=0;i<STACK_COUNT;i++) {
			piles[i] = new Pile(PileType.PLACE);
			Pile ps=piles[i];
			ps.getRec().setLayoutX(1050);
			ps.getRec().setLayoutY(295+i*100);
			ps.getRec().setOnDragOver(dragEvent -> { 
				Carte c=(Carte) dragEvent.getGestureSource();
				DraggedCartes d=new DraggedCartes(c);
				final Dragboard dragBroard = dragEvent.getDragboard(); 
				if (dragEvent.getGestureSource()!=ps.getRec() && dragBroard.hasString()) {
					if((d.getBottomC().getVal()==Valeur.AS) && (d.taille()==1))	dragEvent.acceptTransferModes(TransferMode.MOVE); 
				} 
				dragEvent.consume();
			});
			piles[i].getRec().setOnDragDropped(dragEvent -> { 
				boolean success = false;
				try { 
			        final Dragboard dragBroard = dragEvent.getDragboard(); 
			        Carte c=(Carte) dragEvent.getGestureSource();
			        DraggedCartes d=new DraggedCartes(c);
			        d.getBottomC().toFront();
			        d.getBottomC().setLayoutX(ps.getRec().getLayoutX());
			        d.getBottomC().setLayoutY(ps.getRec().getLayoutY());
			        if(d.getBottomC().getPile().getType()==PileType.LIGNEE)	d.getBottomC().getPile().dragRetour(d);
			        d.getBottomC().getPile().retirer(c.getPile().indexOf(c));
			        d.getBottomC().setPile(ps);
			        d.getBottomC().removeMouseEventHandler();
			        success=true;
				}catch (Exception ex) {}
				finally { 
					dragEvent.setDropCompleted(success); 
					dragEvent.consume(); 
			    } 
			});
			this.getChildren().add(piles[i].getRec());
		}
		
		this.pioche.peek().setLayoutX(1040);
		this.pioche.peek().setLayoutY(100);
		for(Carte carte:this.pioche)	carte.setPile(main);
		getChildren().add(this.pioche.peek());
		addMouseClickedEvent(this.pioche.peek());
	}
	
	
	void addMouseClickedEvent(Carte c) {
		c.setOnMouseClicked(onMouseClickedHandler);
		c.setCursor(Cursor.HAND);
	}
	
	void removeMouseClickedEvent(Carte c) {
		c.removeEventFilter(MouseEvent.MOUSE_CLICKED, onMouseClickedHandler);
	}
	 	
	void restart() {
		this.getChildren().clear();
		pioche = Carte.packet();
		this.initialisation();
		this.distribution();
	}
	
	boolean estFinie() {
		int c=0;
		for(int i=0;i<STACK_COUNT;i++) {
			if(piles[i].taille()==13 && piles[i]==piles[i+1]) c++;
		}
		if(c==STACK_COUNT-1) return true;
		return false;
	}

}
