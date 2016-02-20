/**
 * 
 */
package com.pic.organizer.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * @author mkorotkovas
 *
 */
public class AboutController implements Initializable
{
  
  @FXML
  private TextArea aboutTxt;

  @Override
  public void initialize(URL inArg0, ResourceBundle inArg1)
  {
    aboutTxt.setText("Hello there!");
  }
  
  @FXML
  public void handleAboutClose(ActionEvent inEvent)
  {
    Stage aboutStage = (Stage)aboutTxt.getParent().getScene().getWindow();
    aboutStage.close();
  }

  public TextArea getAboutTxt()
  {
    return aboutTxt;
  }

  public void setAboutTxt(TextArea inAboutTxt)
  {
    aboutTxt = inAboutTxt;
  }

}
