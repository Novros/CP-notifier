package cz.novros.cp.rest.client.jms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

import cz.novros.cp.jms.entity.Attributes;
import cz.novros.cp.jms.entity.Parcel;
import cz.novros.cp.jms.entity.State;
import cz.novros.cp.rest.client.entity.States;

@UtilityClass
public class EntityConverter {

	public static Collection<Parcel> convertParcels(@Nonnull final Collection<cz.novros.cp.rest.client.entity.Parcel> parcels) {
		final Collection<Parcel> myParcels = new HashSet<>();

		for (final cz.novros.cp.rest.client.entity.Parcel parcel : parcels) {
			myParcels.add(convertParcel(parcel));
		}

		return myParcels;
	}

	private static Parcel convertParcel(@Nonnull final cz.novros.cp.rest.client.entity.Parcel parcel) {
		final Parcel myParcel = new Parcel();

		myParcel.setParcelTrackingNumber(parcel.getId());
		myParcel.setAttributes(convertAttributes(parcel.getAttributes()));
		myParcel.setStates(convertStates(parcel.getStates()));

		return myParcel;
	}

	private static List<State> convertStates(@Nonnull final States states) {
		final List<State> myStates = new ArrayList<>();

		for (final cz.novros.cp.rest.client.entity.State state : states.getState()) {
			myStates.add(convertState(state));
		}

		return myStates;
	}

	private static State convertState(@Nonnull final cz.novros.cp.rest.client.entity.State state) {
		final State myState = new State();

		myState.setDate(state.getDate());
		myState.setLatitude(state.getLatitude());
		myState.setLongitude(state.getLongitude());
		myState.setPostcode(state.getPostcode());
		myState.setPostOffice(state.getPostOffice());
		myState.setText(state.getText());
		myState.setTimeDeliveryAttempt(state.getTimeDeliveryAttempt());

		return myState;
	}

	private static Attributes convertAttributes(@Nonnull final cz.novros.cp.rest.client.entity.Attributes attributes) {
		final Attributes myAttributes = new Attributes();

		myAttributes.setCashOnDelivery(attributes.getCashOnDelivery());
		myAttributes.setCurrency(attributes.getCurrency());
		myAttributes.setDeliveryDate(attributes.getDeliveryDate());
		myAttributes.setDeliveryFrom(attributes.getDeliveryFrom());
		myAttributes.setDeliveryTo(attributes.getDeliveryTo());
		myAttributes.setOriginCountry(attributes.getOriginCountry());
		myAttributes.setParcelType(attributes.getParcelType());
		myAttributes.setPhoneName(attributes.getPhoneName());
		myAttributes.setPhoneNumber(attributes.getPhoneNumber());
		myAttributes.setPhoneType(attributes.getPhoneType());
		myAttributes.setQuantity(attributes.getPicies());
		myAttributes.setStoredDays(attributes.getStoredDays());
		myAttributes.setStoredTo(attributes.getStoredTo());
		myAttributes.setTargetCountry(attributes.getTargetCountry());
		myAttributes.setWeight(attributes.getWeight());

		return myAttributes;
	}
}
