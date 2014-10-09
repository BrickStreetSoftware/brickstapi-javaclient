package brickst.connectapi;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents an external event for an event-triggered campaign.
 * @author cmaeda
 *
 */
public class Event 
{
	public Long id;
	public String eventName;
	public String status;
	public long customerId;
	public Long externalId;
	public String xml;
	public Long eventId;
	
	public Boolean sendEmail;
	public Boolean archive;
	public Boolean subscribe;
	public Date effectiveDate;
	public Date expirationDate;
	
	// parameters
	public ArrayList<EventParameter> parameters;
	// attachments
	public ArrayList<EventAttachment> attachments;
	
}
