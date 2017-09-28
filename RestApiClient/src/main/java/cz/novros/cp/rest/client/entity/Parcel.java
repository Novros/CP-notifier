package cz.novros.cp.rest.client.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Parcel {

	@JsonProperty("id")
	String id;

	@JsonProperty("attributes")
	Attributes attributes;

	@JsonProperty("states")
	States states;
}
