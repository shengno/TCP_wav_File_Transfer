/**
 * 
 */
package edu.pkusz.sheng;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;



/**
 * @author sheng
 *
 */
public final class TransferThread implements Runnable
{
	private Socket Client = null;
	private static int NumberOfClients = 0;
	private final int MAXNUMBEROFCLIENTS = 20;
	
	public TransferThread(Socket Client)
	{
		this.Client = Client;
		NumberOfClients++;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		
		if (TransferThread.NumberOfClients < this.MAXNUMBEROFCLIENTS)
		{
			BufferedInputStream SocketInput = null;
			
			
			
			// TODO Auto-generated method stub
			try 
			{
				SocketInput = new BufferedInputStream(Client.getInputStream());
			}
			catch (IOException e2) 
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
			
			// TODO 添加控制流程
			
			System.out.println("The ip in client is" + Client.getRemoteSocketAddress());
			System.out.println("The name of client is " + Client.getInetAddress().getHostName());
			
			
			Transfer Tran = new Transfer(Client);		
			
			System.out.println("Starting receiving.");
			Tran.Recieving();
						
			
			// 关闭输出的文件和Socket
			try
			{
			SocketInput.close();
			Client.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			System.out.println("client socket is end.");
			TransferThread.NumberOfClients--;
		}
		
		
	}
	
	
}
