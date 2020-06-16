package nchu.wuxi.xmeet.controller;

import nchu.wuxi.xmeet.entity.TCustomer;
import nchu.wuxi.xmeet.servlce.AccountService;
import nchu.wuxi.xmeet.utils.RestReturn;
import nchu.wuxi.xmeet.utils.StringReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther WuXi
 * @create 2020/4/4
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping("/hello")
    public Object hello(){
        return ("hello");
    }
    @RequestMapping(value = "/login",method=RequestMethod.POST)
    public Object hello(String phone, String password){
        TCustomer tCustomer = new TCustomer();
        tCustomer.setPhone(phone);
        tCustomer.setPassword(password);
        System.out.println(tCustomer.toString());
        return accountService.login(tCustomer);
    }
    @RequestMapping("/register")
    public RestReturn register(TCustomer user){
        System.out.println(user.toString());
        return accountService.register(user);
    }
    @RequestMapping("/loginWithoutPassword/{phone}")
    public RestReturn loginWithoutPassword(@PathVariable String phone){
        System.out.println("loginWithoutPassword"+phone);
        return accountService.loginWithoutPassword(phone);
    }


}
