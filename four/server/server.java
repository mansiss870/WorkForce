import java.io.*;
import java.net.*;
public class Receiver {
  private readonly UdpClient udp = new UdpClient(15000);
  private void StartListening()
  {
    this.udp.BeginReceive(Receive, new object());
  }
  private void Receive(IAsyncResult ar)
  {
    IPEndPoint ip = new IPEndPoint(IPAddress.Any, 15000);
    byte[] bytes = udp.EndReceive(ar, ref ip);
    string message = Encoding.ASCII.GetString(bytes);
    StartListening();
  }
}