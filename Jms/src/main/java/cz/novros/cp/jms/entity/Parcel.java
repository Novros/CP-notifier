package cz.novros.cp.jms.entity;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Parcel {

	String parcelTrackingNumber;

	Attributes attributes;

	List<State> states;
}
