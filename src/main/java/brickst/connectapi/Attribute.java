package brickst.connectapi;

// metadata class describing an attribute
public class Attribute 
{
	// master, attribute, channel address, preference, multi-address preference 
	public String type;

	public String name;
	public int dataType;
	public boolean singleValued;
	public String[] boundedValues;
}
