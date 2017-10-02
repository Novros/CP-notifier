package cz.novros.cp.database.user.service;

import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
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
	public boolean registerUser(@Nonnull final cz.novros.cp.common.entity.User user) {
		final User databaseUser = new User(user.getUsername(), hashPassword(user.getPassword()), ImmutableSet.of());

		if (userRepository.findOne(databaseUser.getEmail()) != null) {
			return false;
		}

		return userRepository.save(databaseUser) != null;
	}

	@Override
	public boolean loginUser(@Nonnull final cz.novros.cp.common.entity.User user) {
		return userRepository.findByEmailAndPassword(user.getUsername(), hashPassword(user.getPassword())) != null;
	}

	private static String hashPassword(@Nonnull final String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
