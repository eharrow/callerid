package callerId.consumer;

import com.breeweb.slimconnect.data.Player;
import com.breeweb.slimconnect.data.ReturnData;
import com.breeweb.slimconnect.exception.DataException;
import com.breeweb.slimconnect.iface.delegate.Slimp3Delegate;
import java.io.IOException;
import org.apache.log4j.Logger;










public class SqueezeboxCallerIdClient
  extends CallerIdClient
{
  public SqueezeboxCallerIdClient()
  {
    logger.info("inited");
  }
  
  public static void main(String[] args)
    throws IOException
  {
    SqueezeboxCallerIdClient client = new SqueezeboxCallerIdClient();
    client.setSlimserverIpAddress("192.168.0.4");
    client.setSlimserverIpPort(9090);
    client.onRing("FXO:CNDD name=, number=07941341409");
  }
  
  protected void onRing(String msg)
  {
    if (msg != null)
    {
      logger.info(msg);
      tellSqueezebox(msg);
    }
  }
  
  private void tellSqueezebox(String cmd)
  {
    Slimp3Delegate slimp3 = null;
    Player[] players = (Player[])null;
    try
    {
      slimp3 = Slimp3Delegate.getInstance(slimserverIpAddress, slimserverIpPort);
      players = slimp3.getPlayers();
      for (int i = 0; i < players.length; i++)
      {
        Player player = players[i];
        String id = player.getId();
        String name = player.getName();
        slimp3.setPlayer(player);
        ReturnData d = slimp3.setDisplay("Caller", cmd, 10);
        logger.debug(d);
      }
      
    }
    catch (DataException e)
    {
      logger.error(e.getMessage());
    }
    finally
    {
      slimp3.closeConnections();
    }
  }
  

  public String getSlimserverIpAddress()
  {
    return slimserverIpAddress;
  }
  
  public void setSlimserverIpAddress(String host)
  {
    slimserverIpAddress = host;
  }
  
  public int getSlimserverIpPort()
  {
    return slimserverIpPort;
  }
  
  public void setSlimserverIpPort(int port)
  {
    slimserverIpPort = port;
  }
  






  private static final Logger logger = Logger.getLogger(SqueezeboxCallerIdClient.class);
  private int slimserverIpPort;
  private String slimserverIpAddress;
}
