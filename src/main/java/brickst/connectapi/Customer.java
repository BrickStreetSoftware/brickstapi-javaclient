package brickst.connectapi;

import java.util.ArrayList;
import java.util.Date;

public class Customer 
{
    public Long id;
    public String altCustomerID;
    public int statusCode;
    public String emailAddress;
    public String smsNumber;

    public String lastName;
    public String firstName;
    public String middleName;
    public String salutation;
    public String organization;

    public String addressLine1;
    public String addressLine2;
    public String city;
    public String state;
    public String province;
    public String postalCode;
    public String country;

    public Date birthDate;
    public String phone;
    public String altPhone;
    public String fax;

    public ArrayList<CustomerAttribute> attributes;
    public ArrayList<CustomerAttribute> channelAddresses;

    public Customer()
    {
    	attributes = new ArrayList<CustomerAttribute>();
    	channelAddresses = new ArrayList<CustomerAttribute>();
    }
}
