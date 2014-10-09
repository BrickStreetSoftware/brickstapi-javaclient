package brickst.connectapi;

import java.util.ArrayList;

/**
 * Represents an event name.
 * @author cmaeda
 *
 */
public class EventMaster
{
	public Long id;
	public String name;
	public String description;
	public Boolean includeXML;
	
	public ArrayList<EventParameterMaster> parameters;
	
	public EventMaster()
	{
		parameters = new ArrayList<EventParameterMaster>();
	}
}
