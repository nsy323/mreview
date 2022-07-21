package org.nsy.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    private Long mno;

    private String title;

    /**
     * 영화이미지 List
     *
     * @Builder.Default : builder로 인스턴스 생성시 초기화할 값을 정할 수 있음(빌터패턴을 쓰는데 필드에 객체타입이 있다면 꼭 써주기)
     *
     * @Builder.Dafault를 쓰면 초기화 imageDTOList = [],
     *                    안쓰면 imageDTOList = null
     */
    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();

    private double avg; //영화평균 평점

    private int reviewCnt;  //영화 리뷰 수

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
