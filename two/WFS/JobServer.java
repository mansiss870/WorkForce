import com.google.gson.JsonArray.*;
import com.google.gson.stream.*;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;
import java.util.zip.*;
class UnZipper
{
public static byte bufferSize[]=new byte[1024];
public static void unZipUtility(String sourcePath,ZipInputStream zipIn) throws Exception
{
String destDir=sourcePath.substring(0,sourcePath.length()-4);
System.out.println(sourcePath+","+destDir);
File file=new File(destDir);
if(file.exists())
{
file.delete();
file.mkdir();
}
else
{
file.mkdir();
}
ZipEntry entry=zipIn.getNextEntry();
String filePath;
while(entry!=null)
{
filePath=entry.getName();
if(!entry.isDirectory())
{
System.out.println(filePath+" file");
BufferedOutputStream baos=new BufferedOutputStream(new FileOutputStream(new File(filePath)));
int count=0;
while(true)
{
count=zipIn.read(bufferSize);
if(count==-1) break;
baos.write(bufferSize,0,count);
baos.flush();
}
baos.close();
}
else{
System.out.println(filePath+" dir");
File subDir=new File(filePath);
subDir.mkdir();
}
zipIn.closeEntry();
entry=zipIn.getNextEntry();
}
zipIn.close();
}

}
class JobServer 
{
private ServerSocket serverSocket;
private int portNumber;
JobServer(int portNumber) 
{
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
public void startListening() 
{
try{
Socket ck;
while(true) 
{
System.out.println("Server is listening on port : "+this.portNumber);
ck=serverSocket.accept();
System.out.println("Request arrived ");
new RequestProcessor(ck); 
}
}catch(Exception e) 
{
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
fileName=jo.get("zippedFile").getAsString();
lengthOfFile=jo.get("zippedFileSize").getAsLong();
int e,f;
int bufferSize=1024;
byte bytes[]=new byte[bufferSize];
int byteRead=0;
int byteCount;
System.out.println("Receiving file : "+fileName);
File file=new File(fileName);
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
bos.close();
ck.close();
System.out.println("File received");
ZipInputStream zis=new ZipInputStream(new FileInputStream(new File(fileName)));
UnZipper.unZipUtility(fileName,zis);
zis.close();
JsonArray array=(JsonArray)jo.get("jars");
Iterator<JsonElement> it=array.iterator();
URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
Method method; 
URL url;
File ufile; 
String path;
while(it.hasNext())
{
path=it.next().getAsString();
ufile =new File(path);
System.out.println(path);
url = ufile.toURI().toURL();
method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
method.setAccessible(true);
method.invoke(classLoader,new Object[]{url});
}
JsonArray arr=(JsonArray)jo.get("jobs");
Iterator<JsonElement> itt=arr.iterator();
JsonArray ar=(JsonArray)jo.get("process");
Iterator<JsonElement> it1=ar.iterator();
while(itt.hasNext())
{
System.out.println("one");
Class c=Class.forName(itt.next().getAsString());
Method mm=c.getDeclaredMethod(it1.next().getAsString());
mm.invoke(c.newInstance());
}


}catch(Exception e)
{
e.printStackTrace();}
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