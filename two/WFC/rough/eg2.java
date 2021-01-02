import com.google.gson.JsonArray.*;
import com.google.gson.*;
import com.google.gson.stream.*;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;
import java.util.*;
class test
{
public static void main(String gg[])
{
try{
String profilePath=gg[0];
JsonReader reader=new JsonReader(new FileReader(profilePath));
JsonObject jo=new Gson().fromJson(reader,JsonObject.class);
JsonArray array=(JsonArray)jo.get("jars");
Iterator<JsonElement> it=array.iterator();
URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
Method method; 
URL url;
File file; 
String path;
while(it.hasNext())
{
path=it.next().getAsString();
file =new File(path);
System.out.println(path);
url = file.toURI().toURL();
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
Method m=c.getDeclaredMethod(it1.next().getAsString());
m.invoke(c.newInstance());
}
}catch(Exception e)
{
e.printStackTrace();
}
}
}