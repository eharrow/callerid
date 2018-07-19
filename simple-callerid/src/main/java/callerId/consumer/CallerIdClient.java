package callerId.consumer;

import callerId.consumer.address.AddressBook;
import callerId.consumer.address.Person;
import callerId.consumer.address.PhoneNumber;
import callerId.consumer.address.SimpleAddressBookImpl;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;








public class CallerIdClient
{
  private static final Logger logger = Logger.getLogger(CallerIdClient.class.getName());
  private static final Logger FXOLogger = Logger.getLogger("Inbound");
  private static final String DEFAULT_ADDRESS = "230.0.0.1";
  private static final int DEFAULT_PORT = 9013;
  private String address;

  public static void main(String[] args)
    throws Exception
  {
    CallerIdClient client = new CallerIdClient();

    System.out.println(client.lookup("FXO:CNDD name=, number=07****140"));
    System.out.println(client.lookup("FXO:CNDD name=WITHELD, number="));
  }


  private int port;

  private AddressBook addressBook;

  private Pattern p;
  private Properties areacodes;
  public CallerIdClient()
  {
    address = "230.0.0.1";
    port = 9013;
  }

  protected final SimpleAddressBookImpl createAddressBookImpl() throws FileNotFoundException, IOException
  {
    return new SimpleAddressBookImpl();
  }

  public final String getAddress() {
    return address;
  }

  public final int getPort() {
    return port;
  }

  public final void listen() throws IOException {
    InetAddress inetAddress = InetAddress.getByName(address);
    DatagramSocket socket = new DatagramSocket(port);
    byte[] message = new byte['Ā'];
    for (;;) {
      DatagramPacket packet = new DatagramPacket(message, message.length);

      socket.receive(packet);
      int len = packet.getLength();
      String msg = new String(packet.getData(), 0, len).trim();
      onRing(msg);
    }
  }

  protected String lookup(String msg) {
    String detail = null;

    if (p == null) {
      p = Pattern.compile(numberPattern());
    }

    if (msg.startsWith("FXO:CNDD")) {
      FXOLogger.debug(msg);
    }

    Matcher m = p.matcher(msg);
    if (m.find()) {
      String phoneNumber = m.group(2);
      String name = m.group(1);

      if ((phoneNumber != null) && (!"".equals(phoneNumber))) {
        try
        {
          if (addressBook == null) {
            addressBook = createAddressBookImpl();
          }

          Person lookUpName = addressBook.lookUpName(new PhoneNumber(phoneNumber));


          if (lookUpName != null) {
            detail = lookUpName.toString();
          } else {
            detail = lookupAreacode(phoneNumber);
          }
        } catch (FileNotFoundException e) {
          logger.error(e.getMessage());
        } catch (IOException e) {
          logger.error(e.getMessage());
        }
      } else if ((name != null) && (!"".equals(name))) {
        detail = name;
      }
    }

    return detail;
  }

  protected final String lookupAreacode(String phoneNumber) throws IOException {
    String retVal = phoneNumber;

    if (areacodes == null) {
      areacodes = loadAreacodes();
    }

    int endIdx = 3;
    String key = null;
    String candidate = "none";

    while ((endIdx < 6) && (candidate.equals("none"))) {
      key = phoneNumber.substring(1, endIdx);
      candidate = areacodes.getProperty(key, "none");
      endIdx++;
    }

    if (!candidate.equals("none")) {
      retVal = phoneNumber + " (" + candidate + ")";
    }

    return retVal;
  }

  protected final Properties loadAreacodes() throws FileNotFoundException, IOException
  {
    String areacodesFile = System.getProperty("areacodesfile", "areacodes.txt");
    Properties p = new Properties();
    File file = new File(areacodesFile);
    logger.info("Attempting to load areacodes file '" + file.getAbsolutePath() + "'");
    InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
    p.load(bufferedInputStream);

    return p;
  }

  protected String numberPattern() {
    return "^FXO:CNDD name=(.*), number=(.*)$";
  }

  protected void onRing(String msg) {
    lookup(msg);
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
