package application;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TabFilter {
	
	ToggleGroup radioButtonGroup = new ToggleGroup();
	RadioButton radioButtonBoxFilter= new RadioButton("Filtru cutie");
	RadioButton radioButtonGaussianFilter = new RadioButton("Filtru Gaussian");
	Button sizeButton = new Button("Marime filtru");
	TextField textFieldGaussian = new TextField();
	VBox vBoxRadioButtons = new VBox();
	BorderPane root = new BorderPane();
	final TabImage tabImage;
	Slider sigmaSlider = new Slider();
	HBox hBoxGaussSelect = new HBox();
	Label labelGaussSlider = new Label();
	Label labelGaussSize = new Label("Marime filtru");
	Button buttonSaveSize = new Button("Salveaza marimea");
	HBox hBoxBox = new HBox();
	ChoiceBox<String> choiceBoxBox = new ChoiceBox<String>();
	TableView<float[]> table;
	HBox hBoxTable = new HBox();
	Button buttonTable = new Button("Vizualizeaza filtru");
	int size;
	float sigma;
	Stage newWindow;
	ProgressIndicator progress = new ProgressIndicator();
	
	public BorderPane getRoot() {
		return root;
	}

	final Stage primaryStage;
	
	public TabFilter(final TabImage tabImage, final Stage ps) {
		primaryStage = ps;
		this.tabImage = tabImage;
		sigma = 5f;
		size = 3;
		table = new TableView<float[]>();
		table.getColumns().setAll(createColumns(size));
		table.setMaxSize(600, 550);

		textFieldGaussian.setMaxSize(50, 10);
		
		labelGaussSlider.setText(String.format("Sigma = %.2f", sigma));
		
		radioButtonGaussianFilter.setToggleGroup(radioButtonGroup);
		radioButtonBoxFilter.setToggleGroup(radioButtonGroup);
		radioButtonBoxFilter.setSelected(true);

		hBoxGaussSelect.setSpacing(20);
		hBoxGaussSelect.getChildren().addAll(radioButtonGaussianFilter, sigmaSlider, 
				labelGaussSlider, sizeButton);

		hBoxBox.setSpacing(20);
		hBoxBox.getChildren().addAll(radioButtonBoxFilter, choiceBoxBox);
		
		choiceBoxBox.setItems(FXCollections.observableArrayList("Cutie simpla", "Cutie ponderata"));
		choiceBoxBox.getSelectionModel().selectFirst();
		
		choiceBoxBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if(newValue.intValue() == 0) {
					tabImage.setKernel(MyImage.boxKernel(3));
				}
				if(newValue.intValue() == 1) {
					tabImage.setKernel(MyImage.boxWeightKernel(3));
				}
			}
			
		});
		
		root.setCenter(vBoxRadioButtons);
		
		hBoxTable.getChildren().addAll(buttonTable, table);
		hBoxTable.setSpacing(10);
		
		vBoxRadioButtons.getChildren().addAll(hBoxBox, hBoxGaussSelect,hBoxTable);
		vBoxRadioButtons.setSpacing(10);
		vBoxRadioButtons.setPadding(new Insets(30,10,10,10));
		
		sizeButton.setOnAction(new EventHandler<ActionEvent>() {
			 
	         @Override
	         public void handle(ActionEvent event) {
	        	 
	        	HBox textAndLabel = new HBox(textFieldGaussian, labelGaussSize, progress);
	        	VBox buttonAndHBox = new VBox(textAndLabel, buttonSaveSize);
	 
	        	buttonAndHBox.setSpacing(10);
	        	buttonAndHBox.setPadding(new Insets(10,10,10,10));
	        	textAndLabel.setSpacing(10);
	        	
	            StackPane secondaryLayout = new StackPane();
	            secondaryLayout.getChildren().add(buttonAndHBox);
	 
	            Scene secondScene = new Scene(secondaryLayout, 230, 100);
	 
	            // New window (Stage)
	            newWindow = new Stage();
	            newWindow.setTitle("Marime Filtru");
	            newWindow.setScene(secondScene);
	 
	            // Specifies the modality for new window.
	            newWindow.initModality(Modality.WINDOW_MODAL);
	 
	            // Specifies the owner Window (parent) for new window
	            newWindow.initOwner(primaryStage);
	 
	            // Set position of second window, related to primary window.
	            newWindow.setX(primaryStage.getX() + 200);
	            newWindow.setY(primaryStage.getY() + 100);
	 
	            newWindow.show();
	         }
	      });
		
		radioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				RadioButton chk = (RadioButton)newValue.getToggleGroup().getSelectedToggle(); // Cast object to radio button
	            //System.out.println("Selected Radio Button - "+chk.getText());
	            if(chk.getText() == "Filtru cutie")
	            {
	            	tabImage.setKernel(MyImage.boxKernel(3));
	            }
	            if(chk.getText() == "Filtru Gaussian")
	            {
	            	if(sigma == 0)
	            	{
	            		sigma = 0.1f;
	            	}
	            	tabImage.setKernel(MyImage.gaussianKernel(sigma,2));
	            }
			}
			
		});
		
		sigmaSlider.setMin(0);
		sigmaSlider.setMax(10);
		sigmaSlider.setBlockIncrement(0.1);
		sigmaSlider.setMajorTickUnit(0.5);
		sigmaSlider.setValue(5);

		sigmaSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				sigma = newValue.floatValue();
				labelGaussSlider.setText(String.format("Sigma = %.2f", sigma));
				if(sigma == 0)
            	{
            		sigma = 0.1f;
            	}
            	tabImage.setKernel(MyImage.gaussianKernel(sigma,2));
            	System.out.println(sigma);
			}
			
		});

		textFieldGaussian.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	textFieldGaussian.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		        if( !newValue.isEmpty() && newValue != "0")
		        {		        	
		        	int number = Integer.parseInt(textFieldGaussian.getText());
		        	System.out.println(number);
		        	if(number == 0)
			        {
			        	textFieldGaussian.setText("");
			        }
		        	if(number % 10 == 0)
		        	{
		        		textFieldGaussian.setText(Integer.toString(number / 10));
		        		number = 0;
		        	}
			        if(number > 9 && number != 0)
			        {
			        	textFieldGaussian.setText(Integer.toString(number % 10));
			        }
			        if(number <= 9 && number != 0)
			        {
			        	progress.setProgress((float)number/10);
			        }
		        }
		    }
		});
		
		buttonSaveSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(textFieldGaussian.getText() != "0" && !textFieldGaussian.getText().isEmpty() )
				{
					size = Integer.parseInt(textFieldGaussian.getText());
					newWindow.close();
				}
				else
				{
					new Alert(Alert.AlertType.ERROR, "Introduceti un numar intre 1 si 9").showAndWait();
				}
			}
			
		});
		
		buttonTable.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				table.getItems().clear();
				table.setItems(FXCollections.observableArrayList(MyImage.boxWeightKernel(3)));
				RadioButton chk = (RadioButton)radioButtonGroup.getSelectedToggle();
				if(chk.getText() == "Filtru cutie") {
					int index = choiceBoxBox.getSelectionModel().getSelectedIndex();
					if(index == 0) {
						table.getItems().clear();
						table.getColumns().setAll(createColumns(size));
						table.setItems(FXCollections.observableArrayList(MyImage.boxKernel(size)));
					}
					if(index == 1) {
						table.getItems().clear();
						table.getColumns().setAll(createColumns(3));
						table.setItems(FXCollections.observableArrayList(MyImage.boxWeightKernel(3)));
					}
				}
				if(chk.getText() == "Filtru Gaussian") {
					table.getItems().clear();
					int s = size % 2 == 0 ? (size / 2) : ((size - 1) / 2);
					table.getColumns().setAll(createColumns(size % 2 == 0 ? size+1 : size));
					table.setItems(FXCollections.observableArrayList(MyImage.gaussianKernel(sigma,s)));
				}
			}
		});
		
	}

    private List<TableColumn<float[], Float>> createColumns(int size) {
        return IntStream.range(0, size)
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    private TableColumn<float[], Float> createColumn(int c) {
        TableColumn<float[], Float> col = new TableColumn<>();
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[c]));
        col.setSortable(false);
        col.setResizable(false);
        col.setPrefWidth(150);
        return col;
    }

}
