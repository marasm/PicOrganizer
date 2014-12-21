/**
 * 
 */
package com.pic.organizer.valueobjects;

import java.util.Date;

/**
 * @author mkorotkovas
 *
 */
public class ImageInfoVO
{
  private String origName;
  private String newName;
  private String sourcePath;
  private String destPath;
  private Date   dateTaken;
  private String cameraMake;
  
  public ImageInfoVO()
  {
    
  }

  public ImageInfoVO(String inOrigName,  
                     String inNewName,   
                     String inSourcePath,
                     String inDestPath,
                     Date inDateTaken, 
                     String inCameraMake)
  {
    origName = inOrigName;  
    newName = inNewName;   
    sourcePath = inSourcePath;
    destPath = inDestPath;  
    dateTaken = inDateTaken; 
    cameraMake = inCameraMake;
  }
  
  
  public String getOrigName()
  {
    return origName;
  }
  public void setOrigName(String inOrigName)
  {
    origName = inOrigName;
  }
  public String getNewName()
  {
    return newName;
  }
  public void setNewName(String inNewName)
  {
    newName = inNewName;
  }
  public String getSourcePath()
  {
    return sourcePath;
  }
  public void setSourcePath(String inSourcePath)
  {
    sourcePath = inSourcePath;
  }
  public String getDestPath()
  {
    return destPath;
  }
  public void setDestPath(String inDestPath)
  {
    destPath = inDestPath;
  }
  public Date getDateTaken()
  {
    return dateTaken;
  }
  public void setDateTaken(Date inDateTaken)
  {
    dateTaken = inDateTaken;
  }
  public String getCameraMake()
  {
    return cameraMake;
  }
  public void setCameraMake(String inCameraMake)
  {
    cameraMake = inCameraMake;
  }
}
