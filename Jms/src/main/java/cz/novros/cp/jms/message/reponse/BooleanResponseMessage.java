package cz.novros.cp.jms.message.reponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.message.AbstractJmsMessage;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BooleanResponseMessage extends AbstractJmsMessage {

	boolean ok;
}
