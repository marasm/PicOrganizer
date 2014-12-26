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
  private String destDirectory;
  
  
  public void writeImageFilesToDestDirectory(List<ImageInfoVO> inImageList, 
      String inDestDirectory,
      int inMaxFilesPerDir)
  {
    imageList = inImageList;
    maxFilesPerDir = inMaxFilesPerDir;
    destDirectory = inDestDirectory;
    
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
      String curDestFilePath = null;
      for (ImageInfoVO imageVO : imageList)
      {
        if(isCancelled())
          break;
        
        if (folderCount > maxFilesPerDir)
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
        }
        
        curFolderName = sdf.format(imageVO.getDateTaken()) +
              StringUtil.leftZeroPadNumber(sameDayFolderCount, 3);
        
        curDestFilePath = destDirectory + "/" + curFolderName + "/" +
            sfd.format(imageVO.getDateTaken()) + "_" + StringUtil.leftZeroPadNumber(totalCount, 6) + 
            StringUtil.getExtentionFromFileName(imageVO.getName());
        
        // FileUtils.copyFile(
        //   new File(imageVO.getSourcePath() + "/" + imageVO.getName()), 
        //   new File(curDestFilePath);        
          
        System.out.println("Dest file: " + curDestFilePath);
          
        try
        {
          Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
        }
        
        updateProgress(totalCount, imageList.size());
        
        folderCount++;
        totalCount++;
      }
      updateProgress(1,1);
      return totalCount;
    }
  }
}
