package com.tungliew.controller;

import com.tungliew.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController { //专门负责登录注册
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path={"/reg/"},method={RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next, //强制注册
                      @RequestParam(value="rememberme",defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map = userService.register(username,password);
            if(map.containsKey("ticket")){ //向数据库中添加ticket成功
                Cookie cookie = new Cookie("ticket",map.get("a_ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie); //把cookie添加到response中，浏览器添加cookie
                if(StringUtils.isBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch(Exception e){
            logger.error("注册异常"+e.getMessage());
            model.addAttribute("msg","服务器错误");
            return "login"; //重新返回登录界面
        }
    }

    @RequestMapping(path={"/relogin"},method = {RequestMethod.GET})
    public String reloginPage(Model model,
                              @RequestParam(value="next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }


    @RequestMapping(path={"/logout"},method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(path={"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam("rememberme") boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("a_ticket")){
                Cookie cookie = new Cookie("ticket",map.get("a_ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                if(StringUtils.isNoneBlank(next)){
                    return "redirect:"+next;
                }

                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }
}
