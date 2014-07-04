
package cn.edu.buaa.act.SCAS.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import cn.edu.buaa.act.SCAS.po.ARINC653.Module;
import cn.edu.buaa.act.SCAS.po.ARINC653.Partition;
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
	
	@RequestMapping(value = "/refreshDirectory", method= RequestMethod.GET)
	public ModelAndView refreshDirectory(HttpServletRequest request) throws JSONException{
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map attributes = new HashMap();
		
		String directory = fileManageService.getDirectory();
		attributes.put("directory", directory);
		view.setAttributesMap(attributes);
		
		logger.info("刷新工程目录："+directory);
		return new ModelAndView(view);
	}
	
	@RequestMapping(value = "/showModuleModel",method=RequestMethod.GET)
	public ModelAndView showModuleModel(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		
		ModelAndView mav = new ModelAndView("module");
		Module module = fileManageService.getModule(filename);
		mav.addObject("module", module);
		
		mav.addObject("filename", "\""+filename+"\"");
//		response.getWriter().println("hello");
		return mav;
	}
	
	@RequestMapping(value = "/showPartitionModel",method=RequestMethod.GET)
	public ModelAndView showPartitionModel(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		
		ModelAndView mav = new ModelAndView("partition");
		Partition partition = fileManageService.getPartition(filename);
		mav.addObject("partition", partition);
		
		mav.addObject("filename", "\""+filename+"\"");
//		response.getWriter().println("hello");
		return mav;
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
	
	@RequestMapping(value = "/showCCode",method=RequestMethod.GET)
	public ModelAndView showCCode(HttpServletRequest request, String filename) throws IOException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("ccode");
		String code = fileManageService.getCCode(filename);
		mav.addObject("ccode",code);
		mav.addObject("filename","\""+filename+"\"");
		return mav;
	}
	
	@RequestMapping(value="/generateCCode",method=RequestMethod.POST)
	public void generateCCode(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		Module module = fileManageService.getModule(filename);
		codeGenerationService.generateCCode(module, filename);
		
	}
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public void test(HttpServletRequest request,HttpServletResponse response) throws DocumentException, IOException{
		 String[] arr = request.getParameterValues("Bike");
		 if(arr == null){
			 logger.info("aaaa");
		 }
		 for(String s : arr){
			 logger.info(s);
		 }
		

		
	}
}
