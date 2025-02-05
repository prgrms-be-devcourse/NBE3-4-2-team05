package z9.second.domain.classes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import z9.second.domain.classes.entity.ClassEntity;

public class ClassResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ClassResponseData {
        private final Long id;
        private final String name;
        private final String favorite;
        private final String description;

        public static ClassResponseData from(ClassEntity classes) {
            return ClassResponseData
                    .builder()
                    .id(classes.getId())
                    .name(classes.getName())
                    .favorite(classes.getFavorite())
                    .description(classes.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class EntryResponseData {
        private final String name;
        private final String favorite;
        private final String description;

        public static EntryResponseData from(ClassEntity classes) {
            return EntryResponseData.builder()
                   .name(classes.getName())
                   .favorite(classes.getFavorite())
                   .description(classes.getDescription())
                   .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class JoinResponseData {
        private final Long id;
        private final String name;

        public static JoinResponseData from(ClassEntity classes) {
            return JoinResponseData
                    .builder()
                    .id(classes.getId())
                    .name(classes.getName())
                    .build();
        }
    }
}
