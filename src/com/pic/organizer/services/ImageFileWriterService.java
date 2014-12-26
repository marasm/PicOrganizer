/**
 * 
 */
package com.pic.organizer.services;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;

import com.marasm.exceptions.OperationFailedException;
import com.marasm.util.FileUtil;
import com.marasm.util.StringUtil;
import com.pic.organizer.valueobjects.ImageInfoVO;

/**
 * @author mkorotkovas
 *
 */
public class ImageFileWriterService extends Service<Integer>
{
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
  
  private List<ImageInfoVO> imageList;
  private int maxFilesPerDir;
  private String destDirectory;
  
  
  public void writeImageFilesToDestDirectory(List<ImageInfoVO> inImageList, 
      String inDestDirectory,
      int inMaxFilesPerDir)
      throws OperationFailedException
  {
    imageList = inImageList;
    maxFilesPerDir = inMaxFilesPerDir;
    destDirectory = inDestDirectory;
    
    if (!inImageList.isEmpty())
    {
      try
      {
        this.start();
        
      }
      catch (Exception e)
      {
        throw new OperationFailedException(e.getMessage());
      }
    }
  }
  
  @Override
  protected Task<Integer> createTask()
  {
    return new FileWriterTask();
  }

  private class FileWriterTask extends Task<Integer>
  {
    
    @Override
    public Integer call() throws IOException
    {
      
      int totalCount = 0;
      int folderCount = 0;
      int sameDayFolderCount = 1;
      int sameDayFileCount = 1;
      String curFolderName = null;
      String curDestFilePath = null;
      for (ImageInfoVO imageVO : imageList)
      {
        if(isCancelled())
          break;
        
        if (folderCount >= maxFilesPerDir)
        {
          folderCount = 0;
          if (curFolderName.startsWith(sdf.format(imageVO.getDateTaken())))
          {
            sameDayFolderCount++;
          }
        }
        if (StringUtil.isEmpty(curFolderName) || 
            !curFolderName.startsWith(sdf.format(imageVO.getDateTaken())))
        {
          folderCount = 0;
          sameDayFolderCount = 1;
          sameDayFileCount = 1;
        }
        
        curFolderName = sdf.format(imageVO.getDateTaken()) + "_" +
              StringUtil.leftZeroPadNumber(sameDayFolderCount, 3);
        
        curDestFilePath = destDirectory + "/" + curFolderName + "/" +
            sdf.format(imageVO.getDateTaken()) + "_" + 
            StringUtil.leftZeroPadNumber(sameDayFileCount, 6) + 
            FileUtil.getExtentionFromFileName(imageVO.getName());
        
         FileUtils.copyFile(
           new File(imageVO.getSourcePath() + "/" + imageVO.getName()), 
           new File(curDestFilePath));        
          
        updateProgress(totalCount, imageList.size());
        
        sameDayFileCount++;
        folderCount++;
        totalCount++;
      }
      updateProgress(1,1);
      return totalCount;
    }
  }
}
