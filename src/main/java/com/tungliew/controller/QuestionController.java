package com.tungliew.controller;

import com.tungliew.model.HostHolder;
import com.tungliew.model.Question;
import com.tungliew.service.QuestionService;
import com.tungliew.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    //添加一个问题
    @RequestMapping(path={"/question/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try{
            Question question = new Question(); //建立一个新的Question对象
            question.setTitle(title); //新添加问题的题目
            question.setContent(content); //新添加问题的内容
            question.setCreatedDate(new Date());  //新添加问题的创立时间
            if(hostHolder.getUser()==null){ //如果当前用户未登录
                question.setUserId(WendaUtil.ANONYMOUS_USERID); //匿名用户
            }else{
                question.setUserId(hostHolder.getUser().getId()); //将问题与用户id关联起来
            }

            if(questionService.addQuestion(question)>0){ //如果问题添加成功
                return WendaUtil.getJSONString(0);
            }

        }catch(Exception e){
            logger.error("增加题目失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }
}
