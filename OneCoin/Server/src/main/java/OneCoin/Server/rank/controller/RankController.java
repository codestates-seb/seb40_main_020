package OneCoin.Server.rank.controller;

import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.rank.dto.RankDto;
import OneCoin.Server.rank.entity.RankEntity;
import OneCoin.Server.rank.mapper.RankMapper;
import OneCoin.Server.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranks")
public class RankController {
    private final RankService rankService;
    private final RankMapper rankMapper;
    @GetMapping
    public ResponseEntity getRanks() {
        List<RankEntity> top10Roi = rankService.getTop10();
        RankDto rankDto = rankMapper.userRoisToRankDtos(top10Roi);
        return new ResponseEntity(rankDto, HttpStatus.OK);
    }
}
