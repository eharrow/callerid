package callerId.consumer.address;




public class Address
{
  private PhoneNumber phoneNumber;
  


  private Person person;
  


  public Address(PhoneNumber number, Person person)
  {
    phoneNumber = number;
    this.person = person;
  }
  
  public PhoneNumber getPhoneNumber()
  {
    return phoneNumber;
  }
  
  public void setPhoneNumber(PhoneNumber phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }
  
  public Person getPerson()
  {
    return person;
  }
  
  public void setPerson(Person person)
  {
    this.person = person;
  }
}
