/**
 * 
 */
package com.pic.organizer.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.marasm.exceptions.OperationFailedException;
import com.marasm.util.StringUtil;
import com.pic.organizer.valueobjects.ImageInfoVO;

/**
 * @author mkorotkovas
 *
 */
public class ImageFileService
{
  public List<ImageInfoVO> getImagesFromDirectories(
      List<String> inSourceDirList)
  throws OperationFailedException
  {
    List<ImageInfoVO> res = new ArrayList<ImageInfoVO>();
    
    for (String curSrcDir : inSourceDirList)
    {
      res.addAll(getImagesFromDirectory(curSrcDir));
    }
    
    return res;
  }
  
  public List<ImageInfoVO> getImagesFromDirectory(String inSrcDir)
  throws OperationFailedException
  {
    List<ImageInfoVO> res = new ArrayList<ImageInfoVO>();
    
    File srcDirF = new File(inSrcDir);
    if (srcDirF.isDirectory())
    {
      //TODO
      
    }
    else
    {
      throw new OperationFailedException(
          "Source directory " + inSrcDir + " is not valid");
    }
    
    return res;
  }
  
  public static String getExtentionFromFileName(String inFileName)
  {
    if (StringUtil.isEmpty(inFileName))
    {
      return inFileName;
    }
    else
    {
      if (inFileName.lastIndexOf(".") <= 0)
      {
        return inFileName;
      }
      return inFileName.substring(inFileName.lastIndexOf("."), 
          inFileName.length());
    }
  }
}
