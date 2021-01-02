import com.google.gson.*;
import com.google.gson.stream.*;
import java.net.*;
import java.io.*;
class JobClient
{
public static void main(String data[]) {
try{
String server=data[0];
int port=Integer.parseInt(data[1]);
String profileFilePath=data[2];
JsonReader reader=new JsonReader(new FileReader(profileFilePath));
JsonObject jo=new Gson().fromJson(reader,JsonObject.class);
String jar=jo.get("jar").getAsString();
String filePath=jo.get("path").getAsString();
filePath=filePath+"/"+jar;
File file=new File(filePath);
if(file.exists()==false) 
{
System.out.println("File Not Found : "+filePath);
return; 
}
jo.remove("path");
jo.addProperty("jarSize",new Long(file.length()));
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
long lengthOfFile=file.length();
FileInputStream fileInputStream;
fileInputStream=new FileInputStream(file);
BufferedInputStream bis=new BufferedInputStream(fileInputStream);
byte contents[]=new byte[1024];
int bytesRead;
i=0;
while(i<lengthOfFile) {
bytesRead=bis.read(contents);
if(bytesRead<0) break;
outputStream.write(contents,0,bytesRead);
outputStream.flush();
int m=-1;
while(m==-1) m=inputStream.read(ack);
// some check required
i=i+bytesRead; }
fileInputStream.close();
System.out.println("bytes of file sent : "+i);
// some more code required over here to parse the response
socket.close();
System.out.printf("File sent");
}catch(Exception exception) 
{
System.out.println(exception); 
}
}
}
