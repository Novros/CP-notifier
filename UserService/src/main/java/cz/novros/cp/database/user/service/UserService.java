package cz.novros.cp.database.user.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.user.dao.UserRepository;
import cz.novros.cp.database.user.entity.User;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements cz.novros.cp.common.service.UserService {

	UserRepository userRepository;

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final String[] trackingNumbers) {
		User user = userRepository.findOne(username);

		if (user == null) {
			return ImmutableSet.of();
		}

		user.getTrackingNumbers().addAll(Arrays.asList(trackingNumbers));
		user = userRepository.save(user);

		return user.getTrackingNumbers();
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final String[] trackingNumbers) {
		User user = userRepository.findOne(username);

		if (user == null) {
			return ImmutableSet.of();
		}

		user.getTrackingNumbers().removeAll(Arrays.asList(trackingNumbers));
		user = userRepository.save(user);

		return user.getTrackingNumbers();
	}

	@Nonnull
	public Set<String> readTrackingNumbers(@Nonnull final String email) {
		final User user = userRepository.findOne(email);

		if (user == null) {
			return ImmutableSet.of();
		}

		return user.getTrackingNumbers();
	}
}
