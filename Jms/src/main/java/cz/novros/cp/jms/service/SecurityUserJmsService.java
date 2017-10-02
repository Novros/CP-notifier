package cz.novros.cp.jms.service;

import javax.annotation.Nonnull;

import cz.novros.cp.jms.message.user.LoginUserMessage;
import cz.novros.cp.jms.message.user.RegisterUserMessage;

public interface SecurityUserJmsService {

	void registerUser(@Nonnull final RegisterUserMessage message);

	void loginUser(@Nonnull final LoginUserMessage message);
}
