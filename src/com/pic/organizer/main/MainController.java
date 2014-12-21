package com.pic.organizer.main;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.pic.organizer.services.ImageFileService;
import com.pic.organizer.util.StringUtil;

public class MainController implements Initializable
{
  @FXML
	private ListView<String> srcDirList;
  @FXML
  private TextField destDir;
  @FXML
  private TextField maxFilesInDir;
  @FXML
  private CheckBox resizeForWeb;
  @FXML
  private TextArea outputTxt;
  
  @FXML
  private Button removeSrcDirBtn;
  @FXML
  private Button startBtn;
	
	@Override
  public void initialize(URL inLocation, ResourceBundle inResources)
  {
	  srcDirList.getSelectionModel().selectedItemProperty().addListener(
	      (observable, oldValue, newValue) -> 
	      {
	        handleSrcListValueChanged(oldValue, newValue);
	      });
    logNormal("Initialization success.");
  }

  @FXML
  public void handleAddSrcDirectory(ActionEvent inEvent)
  {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Select source directory");
    
    File selectedDirectory = chooser.showDialog(getWindow());
    srcDirList.getItems().add(selectedDirectory.getAbsolutePath());
    logNormal("Added new source directory " + selectedDirectory.getAbsolutePath());
  }
  
  public void handleSrcListValueChanged(String inOldValue, String inNewValue)
  {
    if (inNewValue != null)
    {
      removeSrcDirBtn.setDisable(false);
    }
  }
  
  @FXML
  public void handleRemoveSrcDirectory(ActionEvent inEvent)
  {
    String selectedItem = srcDirList.getSelectionModel().getSelectedItem();
    if (selectedItem != null)
    {
      srcDirList.getItems().remove(selectedItem);
      logNormal("Source directory " + selectedItem + " removed.");
    }
    if (srcDirList.getItems().isEmpty())
    {
      removeSrcDirBtn.setDisable(true);
    }
  }
  
  @FXML
  public void handleSelectDestDirectory(ActionEvent inEvent)
  {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Select destination directory");
    
    File selectedDirectory = chooser.showDialog(getWindow());
    destDir.setText(selectedDirectory.getAbsolutePath());
    logNormal("Destination directory set to " + selectedDirectory.getAbsolutePath());
  }
  
  @FXML
  public void handleStartProcessing(ActionEvent inEvent)
  {
    if(validateInputs())
    {
      logNormal("Staring processing...");
    }
    else
    {
      displayModal("Error. Check output for details");
    }
  }

  @FXML
  public void handleResetMenuItem(ActionEvent inEvent)
  {
    srcDirList.setItems(FXCollections.observableArrayList());
    removeSrcDirBtn.setDisable(true);
    logNormal("All fields reset.");
    
  }
  
  @FXML
  public void handleQuitMenuItem(ActionEvent inEvent)
  {
    System.exit(0);
  }
  
  protected ImageFileService getFileService()
  {
    return new ImageFileService();
  }
  
  private void displayModal(String inMessage)
  {
    final Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.APPLICATION_MODAL);
  
    Label msgLabel = new Label(inMessage);
    msgLabel.setAlignment(Pos.BASELINE_CENTER);
  
    Button closeBtn = new Button("Close");
    closeBtn.setOnAction(( event) ->
    {
      dialogStage.close();
    });
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.BASELINE_CENTER);
    hBox.setSpacing(40.0);
    hBox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
    hBox.getChildren().addAll(closeBtn);
  
    VBox vBox = new VBox();
    vBox.setSpacing(30.0);
    vBox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
    vBox.getChildren().addAll(msgLabel, hBox);
  
    dialogStage.setScene(new Scene(vBox));
    dialogStage.show();
  }

  private boolean validateInputs()
  {
    logNormal("Validating selections.");
    if (srcDirList.getItems() == null || srcDirList.getItems().isEmpty())
    {
      logError("You must specify at least one source directory");
    }
    if (StringUtil.isEmpty(destDir.getText()))
    {
      logError("You must specify the destination directory");
    }
    return false;
  }

  private Scene getScene()
  {
    return srcDirList.getParent().getScene();
  }
  
  private Window getWindow()
  {
    return getScene().getWindow();
  }
  
  private void logNormal(String inMsg)
  {
    outputTxt.appendText(inMsg + "\n");
  }

  private void logError(String inMsg)
  {
    outputTxt.appendText("ERROR: " + inMsg + "\n");
  }

  public ListView<String> getSrcDirList()
  {
    return srcDirList;
  }

  public void setSrcDirList(ListView<String> inSrcDirList)
  {
    srcDirList = inSrcDirList;
  }

  public TextArea getOutputTxt()
  {
    return outputTxt;
  }

  public void setOutputTxt(TextArea inOutputTxt)
  {
    outputTxt = inOutputTxt;
  }

  public TextField getDestDir()
  {
    return destDir;
  }

  public void setDestDir(TextField inDestDir)
  {
    destDir = inDestDir;
  }

  public TextField getMaxFilesInDir()
  {
    return maxFilesInDir;
  }

  public void setMaxFilesInDir(TextField inMaxFilesInDir)
  {
    maxFilesInDir = inMaxFilesInDir;
  }

  public CheckBox getResizeForWeb()
  {
    return resizeForWeb;
  }

  public void setResizeForWeb(CheckBox inResizeForWeb)
  {
    resizeForWeb = inResizeForWeb;
  }

  public Button getRemoveSrcDirBtn()
  {
    return removeSrcDirBtn;
  }

  public void setRemoveSrcDirBtn(Button inRemoveSrcDirBtn)
  {
    removeSrcDirBtn = inRemoveSrcDirBtn;
  }

  public Button getStartBtn()
  {
    return startBtn;
  }

  public void setStartBtn(Button inStartBtn)
  {
    startBtn = inStartBtn;
  }
	
	

}
