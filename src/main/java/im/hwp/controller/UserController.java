package im.hwp.controller;

import im.hwp.entity.User;
import im.hwp.service.UserService;
import im.hwp.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "login", method = {RequestMethod.GET,RequestMethod.POST})
    public String login(String username, String password, String verifyCode, HttpSession session){
        //获取主体对象
        String code = (String) session.getAttribute("code");
        System.out.println("用户传上来的code：" + verifyCode + " 系统存储的code：" + code);
        try {
            if(code.equalsIgnoreCase(verifyCode)){
                session.removeAttribute("code");
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(username,password));
                return "redirect:/user/index";
            }else{
                throw new RuntimeException("图片验证码错误");
            }
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误");
        }catch(IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "redirect:/user/loginview";

    }

    @RequestMapping("loginview")
    public String login(){
        return "login";
    }

    @RequestMapping("registerview")
    public String register(){
        return "register";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }
    @RequestMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return  "redirect:/user/loginview";
    }

    /**
     * 用户注册
     * @param
     * @return
     */
    @RequestMapping("register")
    public String register(User user){
        try {
            userService.register(user);
            return "redirect:/login.html";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register.html";
        }
    }

    @RequestMapping("update")
    @RequiresPermissions("user:update:*")
    public String updateInfo(){
        try{
            System.out.println("用户获得权限执行");
        }catch (Exception e){
            System.out.println(e);
        }
        return "redirect:/index.html";
    }

    @RequestMapping("getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        //生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //验证码存入session
        session.setAttribute("code",code);
        //验证码生成图片
        ServletOutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220,60,os, code);
    }
}
