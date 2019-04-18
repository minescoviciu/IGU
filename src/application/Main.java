package application;
	
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			final TabImage tabImage = new TabImage(img);
			
			StackPane root = new StackPane();
			
			TabPane tabPane = new TabPane();
			tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
			
			Tab tab1 = new Tab("Imagini");
			Tab tab2 = new Tab("Filtre");
			
			root.getChildren().add(tabPane);
			tabPane.getSelectionModel().select(0);
			tabPane.getTabs().addAll(tab1,tab2);
			
			TabFilter tabFilter =  new TabFilter(tabImage, primaryStage);
			
			BorderPane rootTab1 = new BorderPane();
			tab1.setContent(tabFilter.getRoot());

			tab2.setContent(tabImage.getRoot());
			
			Scene scene = new Scene(root,1000,550);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	float[][] kernel;
	ImageView imageViewRight;
	
	
	private static MyImage img;
	
	public static void main(String[] args) {
		img = new MyImage(args[0], args[1]);
//		float[][] kernel = img.boxKernel(3);
//		float[][] kernel = img.makeKernel(1, 2);
//		for (int i = 0; i < kernel.length; i++) {
//			for (int j = 0; j < kernel[0].length; j++) {
//				System.out.print(kernel[i][j] + " ");
//			}
//			System.out.println();
//		}
//		img.convolution(kernel);
//		img.saveImage();
		launch(args);
	}
}
