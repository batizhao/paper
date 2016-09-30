package io.github.batizhao.web;

import io.github.batizhao.domain.Score;
import io.github.batizhao.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author batizhao
 * @since 2016/9/29
 */
@Controller
@RequestMapping("score")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        Iterable<Score> scores = scoreService.findByAccountId();
        model.addAttribute("scores", scores);
        return "score/index";
    }
}
