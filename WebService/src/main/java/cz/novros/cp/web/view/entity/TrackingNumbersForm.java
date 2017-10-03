package cz.novros.cp.web.view.entity;

import javax.annotation.Nonnull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.common.CommonConstants;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackingNumbersForm {

	String trackingNumbers;

	@Nonnull
	public String[] getTrackingNumbersArray() {
		if (trackingNumbers == null) {
			return new String[]{};
		}

		return trackingNumbers.split(CommonConstants.TRACKING_NUMBER_DELIMITER);
	}
}
