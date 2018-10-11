package cn.kiway.kiway_ai.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public void index(HttpServletResponse response){
	try {
	    response.sendRedirect("static/index.html");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}
