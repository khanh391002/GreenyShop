package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
	Optional<User> findByEmailAndStatusIsTrue(String email);
	
	List<User> findAllByStatusIsTrue();

	@Query(value = "SELECT u.* FROM greeny_shop.user u "
			+ "LEFT JOIN greeny_shop.users_roles ur ON u.user_id = ur.user_id "
			+ "LEFT JOIN greeny_shop.role r ON ur.role_id = r.id "
			+ "WHERE r.name = 'ROLE_ADMIN' AND u.status = '1' ", nativeQuery = true)
	List<User> findAllUserIsAdmin();
	
}
