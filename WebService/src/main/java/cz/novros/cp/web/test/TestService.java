package cz.novros.cp.web.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cz.novros.cp.common.entity.Attributes;
import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.entity.State;
import cz.novros.cp.common.entity.User;
import cz.novros.cp.common.service.ApplicationService;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.common.service.UserService;

@Service
@Scope("singleton")
@Profile("test")
public class TestService implements UserService, ParcelService, SecurityUserService, ApplicationService {

	private static final Collection<String> serviceTrackingNumbers = new HashSet<String>() {{
		addAll(ImmutableList.of("123456789", "987654321", "123498765"));
	}};

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final String[] trackingNumbers) {
		return Collections2.transform(readTrackingNumbers("a"), new Function<String, Parcel>() {
			@Nullable
			@Override
			public Parcel apply(@Nullable final String input) {
				return createParcel(input);
			}
		});
	}

	@Nonnull
	@Override
	public Collection<Parcel> saveParcels(@Nonnull final Collection<Parcel> parcels) {
		return ImmutableList.of();
	}

	@Override
	public void removeParcels(@Nonnull final String[] trackingNumbers) {
	}

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final String[] trackingNumbers) {
		serviceTrackingNumbers.addAll(Arrays.asList(trackingNumbers));
		return serviceTrackingNumbers;
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final String[] trackingNumbers) {
		serviceTrackingNumbers.removeAll(Arrays.asList(trackingNumbers));
		return serviceTrackingNumbers;
	}


	@Override
	public Collection<String> readTrackingNumbers(@Nonnull final String username) {
		return serviceTrackingNumbers;
	}

	private Parcel createParcel(@Nonnull final String trackingNumber) {
		final List<State> states = new ArrayList<>();
		states.add(createState(1));
		states.add(createState(2));

		final Attributes attributes = new Attributes();
		attributes.setWeight(BigDecimal.ONE);
		attributes.setTargetCountry("Czech repubulic");
		attributes.setStoredTo(new Date());
		attributes.setStoredDays(14);
		attributes.setQuantity(2);
		attributes.setPhoneType(1);
		attributes.setPhoneNumber("+420" + trackingNumber);
		attributes.setPhoneName("Mobile number");
		attributes.setOriginCountry("China");
		attributes.setCurrency("CZK");
		attributes.setCashOnDelivery(BigDecimal.TEN);

		final Parcel parcel = new Parcel();
		parcel.setParcelTrackingNumber(trackingNumber);
		parcel.setAttributes(attributes);
		parcel.setStates(states);

		return parcel;
	}

	private State createState(final int number) {
		final State state = new State();

		state.setText("Text of state " + number);
		state.setPostOffice("Some post office" + number);
		state.setPostcode(100009);
		state.setLongitude(29.3);
		state.setLatitude(90.2);
		state.setDate(new Date());

		return state;
	}

	@Override
	public boolean registerUser(@Nonnull final User user) {
		return true;
	}

	@Override
	public boolean loginUser(@Nonnull final User user) {
		return true;
	}

	@Override
	public Collection<Parcel> refreshParcels(@Nonnull final String[] trackingNumbers) {
		return readParcels(trackingNumbers);
	}
}
