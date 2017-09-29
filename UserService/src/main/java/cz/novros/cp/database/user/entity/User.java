package cz.novros.cp.database.user.entity;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

	@Id
	String email;
	String password;

	@ElementCollection(fetch = FetchType.EAGER)
	Set<String> trackingNumbers;
}
