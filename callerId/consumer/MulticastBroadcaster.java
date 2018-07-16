package callerId.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;






public class MulticastBroadcaster
  extends CallerIdClient
{
  private static final Logger logger = Logger.getLogger(MulticastBroadcaster.class);
  private InetAddress group;
  
  public MulticastBroadcaster() throws UnknownHostException
  {
    logger.info("MulticastBroadcaster started");
    group = InetAddress.getByName("230.0.0.1");
    logger.info("Will broadcast to '230.0.0.1'");
  }
  
  protected void onRing(String msg) {
    if (msg != null) {
      logger.debug(msg);
      try
      {
        MulticastSocket socket = new MulticastSocket();
        byte[] buf = new byte['Ā'];
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        
        socket.send(packet);
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
  }
  
  public static final void main(String[] args) throws UnknownHostException {
    MulticastBroadcaster test = new MulticastBroadcaster();
    

    System.out.print("Enter text to broadcast: ");
    

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    String userName = null;
    

    try
    {
      while ((userName = br.readLine()) != null) {
        System.out.println(userName);
        test.onRing(userName);
      }
    } catch (IOException ioe) {
      System.out.println("IO error trying to read your name!");
      System.exit(1);
    }
  }
}
