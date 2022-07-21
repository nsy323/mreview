package org.nsy.mreview.repository;

import org.junit.jupiter.api.Test;
import org.nsy.mreview.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * 100명의 회원 등록
     */
    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("n"+i+"@nsy.org")
                    .pw("1111")
                    .nickname("reviewer"+i)
                    .build();

            memberRepository.save(member);
        });
    }

    /**
     * 회원 삭제
     */
    @Commit
    @Transactional
    @Test
    public void testDeleteMember(){

        Long mid = 2L;

        Member member = Member.builder().mid(mid).build();

        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }

}
