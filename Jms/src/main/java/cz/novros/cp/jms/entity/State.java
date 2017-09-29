package cz.novros.cp.jms.entity;

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
}
