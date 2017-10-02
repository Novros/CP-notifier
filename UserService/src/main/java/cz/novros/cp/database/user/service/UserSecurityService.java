package cz.novros.cp.database.user.service;

import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import com.google.common.hash.Hashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.database.user.dao.UserRepository;
import cz.novros.cp.database.user.entity.User;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserSecurityService implements SecurityUserService {

	UserRepository userRepository;

	@Override
	public boolean registerUser(@Nonnull final String username, @Nonnull final String password) {
		final User user = new User();
		user.setEmail(username);
		user.setPassword(hashPassword(password));

		if (userRepository.findOne(username) != null) {
			return false;
		}

		return userRepository.save(user) != null;
	}

	@Override
	public boolean loginUser(@Nonnull final String username, @Nonnull final String password) {
		return userRepository.findByEmailAndPassword(username, hashPassword(password)) != null;
	}

	private static String hashPassword(@Nonnull final String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
