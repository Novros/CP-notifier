package cz.novros.cp.jms;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConstants {

	public static final String TRACKING_NUMBER_DELIMITER = ";";
	public static final String RESPONSE_PREFIX = "response-";
	public static final String SELECTOR = "JMSCorrelationID=";

	public static String getResponseSelector(@Nonnull final String messageId) {
		return SELECTOR + "'" + RESPONSE_PREFIX + messageId + "'";
	}
}
