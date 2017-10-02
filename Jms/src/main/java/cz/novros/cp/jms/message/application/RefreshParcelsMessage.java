package cz.novros.cp.jms.message.application;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshParcelsMessage extends TrackingNumbersMessage {
}
