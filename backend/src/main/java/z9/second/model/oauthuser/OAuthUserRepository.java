package z9.second.model.oauthuser;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {

    Optional<OAuthUser> findByProviderAndUid(OAuthProvider provider, String uid);
}
