package callerId.consumer.address;




public class PhoneNumber
{
  private String number;
  


  public PhoneNumber(String number)
  {
    this.number = number;
  }
  
  public String getNumber()
  {
    return number;
  }
  
  public void setNumber(String number)
  {
    this.number = number;
  }
  
  public boolean equals(Object arg0)
  {
    if (!(arg0 instanceof PhoneNumber))
    {
      return false;
    }
    
    PhoneNumber other = (PhoneNumber)arg0;
    return number.equals(number);
  }
  

  public int hashCode()
  {
    return number.hashCode();
  }
}
