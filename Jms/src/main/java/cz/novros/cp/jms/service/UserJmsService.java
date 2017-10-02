package cz.novros.cp.jms.service;

import javax.annotation.Nonnull;

import cz.novros.cp.jms.message.user.AddTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.ReadTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.RemoveTrackingNumbersMessage;

public interface UserJmsService {

	void addTrackingNumbers(@Nonnull final AddTrackingNumbersMessage message);

	void removeTrackingNumbers(@Nonnull final RemoveTrackingNumbersMessage message);

	void readTrackingNumbers(@Nonnull final ReadTrackingNumbersMessage message);
}
