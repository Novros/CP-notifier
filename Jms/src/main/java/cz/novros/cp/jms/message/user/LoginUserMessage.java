package cz.novros.cp.jms.message.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class LoginUserMessage extends UserMessage {
}
