package cz.novros.cp.jms.message.parcel;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.entity.Parcel;
import cz.novros.cp.jms.message.AbstractJmsMessage;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class ParcelsMessage extends AbstractJmsMessage {

	Collection<Parcel> parcels;
}
