package cz.novros.cp.web.rest;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.service.UserService;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class UserRestService extends AbstractRestClient implements UserService {

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
