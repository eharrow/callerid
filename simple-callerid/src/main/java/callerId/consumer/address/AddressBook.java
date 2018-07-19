package callerId.consumer.address;

public abstract interface AddressBook
{
  public abstract Person lookUpName(PhoneNumber paramPhoneNumber);
}
