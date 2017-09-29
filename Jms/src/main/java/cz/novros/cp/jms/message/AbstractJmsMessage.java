package cz.novros.cp.jms.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractJmsMessage {

	/** Queue for response. */
	String senderQueue;
	/** Identifier of sent message to pair it with response. */
	String messageId;

	/** If message contains error or not. */
	@NonFinal
	boolean error = false;
}
