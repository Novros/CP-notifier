package cz.novros.cp.jms;

import lombok.experimental.UtilityClass;

/**
 * This class contains names of jms queues of each service.
 */
@UtilityClass
public class QueueNames {

	public static final String REST_CP_QUEUE = "rest-cp";
	public static final String WEB_QUEUE = "web-queue";
	public static final String DATABASE_PARCEL_QUEUE = "database-parcel";
	public static final String DATABASE_USER_QUEUE = "database-user";
	public static final String APPLICATION_QUEUE = "application";
}
