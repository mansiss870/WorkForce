import java.net.*;
import java.io.*;
public class SlaveStarter
{
private int slaveStarterPort;
private int slavePort;
private ServerSocket serverSocket;
public SlaveStarter(int slaveStarterPort,int slavePort)
{
this.slaveStarterPort=slaveStarterPort;
this.slavePort=slavePort;
try{
serverSocket=new ServerSocket(this.slaveStarterPort);
startListening();
}catch(Exception e)
{
System.out.println(e);
System.exit(0);
}
}
private void startListening()
{
try{
Socket socket;
while(true)
{
System.out.println("SlaveStarter startsListening...");
socket=serverSocket.accept();
System.out.println("Request arrived");
new Manager(socket,this.slavePort);
}
}catch(Exception e)
{
e.printStackTrace();
}
}
public static void main(String gg[])
{
SlaveStarter slaveStarter=new SlaveStarter(Integer.parseInt(gg[0]),Integer.parseInt(gg[1]));
}
}
class Manager extends Thread
{
private int slavePort;
private Socket socket;
private InputStream is;
private OutputStream os;
Manager(Socket socket,int slaveport)
{
this.socket=socket;
this.slavePort=slavePort;
start();
}
public void run()
{
try{
is=socket.getInputStream();
os=socket.getOutputStream();
byte bytes[]=new byte[4];
int m=-1;
while(m==-1) m=is.read(bytes);
int receivedPortForSlave=0;
byte b[]=new byte[1];
for(int i=0;i<4;i++)
{
receivedPortForSlave=(receivedPortForSlave*10)+bytes[i];
}
System.out.println(receivedPortForSlave);
try{
b[0]=1;
os.write(b,0,1);
os.flush();
System.out.println("connected");
socket.close();
new Slave(this.slavePort);
System.out.println("asdf");
}catch(Exception e)
{
b[0]=2;
os.write(b,0,1);
os.flush();
System.out.println("note connected");
socket.close();
e.printStackTrace();
}
}catch(Exception e)
{
e.printStackTrace();
}
}
}