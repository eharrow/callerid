package callerId.consumer.address;


public class Person
{
  private String firstName;
  
  private String lastName;
  
  private String type;
  

  public Person() {}
  

  public Person(String firstName, String lastName, String type)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.type = type;
  }
  
  public String getFirstName()
  {
    return firstName;
  }
  
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String company)
  {
    type = company;
  }
  
  public String getLastName()
  {
    return lastName;
  }
  
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  
  public String toString()
  {
    return firstName + " " + lastName + " (" + type + ")";
  }
}
