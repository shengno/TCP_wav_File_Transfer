package edu.pkusz.sheng;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;



/**
 * 
 */

/**
 * @author sheng
 *
 */
public class DataPackage
{
	//
	protected boolean IsReceiver = false;
	
	// The file header
	protected PackageType Type = null;
	protected int NumberOfPackages = 0;
	protected int IndexOfPackages  = 0;
	protected int SizeOfData       = 0;
	
	// The content of the data
	protected byte[] Content = null;
	
	
	public DataPackage(boolean IsReceiver)
	{
		this.IsReceiver = IsReceiver;
	}
	
	
	public PackageType GetType()
	{
		if (IsReceiver == true)
		{
			return this.Type;
		}
		
		return PackageType.NULLTYPE;
	}
	
	
	public int GetNumberOfPackages()
	{
		if (IsReceiver == true)
		{
			return this.NumberOfPackages;
		}
		
		return 0;
	}
	
	
	public int GetIndexOfPackages()
	{
		if (IsReceiver == true)
		{
			return this.IndexOfPackages;
		}
		
		return 0;
	}
	
	
	public int GetSizeOfData()
	{
		if (IsReceiver == true)
		{
			return this.SizeOfData;
		}
		
		return 0;
	}
	
	
	public byte[] GetContent()
	{
		if (IsReceiver == true)
		{
			return this.Content;
		}
		
		return null;
	}
	
	
	
	public boolean SetType(PackageType Type)
	{
		if (IsReceiver == false)
		{
			this.Type = Type;
			return true;
		}
		
		return false;
	}
	
	
	public boolean SetNumberOfPackages(int NumberOfPackages)
	{
		if (IsReceiver == false)
		{
			this.NumberOfPackages = NumberOfPackages;
			return true;
		}
		
		return false;
	}
	
	
	public boolean SetIndexOfPackages(int IndexOfPackages)
	{
		if (IsReceiver == false)
		{
			this.IndexOfPackages = IndexOfPackages;
			return true;
		}
		
		return false;
	}
	
	
	public boolean SetSizeOfData(int SizeOfData)
	{
		if (IsReceiver == false)
		{
			this.SizeOfData = SizeOfData;
			return true;
		}
		
		return false;
	}
	
	
	
	
	public boolean SetContent(byte[] Content)
	{
		if (IsReceiver == false)
		{
			this.Content = Content;
			return true;
		}
		
		return false;
	}
	
	//
	protected  byte[] Packing()
	{
		byte[] packageData = new byte[BufferSize.PACKAGEHEADERSIZE + SizeOfData];
		
		byte [] Tmp;
		int Offset = 0;
		
		// The file type
		Tmp = Converter.ConvertIntToByteArray(Type.ordinal());
		System.arraycopy(Tmp, 0, packageData, Offset, BufferSize.INTSIZE);
		Offset += BufferSize.INTSIZE;
		
		// The NumberOfPackages
		Tmp = Converter.ConvertIntToByteArray(NumberOfPackages);
		System.arraycopy(Tmp, 0, packageData, Offset, BufferSize.INTSIZE);
		Offset += BufferSize.INTSIZE;
		
		// The IndexOfPackages
		Tmp = Converter.ConvertIntToByteArray(IndexOfPackages);
		System.arraycopy(Tmp, 0, packageData, Offset, BufferSize.INTSIZE);
		Offset += BufferSize.INTSIZE;
		
		
		// The DataSize
		Tmp = Converter.ConvertIntToByteArray(SizeOfData);
		System.arraycopy(Tmp, 0, packageData, Offset, BufferSize.INTSIZE);
		Offset += BufferSize.INTSIZE;
		
		
		// Content
		System.arraycopy(Content, 0, packageData, Offset, SizeOfData);
		
		
		return packageData;
	}
	
	
	
	protected boolean Unpacking(byte[] Data, int Lenth)
	{
		byte [] Tmp = new byte[BufferSize.INTSIZE];
		int Offset = 0;
		
		// Package Type
		System.arraycopy(Data, Offset, Tmp, 0, BufferSize.INTSIZE);
		this.Type = PackageType.values()[Converter.ConvertByteArrayToInt(Tmp)];
		Offset += BufferSize.INTSIZE;
		
		// The NumberOfPackages
		System.arraycopy(Data, Offset, Tmp, 0, BufferSize.INTSIZE);
		this.NumberOfPackages = Converter.ConvertByteArrayToInt(Tmp);
		Offset += BufferSize.INTSIZE;
		
		// The Index of Packages
		System.arraycopy(Data, Offset, Tmp, 0, BufferSize.INTSIZE);
		this.IndexOfPackages = Converter.ConvertByteArrayToInt(Tmp);
		Offset += BufferSize.INTSIZE;
		
		// The SizeOfData
		System.arraycopy(Data, Offset, Tmp, 0, BufferSize.INTSIZE);
		this.SizeOfData = Converter.ConvertByteArrayToInt(Tmp);
		Offset += BufferSize.INTSIZE;
			
		
//		System.out.println("The number of package is " + this.NumberOfPackages);
//		System.out.println("The index of package is " + this.IndexOfPackages);
//		System.out.println("The size of data is " + this.SizeOfData);
		
		// The content
		this.Content = new byte[this.SizeOfData];
		System.arraycopy(Data, Offset, this.Content, 0, SizeOfData);
		
		return true;
	}
	
	
	
	public boolean Send(BufferedOutputStream OutputStream) 
	{
		// TODO Auto-generated method stub
		
		if (IsReceiver == false)
		{
			int PackageSize = BufferSize.PACKAGEHEADERSIZE + SizeOfData;
			byte [] TmpData = Packing();
			
			byte [] SendData = null;
			
			// 当数据包的大小小于缓冲区的大小时，填充数据包
			if (BufferSize.BUFFERSIZE > PackageSize)
			{
				SendData = new byte[BufferSize.BUFFERSIZE];
				System.arraycopy(TmpData, 0, SendData, 0, PackageSize);
			}
			else  //当数据包的大小等于缓冲区的时候，不做其他处理
			{
				SendData = TmpData;
			}
			
			try {
				OutputStream.write(SendData, 0, BufferSize.BUFFERSIZE);
				OutputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
	public int Receive(BufferedInputStream InputStream) 
	{
		// TODO Auto-generated method stub
		
		if (IsReceiver == true)
		{
			byte[] Data = new byte[BufferSize.BUFFERSIZE];
			int Len = 0;
			
			try 
			{
				Len = InputStream.read(Data);
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			System.out.println("The len of data is " + Len);
			if (Unpacking(Data, Len))
			{
				return Len;
			}
						
			return 0;
		}
		else
		{
			return 0;
		}
	}
	
	
	public boolean SaveContent(BufferedOutputStream OutputFile) 
	{
		if (IsReceiver == true)
		{
		// TODO Auto-generated method stub
			try 
			{
				OutputFile.write(Content, 0, this.SizeOfData);
				OutputFile.flush();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return true;
		}
		else 
		{
			return false;
		}
		
	}
	
}
