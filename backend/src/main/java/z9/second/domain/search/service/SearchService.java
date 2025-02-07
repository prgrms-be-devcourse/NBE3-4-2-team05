package z9.second.domain.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.favorite.repository.FavoriteRepository;
import z9.second.domain.search.SortBy;
import z9.second.domain.search.dto.SearchResponseDto;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.model.userfavorite.UserFavoriteRepository;

import java.util.List;
import java.util.stream.Collectors;

import static z9.second.domain.search.SortBy.CREATED_DESC;
import static z9.second.domain.search.SortBy.FAVORITE;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ClassRepository classRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    @Transactional(readOnly = true)
    public List<SearchResponseDto> searchClasses(SortBy sortBy, Long userId) {
        // 로그인 상태에 따른 기본 정렬 설정
        if (sortBy == null) {
            sortBy = (userId != null) ? FAVORITE : CREATED_DESC;
        }

        List<ClassEntity> classes;
        try {
            switch (sortBy) {
                case FAVORITE -> {
                    if (userId != null) {
                        // 로그인: 사용자의 관심사와 일치하는 모임만 조회
                        List<String> userFavorites = userFavoriteRepository.findFavoriteNamesByUserId(userId);
                        // 데이터 확인을 위한 로깅 추가
                        log.info("userId: {}", userId);
                        log.info("User Favorites: {}", userFavorites);

                        // ClassEntity의 favorite 값들도 확인
                        List<String> allClassFavorites = classRepository.findAll().stream()
                                .map(ClassEntity::getFavorite)
                                .distinct()
                                .collect(Collectors.toList());
                        log.info("All Class Favorites: {}", allClassFavorites);

                        classes = classRepository.findByUserFavorites(userFavorites);
                        log.info("Found Classes Size: {}", classes.size());
                    } else {
                        // 비로그인: 전체 모임을 관심사별, 가나다순 정렬
                        classes = classRepository.findByFavorites();
                    }
                }
                case NAME_ASC -> classes = classRepository.findAllByOrderByName();
                case PARTICIPANT_DESC -> classes = classRepository.findByParticipantSort();
                case CREATED_ASC -> classes = classRepository.findAllByOrderByCreatedAtAsc();
                case CREATED_DESC -> classes = classRepository.findAllByOrderByCreatedAtDesc();
                default -> classes = classRepository.findAllByOrderByCreatedAtDesc();
            }

            // 데이터 확인을 위한 로깅 추가
            List<SearchResponseDto> result = classes.stream()
                    .map(SearchResponseDto::from)
                    .collect(Collectors.toList());

            log.info("Final result size: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("Failed to search classes", e);
            throw new CustomException(ErrorCode.CLASS_READ_FAILED);
        }
    }
}
