package z9.second.model.userfavorite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {

    void deleteByUserId(Long userId);
}
