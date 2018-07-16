package callerId.consumer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.util.logging.Logger;









public class MacCallerIdAnnouncer
{
  private static final Logger logger = Logger.getLogger("MacCallerIdAnnouncer");
  
  public MacCallerIdAnnouncer() {}
  
  public static void main(String[] args)
    throws IOException
  {
    MulticastSocket socket = new MulticastSocket(4446);
    InetAddress group = InetAddress.getByName("230.0.0.1");
    socket.joinGroup(group);
    
    logger.info("Listening...");
    
    DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
    
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        MacCallerIdAnnouncer.logger.info("Shutting down MacCallerIdAnnouncer");
      }
    }));
    

    for (;;)
    {
      byte[] buf = new byte['Ā'];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);
      
      String received = new String(packet.getData());
      Thread t1 = new Thread(new Runnable()
      {
        public void run() {
          try {
            Runtime.getRuntime().exec("say Caller " + val$received);
          } catch (IOException e) {
            MacCallerIdAnnouncer.logger.severe(e.getMessage()); } } }, "say program");
      





      Thread t2 = new Thread(new Runnable()
      {
        public void run() {
          try {
            String[] p = { "/usr/local/bin/growlnotify", "-m " + val$received, "-t Inbound Call" };
            
            Runtime.getRuntime().exec(p);


          }
          catch (Exception e)
          {

            MacCallerIdAnnouncer.logger.severe(e.getMessage()); } } }, "growl");
      


      Thread t3 = new Thread(new Runnable()
      {
        public void run() {
          try {
            String skypecmd = "/Users/ewan/Applications/skype.sh";
            
            MacCallerIdAnnouncer.logger.info("received: " + val$received);
            String receivedStr = " \"" + val$received + "\"";
            MacCallerIdAnnouncer.logger.info("receivedStr: " + receivedStr);
            String cmd = "/Users/ewan/Applications/skype.sh";
            String[] p = { cmd, val$received };
            Runtime.getRuntime().exec(p);
          } catch (Exception e) {
            MacCallerIdAnnouncer.logger.severe(e.getMessage()); } } }, "skype.sh");
      



      t1.start();
      t2.start();
      t3.start();
    }
  }
}
