package z9.second.domain.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z9.second.domain.favorite.entity.FavoriteEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
}
