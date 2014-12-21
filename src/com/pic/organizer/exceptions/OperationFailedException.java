/**
 * 
 */
package com.pic.organizer.exceptions;

/**
 * @author mkorotkovas
 *
 */
public class OperationFailedException extends Exception
{
  private static final long serialVersionUID = 1L;
  
  public OperationFailedException()
  {
    super();
    
  }
  public OperationFailedException(String inMsg)
  {
    super(inMsg);
    
  }
  
}
