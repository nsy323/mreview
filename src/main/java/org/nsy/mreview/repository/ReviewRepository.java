package org.nsy.mreview.repository;

import org.nsy.mreview.entity.Member;
import org.nsy.mreview.entity.Movie;
import org.nsy.mreview.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * @EntityGraph는 연관관계에 있는 데이터 조회시 Fetch속성같이 Lazy로 돼있을 경우
     * Eager로 일시적으로 변경하여 조회하여
     * 엔티티의 특정한 속성을 같이 로딩하도록 표시
     *
     * - attributePaths : 로딩설정을 변경하고 싶은 속성의 이름을 배열로 명시
     * - type : 어떤 방식으로 적용할 것인지 설정
     * - EntityGraph.EntityGraphType.FETCH : attributePaths에 명시한 속성은 Eager, 나머지는 Lazy
     * - EntityGraph.EntityGraphType.LOAD : attributesPaths에 명시한 속성은 Eager, 나머지는 엔티티 클레스에 명시되거나 기본방식으로 처리
     *
     *  영화리뷰 조회
     *
     * @param movie
     * @return
     */
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    /**
     * 특정회원 삭제(review테이블 삭제 후 m_member테이블 삭제)
     * @param member
     */
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);
}
