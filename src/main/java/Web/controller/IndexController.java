package Web.controller;

import Web.dto.MemberDto;
import Web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {


    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String main(){
        return "main";
    }
    @GetMapping("/member/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/member/login")
    public String login(){
        return "login";
    }

    @PostMapping("/member/signupcontoller")
    public String signupcontoller(MemberDto memberDto){
        indexService.signup(memberDto);
        return "redirect:/";
    }

}
