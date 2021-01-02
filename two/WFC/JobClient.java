import java.io.*;
import java.net.*;
import com.google.gson.*;
import com.google.gson.stream.*;
import java.util.*;
import java.util.zip.*;
class ZipUtility
{
public static void zipFolder(String path,String srcPath,ZipOutputStream zos) throws Exception
{
List<File> directories=new LinkedList<>();
File file;
File[] files;
byte[] bytes;
file=new File(srcPath);
files=file.listFiles();
FileInputStream fis=null;
bytes=new byte[1024];
File subDirectory=null;
for(File dir:files)
{
if(dir.isDirectory())
{
//dir.mkdir();
zos.putNextEntry(new ZipEntry(srcPath+"/"+dir.getName()+"/"));
directories.add(dir);
}
else
{
fis=new FileInputStream(srcPath+"/"+dir.getName());
zos.putNextEntry(new ZipEntry(srcPath+"/"+dir.getName()));
zos.flush();
int count=0;
while(true)
{
count=fis.read(bytes);
if(count==-1) break;
zos.write(bytes,0,count);
zos.flush();
}
}
}
while(true)
{
if(directories.size()==0)
{
return;
}
else
{
subDirectory=directories.get(0);
directories.remove(0);
path=srcPath+"/";
zipFolder(path,path+subDirectory.getName(),zos);
}
}
}
}

class JobClient
{
public static void main(String data[]) {
try{
String server=data[0];
int port=Integer.parseInt(data[1]);
String profileFilePath=data[2];
JsonReader reader=new JsonReader(new FileReader(profileFilePath));
JsonObject jo=new Gson().fromJson(reader,JsonObject.class);
String dirpath=jo.get("path").getAsString();
File file=new File(dirpath);
if(file.exists()==false) {
System.out.println("File Not Found : "+dirpath);
return; 
}
jo.remove("path");
File zippedFile=new File(dirpath+".zip");
if(zippedFile.exists()) zippedFile.delete();
FileOutputStream fos=new FileOutputStream(new File(dirpath+".zip"));
ZipOutputStream zos=new ZipOutputStream(fos);
ZipUtility.zipFolder("/",dirpath,zos);
//fos.close();
zos.close();
jo.addProperty("zippedFile",new String(dirpath+".zip"));
jo.addProperty("zippedFileSize",new Long(zippedFile.length()));
String jsonString=jo.toString();
byte jsonStringBytes[]=jsonString.getBytes();
// some check required
byte header[];
header=new byte[8192];
for(int i=0;i<jsonStringBytes.length;i++) header[i]=jsonStringBytes[i];
Socket socket=new Socket(server,port);
OutputStream outputStream;
outputStream=socket.getOutputStream();
outputStream.write(header,0,8192);
outputStream.flush();
byte ack[]=new byte[1];
InputStream inputStream=socket.getInputStream();
int i;
i=-1;
while(i==-1) i=inputStream.read(ack);
// some check required
// header sent
int bufferSize=1024;
int numberOfBytesToWrite=bufferSize;
long lengthOfFile=zippedFile.length();
FileInputStream fileInputStream;
fileInputStream=new FileInputStream(zippedFile);
BufferedInputStream bis=new BufferedInputStream(fileInputStream);
byte contents[]=new byte[1024];
int bytesRead;
i=0;
while(i<lengthOfFile) 
{
bytesRead=bis.read(contents);
if(bytesRead<0) break;
outputStream.write(contents,0,bytesRead);
outputStream.flush();
int m=-1;
while(m==-1) m=inputStream.read(ack);
// some check required
i=i+bytesRead; 
}
fileInputStream.close();
System.out.println("bytes of file sent : "+i);
socket.close();
System.out.printf("File sent");
}catch(Exception exception) 
{
exception.printStackTrace();
}
}
}