package cz.novros.cp.web.view;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.common.CommonConstants;
import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.common.service.UserService;
import cz.novros.cp.web.view.entity.TrackingNumbersForm;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultController {

	UserService userService;
	ParcelService parcelService;

	@Autowired
	public DefaultController(@Nonnull final UserService userService, @Nonnull final ParcelService parcelService) {
		this.userService = userService;
		this.parcelService = parcelService;
	}

	@GetMapping(value = {"/", "/home"})
	public String home(final Model model) {
		model.addAttribute("parcels", readParcelsForUser());
		return "home";
	}

	@GetMapping("/tracking-numbers")
	public String trackingNumbers(@Nonnull final Model model) {
		return displayTrackingNumbers(model, null);
	}

	@PostMapping("/add-tracking")
	public String addTrackingNumbers(@ModelAttribute @Nullable final TrackingNumbersForm trackingNumbers, @Nonnull final Model model) {
		if (trackingNumbers != null && !trackingNumbers.getTrackingNumbersCollection().isEmpty()) {
			return displayTrackingNumbers(model, userService.addTrackingNumbers(getLoggedUsername(), trackingNumbers.getTrackingNumbersCollection()));
		} else {
			return trackingNumbers(model);
		}
	}

	@GetMapping("/remove-tracking") // FIXME - Maybe POST?
	public String removeTrackingNumbers(@RequestParam @Nullable final String trackingNumbers, @Nonnull final Model model) {
		if (trackingNumbers != null && !trackingNumbers.isEmpty()) {
			return displayTrackingNumbers(model, userService.removeTrackingNumbers(getLoggedUsername(), Arrays.asList(trackingNumbers.split(CommonConstants.TRACKING_NUMBER_DELIMITER))));
		} else {
			return trackingNumbers(model);
		}
	}

	@GetMapping("/tracked-parcels")
	public String tracking(@Nonnull final Model model) {
		model.addAttribute("parcels", readParcelsForUser());
		return "tracked_parcels";
	}

	private String displayTrackingNumbers(@Nonnull final Model model, @Nullable final Collection<String> trackingNumbers) {
		model.addAttribute("trackingNumbers", trackingNumbers == null ? userService.readTrackingNumbers(getLoggedUsername()) : trackingNumbers);
		model.addAttribute("formTrackingNumbers", new TrackingNumbersForm());
		return "tracking_numbers";
	}

	private Collection<Parcel> readParcelsForUser() {
		final Collection<String> trackingNumbers = userService.readTrackingNumbers(getLoggedUsername());
		return parcelService.readParcels(trackingNumbers);
	}

	private String getLoggedUsername() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user = (User) auth.getPrincipal();
		return user.getUsername();
	}
}
