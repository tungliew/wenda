package com.tungliew.controller;

import com.tungliew.model.User;
import com.tungliew.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController { //访问首页
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    WendaService wendaService;

    @RequestMapping(path={"/","/index"})
    @ResponseBody  //返回的是字符串，不是模板
    public String index(HttpSession httpSession){
        logger.info("Visit Home.");
        return wendaService.getMessage(2)+" Hello,tungliew! " + httpSession.getAttribute("message") ; //从跳转页面带来消息
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"}) //访问某个用户
    @ResponseBody  //返回的是字符串，不是模板
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type",defaultValue = "8") int type,
                          @RequestParam(value = "key",defaultValue = "kwonjiyong",required = false) String key){
        return String.format("Profile of %s,%d. ",groupId,userId)+
                String.format("The type is %d, the key is %s.",type,key);
    }

    @RequestMapping(path={"/vm"},method = {RequestMethod.GET}) //通过模板进行渲染
    public String template(Model model){ //Model把数据添加到模板中
        model.addAttribute("name","tungliew");

        List<String> colors = Arrays.asList(new String[]{"red","green","blue"});
        model.addAttribute("colors",colors);

        Map<String,String> maps = new HashMap<>();
        for(int i=0;i<5;i++){
            maps.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("maps",maps);

        model.addAttribute("user",new User("tungliew"));
        return "home";
    }

    @RequestMapping(path={"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionID){
        StringBuilder sb = new StringBuilder();

        sb.append("CookieValue: "+sessionID+"<br>"); //直接读取session值

        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie: httpServletRequest.getCookies()){
                sb.append("Cookie: "+cookie.getName()+"; Value: "+cookie.getValue()+"<br>");
            }
        }

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+": "+httpServletRequest.getHeader(name)+"<br>");
        }
        sb.append(httpServletRequest.getMethod()+"<br>");
        sb.append(httpServletRequest.getQueryString()+"<br>");
        sb.append(httpServletRequest.getPathInfo()+"<br>");
        sb.append(httpServletRequest.getRequestURI()+"<br>");

        httpServletResponse.addHeader("nowcoderId","hello");
        httpServletResponse.addCookie(new Cookie("username","nowcoder"));
        return sb.toString();
    }

    /***@RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,
                           HttpSession httpSession){
        httpSession.setAttribute("message","jump from redirect");
        //return "redirect:/vm"; //重定向
        return "redirect:/";
    }

    ***/

    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.GET})  //重定向，301
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession){
        httpSession.setAttribute("message","jump from redirect");
        RedirectView red = new RedirectView("/",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path={"/admin"},method = RequestMethod.GET)
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if(key.equals("admin")){
            return "Hello,admin!";
        }
        throw new IllegalArgumentException("No!");  //处理异常
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "Error: "+e.getMessage();
    }

}
