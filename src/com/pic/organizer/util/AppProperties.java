/**
 * 
 */
package com.pic.organizer.util;

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
      try
      {
        props.load(Object.class.getResourceAsStream(
                "/app.properties"));
      }
      catch (Exception e)
      {
        System.out.println("ERROR: unable to read app.properties file!");
      }
    }
    return props.getProperty(inPropName, "");
  }
  
  
}
