package org.nsy.mreview.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nsy.mreview.dto.ReviewDTO;
import org.nsy.mreview.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰전체 List
     * @param mno
     * @return
     */
    @GetMapping("{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno){

        log.info("list.........................mno : " + mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    /**
     * 영화 리뷰 등록
     * @param reviewDTO
     * @return
     */
    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO reviewDTO){

        log.info("add Movie Review.....................");
        log.info("reviewDTO : " + reviewDTO);

        Long reviewnum = reviewService.register(reviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    /**
     * 영화 리뷰 수정
     * @param reviewnum
     * @param movieReviewDTO
     * @return
     */
    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable("reviewnum") Long reviewnum,
                                             @RequestBody ReviewDTO movieReviewDTO){

        log.info("mofify Movie Review.................");
        log.info("reviewDTO : " + movieReviewDTO);

        reviewService.modify(movieReviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    /**
     * 영화 리뷰 삭제
     * @param reviewnum
     * @return
     */
    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable("reviewnum") Long reviewnum){

        log.info("delete Movie Review.............");
        log.info("reviewnum : " + reviewnum);

        reviewService.delete(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }
}
