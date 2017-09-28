package cz.novros.cp.rest.client.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class State {

	@JsonProperty("id")
	String id;

	@JsonProperty("date")
	Date date;

	@JsonProperty("text")
	String text;

	@JsonProperty("postcode")
	int postcode;

	@JsonProperty("postOffice")
	String postOffice;

	@JsonProperty("idIcon")
	int idIcon;

	@JsonProperty("latitude")
	double latitude;

	@JsonProperty("longitude")
	double longitude;

	@JsonProperty("timeDeliveryAttempt")
	Date timeDeliveryAttempt;

	@JsonProperty("publicAccess")
	boolean publicAccess;
}
