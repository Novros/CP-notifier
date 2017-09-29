package cz.novros.cp.database.user.service;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.hash.Hashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.user.dao.UserRepository;
import cz.novros.cp.database.user.entity.User;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	UserRepository userRepository;

	@Autowired
	public UserService(@NonNull final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Nullable
	public User loginUser(@NonNull final String email, @NonNull final String password) {
		return userRepository.findByEmailAndPassword(email, hashPassword(password));
	}

	@Nullable
	public User createUser(@NonNull final String email, @NonNull final String password) {
		final User user = new User();
		user.setEmail(email);
		user.setPassword(hashPassword(password));

		if (userRepository.findByEmail(email) != null) {
			return null;
		}

		return userRepository.save(user);
	}

	@NonNull
	public User addTrackingNumbers(@NonNull final String email, @NonNull final Set<String> trackingNumbers) {
		final User user = userRepository.findOne(email);

		user.getTrackingNumbers().addAll(trackingNumbers);

		return userRepository.save(user);
	}

	private static String hashPassword(@NonNull final String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
