package z9.second.domain.classes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import z9.second.domain.classes.entity.ClassEntity;

public class ClassResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseData {
        private final Long id;
        private final String name;
        private final String favorite;
        private final String description;

        public static ResponseData from(ClassEntity classes) {
            return ResponseData
                    .builder()
                    .id(classes.getId())
                    .name(classes.getName())
                    .favorite(classes.getFavorite())
                    .description(classes.getDescription())
                    .build();
        }
    }
}
