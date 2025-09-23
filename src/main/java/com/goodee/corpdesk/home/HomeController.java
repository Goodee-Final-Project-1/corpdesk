package com.goodee.corpdesk.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "login";
	}

	@GetMapping("login/{msg}")
	public String login(@PathVariable String msg, Model model) {
		msg = msg.replaceAll("\\+", " ");
		model.addAttribute("msg", msg);
		return "login";
	}

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }
}
