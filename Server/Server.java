/**
 * 
 */
package edu.pkusz.sheng;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author sheng
 *
 */
public class Server
{
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		// 设置服务器监听6667端口
		int Port = 6667;
	    ServerSocket Server = new ServerSocket(Port);
	    
	    boolean IsRun = true;
	    
	    Socket Client = null;
	    
	    while (IsRun)
	    {
	    	Client = Server.accept();
	    	System.out.println("The client is connected");
	    	
	    	new Thread(new TransferThread(Client)).run();
	    	
	    }
			
			

	}
	
}
