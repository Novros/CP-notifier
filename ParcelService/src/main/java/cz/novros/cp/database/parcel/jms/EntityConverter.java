package cz.novros.cp.database.parcel.jms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

import cz.novros.cp.database.parcel.entity.Attributes;
import cz.novros.cp.database.parcel.entity.Parcel;
import cz.novros.cp.database.parcel.entity.State;

@UtilityClass
public class EntityConverter {

	public static Collection<Parcel> convertParcelFromCommon(@Nonnull final Iterable<cz.novros.cp.common.entity.Parcel> jmsParcels) {
		final Collection<Parcel> myParcels = new HashSet<>();

		for (final cz.novros.cp.common.entity.Parcel parcel : jmsParcels) {
			myParcels.add(convertParcelFromCommon(parcel));
		}

		return myParcels;
	}

	private static Parcel convertParcelFromCommon(@Nonnull final cz.novros.cp.common.entity.Parcel jmsParcel) {
		final Parcel myParcel = new Parcel();

		myParcel.setParcelTrackingNumber(jmsParcel.getParcelTrackingNumber());
		myParcel.setAttributes(convertAttributesFromCommon(jmsParcel.getAttributes()));
		myParcel.setStates(convertStatesFromCommon(jmsParcel.getStates()));

		return myParcel;
	}

	private static List<State> convertStatesFromCommon(@Nonnull final Collection<cz.novros.cp.common.entity.State> jmsStates) {
		final List<State> myStates = new ArrayList<>();

		for (final cz.novros.cp.common.entity.State state : jmsStates) {
			myStates.add(convertStateFromCommon(state));
		}

		return myStates;
	}

	private static State convertStateFromCommon(@Nonnull final cz.novros.cp.common.entity.State jmsState) {
		final State myState = new State();

		myState.setDate(jmsState.getDate());
		myState.setLatitude(jmsState.getLatitude());
		myState.setLongitude(jmsState.getLongitude());
		myState.setPostcode(jmsState.getPostcode());
		myState.setPostOffice(jmsState.getPostOffice());
		myState.setText(jmsState.getText());
		myState.setTimeDeliveryAttempt(jmsState.getTimeDeliveryAttempt());

		return myState;
	}

	private static Attributes convertAttributesFromCommon(@Nonnull final cz.novros.cp.common.entity.Attributes jmsAttributes) {
		final Attributes myAttributes = new Attributes();

		myAttributes.setCashOnDelivery(jmsAttributes.getCashOnDelivery());
		myAttributes.setCurrency(jmsAttributes.getCurrency());
		myAttributes.setDeliveryDate(jmsAttributes.getDeliveryDate());
		myAttributes.setDeliveryFrom(jmsAttributes.getDeliveryFrom());
		myAttributes.setDeliveryTo(jmsAttributes.getDeliveryTo());
		myAttributes.setOriginCountry(jmsAttributes.getOriginCountry());
		myAttributes.setParcelType(jmsAttributes.getParcelType());
		myAttributes.setPhoneName(jmsAttributes.getPhoneName());
		myAttributes.setPhoneNumber(jmsAttributes.getPhoneNumber());
		myAttributes.setPhoneType(jmsAttributes.getPhoneType());
		myAttributes.setQuantity(jmsAttributes.getQuantity());
		myAttributes.setStoredDays(jmsAttributes.getStoredDays());
		myAttributes.setStoredTo(jmsAttributes.getStoredTo());
		myAttributes.setTargetCountry(jmsAttributes.getTargetCountry());
		myAttributes.setWeight(jmsAttributes.getWeight());

		return myAttributes;
	}

	public static Collection<cz.novros.cp.common.entity.Parcel> convertParcelToCommon(@Nonnull final Iterable<Parcel> parcels) {
		final Collection<cz.novros.cp.common.entity.Parcel> myParcels = new HashSet<>();

		for (final Parcel parcel : parcels) {
			myParcels.add(convertParcelToCommon(parcel));
		}

		return myParcels;
	}

	private static cz.novros.cp.common.entity.Parcel convertParcelToCommon(@Nonnull final Parcel parcel) {
		final cz.novros.cp.common.entity.Parcel jmsParcel = new cz.novros.cp.common.entity.Parcel();

		jmsParcel.setParcelTrackingNumber(parcel.getParcelTrackingNumber());
		jmsParcel.setAttributes(convertAttributesToCommon(parcel.getAttributes()));
		jmsParcel.setStates(convertStatesToCommon(parcel.getStates()));

		return jmsParcel;
	}

	private static List<cz.novros.cp.common.entity.State> convertStatesToCommon(@Nonnull final Collection<State> states) {
		final List<cz.novros.cp.common.entity.State> jmsStates = new ArrayList<>();

		for (final State state : states) {
			jmsStates.add(convertStateToCommon(state));
		}

		return jmsStates;
	}

	private static cz.novros.cp.common.entity.State convertStateToCommon(@Nonnull final State state) {
		final cz.novros.cp.common.entity.State jmsState = new cz.novros.cp.common.entity.State();

		jmsState.setDate(state.getDate());
		jmsState.setLatitude(state.getLatitude());
		jmsState.setLongitude(state.getLongitude());
		jmsState.setPostcode(state.getPostcode());
		jmsState.setPostOffice(state.getPostOffice());
		jmsState.setText(state.getText());
		jmsState.setTimeDeliveryAttempt(state.getTimeDeliveryAttempt());

		return jmsState;
	}

	private static cz.novros.cp.common.entity.Attributes convertAttributesToCommon(@Nonnull final Attributes attributes) {
		final cz.novros.cp.common.entity.Attributes jmsAttributes = new cz.novros.cp.common.entity.Attributes();

		jmsAttributes.setCashOnDelivery(attributes.getCashOnDelivery());
		jmsAttributes.setCurrency(attributes.getCurrency());
		jmsAttributes.setDeliveryDate(attributes.getDeliveryDate());
		jmsAttributes.setDeliveryFrom(attributes.getDeliveryFrom());
		jmsAttributes.setDeliveryTo(attributes.getDeliveryTo());
		jmsAttributes.setOriginCountry(attributes.getOriginCountry());
		jmsAttributes.setParcelType(attributes.getParcelType());
		jmsAttributes.setPhoneName(attributes.getPhoneName());
		jmsAttributes.setPhoneNumber(attributes.getPhoneNumber());
		jmsAttributes.setPhoneType(attributes.getPhoneType());
		jmsAttributes.setQuantity(attributes.getQuantity());
		jmsAttributes.setStoredDays(attributes.getStoredDays());
		jmsAttributes.setStoredTo(attributes.getStoredTo());
		jmsAttributes.setTargetCountry(attributes.getTargetCountry());
		jmsAttributes.setWeight(attributes.getWeight());

		return jmsAttributes;
	}
}
