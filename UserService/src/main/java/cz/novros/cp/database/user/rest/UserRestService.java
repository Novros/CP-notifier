package cz.novros.cp.database.user.rest;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.service.UserService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestService;

@RestController
@RequestMapping(EndpointNames.USER_SERVICE_ENDPOINT)
@Primary
@Profile("rest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserRestService extends AbstractRestService implements UserService {

	cz.novros.cp.database.user.service.UserService userService;

	@Nonnull
	@Override
	@RequestMapping(value = EndpointNames.ADD_TRACKING_USER_ENDPOINT, method = RequestMethod.POST)
	public Collection<String> addTrackingNumbers(@PathVariable @Nonnull final String username, @RequestBody @Nonnull final String[] trackingNumbers) {
		log.info("Adding tracking numbers(count={}) to user({}).", trackingNumbers.length, username);

		final Collection<String> updatedTrackingNumbers = userService.addTrackingNumbers(username, trackingNumbers);

		log.info("Tracking numbers(count={}) of user({}) were updated.", updatedTrackingNumbers.size(), username);

		return updatedTrackingNumbers;
	}

	@Nonnull
	@Override
	@RequestMapping(value = EndpointNames.REMOVE_TRACKING_USER_ENDPOINT, method = RequestMethod.POST)
	public Collection<String> removeTrackingNumbers(@PathVariable @Nonnull final String username, @RequestBody @Nonnull final String[] trackingNumbers) {
		log.info("Removing tracking numbers(count={}) to user({}).", trackingNumbers.length, username);

		final Collection<String> updatedTrackingNumbers = userService.removeTrackingNumbers(username, trackingNumbers);

		log.info("Tracking numbers(count={}) of user({}) were updated.", updatedTrackingNumbers.size(), username);

		return updatedTrackingNumbers;
	}

	@Override
	@RequestMapping(value = EndpointNames.READ_TRACKING_USER_ENDPOINT, method = RequestMethod.GET)
	public Collection<String> readTrackingNumbers(@PathVariable @Nonnull final String username) {
		log.info("Reading tracking numbers to user({}).", username);

		final Collection<String> updatedTrackingNumbers = userService.readTrackingNumbers(username);

		log.info("Tracking numbers(count={}) of user({}) were read.", updatedTrackingNumbers.size(), username);

		return updatedTrackingNumbers;
	}
}
