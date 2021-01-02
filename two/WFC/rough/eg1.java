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
addPath(profilePath);
Class c=Class.forName("two.task.JobOne");
Method m=c.getDeclaredMethod("doJobO");
m.invoke(c.newInstance());
System.out.println("code has properly executed");
}catch(Exception e)
{
e.printStackTrace();
}
}
public static void addPath(String s) throws Exception {
  File f=new File(s);
  URL u=f.toURI().toURL();
System.out.println(u);  
  URLClassLoader urlClassLoader=(URLClassLoader)ClassLoader.getSystemClassLoader();
  Class urlClass=urlClassLoader;
  Method method=urlClass.getDeclaredMethod("addURL",new Class[]{URL.class});
  method.setAccessible(true);
  method.invoke(urlClassLoader,new Object[]{u});
}
 
}