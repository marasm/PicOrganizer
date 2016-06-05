/**
 * 
 */
package com.pic.organizer.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;

import com.marasm.exceptions.OperationFailedException;
import com.marasm.util.FileUtil;
import com.marasm.util.StringUtil;
import com.pic.organizer.types.MediaType;
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
  private boolean resizeForWeb;
  private boolean useDaySubfolders;
  
  
  public void writeImageFilesToDestDirectory(List<ImageInfoVO> inImageList, 
      String inDestDirectory,
      boolean inUseDaySubfolders,
      int inMaxFilesPerDir,
      boolean inResizeForWeb)
      throws OperationFailedException
  {
    imageList = inImageList;
    maxFilesPerDir = inMaxFilesPerDir;
    destDirectory = inDestDirectory;
    resizeForWeb = inResizeForWeb;
    useDaySubfolders = inUseDaySubfolders;
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
      String curSubFolderName = null;
      String curDestFolder = null;
      String curDestFilePath = null;
      for (ImageInfoVO imageVO : imageList)
      {
        if(isCancelled())
          break;
        
        if (folderCount >= maxFilesPerDir)
        {
          folderCount = 0;
          if (curSubFolderName.startsWith(sdf.format(imageVO.getDateTaken())))
          {
            sameDayFolderCount++;
          }
        }
        if (StringUtil.isEmpty(curSubFolderName) || 
            !curSubFolderName.startsWith(sdf.format(imageVO.getDateTaken())))
        {
          folderCount = 0;
          sameDayFolderCount = 1;
          sameDayFileCount = 1;
        }
        
        curSubFolderName = sdf.format(imageVO.getDateTaken()) + "_" +
            StringUtil.leftZeroPadNumber(sameDayFolderCount, 3);
        
        curDestFolder = destDirectory + "/" + 
            (useDaySubfolders ? curSubFolderName : ""); //ONLY use subfolders if requested
        
        curDestFilePath = curDestFolder + "/" +
            sdf.format(imageVO.getDateTaken()) + "_" + 
            StringUtil.leftZeroPadNumber(sameDayFileCount, 6) + 
            FileUtil.getExtentionFromFileName(imageVO.getName());
        
        updateMessage("Processing: " + imageVO.getSourcePath() + "/" + imageVO.getName());
        if (resizeForWeb && imageVO.getMediaType() == MediaType.IMAGE)
        {
          BufferedImage result = Scalr.resize(
              ImageIO.read(new File(imageVO.getSourcePath() + "/" + imageVO.getName())), 
              2600);
          FileUtils.forceMkdir(new File(curDestFolder));
          File outFile = new File(curDestFilePath);
          ImageIO.write(result, 
              FileUtil.getExtentionFromFileName(imageVO.getName()).substring(1), 
              outFile);
        }
        else
        {
          FileUtils.copyFile(
              new File(imageVO.getSourcePath() + "/" + imageVO.getName()), 
              new File(curDestFilePath));
        }
         
        
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
