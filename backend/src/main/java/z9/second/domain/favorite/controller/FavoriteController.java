package z9.second.domain.favorite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.favorite.dto.FavoriteResponse;
import z9.second.domain.favorite.service.FavoriteService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
@Tag(name = "Favorite Controller", description = "관심사 컨트롤러")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    @Operation(summary = "관심사 조회")
    public BaseResponse<List<FavoriteResponse.ResponseData>> getFavorites() {
        List<FavoriteResponse.ResponseData> favoriteList = favoriteService.findAll();

        return BaseResponse.ok(SuccessCode.SUCCESS, favoriteList);
    }
}
