package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	// favorite
	@Query(value = "SELECT * FROM favorites where product_id  = ? and user_id = ?;", nativeQuery = true)
	public Optional<Favorite> selectSaves(Long productId, Long userId);

	@Query(value = "SELECT * FROM favorites where user_id = ?;", nativeQuery = true)
	public List<Favorite> selectAllSaves(Long userId);

	@Query(value = "SELECT Count(favorite_id)  FROM favorites  where user_id = ?;", nativeQuery = true)
	public Integer selectCountSave(Long userId);

}
