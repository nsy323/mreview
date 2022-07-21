package org.nsy.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewnum; //review num

    private Long mno;   //Movie mno

    private Long mid;   //Member mid

    private String nickname;    //Member nickname

    private String email;       //Member email

    private int grade;      //평점

    private String text;    //리뷰내용

    private LocalDateTime regDate, modDate; //등록일, 수정일
}
