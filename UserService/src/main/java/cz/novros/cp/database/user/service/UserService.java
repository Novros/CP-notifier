package cz.novros.cp.database.user.service;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.user.dao.UserRepository;
import cz.novros.cp.database.user.entity.User;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	UserRepository userRepository;

	@Autowired
	public UserService(@Nonnull final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Nullable
	public User loginUser(@Nonnull final String email, @Nonnull final String password) {
		return userRepository.findByEmailAndPassword(email, hashPassword(password));
	}

	@Nullable
	public User createUser(@Nonnull final String email, @Nonnull final String password) {
		final User user = new User();
		user.setEmail(email);
		user.setPassword(hashPassword(password));

		if (userRepository.findByEmail(email) != null) {
			return null;
		}

		return userRepository.save(user);
	}

	@Nullable
	public User addTrackingNumbers(@Nonnull final String email, @Nonnull final Set<String> trackingNumbers) {
		final User user = userRepository.findOne(email);

		if (user == null) {
			return null;
		}

		user.getTrackingNumbers().addAll(trackingNumbers);

		return userRepository.save(user);
	}

	@Nonnull
	public Set<String> readTrackingNumbers(@Nonnull final String email) {
		final User user = userRepository.findByEmail(email);
		return user == null ? ImmutableSet.of() : user.getTrackingNumbers();
	}

	private static String hashPassword(@Nonnull final String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
