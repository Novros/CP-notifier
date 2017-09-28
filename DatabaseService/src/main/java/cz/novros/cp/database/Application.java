package cz.novros.cp.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import cz.novros.cp.database.entity.Attributes;
import cz.novros.cp.database.entity.Parcel;
import cz.novros.cp.database.service.DatabaseService;

@ComponentScan
@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(DatabaseService service) {
		return (args) -> {
			final List<Parcel> parcelList = new ArrayList<>();

			final Parcel parcel = new Parcel();
			parcel.setParcelTrackingNumber("Tracking");
			final Attributes attributes = new Attributes();
			attributes.setOriginCountry("Nekdo");
			parcel.setAttributes(attributes);
			parcel.setStates(new ArrayList<>());
			
			parcelList.add(parcel);

			service.saveParcels(parcelList);

			log.info("Parcels found with readAll():");
			log.info("-------------------------------");
			for (final Parcel parcel1 : service.readAll()) {
				log.info(parcel1.toString());
			}
			log.info("");

			log.info("Parcel found with tracking number(tracking):");
			log.info("--------------------------------");
			for (final Parcel parcel1 : service.findByTrackingNumber(new HashSet<String>() {{
				add("Tracking");
			}})) {
				log.info(parcel1.toString());
			}
			log.info("");
		};
	}
}
