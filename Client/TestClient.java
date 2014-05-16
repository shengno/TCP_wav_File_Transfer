package edu.pkusz.sheng;

import java.io.IOException;

public class TestClient
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		String Name = "123.mp3";
		
		System.out.println("Starting sending.");
		//Transfer.SendUserName(Name);
//		Transfer.SendRequest();
//		System.out.println("Ending sending.");
		
//		String ReceiName = Transfer.ReceiveUserName();
//		System.out.println("The receiver name is " + ReceiName);
		
		Transfer.SendUserName(Name);
		
		System.out.println("Sending file.");
		String FileName = "E:/123.mp3";
		try {
			Transfer.SendFile(FileName);
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("End sending file.");
		
		Transfer.SendAuthorisztionRequest();
		String AuthroStirng = Transfer.ReceiveAuthorizen();
		System.out.println("The author is " + AuthroStirng);
		
		Transfer.SendTextRequest();
		String Text = Transfer.ReceiveText();
		System.out.println("The Text is " + Text);
		
		Transfer.SendRequest();
		String ResultText = Transfer.ReceiveResult();
		System.out.println("The ResultText is " + ResultText);
		
		Transfer.SendClosedRequest();
		
//		try {
//			Transfer.Close();
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("Ending. ");
		
	}
	
}
