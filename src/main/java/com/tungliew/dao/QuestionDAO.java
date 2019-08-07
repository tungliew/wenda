package com.tungliew.dao;

import com.tungliew.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionDAO {  //与数据库进行交互
    String TABLE_NAME = "question";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question); //使用注解，把新问题添加到数据库

    @Select({"select from ", TABLE_NAME, " where id=#{id}"})
    Question getById(int id);

    List<Question> selectLatestQuestion(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);
    //用xml进行配置

}
