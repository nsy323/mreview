package org.nsy.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nsy.mreview.dto.ReviewDTO;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.Review;
import org.nsy.mreview.repository.MovieRepository;
import org.nsy.mreview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 List 조회
     * @param mno
     * @return
     */
    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) {

        Movie movie = Movie.builder().mno(mno).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        return result.stream().map(movieReview -> (
            entityToDto(movieReview))
        ).collect(Collectors.toList());
    }

    /**
     * 영화리뷰 등록
     * @param movieReviewDTO
     * @return
     */
    @Override
    public Long register(ReviewDTO movieReviewDTO) {

        Review movieReview = dtoToEntity(movieReviewDTO);

        reviewRepository.save(movieReview); //저장

        return movieReview.getReviewnum();
    }

    /**
     * 영화리뷰 수정
     * @param movieReviewDTO
     */
    @Override
    public void modify(ReviewDTO movieReviewDTO) {

        Optional<Review> result = reviewRepository.findById(movieReviewDTO.getReviewnum());

        if(result.isPresent()){
            Review movieReview = result.get();

            movieReview.changeGrade(movieReviewDTO.getGrade());
            movieReview.changeText(movieReviewDTO.getText());

            reviewRepository.save(movieReview);
        }
    }

    /**
     * 영화리뷰 삭제
     * @param reviewnum
     */
    @Override
    public void delete(Long reviewnum) {
        reviewRepository.deleteById(reviewnum);
    }
}
