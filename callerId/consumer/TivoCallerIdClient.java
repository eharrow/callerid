package callerId.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Properties;
import org.apache.log4j.Logger;










public class TivoCallerIdClient
  extends CallerIdClient
{
  private Properties props;
  
  public TivoCallerIdClient()
    throws Exception
  {
    props = new Properties();
    try
    {
      InputStream resourceAsStream = getClass().getResourceAsStream("/tivo.properties");
      props.load(new BufferedReader(new InputStreamReader(resourceAsStream)));
    } catch (Exception e) {
      logger.error("Unable to find file ", e);
      throw e;
    }
    StringWriter sw = new StringWriter();
    props.list(new PrintWriter(sw));
    logger.info(sw.toString());
    urlTemplate = props.getProperty("urlTemplate");
    

    delay = Integer.parseInt(props.getProperty("delay", "7"));
    x = 1;
    y = 1;
    fg = props.getProperty("fg");
    bg = props.getProperty("bg");
  }
  
  public static void main(String[] args) throws Exception {
    CallerIdClient client = new TivoCallerIdClient();
    client.onRing("FXO:CNDD name=, number=0794134140");
  }
  
  protected void onRing(String msg) {
    if (msg != null) {
      logger.debug(msg);
      tellTivo(url(msg));
    }
  }
  
  private void tellTivo(String url) {
    try {
      logger.debug(url);
      URL address = new URL(url);
      URLConnection conn = address.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      
      String str;
      while ((str = in.readLine()) != null) {}
      
      in.close();
    } catch (MalformedURLException e) {
      logger.error(e.getMessage(), e);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }
  
  private String url(String detail) {
    String url = null;
    try {
      url = MessageFormat.format(urlTemplate, new Object[] { URLEncoder.encode(detail, "UTF-8"), new Integer(delay), new Integer(x), new Integer(y), fg, bg });

    }
    catch (UnsupportedEncodingException e)
    {
      logger.error(e);
      url = "Unknown";
    }
    return url;
  }
  
  public String getUrlTemplate() {
    return urlTemplate;
  }
  
  public void setUrlTemplate(String url) {
    urlTemplate = url;
  }
  
  public String getBg() {
    return bg;
  }
  
  public void setBg(String bg) {
    this.bg = bg;
  }
  
  public int getDelay() {
    return delay;
  }
  
  public void setDelay(int delay) {
    this.delay = delay;
  }
  
  public String getFg() {
    return fg;
  }
  
  public void setFg(String fg) {
    this.fg = fg;
  }
  
  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  









  private static final Logger logger = Logger.getLogger(TivoCallerIdClient.class);
  private String urlTemplate;
  private int delay;
  private int x;
  private int y;
  private String fg;
  private String bg;
}
