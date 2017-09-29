package cz.novros.cp.jms.message.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadTrackingNumbersMessage extends UserMessage {

}
