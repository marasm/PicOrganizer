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
  private String name;
  private String sourcePath;
  private Date   dateTaken;
  private String cameraMake;
  
  public ImageInfoVO()
  {
    
  }

  public ImageInfoVO(String inOrigName,  
                     String inSourcePath,
                     Date inDateTaken, 
                     String inCameraMake)
  {
    name = inOrigName;  
    sourcePath = inSourcePath;
    dateTaken = inDateTaken; 
    cameraMake = inCameraMake;
  }
  
  
  public String getSourcePath()
  {
    return sourcePath;
  }
  public void setSourcePath(String inSourcePath)
  {
    sourcePath = inSourcePath;
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
  
  @Override
  public String toString()
  {
    return "["
        + "name: " + name  
        + " sourcePath: " + sourcePath
        + " dateTaken: " + dateTaken
        + " cameraMake: " + cameraMake
        + "]";
  }

  public String getName()
  {
    return name;
  }

  public void setName(String inName)
  {
    name = inName;
  }
}
