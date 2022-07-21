package org.nsy.mreview.repository;

import org.nsy.mreview.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * 영화 List (영화, 영화이미지, 영화리뷰의 갯수) - 페이지 처리
     * @param pageable
     * @return
     */
    /*
    @Query("select m, mi, count(r) "
            + "from Movie m "
            + "left join MovieImage mi on mi.movie = m "
            + "and mi.inum = (select max(mi2.inum) from MovieImage mi2 where mi2.movie = m) "
            + "left join Review r on r.movie = m "
            + "group by m ")    //mi.inum의 최대값이 나오는 쿼리 */
    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct  r) "
            + "from Movie m "
            + "left outer join Review r on r.movie = m "
            + "left outer join MovieImage mi on mi.movie = m "
            + "group by m")  //mi.inum의 최소값이 나오는 쿼리
    Page<Object[]> getListPage(Pageable pageable);

    /**
     *  하나의 영화에 img List, 영화평점, 리뷰개수 조회
     * @param mno
     * @return
     */
    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) "
            + "from Movie m "
            + "left outer join MovieImage mi on mi.movie = m "
            + "left outer join Review r on r.movie = m "
            + "where m.mno = :mno "
            + "group by mi ")
    List<Object[]> getMovieWithAll(Long mno);


}
