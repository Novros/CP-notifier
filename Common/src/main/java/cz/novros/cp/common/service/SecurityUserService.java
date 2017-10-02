package cz.novros.cp.common.service;

import javax.annotation.Nonnull;

/**
 * Interface which defines method over user security.
 */
public interface SecurityUserService {

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
}
