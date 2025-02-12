package z9.second.domain.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import z9.second.domain.favorite.entity.FavoriteEntity;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    @Query("SELECT f FROM FavoriteEntity f WHERE f.name IN :names")
    List<FavoriteEntity> findByNameIn(@Param("names") List<String> names);

    @Query("SELECT f FROM FavoriteEntity f WHERE f.name = :name")
    Optional<FavoriteEntity> findByName(@Param("name") String name);
}
