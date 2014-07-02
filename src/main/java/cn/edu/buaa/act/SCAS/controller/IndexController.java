
package cn.edu.buaa.act.SCAS.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import cn.edu.buaa.act.SCAS.service.CodeGenerationService;
import cn.edu.buaa.act.SCAS.service.FileManageService;
import cn.edu.buaa.act.SCAS.po.ARINC653.Process;
@Controller
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private FileManageService fileManageService;
	@Autowired
	private CodeGenerationService codeGenerationService;
	
	@RequestMapping(value = "/", method= RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) throws JSONException{
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		String directory = fileManageService.getDirectory();
		logger.info("工程目录："+directory);
		view.addObject("directory", directory);
		
		return view;
	}
	
	@RequestMapping(value = "/showTaskModel",method=RequestMethod.GET)
	public ModelAndView showTaskModel(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("task");
		Process process = fileManageService.getProcess(filename);
		mav.addObject("process", process);
		
		mav.addObject("filename", "\""+filename+"\"");
//		response.getWriter().println("hello");
		return mav;
	}
	
	@RequestMapping(value = "/saveTaskByXml", method=RequestMethod.POST)
	public void saveTaskByXml(HttpServletRequest request,HttpServletResponse response, String taskXml,String filename) throws IOException{
		logger.info(filename);
		logger.info(taskXml);
		fileManageService.saveTaskByXml(taskXml, filename);
		
	}
	
	@RequestMapping(value="/generateCCode",method=RequestMethod.POST)
	public void generateCCode(HttpServletRequest request,HttpServletResponse response, String filename){
		logger.info(filename);
		
	}
}
