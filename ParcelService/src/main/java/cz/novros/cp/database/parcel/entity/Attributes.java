package cz.novros.cp.database.parcel.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attributes {

	String parcelType;

	BigDecimal weight;

	String currency;

	int phoneType;

	String phoneName;

	String phoneNumber;

	BigDecimal cashOnDelivery;

	int quantity;

	Date storedTo;

	int storedDays;

	String originCountry;

	String targetCountry;

	Date deliveryDate;

	Date deliveryFrom;

	Date deliveryTo;
}
