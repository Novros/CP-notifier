package cz.novros.cp.jms.message.user;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class AddTrackingNumbersMessage extends UserMessage {
	
	Collection<String> trackingNumbers;
}
