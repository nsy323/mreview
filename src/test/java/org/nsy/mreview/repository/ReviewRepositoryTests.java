package org.nsy.mreview.repository;

import org.junit.jupiter.api.Test;
import org.nsy.mreview.entity.Member;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    ReviewRepository reviewRepository;

    /**
     * 200개의 Review 등록
     */
    @Test
    public void insertMovieReviews(){

        IntStream.rangeClosed(1,200).forEach(i -> {

            //영화번호
            Long mno = (long)(Math.random()*100) + 1;

            //리뷰어번호
            Long mid = ((long)Math.random()*100 + 1);

            Member member =  Member.builder().mid(mid).build();
            Movie movie = Movie.builder().mno(mno).build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(movie)
                    .grade((int)(Math.random()*5) + 1)  //1,2,3,4,5
                    .text("이영화에 대한 느낌..."+ i)
                    .build();

            reviewRepository.save(movieReview);
        });
    }

    @Test
    public void testGetMovieReviews(){

        Movie movie = Movie.builder().mno(81L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {
            System.out.print(movieReview.getReviewnum());
            System.out.print("\t" + movieReview.getGrade());
            System.out.print("\t" +movieReview.getText());
            System.out.print("\t" +movieReview.getMember().getEmail());
            System.out.println("---------------------------------------");
        });

    }
}
