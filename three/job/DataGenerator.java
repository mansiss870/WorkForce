import java.util.*;
import java.io.*;
class DataGenerator
{
public static void main(String gg[])
{
String dataFile=gg[0];
try{
File file=new File(dataFile);
if(file.exists()) file.delete();
RandomAccessFile raf=new RandomAccessFile(file,"rw");
long j=System.nanoTime();
int x=1;
while(x<100)
{
raf.writeBytes(UUID.randomUUID().toString()+"\r\n");
x++;
}
raf.close();
long k=System.nanoTime();
long r=k-j;
System.out.println("Time taken "+r/1000000+"milli senconds.");
}catch(Exception e)
{
e.printStackTrace();
}

}
}