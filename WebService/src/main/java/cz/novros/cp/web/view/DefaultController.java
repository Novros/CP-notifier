package cz.novros.cp.web.view;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.entity.Parcel;
import cz.novros.cp.web.service.ParcelService;
import cz.novros.cp.web.service.UserService;

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

	@GetMapping("/add-tracking")
	public String addTracking(@Nonnull final Model model) {
		model.addAttribute("trackingNumbers", userService.readTrackingNumbers("a")); // FIXME
		return "add_tracking";
	}

	@GetMapping("/tracking")
	public String tracking(@Nonnull final Model model) {
		model.addAttribute("parcels", readParcelsForUser());
		return "tracking";
	}

	private Collection<Parcel> readParcelsForUser() {
//		final Collection<String> trackingNumbers = userService.readTrackingNumbers(getLoggedUsername()); // FIXME
		return parcelService.readParcels(ImmutableList.of());
	}

	private String getLoggedUsername() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user = (User) auth.getPrincipal();
		return user.getUsername();
	}
}
