package com.pic.organizer;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

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
    log("Initialization success.");
  }

  @FXML
  public void handleAddSrcDirectory(ActionEvent inEvent)
  {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Select source directory");
    
    File selectedDirectory = chooser.showDialog(getWindow());
    srcDirList.getItems().add(selectedDirectory.getAbsolutePath());
    log("Added new source directory " + selectedDirectory.getAbsolutePath());
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
      log("Source directory " + selectedItem + " removed.");
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
    log("Destination directory set to " + selectedDirectory.getAbsolutePath());
  }
  
  @FXML
  public void handleResetMenuItem(ActionEvent inEvent)
  {
    srcDirList.setItems(FXCollections.observableArrayList());
    removeSrcDirBtn.setDisable(true);
    log("All fields reset.");
    
  }
  
  @FXML
  public void handleQuitMenuItem(ActionEvent inEvent)
  {
    System.exit(0);
  }

  private Scene getScene()
  {
    return srcDirList.getParent().getScene();
  }
  
  private Window getWindow()
  {
    return getScene().getWindow();
  }
  
  private void log(String inMsg)
  {
    outputTxt.appendText(inMsg + "\n");
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
