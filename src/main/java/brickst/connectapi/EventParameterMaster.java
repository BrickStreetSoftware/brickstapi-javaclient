package brickst.connectapi;

public class EventParameterMaster
{
    // DATA TYPES (cf common.bl.cnversation.EventParameter.getPossibleDataTypes in Connect src)
    // NOTE: The codes are originally from java.sql.Types in JDBC.
    public static int TYPE_SINGLE_CHARACTER = 1;
    public static int TYPE_NUMBER = 3;
    public static int TYPE_STRING = 12;
    public static int TYPE_DATE = 93;
    public static int TYPE_URL = 3000;

    public String name;
    public int dataType;
    public boolean forwarded = true;	// should always be true
}
