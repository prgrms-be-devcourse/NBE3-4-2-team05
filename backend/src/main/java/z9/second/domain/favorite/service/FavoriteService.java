package z9.second.domain.favorite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import z9.second.domain.favorite.dto.FavoriteResponse;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.favorite.repository.FavoriteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public List<FavoriteResponse.ResponseData> findAll() {
        List<FavoriteEntity> favoriteList = favoriteRepository.findAll();

        return favoriteList.stream()
                .map(FavoriteResponse.ResponseData::from)
                .toList();
    }
}
