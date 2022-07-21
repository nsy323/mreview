package org.nsy.mreview.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nsy.mreview.dto.MovieDTO;
import org.nsy.mreview.dto.PageRequestDTO;
import org.nsy.mreview.repository.MovieRepository;
import org.nsy.mreview.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    /**
     * 영화등록화면으로 이동
     */
    @GetMapping("/register")
    public void register(){
        log.info("register............");
    }

    /**
     * 영화 등록
     * @param movieDTO
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes){

        log.info("movieDTO : " + movieDTO);

        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    /**
     * 영화 목록 List
     * @param pageRequestDTO
     * @param model
     */
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("PageRequestDTO : " + pageRequestDTO);

        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    /**
     * 영화 상세보기
     * @param mno
     * @param requestDTO
     * @param model
     */
    @GetMapping(value= {"/read", "/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){

        log.info("mno : " + mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto", movieDTO);
    }
}
