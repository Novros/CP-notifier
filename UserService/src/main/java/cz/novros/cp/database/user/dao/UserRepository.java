package cz.novros.cp.database.user.dao;

import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;

import cz.novros.cp.database.user.entity.User;

public interface UserRepository extends CrudRepository<User, String> {
	
	User findByEmail(@NonNull final String email);

	User findByEmailAndPassword(@NonNull final String email, @NonNull final String password);
}
