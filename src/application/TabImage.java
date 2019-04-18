package application;

import java.awt.image.BufferedImage;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabImage {
	
	HBox hBoxImages = new HBox();
	VBox vBoxButtonImages = new VBox();
	Button buttonApplyFilter = new Button("Blureaza");
	BorderPane root = new BorderPane();
	ImageView imageViewLeft = new ImageView();
	ImageView imageViewRight = new ImageView();
	MyImage myImg;
	float[][] kernel;
	HBox hBoxButton = new HBox();
	final ProgressBar progress = new ProgressBar();
	CallbackExecutor th = new CallbackExecutor();
	
	public BorderPane getRoot() {
		return root;
	}
	
	public void setKernel(float[][] newKernel) {
		this.kernel = newKernel;
	}
	
	public void loadImgLeft(Image img) {
		imageViewLeft.setImage(img);
	}
	
	public void loadImgLeft(BufferedImage img) {
		Image newImg = SwingFXUtils.toFXImage(img, null);
		imageViewLeft.setImage(newImg);
	}
	
	public void loadImgRight(Image img) {
		imageViewRight.setImage(img);
	}
	
	public void loadImgRight(BufferedImage img) {
		Image newImg = SwingFXUtils.toFXImage(img, null);
		imageViewRight.setImage(newImg);
	}
	
	public TabImage(final MyImage myImage) {
		hBoxImages.setAlignment(Pos.CENTER);
		hBoxImages.setSpacing(10);
		vBoxButtonImages.setAlignment(Pos.CENTER);
		vBoxButtonImages.setSpacing(10);
		
		progress.setProgress(0);
		
		hBoxButton.getChildren().addAll(buttonApplyFilter, progress);
		hBoxButton.setAlignment(Pos.CENTER);
		hBoxButton.setSpacing(10);
		
		hBoxImages.getChildren().addAll(imageViewLeft, imageViewRight);
		vBoxButtonImages.getChildren().addAll(hBoxImages, hBoxButton);
		
		myImg = myImage;
		this.loadImgLeft(myImg.getOrigBufferedImage());
		
		root.setCenter(vBoxButtonImages);
//		rootTab1.setTop(vBoxRadioButtons);
		
		
		buttonApplyFilter.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				myImage.setKernelTask(kernel);
				Task<Void> task = myImg.convolutionTask();
				progress.progressProperty().bind(task.progressProperty());
				task.setOnSucceeded(e -> updateImageView());
				
				new Thread(task).start();
			}
		});
	}
	
	private void updateImageView()
	{
		Image imageRight = SwingFXUtils.toFXImage(myImg.getBufferedImage(), null);
		imageViewRight.setImage(imageRight);
	}
}
