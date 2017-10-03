package cz.novros.cp.rest;

/**
 * Contains names of endpoints for all rest services.
 */
public class EndpointNames {

	public static final String PARCEL_SERVICE_ENDPOINT = "/parcel";
	public static final String READ_PARCELS_ENDPOINT = "/"; // FIXME - Maybe empty ?
	public static final String SAVE_PARCELS_ENDPOINT = "/"; // FIXME - Maybe empty ?
	public static final String REMOVE_PARCELS_ENDPOINT = "/"; // FIXME - Maybe empty ?

	public static final String USER_SERVICE_ENDPOINT = "/user";
	public static final String ADD_TRACKING_USER_ENDPOINT = "/{username}/add-tracking"; // FIXME - change to tracking - POST
	public static final String REMOVE_TRACKING_USER_ENDPOINT = "/{username}/remove-tracking"; // FIXME - change to tracking - DELETE
	public static final String READ_TRACKING_USER_ENDPOINT = "/{username}/tracking";  // FIXME - change to tracking - GET
	public static final String USERNAME_PARAM = "username";

	public static final String SECURITY_SERVICE_ENDPOINT = "/security";
	public static final String REGISTER_USER_ENDPOINT = "/register";
	public static final String LOGIN_USER_ENDPOINT = "/login";

	public static final String APPLICATION_SERVICE_ENDPOINT = "/application";
	public static final String REFRESH_PARCEL_ENDPOINT = "/refresh";

	public static final String CZECH_POST_SERVICE = "/cp";
}
