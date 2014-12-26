/**
 * 
 */
package com.pic.organizer.services;

import java.text.SimpleDateFormat;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

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
  
  
  public void writeImageFilesToDestDirectory(List<ImageInfoVO> inImageList, 
      int inMaxFilesPerDir)
  {
    imageList = inImageList;
    maxFilesPerDir = inMaxFilesPerDir;
    if (!inImageList.isEmpty())
    {
      this.start();
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
    public Integer call()
    {
      System.out.println("run called");
      
      int totalCount = 0;
      int folderCount = 0;
      int sameDayFolderCount = 1;
      String curFolderName = null;
      for (ImageInfoVO imageVO : imageList)
      {
        if(isCancelled())
          break;
        
        if (folderCount > maxFilesPerDir)
        {
          
        }
        if (StringUtil.isEmpty(curFolderName) || 
            !curFolderName.startsWith(sdf.format(imageVO.getDateTaken())))
        {
          curFolderName = sdf.format(imageVO.getDateTaken()) +
              StringUtil.leftZeroPadNumber(sameDayFolderCount, 3);
          folderCount = 0;
        }
        
          
        updateProgress(totalCount, imageList.size());
//        FileUtils.copyFile(srcFile, destFile);
        try
        {
          Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
        }
        
        folderCount++;
        totalCount++;
      }
      updateProgress(1,1);
      return totalCount;
    }
  }
}
