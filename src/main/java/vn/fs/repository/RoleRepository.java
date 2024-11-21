package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);

	@Query(value = "SELECT r.* FROM role r WHERE r.id IN "
			+ "(SELECT MIN(r2.id) FROM role r2 WHERE r2.name IN :names GROUP BY r2.name) ", nativeQuery = true)
	List<Role> findAllByNameIn(List<String> names);
	
	@Query(value = "SELECT r.* FROM role r WHERE r.id IN "
			+ "(SELECT MIN(r2.id) FROM role r2 GROUP BY r2.name) ", nativeQuery = true)
	List<Role> findAllNotDuplicate();
}
