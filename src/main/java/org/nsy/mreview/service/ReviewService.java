package org.nsy.mreview.service;

import org.nsy.mreview.dto.ReviewDTO;
import org.nsy.mreview.entity.Member;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    /**
     * 영화의 모든 영화리뷰를 가져온다.
     * @param mno
     * @return
     */
    List<ReviewDTO> getListOfMovie(Long mno);

    /**
     * 영화 리뷰 등록
     * @param movieReviewDTO
     * @return
     */
    Long register(ReviewDTO movieReviewDTO);

    /**
     * 특정한 영화리뷰 수정
     * @param movieReviewDTO
     */
    void modify(ReviewDTO movieReviewDTO);

    /**
     * 특정한 영화리뷰 삭제
     * @param reviewnum
     */
    void delete(Long reviewnum);

    /**
     * dto를 entity로 변환하여 반환
     * @param movieReviewDTO
     * @return
     */
    default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Review movieReview = Review.builder()
                .reviewnum(movieReviewDTO.getMno())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();
        return movieReview;
    }

    /**
     * entity를 dto로 변환하여 반환
     * @param movieReview
     * @return
     */
    default ReviewDTO entityToDto(Review movieReview){
        ReviewDTO movieReviewDTO = ReviewDTO.builder()
                .reviewnum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickname(movieReview.getMember().getNickname())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return movieReviewDTO;
    }
}
