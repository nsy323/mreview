package org.nsy.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nsy.mreview.dto.MovieDTO;
import org.nsy.mreview.dto.PageRequestDTO;
import org.nsy.mreview.dto.PageResultDTO;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.MovieImage;
import org.nsy.mreview.repository.MovieImageRepository;
import org.nsy.mreview.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final MovieImageRepository movieImageRepository;

    /**
     * 영화 등록
     * @param movieDTO
     * @return
     */
    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        Movie movie = (Movie) entityMap.get("movie");

        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);    //영화등록

        //영화이미지 등록
        movieImageList.forEach(movieImage -> {
            movieImageRepository.save(movieImage);
        });

        return movie.getMno();
    }

    /**
     * 영화  List
     * @param requestDTO
     * @return
     */
    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie) arr[0],
                (List<MovieImage>)(Arrays.asList((MovieImage) arr[1])),
                (Double) arr[2],
                (Long) arr[3] )
        );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * 영화 상세보기
     * @param mno
     * @return
     */
    @Override
    public MovieDTO getMovie(Long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        Movie movie = (Movie) result.get(0)[0];     //영화(모든 row가 동일한 값)

        List<MovieImage> movieImageList = new ArrayList<>();    //영화의 이미지(영화의 이미지 개수만큼 MovieImage객체 필요

        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];

            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2]; //평균 평점(모든 row가 동일한 값)
        Long reviewCnt = (Long) result.get(0)[3];  //리뷰개수(모드 row가 동일한 값)

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }
}
