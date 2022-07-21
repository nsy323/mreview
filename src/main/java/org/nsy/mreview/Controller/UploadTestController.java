package org.nsy.mreview.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class UploadTestController {

    /**
     * upload화면으로 이동
     */
    @GetMapping("/uploadEx")
    public void uploadEx(){
       log.info("uploadEx................");
    }
}
