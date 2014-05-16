/**
 * 
 */
package edu.pkusz.sheng;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author sheng
 *
 */
public class Transfer
{
	private  Socket ClientSocket = null;
	private  BufferedOutputStream SocketOutputStream = null;
	private  BufferedInputStream  SocketInputStream  = null;
	
	
	public Transfer(Socket ClientSocket)
	{
		this.ClientSocket = ClientSocket;
	}
	
	
	private  void CreateSocketOutputStream()
	{
		try {
			SocketOutputStream = new BufferedOutputStream(ClientSocket.getOutputStream());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private  void CreateSocketInputStream()
	{
		
		try {
			SocketInputStream = new BufferedInputStream(ClientSocket.getInputStream());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public  boolean SendPackage(DataPackage PackageOfData)
	{
		if (SocketOutputStream == null)
		{
			CreateSocketOutputStream();
		}
		
		return PackageOfData.Send(SocketOutputStream);
	}
	
	
	public boolean SendPackage(BufferedInputStream InputFile, PackageType Type, int LenOfData) throws IOException
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
	
	
	
	
	
	public boolean SendString(PackageType Type, String InputString)
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
	
	
	
	public boolean SendUserName(String UserName)
	{
		PackageType Type = PackageType.USERNAME;
		
		return SendString(Type, UserName);
	}
	
	
	
	public boolean SendAuthorization(String Authorizen)
	{
		PackageType Type = PackageType.REPLAYAUTHORIZATION;
		
		return SendString(Type, Authorizen);
	}
	
	
	
	public boolean SendResult(String Result)
	{
		PackageType Type = PackageType.REPLAYRESULT;
		
		return SendString(Type, Result);	
	}
	
	
	public boolean SendText(String Text)
	{
		PackageType Type = PackageType.REPLAYTEXT;
		return SendString(Type, Text);
	}
	
	
	private boolean SendClosedRequest()
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
	
	
	
	public boolean SendFile(String FileName) throws IOException
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
	
	
	public boolean Close() throws IOException
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
	
	
	public String ReceiveString(PackageType Type)
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
	
	
	public String ReceiveText()
	{
		PackageType Type = PackageType.REPLAYTEXT;
		return ReceiveString(Type);
	}
	
	
	public String ReceiveUserName()
	{
		PackageType Type = PackageType.USERNAME;
		return ReceiveString(Type);
	}
	
	
	public String ReceiveAuthorizen()
	{
		PackageType Type = PackageType.REPLAYAUTHORIZATION;
		return ReceiveString(Type);
	}
	
	
	public String ReceiveResult()
	{
		PackageType Type = PackageType.REPLAYRESULT;
		return ReceiveString(Type);
	}
	
	
	private String OutputFileName = null;
	private BufferedOutputStream OutputFileStream = null;
	private int ReceiveIndex = 0;
	
	public void Recieving()
	{
		boolean IsRun = true;
		
		DataPackage Data = new DataPackage(true);
		int i = CONSTANT.CLOSED;
		
		while(IsRun)
		{
			if (SocketInputStream == null)
			{
				CreateSocketInputStream();
			}
			
			if (Data.Receive(SocketInputStream) != -1)
			{
				switch (Data.GetType().ordinal())
				{
					case CONSTANT.USERNAME:
						OutputFileName = new String(Data.GetContent(), 0, Data.SizeOfData);
						System.out.println("The reciveing name is " + OutputFileName);
						break;
						
					case CONSTANT.FILEDATA:
						if (ReceiveFile(Data) == false)
						{
							System.out.println("Error in receivint file.");
						}
						break;				
						
						
					case CONSTANT.REQUESTAUTHORIZATION:
						SendAuthorization("Test");
						break;
						
						
					case CONSTANT.REQUESTTEXT:
						SendText("Text is send.");
						break;
						
						
					case CONSTANT.REQUESTRESULT:
						SendResult("Result is null.");
						break;
						
						
					case CONSTANT.CLOSED:
						IsRun = false;
						break;
						
					default:
							break;
				}
			}
		}
	}
	
	
	
	private boolean ReceiveFile(DataPackage Data)
	{
		if (this.OutputFileName == null)
		{
			return false;
		}
		
		if (this.OutputFileStream == null)
		{
			try 
			{
				OutputFileStream = new BufferedOutputStream(new FileOutputStream(CONSTANT.Path + OutputFileName + CONSTANT.FileType));
			}
			catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		
		if ((this.ReceiveIndex + 1) != Data.IndexOfPackages)
		{
			return false;
		}
		
		boolean result = false;
		result = Data.SaveContent(OutputFileStream);
		
		this.ReceiveIndex++;
		
		if (Data.IndexOfPackages == Data.NumberOfPackages)
		{
			try 
			{
				this.OutputFileStream.close();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.OutputFileStream = null;
			//this.OutputFileName = null;
		}
			
		return result;
	}
	
}
