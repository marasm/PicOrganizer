/**
 * 
 */
package com.pic.organizer.valueobjects;

import java.util.Date;

import com.pic.organizer.types.MediaType;

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
  private MediaType mediaType = MediaType.UNKNOWN;
  
  public ImageInfoVO()
  {
    
  }

  public ImageInfoVO(String inOrigName,  
                     String inSourcePath,
                     Date inDateTaken, 
                     String inCameraMake,
                     MediaType inMediaType)
  {
    name = inOrigName;  
    sourcePath = inSourcePath;
    dateTaken = inDateTaken; 
    cameraMake = inCameraMake;
    mediaType = inMediaType;
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

  public MediaType getMediaType()
  {
    return mediaType;
  }

  public void setMediaType(MediaType inMediaType)
  {
    mediaType = inMediaType;
  }
}
