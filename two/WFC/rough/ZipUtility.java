import java.io.*;
import java.util.*;
import java.util.zip.*;
public class ZipUtility
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
directories.add(dir);
}
else
{
fis=new FileInputStream(srcPath+File.separator+dir.getName());
zos.putNextEntry(new ZipEntry(srcPath+File.separator+dir.getName()));
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
path=srcPath+"\\";
zipFolder(path,path+subDirectory.getName(),zos);
}
}
}
public static void main(String gg[]) throws Exception
{
String srcPath=gg[0];
File file=new File(srcPath+".zip");
if(file.exists()) file.delete();
ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(file));
ZipUtility.zipFolder("\\",srcPath,zos);
zos.close();
}
}
