package callerId.consumer.address;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;











public class SimpleAddressBookImpl
  implements AddressBook
{
  private static final Logger logger = Logger.getLogger(SimpleAddressBookImpl.class);
  private static final String DEFAULT_ADDRESS_FILE = "addresses.txt";
  private Map<PhoneNumber, Address> addresses;
  private String addressFile;
  
  public SimpleAddressBookImpl() throws FileNotFoundException, IOException
  {
    addresses = new HashMap();
    addressFile = System.getProperty("addressfile", "addresses.txt");
    Properties p = new Properties();
    File file = new File(addressFile);
    logger.info("Attempting to load address file '" + file.getAbsolutePath() + "'");
    InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
    p.load(bufferedInputStream);
    
    Enumeration e = p.propertyNames();
    while (e.hasMoreElements()) {
      String num = (String)e.nextElement();
      PhoneNumber phoneNumber1 = new PhoneNumber(num);
      String details = p.getProperty(num);
      String[] detailsSplit = details.split(",");
      if (detailsSplit.length == 3) {
        Person person = new Person(detailsSplit[0], detailsSplit[1], detailsSplit[2]);
        addresses.put(phoneNumber1, new Address(phoneNumber1, person));
      } else {
        logger.warn("Address " + details + " not parseable - skipping!");
      }
    }
  }
  
  public Person lookUpName(PhoneNumber number)
  {
    if (addresses.containsKey(number)) {
      return ((Address)addresses.get(number)).getPerson();
    }
    return null;
  }
}
