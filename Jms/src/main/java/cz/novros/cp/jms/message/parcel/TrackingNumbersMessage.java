package cz.novros.cp.jms.message.parcel;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.message.AbstractJmsMessage;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackingNumbersMessage extends AbstractJmsMessage {
	
	Collection<String> trackingNumbers;
}
