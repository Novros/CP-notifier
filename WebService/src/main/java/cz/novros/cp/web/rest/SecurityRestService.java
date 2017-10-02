package cz.novros.cp.web.rest;

import javax.annotation.Nonnull;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class SecurityRestService extends AbstractRestClient implements SecurityUserService {
	@Override
	public boolean registerUser(@Nonnull final String username, @Nonnull final String password) {
		return false;
	}

	@Override
	public boolean loginUser(@Nonnull final String username, @Nonnull final String password) {
		return false;
	}
}
