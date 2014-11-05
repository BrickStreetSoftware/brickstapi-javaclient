package brickst.connectapi;

public class CustomerAttribute 
{
    public static final String TYPE_ATTRIBUTE = "attribute";
    public static final String TYPE_PREFERENCE = "preference";
    public static final String TYPE_CHANNEL = "channel";
    public static final String TYPE_MULTIADDRESS = "multiaddress";

    public String name;
    public int dataType;
    public String type;
    public String value;
    public String[] preferenceValues;
}
