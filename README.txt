     完整的使用TCP进行wav文件发送的代码可以在我的github上面下载。
    这份代码主要是用来在客户端和服务器端发送wav文件的，其实是可以发送任意类型的文件的，因为在发送的过程中，仅仅是把文件当做字节流的形式发送的，并没有涉及到对文件的解析，因此，理论是可以发送任何类型的文件的，但是，在这个实现的过程中，我把文件的后缀名在程序中指定为.wav格式了，因此，只能发送.wav文件了。
    同时，这份实现是将文件进行分包发送的，每个包的大小是2048字节，同时，文件名和其他的操作命令也是通过数据包进行发送的，同样的，这些包也是2048字节的，因此，要求文件名和其他操作命令的长度不能超过2032个字节。
    数据包的定义主要在BufferSize类中进行定义。
    

    服务器端
    对于服务器端来说，需要设置服务器端使用的端口号，保存的文件的目录，保存的文件的类型，以及接收到不同的类型的数据包时的操作。
    1.对于服务器端口号的设置
    端口号的设置在Server类的主函数中设置。
    
    2.保存文件的目录和文件的类型
    这两个参数在CONSTANT类中进行设置。

    3.接收到不同类型的数据包的操作，在Transfer类的Recieving函数中的switch语句中进行修改。



     客户端
    对于客户端来说，需要设置服务器的IP地址和端口号，这两个参数在CONSTANT类中定义。
    在每次发送文件之前，必须先发送一个用户名给服务器，不然服务器就没有保存文件的名字，同时，客户端并没有一个用来接受各种类型的数据包的函数，因此，每次客户端需要接受一个结果之前，都必须先给服务器端发送Send***Request，然后调用相应的recieving函数接受服务器的回应。
    


     添加其他的操作命令
    对于程序要用到的一些常量，例如数据包的类型对应的整数常量，都放在CONSTANT类中，数据包的类型定义在PackageType类中。
    如果要添加不同类型的操作的话，就需要在PackageType类中添加定义，同时，在CONSTAN类中添加对应的整数常量，在服务器端，需要在Transfer类的Recieving函数中的switch语句中添加相应的case语句和相关的处理过程。在客户端需要编写相应的请求函数send***request函数和相应的接受返回值的函数recieving***函数。





    




    




    






    
