import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.JsonArray.*;
import com.google.gson.*;
import com.google.gson.stream.*;
import java.lang.reflect.*;  
public class Slave
{
private int portNumber;
private ServerSocket serverSocket;
public Slave(int portNumber) throws Exception
{
if(portNumber==7504) throw new Exception("Unable to connect slave4");
this.portNumber=portNumber;
try{
serverSocket=new ServerSocket(this.portNumber);
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
System.out.println("Slave startsListening...");
socket=serverSocket.accept();
System.out.println("Request arrived");
new RequestProcessor(socket);
}
}catch(Exception e)
{
e.printStackTrace();
}
} 
}
class RequestProcessor extends Thread 
{
private Socket socket;
RequestProcessor(Socket socket)
{
this.socket=socket;
start();
}
public void run()
{
try{
InputStream inputStream=socket.getInputStream();
OutputStream outputStream=socket.getOutputStream();
byte receivedBytes[]=new byte[64]; 
byte ack[]=new byte[2];
ack[0]=2;
ack[1]=5;
Long lengthOfFile;
int s=-1;
while(s==-1) s=inputStream.read(receivedBytes);
outputStream.write(ack[0]);
lengthOfFile=TMUtility.bytesToLong(receivedBytes,0,63);
System.out.println(lengthOfFile+"ssssssssssssssssssssssssssssssssssssssssssss");
String zipFileName=java.util.UUID.randomUUID().toString().replaceAll("-","3");
int bufferSize=1024;
byte bytes[]=new byte[bufferSize];
int byteRead=0;
int byteCount;
System.out.println("Receiving file : "+zipFileName);
File file=new File("temp"+File.separator+zipFileName+".zip");
if(file.exists()) file.delete();
System.out.println("Receiving file : "+zipFileName);
FileOutputStream fileOutputStream;
System.out.println("Receiving file : "+zipFileName);
fileOutputStream=new FileOutputStream(file);
BufferedOutputStream bos=new BufferedOutputStream(fileOutputStream);
System.out.println("Receiving file : "+zipFileName);
bytes=new byte[1024];
int i=0;
int bytesRead;
System.out.println("Receiving file : "+zipFileName);
while(i<lengthOfFile)
{
System.out.println("Receiving file :dsfasdfasdfsadfsda "+zipFileName);
bytesRead=inputStream.read(bytes);
System.out.println("1Receiving file :dsfasdfasdfsadfsda "+zipFileName);
if(bytesRead<0) break;
System.out.println("2Receiving file :dsfasdfasdfsadfsda "+zipFileName);
i=i+bytesRead;
outputStream.write(ack,0,2);
outputStream.flush();
bos.write(bytes,0,bytesRead);
bos.flush();
System.out.println(bytesRead+","+i+","+lengthOfFile);
}
System.out.println("Receiving file : "+zipFileName);
bos.close();
String response="File received";

System.out.println("asssssdf1");
byte[] responseBytes=response.getBytes();
outputStream.write(responseBytes);
outputStream.flush();

System.out.println("asssssdf2");
System.out.println("zipFile received: "+zipFileName);
TMZipUtility.unzipIt("temp"+File.separator+zipFileName+".zip","task");
JsonReader reader=new JsonReader(new FileReader("task"+File.separator+"task.json"));
JsonObject jo=new Gson().fromJson(reader,JsonObject.class);
String jar=jo.get("jar").getAsString();
//adding jar to class loader 

System.out.println("asssssdf3");
File f=new File("task"+File.separator+jar);
URL u=f.toURI().toURL();
URLClassLoader urlClassLoader=(URLClassLoader)ClassLoader.getSystemClassLoader();
Class urlClass=URLClassLoader.class;
Method method=urlClass.getDeclaredMethod("addURL",new Class[]{URL.class});
method.setAccessible(true);
method.invoke(urlClassLoader,new Object[]{u});
//code to process File
String className=jo.get("class").getAsString();
String methodName=jo.get("method").getAsString();
String input=jo.get("input").getAsString();

System.out.println("asssssdf4");
File dataFile=new File("task"+File.separator+input);
if(dataFile.exists()==false)
{
System.out.println("dataFile not exists.");
return;
}

System.out.println("asssssdf6");
List<String> strings=new LinkedList<>();
RandomAccessFile raf=new RandomAccessFile(dataFile,"rw");
while(raf.getFilePointer()<dataFile.length())
{
strings.add(raf.readLine());
}
raf.close();
Class c=Class.forName(className);
Method m=c.getDeclaredMethod(methodName,List.class);
List<String> fList =(List)m.invoke(c.newInstance(),new LinkedList<String>(strings));
System.out.println("asssssdf");
for(String sss : fList)
{
System.out.println(sss);
}
}catch(Exception e)
{
e.printStackTrace();
}
}
}