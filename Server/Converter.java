package edu.pkusz.sheng;
/**
 * 
 */


/**
 * @author sheng
 * @version 1.0
 * @date 2014-05-16
 *
 */
public class Converter 
{
	/**
	 * 
	 * description: This funciton is used to Convert the integer to a byte array.
	 * 
	 * @param Input   The input int used to be converted to byte array
	 * @return The byte array 
	 * 
	 * @author sheng
	 * @version 1.0
	 * @date    2014-05-16
	 * 
	 * @history  
	 *     <author>       <date>        <description>
	 *      sheng       2014-05-16        build the moudle
	 * 
	 */
	public static byte[] ConvertIntToByteArray(int Input)
	{
		byte[] result = new byte[4];
		
		for (int Index = 0; Index < 4; Index++)
		{
			result[Index] = (byte)(Input % 32);
			Input = Input / 32;
		}
		
		return result;
	}
	
	
	
	/**
	 * 
	 * description: This funciton is used to Convert the byte array to a byet integer.
	 * 
	 * @param Input   The byte array used to be converted
	 * @return The int 
	 * 
	 * @author sheng
	 * @version 1.0
	 * @date    2014-05-16
	 * 
	 * @history  
	 *     <author>       <date>        <description>
	 *      sheng       2014-05-16        build the moudle
	 */
	public static int ConvertByteArrayToInt(byte[] Input)
	{
		int result = 0;
		
		for (int Index = 0; Index < 4; Index++)
		{
			result = result * 32 + Input[3 - Index];
		}
		
		return result;
	}
	

}
