package z9.second.domain.search.dto;

import lombok.Builder;
import lombok.Getter;
import z9.second.domain.classes.entity.ClassEntity;

@Getter
@Builder
public class SearchResponseDto {
    private Long id;
    private String name;
    private String favorite;
    private String description;
    private int participantCount;
    private Long masterId;

    public static SearchResponseDto from(ClassEntity entity) {
        return SearchResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .favorite(entity.getFavorite())
                .description(entity.getDescription())
                .participantCount(entity.getUsers().size())
                .masterId(entity.getMasterId())
                .build();
    }
}
