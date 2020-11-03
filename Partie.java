package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Partie extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception{
			primaryStage.setTitle("Solitaire");
	        Group root = new Group();
	        
	        ImageView Tapis=new ImageView(new Image("cartesimage/verdebelote.jpg"));
	        Tapis.setFitWidth(1280);
	        Tapis.setFitHeight(800);
	        root.getChildren().add(Tapis);
	        
	        
	        Scene scene=new Scene(root,1280,1000);
	        

	       Solitaire s=new Solitaire();
	       root.getChildren().add(s);
	       primaryStage.setScene(scene);
	       primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}	

}
