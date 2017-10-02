package cz.novros.cp.common.service;

import javax.annotation.Nonnull;

import cz.novros.cp.common.entity.User;

/**
 * Interface which defines method over user security.
 */
public interface SecurityUserService {

	/**
	 * Register user in system.
	 *
	 * @param user User which will be registered to application.
	 *
	 * @return True if user was registered, otherwise false.
	 */
	boolean registerUser(@Nonnull final User user);

	/**
	 * Try login user to system.
	 *
	 * @param user User which will be logged into application.
	 *
	 * @return True if user is logged in, otherwise false.
	 */
	boolean loginUser(@Nonnull final User user);
}
