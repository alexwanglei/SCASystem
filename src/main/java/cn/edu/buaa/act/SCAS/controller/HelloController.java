package cn.edu.buaa.act.SCAS.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/hello", method= RequestMethod.GET)
	public ModelAndView hello(HttpServletRequest request){
		return new ModelAndView("index");
	}
}
