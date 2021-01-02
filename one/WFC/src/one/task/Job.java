package one.task;
import java.net.*;
public class Job
{
public void doJob(URL dataSetURL,URL resultSetURL) throws Exception
{
System.out.println("Job starts");
Thread.sleep(5000);
System.out.println("Fetching data...");
Thread.sleep(5000);
System.out.println("processing data...");
Thread.sleep(5000);
System.out.println("job completed");
}

}