/**
 * 
 */
package com.pic.organizer.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import com.marasm.exceptions.OperationFailedException;
import com.marasm.util.FileUtil;
import com.marasm.util.StringUtil;
import com.pic.organizer.valueobjects.ImageInfoVO;

/**
 * @author mkorotkovas
 *
 */
public class ImageFileReaderService
{
  
  
  
  public List<ImageInfoVO> getImagesFromDirectories(
      List<String> inSourceDirList, 
      boolean inRecursive, boolean inIncludeVideos)
  throws OperationFailedException
  {
    List<ImageInfoVO> res = new ArrayList<ImageInfoVO>();
    
    for (String curSrcDir : inSourceDirList)
    {
      res.addAll(getImagesFromDirectory(curSrcDir, 
          inRecursive, inIncludeVideos));
    }
    
    return res;
  }
  
  public List<ImageInfoVO> getImagesFromDirectory(String inSrcDir, 
      boolean inRecursive, boolean inIncludeVideos)
  throws OperationFailedException
  {
    List<ImageInfoVO> res = new ArrayList<ImageInfoVO>();
    
    File srcDirF = new File(inSrcDir);
    if (srcDirF.isDirectory())
    {
      File[] fileArray = srcDirF.listFiles((file) -> 
      {
        return FileUtil.isAnImageFile(file) ||
              (FileUtil.isAVideoFile(file) && inIncludeVideos) ||
                file.isDirectory();
      });
      for (File file : fileArray)
      {
        try
        {
          if(FileUtil.isAnImageFile(file))
          {
            Metadata mt = ImageMetadataReader.readMetadata(file);
            Directory info = mt.getDirectory(ExifDirectory.class); 
            
            ImageInfoVO imageFileVO = new ImageInfoVO(file.getName(), 
                inSrcDir, 
                info.getDate(ExifDirectory.TAG_DATETIME), 
                info.getString(ExifDirectory.TAG_MAKE));
            
            res.add(imageFileVO);
          }
          else 
          if (FileUtil.isAVideoFile(file))
          {
            res.add(new ImageInfoVO(file.getName(), 
                inSrcDir, 
                new Date(file.lastModified()), 
                "UNKNOWN"));
          }
          else
          if(file.isDirectory() && inRecursive)
          {
            res.addAll(
                getImagesFromDirectory(file.getAbsolutePath(), 
                    inRecursive, inIncludeVideos));
          }
          
        }
        catch (Exception e)
        {
          throw new OperationFailedException("Error processing files: " + 
              e.getMessage());
        }
      }
      
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
