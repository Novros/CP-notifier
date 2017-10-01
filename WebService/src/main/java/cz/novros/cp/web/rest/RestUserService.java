package cz.novros.cp.web.rest;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Service;

import cz.novros.cp.web.service.UserService;

@Service
public class RestUserService implements UserService {
	
	@Override
	public boolean registerUser(@Nonnull final String username, @Nonnull final String password) {
		return false;
	}

	@Override
	public boolean loginUser(@Nonnull final String username, @Nonnull final String password) {
		return false;
	}

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers) {
		return null;
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers) {
		return null;
	}


	@Override
	public Collection<String> readTrackingNumbers(@Nonnull final String username) {
		return null;
	}
}
