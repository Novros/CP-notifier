package cz.novros.cp.database.user.rest;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.User;
import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.database.user.service.UserSecurityService;

@RestController
@RequestMapping("/security")
@Primary
@Profile("rest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SecurityUserRestService implements SecurityUserService {

	UserSecurityService userSecurityService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@Override
	public boolean registerUser(@RequestBody @Nonnull final User user) {
		final String username = user.getUsername();
		log.info("Registering user({}) to application.", username);

		final boolean result = userSecurityService.registerUser(user);

		if (result) {
			log.info("User({}) was registered to application", username);
		} else {
			log.warn("User({}) was not registered to application", username);
		}

		return result;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@Override
	public boolean loginUser(@RequestBody @Nonnull final User user) {
		final String username = user.getUsername();
		log.debug("Trying to login user({}) to application.", username);

		final boolean result = userSecurityService.loginUser(user);

		log.debug("User({}) was {} logged in.", username, result ? "" : "not");

		return result;
	}
}
