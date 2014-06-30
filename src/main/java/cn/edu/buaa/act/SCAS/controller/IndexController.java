
package cn.edu.buaa.act.SCAS.controller;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.buaa.act.SCAS.service.FileManageService;
import cn.edu.buaa.act.SCAS.po.ARINC653.Process;
@Controller
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private FileManageService fileManageService;
	
	@RequestMapping(value = "/", method= RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) throws JSONException{
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		String directory = fileManageService.getDirectory();
		logger.info("工程目录："+directory);
		view.addObject("directory", directory);
		
		return view;
	}
	
	@RequestMapping(value = "/showTaskModel",method=RequestMethod.POST)
	public ModelAndView showTaskModel(HttpServletRequest request, String filename) throws DocumentException{
		logger.info(filename);
		ModelAndView view = new ModelAndView();
		Process process = fileManageService.getProcess(filename);
		return view;
	}
}
