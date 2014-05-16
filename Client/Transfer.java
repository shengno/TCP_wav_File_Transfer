/**
 * 
 */
package edu.pkusz.sheng;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

/**
 * @author sheng
 *
 */
public class Transfer
{
	private static Socket ClientSocket = null;
	private static BufferedOutputStream SocketOutputStream = null;
	private static BufferedInputStream  SocketInputStream  = null;
	
	
	private static void CreateSocket()
	{
		try 
		{
			ClientSocket = new Socket(CONSTANT.IP, CONSTANT.PORT);
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private static void CreateSocketOutputStream()
	{
		if (ClientSocket == null)
		{
			CreateSocket();
		}
		
		try {
			SocketOutputStream = new BufferedOutputStream(ClientSocket.getOutputStream());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static void CreateSocketInputStream()
	{
		if (ClientSocket == null)
		{
			CreateSocket();
		}
		
		try {
			SocketInputStream = new BufferedInputStream(ClientSocket.getInputStream());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static boolean SendPackage(DataPackage PackageOfData)
	{
		if (SocketOutputStream == null)
		{
			CreateSocketOutputStream();
		}
		
		return PackageOfData.Send(SocketOutputStream);
	}
	
	
	public static boolean SendPackage(BufferedInputStream InputFile, PackageType Type, int LenOfData) throws IOException
	{
		final int LENOFCONTENT = BufferSize.BUFFERSIZE - BufferSize.PACKAGEHEADERSIZE;
		
		// The number of packages.
		int NumberOfPackages = LenOfData / LENOFCONTENT;
		if ((LenOfData % LENOFCONTENT) != 0)
		{
			NumberOfPackages++;
		}
		
		DataPackage PackageOfData = new DataPackage(false);
		
		// set the type and the number of packages.
		PackageOfData.SetType(Type);
		PackageOfData.SetNumberOfPackages(NumberOfPackages);
		
		boolean result = false;
		
		byte[] TmpBuffer = new byte[LENOFCONTENT];
		int Len = 0;
		int IndexOfPackages = 1;
		while ((Len = InputFile.read(TmpBuffer)) != -1)
		{
			PackageOfData.SetIndexOfPackages(IndexOfPackages);
			PackageOfData.SetSizeOfData(Len);
			
			// set content
			PackageOfData.SetContent(TmpBuffer);
			
			// send data
			result = SendPackage(PackageOfData);
			IndexOfPackages++;		
			
		}
		return result;
	}
	
	
	
	
	
	public static boolean SendString(PackageType Type, String InputString)
	{	
		if (InputString.length() > BufferSize.BUFFERSIZE)
		{
			return false;
		}
		
		
		DataPackage PackageOfData = new DataPackage(false);
		
		// set the package header
		PackageOfData.SetType(Type);
		PackageOfData.SetNumberOfPackages(1);
		PackageOfData.SetIndexOfPackages(1);
		PackageOfData.SetSizeOfData(InputString.length());
		
		// set the content
		PackageOfData.SetContent(InputString.getBytes());
		
		return SendPackage(PackageOfData);
		
	}
	
	
	
	public static boolean SendUserName(String UserName)
	{
		PackageType Type = PackageType.USERNAME;
		
		return SendString(Type, UserName);
	}
	
	
	public static boolean SendAuthorisztionRequest()
	{
		PackageType Type = PackageType.REQUESTAUTHORIZATION;
		String Request = new String("RequestAuthorization");
		return SendString(Type, Request);
	}
	
	
	
	public static boolean SendRequest()
	{
		PackageType Type = PackageType.REQUESTRESULT;
		String Request = new String("Request");
		
		return SendString(Type, Request);	
	}
	
	public static boolean SendTextRequest()
	{
		PackageType Type = PackageType.REQUESTTEXT;
		String Request = new String("TextRequest");
		return SendString(Type, Request);
	}
	
	
	public static boolean SendClosedRequest()
	{
		PackageType Type = PackageType.CLOSED;
		String Request = new String("Closed");
		
		boolean result = SendString(Type, Request);
		try 
		{
			result = Close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public static boolean SendFile(String FileName) throws IOException
	{
		
		File RawFile = null;
		BufferedInputStream InputFile = null;
		
		try 
		{
			RawFile = new File(FileName);
			InputFile = new BufferedInputStream(new FileInputStream(RawFile));
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int Len = (int) RawFile.length();
		PackageType Type = PackageType.FILEDATA;
		
		boolean result = false;
		result = SendPackage(InputFile, Type, Len);
		
		InputFile.close();
		
		return result;
		
	}
	
	
	public static boolean Close() throws IOException
	{
		if (SocketInputStream != null)
		{
			SocketInputStream.close();
		}
		
		if (SocketOutputStream != null)
		{
			SocketOutputStream.close();
		}
		
		if (ClientSocket != null)
		{
			ClientSocket.close();
		}
		
		return true;
	}
	
	
	public static String ReceiveString(PackageType Type)
	{
		DataPackage Data = new DataPackage(true);
		
		boolean run = true;
		
		if (SocketInputStream == null)
		{
			CreateSocketInputStream();
		}
		
		while (run)
		{
			int Len = 0;
			if ((Len = Data.Receive(SocketInputStream)) != -1)
			{
				if (Data.GetType() == Type)
				{
					run = false;
				}
			}
		}
		
		return new String(Data.Content, 0, Data.SizeOfData);
	}
	
	
	public static String ReceiveText()
	{
		PackageType Type = PackageType.REPLAYTEXT;
		return ReceiveString(Type);
	}
	
	
	public static String ReceiveUserName()
	{
		PackageType Type = PackageType.USERNAME;
		return ReceiveString(Type);
	}
	
	
	public static String ReceiveAuthorizen()
	{
		PackageType Type = PackageType.REPLAYAUTHORIZATION;
		return ReceiveString(Type);
	}
	
	
	public static String ReceiveResult()
	{
		PackageType Type = PackageType.REPLAYRESULT;
		return ReceiveString(Type);
	}
	
}
