import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;
class JobServer {
private ServerSocket serverSocket;
private int portNumber;
JobServer(int portNumber) {
this.portNumber=portNumber;
try{
serverSocket=new ServerSocket(this.portNumber);
startListening();
}catch(Exception e) {
System.out.println(e);
System.exit(0); }}
public void startListening() {
try{
Socket ck;
while(true) {
System.out.println("Server is listening on port : "+this.portNumber);
ck=serverSocket.accept();
System.out.println("Request arrived ");
new RequestProcessor(ck); }
}catch(Exception e) {
System.out.println(e);
}
}
}
class RequestProcessor extends Thread
{
private Socket ck;
RequestProcessor(Socket socket)
{
this.ck=socket;
start();
}
public void run()
{
try
{
InputStream inputStream=ck.getInputStream();
byte ack[]={65};
byte header[]=new byte[8192];
int m=-1;
while(m==-1) m=inputStream.read(header);
OutputStream outputStream=ck.getOutputStream();
outputStream.write(ack,0,1);
outputStream.flush();
String jsonString=new String(header).trim();
System.out.printf("(%s)\n",jsonString);
JsonObject jo=new Gson().fromJson(jsonString,JsonObject.class);
String fileName;
long lengthOfFile;
fileName=jo.get("jar").getAsString();
lengthOfFile=jo.get("jarSize").getAsLong();
String dataSetURL=jo.get("dataSetURL").getAsString();
String resultSetURL=jo.get("resultSetURL").getAsString();
String job=jo.get("job").getAsString();
String process=jo.get("process").getAsString();
int e,f;
int bufferSize=1024;
byte bytes[]=new byte[bufferSize];
int byteRead=0;
int byteCount;
System.out.println("Receiving file : "+fileName);
File file=new File("jars"+File.separator+fileName);
if(file.exists()) file.delete();
FileOutputStream fileOutputStream;
fileOutputStream=new FileOutputStream(file);
BufferedOutputStream bos=new BufferedOutputStream(fileOutputStream);
bytes=new byte[1024];
int i=0;
int bytesRead;
while(true)
{
bytesRead=inputStream.read(bytes);
if(bytesRead<0) break;
i=i+bytesRead;
bos.write(bytes,0,bytesRead);
bos.flush();
outputStream.write(ack);
outputStream.flush();
if(i==lengthOfFile) break;
}
ck.close();
System.out.println("File received");
URLClassLoader urlClassLoader=new URLClassLoader(new URL[]{new File("jars"+File.separator+fileName).toURL()});
Class c=Class.forName(job,true,urlClassLoader);
Method method=c.getMethod(process,new Class[]{URL.class,URL.class});
Object jobObject=c.newInstance();
method.invoke(jobObject,new URL(dataSetURL),new URL(resultSetURL));
}catch(Exception e)
{
System.out.println(e);
}
}
}
class Main
{
public static void main(String data[])
{
int portNumber=Integer.parseInt(data[0]);
JobServer cs=new JobServer(portNumber);
}
}