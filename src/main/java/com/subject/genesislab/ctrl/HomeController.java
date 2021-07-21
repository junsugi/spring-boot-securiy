package com.subject.genesislab.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/index")
    public ModelAndView indexPage(ModelAndView mv){
        logger.debug(" ==> Call indexPage method");
        mv.addObject("title", "로그인 페이지");
        mv.setViewName("/login");

        return mv;
    }
}
