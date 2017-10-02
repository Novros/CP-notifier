package cz.novros.cp.common.entity;

import java.util.List;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Parcel {

	String parcelTrackingNumber;

	Attributes attributes;

	List<State> states;

	@Nullable
	public State getLastState() {
		return states == null || states.isEmpty() ? null : states.get(states.size() - 1);
	}
}
