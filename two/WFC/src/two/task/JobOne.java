package two.task;
import java.net.*;
public class JobOne
{
public void doJobO() throws Exception
{
System.out.println("JobO starts");
Thread.sleep(5000);
System.out.println("Fetching data...");
Thread.sleep(5000);
System.out.println("processing data...");
Thread.sleep(5000);
System.out.println("job completed");
}

}