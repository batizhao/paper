package io.github.batizhao.web;

import io.github.batizhao.domain.Account;
import io.github.batizhao.domain.Course;
import io.github.batizhao.domain.Score;
import io.github.batizhao.dto.ScoreDto;
import io.github.batizhao.service.AccountService;
import io.github.batizhao.service.CourseService;
import io.github.batizhao.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/29
 */
@Controller
@RequestMapping("score")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @Autowired
    AccountService accountService;

    @Autowired
    CourseService courseService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        Iterable<Score> scores = scoreService.findByAccountId();
        model.addAttribute("scores", scores);
        return "score/index";
    }

    @RequestMapping(value = "sort", method = RequestMethod.GET)
    public String sort(Model model){
        List<ScoreDto> scores = scoreService.sumRanking();
        model.addAttribute("scores", scores);
        return "score/sort";
    }

    @RequestMapping(value = "manage", method = RequestMethod.GET)
    public String manage(Model model){
        Iterable<Score> scores = scoreService.finAll();
        model.addAttribute("scores", scores);
        return "score/manage";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model){
        Score score = new Score();
        Iterable<Account> accounts = accountService.findByRoles("student");
        Iterable<Course> courses = courseService.findAll();

        model.addAttribute("score", score);
        model.addAttribute("accounts", accounts);
        model.addAttribute("courses", courses);
        return "score/create-modal";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Score save(@ModelAttribute("score") Score score){
        return scoreService.save(score);
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model){
        Score score = scoreService.findOne(id);
        model.addAttribute("score", score);
        return "score/create-modal";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Score update(@ModelAttribute("score") Score score){
        return scoreService.update(score);
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public boolean delete(@PathVariable Long id){
        scoreService.delete(id);
        return true;
    }
}
