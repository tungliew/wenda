package com.tungliew;

import com.tungliew.dao.QuestionDAO;
import com.tungliew.dao.UserDAO;
import com.tungliew.model.Question;
import com.tungliew.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	@Autowired
    UserDAO userDAO;

	@Autowired
	QuestionDAO questionDAO;

	@Test
	public void initDatabase() {
		Random random = new Random();
		for(int i=0;i<11;i++){
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i+1));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			//user.setPassword("tungliew");
			//userDAO.updatePassword(user);
			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("Title%d",i));
			question.setContent(String.format("Content%d",i));
			questionDAO.addQuestion(question);

		}

		System.out.print(questionDAO.selectLatestQuestion(3,4,5));
	}

}
