package edu.pkusz.sheng;
/**
 * 
 */


/**
 * @author sheng
 *
 */
public class Converter 
{
	/**
	 * 
	 * @param Input The input int used to be converted to byte array
	 * @return The byte array 
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
	 * @param Input
	 * @return
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
