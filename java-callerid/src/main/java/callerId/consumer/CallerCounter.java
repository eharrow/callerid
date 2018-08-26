package callerId.consumer;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

public class CallerCounter
{
  private static CallerCounter instance;
  private final Map<String, Integer> callers = Collections.synchronizedMap(new java.util.HashMap());
  

  private CallerCounter() {}
  
  public static final CallerCounter getInstance()
  {
    if (instance == null) {
      instance = new CallerCounter();
    }
    
    return instance;
  }
  
  public void clearCounter() {
    callers.clear();
  }
  
  public int update(String caller) {
    int current = 0;
    
    if (callers.containsKey(caller)) {
      current = ((Integer)callers.get(caller)).intValue();
    }
    
    int inc = current + 1;
    callers.put(caller, Integer.valueOf(inc));
    
    return inc;
  }
  
  public void printCallerCounts(PrintWriter pw) {
    if (pw != null) {
      pw.print(callers);
    }
  }
}
