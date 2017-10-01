package cz.novros.cp.web.service;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Interface which defines method for working with users in CP system.
 */
public interface UserService {

	/**
	 * Register user in system.
	 *
	 * @param username Username of user.
	 * @param password Password of user.
	 *
	 * @return True if user was registered, otherwise false.
	 */
	boolean registerUser(@Nonnull final String username, @Nonnull final String password);

	/**
	 * Try login user to system.
	 *
	 * @param username Username of user.
	 * @param password Password of user.
	 *
	 * @return True if user is logged in, otherwise false.
	 */
	boolean loginUser(@Nonnull final String username, @Nonnull final String password);

	/**
	 * Add tracking numbers to user.
	 *
	 * @param username        Username of user.
	 * @param trackingNumbers Tracking numbers which will be added.
	 *
	 * @return Return actualized tracking numbers of user.
	 */
	@Nonnull
	Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers);

	/**
	 * Remove tracking numbers from user.
	 *
	 * @param username        Username of user.
	 * @param trackingNumbers Tracking numbers which will be added.
	 *
	 * @return Return actualized tracking numbers of user.
	 */
	@Nonnull
	Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers);

	/**
	 * Read all tracking number of user.
	 *
	 * @param username Username of user.
	 *
	 * @return Collection of tracking number for user.
	 */
	Collection<String> readTrackingNumbers(@Nonnull final String username);
}
