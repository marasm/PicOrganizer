package com.pic.organizer.main;


import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.marasm.util.StringUtil;
import com.pic.organizer.services.ImageFileReaderService;
import com.pic.organizer.services.ImageFileWriterService;
import com.pic.organizer.valueobjects.ImageInfoVO;

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
  private CheckBox recursive;
  @FXML
  private CheckBox includeVideos;
  @FXML
  private CheckBox useDaySubFolders;
  @FXML
  private TextArea outputTxt;
  
  @FXML
  private Button removeSrcDirBtn;
  @FXML
  private Button startBtn;
  @FXML
  private ProgressBar progressBar;
  
  
  private transient ImageFileReaderService fileReaderService;
  private transient ImageFileWriterService fileWriterService;
	
	@Override
  public void initialize(URL inLocation, ResourceBundle inResources)
  {
	  srcDirList.getSelectionModel().selectedItemProperty().addListener(
	      (observable, oldValue, newValue) -> 
	      {
	        handleSrcListValueChanged(oldValue, newValue);
	      });
	  
    fileReaderService = getFileReaderService();
    fileWriterService = getFileWriterService();
    
    logNormal("Initialization success.");
    
    //TEST CODE
//    recursive.setSelected(true);
//    srcDirList.getItems().add("/Users/mkorotkovas/Desktop/PicTestSrc");
//    destDir.setText("/Users/mkorotkovas/Desktop/PicTestDest");
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
  
  @FXML
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
  public void handleUseDaySubfolders(ActionEvent inEvent)
  {
    if (useDaySubFolders.isSelected())
    {
      maxFilesInDir.setDisable(false);
    }
    else
    {
      maxFilesInDir.setDisable(true);
    }
  }
  
  @FXML
  public void handleStartProcessing(ActionEvent inEvent)
  {
    if(validateInputs())
    {
      logNormal("Staring processing...");

      try
      {
        startBtn.setDisable(true);
        logNormal("Building the list of files...");
        List<ImageInfoVO> imageList = 
            fileReaderService.getImagesFromDirectories(
            srcDirList.getItems(), 
            recursive.isSelected(), includeVideos.isSelected());
        logNormal("Sorting " + imageList.size() + " images by date taken...");
        Collections.sort(imageList, (imageVO1, imageVO2) -> 
          {
            if (imageVO1 == null || imageVO1.getDateTaken() == null)
              return -1;
            if (imageVO2 == null || imageVO2.getDateTaken() == null)
              return 1;
            return imageVO1.getDateTaken().compareTo(imageVO2.getDateTaken());
          });

        logNormal("Writing images to destination directory...");
        fileWriterService.reset();
        progressBar.progressProperty().bind(fileWriterService.progressProperty());
        fileWriterService.setOnSucceeded((event) -> 
        {
          startBtn.setDisable(false);
          logNormal("All Done.");
          logNormal("------------------------------------------------");
        });
        fileWriterService.writeImageFilesToDestDirectory(imageList, 
            destDir.getText(),
            useDaySubFolders.isSelected(),
            Integer.parseInt(maxFilesInDir.getText()),
            resizeForWeb.isSelected());
        
      }
      catch (Exception e)
      {
        displayModal("Error. Check output for details");
        logError(e.getMessage());
      }
    }
    else
    {
      displayModal("Error. Check output for details");
    }
  }
  
  @FXML
  public void handleStopBtn(ActionEvent inEvent)
  {
    if (fileWriterService.isRunning())
    {
      boolean cancelSuccess = fileWriterService.cancel();
      if (cancelSuccess)
        logNormal("Cancel successful.");
      else
        logError("Cancel failed.");
    }
    else
      logError("No active task to cancel.");
  }
  
  @FXML 
  public void handleClearOutput(ActionEvent inEvent)
  {
    outputTxt.clear();
  }

  @FXML
  public void handleResetMenuItem(ActionEvent inEvent)
  {
    srcDirList.setItems(FXCollections.observableArrayList());
    removeSrcDirBtn.setDisable(true);
    destDir.setText(null);
    recursive.setSelected(false);
    progressBar.setProgress(0);
    startBtn.setDisable(false);
    logNormal("All fields reset.");
    
  }
  
  @FXML
  public void handleQuitMenuItem(ActionEvent inEvent)
  {
    System.exit(0);
  }

  @FXML
  public void handleAboutMenuItem(ActionEvent inEvent)
  {
    AnchorPane about;
    try
    {
      about = (AnchorPane)FXMLLoader.load(getClass().getResource("About.fxml"));
      Scene aboutScene = new Scene(about,390,235);
      aboutScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      Stage aboutStage = new Stage();
      aboutStage.setTitle("About");
      aboutStage.setScene(aboutScene);
      aboutStage.initOwner(getWindow());
      aboutStage.show();
    }
    catch (Exception e)
    {
      logError("Failed to load the About window information");
    }
    
  }
  

  protected ImageFileReaderService getFileReaderService()
  {
    return new ImageFileReaderService();
  }

  protected ImageFileWriterService getFileWriterService()
  {
    return new ImageFileWriterService();
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
      return false;
    }
    if (StringUtil.isEmpty(destDir.getText()))
    {
      logError("You must specify the destination directory");
      return false;
    }
    return true;
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

  public CheckBox getRecursive()
  {
    return recursive;
  }

  public void setRecursive(CheckBox inRecursive)
  {
    recursive = inRecursive;
  }

  public ProgressBar getProgressBar()
  {
    return progressBar;
  }

  public void setProgressBar(ProgressBar inProgressBar)
  {
    progressBar = inProgressBar;
  }

  public CheckBox getIncludeVideos()
  {
    return includeVideos;
  }

  public void setIncludeVideos(CheckBox inIncludeVideos)
  {
    includeVideos = inIncludeVideos;
  }

  
	
	

}
