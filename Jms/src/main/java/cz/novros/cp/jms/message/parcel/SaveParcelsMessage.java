package cz.novros.cp.jms.message.parcel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class SaveParcelsMessage extends ParcelsMessage {

}
