/**
 * 
 */
package com.pic.organizer.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author mkorotkovas
 *
 */
public class AppProperties
{
  private static Properties props;
  
  
  public static String getProperty(String inPropName)
  {
    if (props == null && inPropName != null)
    {
      props = new Properties();
      try(InputStream inputStream = ClassLoader.getSystemResourceAsStream("app.properties"))
      {
        props.load(inputStream);
      }
      catch (Exception e)
      {
        System.out.println("ERROR: unable to read app.properties file: " + e.getMessage());
        e.printStackTrace();
      }
    }
    return props.getProperty(inPropName, "");
  }
  
  
}
