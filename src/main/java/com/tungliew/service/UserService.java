package com.tungliew.service;

import com.tungliew.dao.LoginTicketDAO;
import com.tungliew.dao.UserDAO;
import com.tungliew.model.LoginTicket;
import com.tungliew.model.User;
import com.tungliew.util.WendaUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public Map<String,Object> register(String username,String password){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){  //对用户名进行检查，不能为空
            map.put("msg","用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)){  //对密码进行检查，不能为空
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username); //对提供的用户名进行查询，看数据库中是否存在

        if(user!=null){
            map.put("msg","用户名已被注册");
            return map;
        }

        //若在数据库中未查询到该用户名
        //检测密码强度
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5)); //创建salt值
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId()); //得到向数据库中添加的ticket
        map.put("ticket",ticket); //

        return map; //注册成功map为null
    }


    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }

       if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码不正确");
            return map;
       }

       String ticket = addLoginTicket(user.getId());
       map.put("a_ticket",ticket);
       map.put("userId",user.getId());
       return map;
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    private String addLoginTicket(int userId){ //向数据库中添加ticket
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date= new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1); //0 for login, 1 for logout
    }
}
