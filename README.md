TCP_wav_File_Transfer
=====================
This is a java implementation in tcp file transfer. The transfer is sending the wav file.

The file sended is by package which size is 2048 byte. 


Clint
The clint must set the IP address and the port of the server in the CONSTANT class.

Before sending the file, the user must send the name of the wav file. Or the program is not have the name to save the file and the program will throw an IO exception.

The clent can recieve the reply of the sever by using the Revieve*** funciton of the transfer class.



Sever
The sever must set the port which to listen in the sever class.

The sever can set the path where the received file save to, and the file type of the received file in the CONSTANT class.


Any bug report is welcomed. Thanks and enjoy it.

