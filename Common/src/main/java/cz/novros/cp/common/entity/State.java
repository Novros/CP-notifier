package cz.novros.cp.common.entity;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class State {

	Long id;

	Date date;

	String text;

	int postcode;

	String postOffice;

	double latitude;

	double longitude;

	Date timeDeliveryAttempt;

	public String displayPostOffice() {
		return postOffice + " " + postcode;
	}

	public String displayGps() {
		return latitude + " " + longitude;
	}

	public String displayTitle() {
		return text + " " + date.toString();
	}
}
