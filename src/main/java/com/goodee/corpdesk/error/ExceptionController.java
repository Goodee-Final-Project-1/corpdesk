package com.goodee.corpdesk.error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("error/**")
public class ExceptionController {

    @GetMapping("{errorCode}")
    public String handleError(@PathVariable("errorCode") String errorCode, Model model){

        model.addAttribute("errorCode", errorCode);

        return "error/error_page";

    }

}
