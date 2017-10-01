package cz.novros.cp.web.test;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import cz.novros.cp.jms.entity.Attributes;
import cz.novros.cp.jms.entity.Parcel;
import cz.novros.cp.jms.entity.State;
import cz.novros.cp.web.service.ParcelService;
import cz.novros.cp.web.service.UserService;

@Service
@Scope("singleton")
@Profile("test")
public class TestService implements UserService, ParcelService {

	private static final Collection<String> serviceTrackingNumbers = new HashSet<String>() {{
		addAll(ImmutableList.of("123456789", "987654321", "123498765"));
	}};

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> trackingNumbers) {
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
	public Collection<Parcel> refreshParcels(@Nonnull final Collection<String> trackingNumbers) {
		return Collections2.transform(readTrackingNumbers("a"), new Function<String, Parcel>() {
			@Nullable
			@Override
			public Parcel apply(@Nullable final String input) {
				return createParcel(input);
			}
		});
	}

	@Override
	public boolean registerUser(@Nonnull final String username, @Nonnull final String password) {
		return true;
	}

	@Override
	public boolean loginUser(@Nonnull final String username, @Nonnull final String password) {
		return true;
	}

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers) {
		serviceTrackingNumbers.addAll(trackingNumbers);
		return serviceTrackingNumbers;
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers) {
		serviceTrackingNumbers.removeAll(trackingNumbers);
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
}
