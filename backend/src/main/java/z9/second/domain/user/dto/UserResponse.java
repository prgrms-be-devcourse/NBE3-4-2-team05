package z9.second.domain.user.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import z9.second.model.user.User;

public class UserResponse {

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserInfo {
        private final String nickname;
        private final String type;
        private final String role;
        private final String createdAt;
        private final List<String> favorite;

        public static UserInfo of(User user, List<String> favorite) {
            String formattedDate = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return UserInfo
                    .builder()
                    .nickname(user.getNickname())
                    .type(user.getType().getValue())
                    .role(user.getRole().getValue())
                    .createdAt(formattedDate)
                    .favorite(favorite)
                    .build();
        }
    }
}
