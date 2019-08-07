package com.tungliew.service;

import com.tungliew.dao.QuestionDAO;
import com.tungliew.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public Question getById(int id) {
        return questionDAO.getById(id);
    }

    public List<Question> selectLatestQuestion(int a, int b, int c){
        return questionDAO.selectLatestQuestion(a,b,c);
    }

    public int addQuestion(Question question){
        //要严格控制用户发布的内容
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));  //html标签过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤
        return questionDAO.addQuestion(question)>0?question.getId():0;
    }
}
