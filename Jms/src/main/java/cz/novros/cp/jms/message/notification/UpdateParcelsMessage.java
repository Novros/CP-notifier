package cz.novros.cp.jms.message.notification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.message.AbstractJmsMessage;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateParcelsMessage extends AbstractJmsMessage {
}
