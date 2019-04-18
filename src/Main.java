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
			StackPane root = new StackPane();
			TabPane tabPane = new TabPane();
			tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
			
			Tab tab1 = new Tab("Imagini");
			Tab tab2 = new Tab("Filtre");
			
			root.getChildren().add(tabPane);
			tabPane.getSelectionModel().select(0);
			tabPane.getTabs().addAll(tab1,tab2);
			
			BorderPane rootTab1 = new BorderPane();
			tab1.setContent(rootTab1);
			
			Scene scene = new Scene(root,1000,500);
			
			Image imageLeft = SwingFXUtils.toFXImage(img.getBufferedImage(), null);
			ImageView imageViewLeft = new ImageView(imageLeft);
			
			imageViewRight = new ImageView();
			
			Button buttonApplyFilter = new Button("Blureaza");
			buttonApplyFilter.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					img.convolution(kernel);
					Image imageRight = SwingFXUtils.toFXImage(img.getBufferedImage(), null);
					imageViewRight.setImage(imageRight);
				}
			});
			
			//imagini
			HBox hBoxImages =  new HBox(imageViewLeft, imageViewRight);
			hBoxImages.setAlignment(Pos.CENTER);
			hBoxImages.setSpacing(10);
			
			//imagini plus buton
			VBox vBoxButtonImages = new VBox(hBoxImages, buttonApplyFilter);
			vBoxButtonImages.setAlignment(Pos.CENTER);
			vBoxButtonImages.setSpacing(10);
			
			//radiobuttons
			ToggleGroup radioButtonGroup = new ToggleGroup();
			
			radioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					// TODO Auto-generated method stub
					RadioButton chk = (RadioButton)newValue.getToggleGroup().getSelectedToggle(); // Cast object to radio button
		            //System.out.println("Selected Radio Button - "+chk.getText());
		            if(chk.getText() == "Filtru cutie")
		            {
		            	kernel = MyImage.boxKernel(3);
		            }
		            if(chk.getText() == "Filtru Gaussian")
		            {
		            	kernel = MyImage.gaussianKernel(5,2);
		            }
				}
				
			});
			
			RadioButton radioButtonBoxFilter= new RadioButton("Filtru cutie");
			radioButtonBoxFilter.setToggleGroup(radioButtonGroup);
			radioButtonBoxFilter.setSelected(true);
			
			RadioButton radioButtonGaussianFilter = new RadioButton("Filtru Gaussian");
			radioButtonGaussianFilter.setToggleGroup(radioButtonGroup);
			
			VBox vBoxRadioButtons = new VBox(radioButtonBoxFilter, radioButtonGaussianFilter);
			
			
			rootTab1.setCenter(vBoxButtonImages);
			rootTab1.setTop(vBoxRadioButtons);
			
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
