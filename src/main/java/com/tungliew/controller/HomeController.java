package com.tungliew.controller;

import com.tungliew.model.Question;
import com.tungliew.model.ViewObject;
import com.tungliew.service.QuestionService;
import com.tungliew.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(path = {"/","/index"},method = RequestMethod.GET)
    public String index(Model model){
        List<Question> questionList = questionService.selectLatestQuestion(0,0,10);
        //从数据库读取10个问题，作为entity显示
        List<ViewObject> vos = new ArrayList<>(); //ViewObject为map集合，list为集合的集合
        for(Question question:questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        return "index";
    }


    private List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<Question> questionList = questionService.selectLatestQuestion(userId,offset,limit);
        //从数据库读取10个问题，作为entity显示
        List<ViewObject> vos = new ArrayList<>(); //ViewObject为map集合，list为集合的集合
        for(Question question:questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"user/{userId"},method={RequestMethod.GET,RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId, Model model){
        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
    }
}
