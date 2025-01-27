package z9.second.domain.classes.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.domain.classes.entity.ClassEntity;

public class ClassRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestData {
        @Size(min = 3, message = "제목은 3글자 이상이어야 합니다.")
        private String name;

        @Size(min = 1, message = "관심사는 1가지를 입력하셔야 합니다.")
        private String favorite;

        @Size(min = 10, message = "내용은 10글자 이상이어야 합니다.")
        private String description;

        public static ClassEntity from(RequestData requestData) {
            return ClassEntity
                           .builder()
                           .name(requestData.getName())
                           .favorite(requestData.getFavorite())
                           .description(requestData.getDescription())
                           .build();
        }
    }
}
