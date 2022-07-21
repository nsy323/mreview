package org.nsy.mreview.service;

import org.nsy.mreview.dto.MovieDTO;
import org.nsy.mreview.dto.MovieImageDTO;
import org.nsy.mreview.dto.PageRequestDTO;
import org.nsy.mreview.dto.PageResultDTO;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.MovieImage;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    /**
     * 영화 등록
     * @param movieDTO
     * @return
     */
    Long register(MovieDTO movieDTO);

    /**
     * MovieDTO를 entity로 변환하여 반환
     *
     * @param movieDTO
     * @return
     */
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){

        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0){

            List<MovieImage> movieImageList = imageDTOList.stream()
                    .map(movieImageDTO -> {
                        MovieImage movieImage = MovieImage.builder()
                                .path(movieImageDTO.getPath())
                                .imgName(movieImageDTO.getImgName())
                                .uuid(movieImageDTO.getUuid())
                                .movie(movie)
                                .build();
                        return movieImage;
                    }).collect(Collectors.toList());

            entityMap.put("imgList", movieImageList);
        }
        return entityMap;
    }

    /**
     * 영화 List 조회
     * @param requestDTO
     * @return
     */
    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);

    /**
     * eitity를 dto로 변환
     *
     * @param movie 영화
     * @param movieImages 영화이미지
     * @param avg   평균평점
     * @param reviewCnt 리뷰수
     * @return
     */
    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt){
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        List<MovieImageDTO> movieImageDTOList = movieImages.stream()
                .map(movieImage -> {
                    return MovieImageDTO.builder()
                            .imgName(movieImage.getImgName())
                            .path(movieImage.getPath())
                            .uuid(movieImage.getUuid())
                            .build();
                }).collect(Collectors.toList());

        movieDTO.setImageDTOList(movieImageDTOList);        //이미지List

        movieDTO.setAvg(avg);       //평균평점

        movieDTO.setReviewCnt(reviewCnt.intValue());    //리뷰 수

        return movieDTO;
    }

    /**
     * 영화 상세보기
     * @param mno
     * @return
     */
    MovieDTO getMovie(Long mno);
}
