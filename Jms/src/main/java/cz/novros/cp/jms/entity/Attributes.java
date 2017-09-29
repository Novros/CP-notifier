package cz.novros.cp.jms.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
