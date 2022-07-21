package org.nsy.mreview.repository;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.jupiter.api.Test;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.MovieImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Movie(영화)와 MovieImage(영화이미지)를 추가하는 테스트코드
 */
@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

    /**
     * 영화 insert, 영화이미지 insert
     */
    @Commit
    @Transactional
    @Test
    public void insertMovies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Movie movie = Movie.builder()
                            .title("Movie......" + i)
                            .build();

            System.out.println("---------------------------------");

            movieRepository.save(movie);

            int count = (int)(Math.random() * 5) + 1;     //1,2,3,4

            for(int j = 0; j < count; j++){
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg")
                        .build();

                imageRepository.save(movieImage);
            }
            System.out.println("==================================");
        });
    }

    /**
     * 영화 List
     */
    @Test
    public void testListPage(){

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));

        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        for(Object[] objects : result.getContent()){
            System.out.println(Arrays.toString(objects));
        }
    }

    /**
     * 영화의 img List
     */
    @Test
    public void testGetMovieWithAll(){

        List<Object[]> result = movieRepository.getMovieWithAll(94L);

        //System.out.println(result);

        for(Object[] objects : result){
            System.out.println(Arrays.toString(objects));
        }

    }


}
