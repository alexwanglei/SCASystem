package cn.edu.buaa.act.SCAS.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.json.JSONArray;
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
import cn.edu.buaa.act.SCAS.service.ModelGenerationService;
import cn.edu.buaa.act.SCAS.po.AppCommunication;
import cn.edu.buaa.act.SCAS.po.Application;
import cn.edu.buaa.act.SCAS.po.Formula;
import cn.edu.buaa.act.SCAS.po.Task;
import cn.edu.buaa.act.SCAS.po.TaskCommunication;
import cn.edu.buaa.act.SCAS.po.Variable;
import cn.edu.buaa.act.SCAS.po.ARINC653.Module;
import cn.edu.buaa.act.SCAS.po.ARINC653.Partition;
import cn.edu.buaa.act.SCAS.po.ARINC653.Process;
/**
 * @Description 首页控制器
 * @author wanglei
 * @date 2014年7月12日
 */
/** 
 * @Description 
 * @author wanglei
 * @date 2014年7月12日 
 */ 
  	
/** 
 * @Description 
 * @author wanglei
 * @date 2014年7月12日 
 */ 
  	
@Controller
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	

	@Autowired
	private FileManageService fileManageService;
	@Autowired
	private CodeGenerationService codeGenerationService;
	@Autowired
	private ModelGenerationService modelGenerationService;
	
	/**
	 * 
	 * @Description 首页
	 * @param request
	 * @return 
	 * @throws JSONException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/", method= RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) throws JSONException{
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		String directory = fileManageService.getDirectory();
		logger.info("工程目录："+directory);
		view.addObject("directory", directory);

		return view;
	}
	
	/**
	 * @Description 刷新目录
	 * @param request
	 * @return
	 * @throws JSONException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/refreshDirectory", method= RequestMethod.GET)
	public ModelAndView refreshDirectory(HttpServletRequest request) throws JSONException{
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String,String> attributes = new HashMap<String,String>();
		
		String directory = fileManageService.getDirectory();
		attributes.put("directory", directory);
		view.setAttributesMap(attributes);
		
		logger.info("刷新工程目录："+directory);
		return new ModelAndView(view);
	}
	
	/**
	 * @Description 打开公式文件
	 * @param request
	 * @param response
	 * @param filename:公式文件在File下的路径
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/showFormulas",method=RequestMethod.GET)
	public ModelAndView showFormulas(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("formula");
		List<Formula> formulas  = fileManageService.getFormula(filename);
		String formulasXml = fileManageService.getXmlString(filename);
		mav.addObject("formulas", formulas);
		mav.addObject("formulasXml",formulasXml);
		mav.addObject("filename", "\""+filename+"\"");
		return mav;
	}
	
	@RequestMapping(value = "/showTasks",method=RequestMethod.GET)
	public ModelAndView showTasks(HttpServletRequest request, String filename) throws DocumentException, IOException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("taskGraph");
		List<Task> tasks  = fileManageService.getTask(filename);
		String tasksXml = fileManageService.getXmlString(filename);
		logger.info("任务数："+tasks.size());
		mav.addObject("tasks", tasks);
		mav.addObject("tasksXml",tasksXml);
		mav.addObject("filename", "\""+filename+"\"");
		return mav;
	}
	
	@RequestMapping(value = "/showApplications",method=RequestMethod.GET)
	public ModelAndView showApplications(HttpServletRequest request, String filename) throws DocumentException, IOException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("partitionGraph");
		List<Application> applications  = fileManageService.getApplication(filename);
		mav.addObject("applications", applications);
//		for(Application app : applications){
//			logger.info("Application: Id="+app.getId()+" Name="+app.getName());
//			for(TaskCommunication tc : app.getTaskCommunications()){
//				logger.info(tc.toString());
//			}
//		}
		//得到应用之间的通信
		List<AppCommunication> appCommList = new ArrayList<AppCommunication>();
		HashSet<Variable> appCommSet = new HashSet<Variable>();
		for(int i=0; i<applications.size(); i++){
			for(int j=0; j<applications.size(); j++){
				if(j==i){
					continue;
				}
				else{
					for(Variable input : applications.get(i).getInputs()){
						for(Variable output : applications.get(j).getOutputs()){
							if(output.equals(input)){
								AppCommunication ac = new AppCommunication();
								ac.setSrcApp(applications.get(j));
								ac.setDstApp(applications.get(i));
								ac.setVariable(output);
								appCommList.add(ac);
								appCommSet.add(output);
							}
						}
					}
				}
			}
		}
		
		mav.addObject("appCommList", appCommList);
		
//		for(AppCommunication ac : appCommList){
//			logger.info("应用之间的通信:\n"+ac.toString());
//		}
		
		//表示系统外部的应用（模块）对应伪分区
		Application externalApp = new Application();
		externalApp.setId(0);
		externalApp.setName("External Application");
		
		//得到外界和应用的通信
		ArrayList<AppCommunication> exCommList = new ArrayList<AppCommunication>();
		for(Application app : applications){
			for(Variable input : app.getInputs()){
				if(!appCommSet.contains(input)){
					AppCommunication ac = new AppCommunication();
					ac.setSrcApp(externalApp);
					ac.setDstApp(app);
					ac.setVariable(input);
					externalApp.getOutputs().add((Variable)input.clone());
					exCommList.add(ac);
				}
			}
		}
		for(Application app : applications){
			for(Variable output : app.getOutputs()){
				if(!appCommSet.contains(output)){
					AppCommunication ac = new AppCommunication();
					ac.setSrcApp(app);
					ac.setDstApp(externalApp);
					ac.setVariable(output);
					externalApp.getInputs().add((Variable)output.clone());
					exCommList.add(ac);
				}
			}
		}
		mav.addObject("exCommList", exCommList);
//		for(AppCommunication ec : exCommList){
//			logger.info("应用与外部的通信:\n"+ec.toString());
//		}
		
		
		String partitionsXml = fileManageService.getXmlString(filename);
		
		mav.addObject("partitionsXml",partitionsXml);
		mav.addObject("filename", "\""+filename+"\"");
		return mav;
	}
	
	
	/**
	 * @Description 从公式生成任务
	 * @param request
	 * @param reponse
	 * @param taskNames 任务名数组
	 * @param formulaIds 任务中包含的公式id数组
	 * @param filename
	 * @author wanglei
	 * @throws DocumentException 
	 * @throws IOException 
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/generateTask",method=RequestMethod.POST)
	public void generateTask(HttpServletRequest request, HttpServletResponse reponse, String[] taskNames, String[] formulaIds, String filename) throws IOException, DocumentException{
//		logger.info(Arrays.toString(taskNames));
//		logger.info(Arrays.toString(formulaIds));
//		logger.info(filename);
		List<Formula> formulas  = fileManageService.getFormula(filename);
		HashMap<String,Formula> formulasMap = new HashMap<String,Formula>();
		for(Formula e: formulas){
			formulasMap.put(Integer.toString(e.getId()), e);
		}
		List<Task> tasks = new ArrayList<Task>();
		for(int i=0; i<taskNames.length; i++){
			Task task = new Task();
			task.setId(i+1);
			task.setName(taskNames[i]);
			String[] id = formulaIds[i].split(";");
			for(String e : id){
				if(formulasMap.get(e)!=null){
					task.getFormulas().add(formulasMap.get(e));
				}
			}
			//除去任务中的其他公式
			ArrayList<Formula> otherFormula = new ArrayList<Formula>();
			for(Entry<String,Formula> entry : formulasMap.entrySet()){
				if(!task.getFormulas().contains(entry.getValue())){
					otherFormula.add(entry.getValue());
				}
			}
			HashSet<Variable> otherInputs = new HashSet<Variable>();
			for(Formula f: otherFormula){
				for(Variable var : f.getVars()){
					otherInputs.add(var);
				}
			}
			task.findIOput(otherInputs);
			tasks.add(task);
		}
		
		//生成任务文件
		if(modelGenerationService.generateTaskFile(tasks, filename)){
			reponse.getWriter().print("success");
		}else{
			reponse.getWriter().print("fail");
		}
		
	}
	
	@RequestMapping(value = "/generateApplication",method=RequestMethod.POST)
	public void generateApplication(HttpServletRequest request, HttpServletResponse reponse, String[] applicationNames, String[] taskIds, String filename) throws DocumentException, IOException{
		logger.info(Arrays.toString(applicationNames));
		logger.info(Arrays.toString(taskIds));
		logger.info(filename);
		
		List<Task> taskList = fileManageService.getTask(filename);
		HashMap<String,Task> tasksMap = new HashMap<String,Task>();
		for(Task t: taskList){
			tasksMap.put(Integer.toString(t.getId()), t);
		}
		List<Application> applications = new ArrayList<Application>();
		for(int i=0; i<applicationNames.length; i++){
			Application application = new Application();
			application.setId(i+1);
			application.setName(applicationNames[i]);
			String[] id = taskIds[i].split(";");
			for(String e : id){
				if(tasksMap.get(e)!=null){
					application.getTasks().add(tasksMap.get(e));
				}
			}
			//除去分区中的其他任务
			ArrayList<Task> otherTask = new ArrayList<Task>();
			for(Entry<String,Task> entry : tasksMap.entrySet()){
				if(!application.getTasks().contains(entry.getValue())){
					otherTask.add(entry.getValue());
				}
			}
			HashSet<Variable> otherInputs = new HashSet<Variable>();
			for(Task t: otherTask){
				for(Variable var : t.getInputs()){
					otherInputs.add(var);
				}
			}
			application.findIOput(otherInputs);
			applications.add(application);
		}
		
		//生成分区应用文件
		if(modelGenerationService.generateApplicationFile(applications, filename)){
			reponse.getWriter().print("success");
		}else{
			reponse.getWriter().print("fail");
		}
		
		
	}
	
	@RequestMapping(value = "/generateSAModel", method=RequestMethod.POST)
	public void generateSAModel(HttpServletRequest request, HttpServletResponse response, String tc, String ac, String ec, String filename) throws DocumentException, JSONException{
		logger.info(tc);
		logger.info(ac);
		logger.info(ec);
		logger.info(filename);
		List<Application> applications  = fileManageService.getApplication(filename);
		JSONArray tcJsonArray = new JSONArray(tc);
		int i=0;
		for(Application app : applications){
			for(TaskCommunication taskCom : app.getTaskCommunications()){
				taskCom.setType(tcJsonArray.getJSONObject(i++).getString("type"));
				System.out.println(taskCom.toString());
			}
		}
		modelGenerationService.gnerateSAModel(applications, ac, ec, filename);
		
		
		
	}
	
	/**
	 * @Description 打开模块模型文件
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
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
	
	/**
	 * @Description 打开分区模型文件
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
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
	
	
	/**
	 * @Description 打开任务模型文件
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
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
	
	/**
	 * @Description 保存公式文件
	 * @param request
	 * @param response
	 * @param formulasXML
	 * @param filename
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/saveFormula",method=RequestMethod.POST)
	public void saveFormula(HttpServletRequest request, HttpServletResponse response,String formulasXml, String filename) throws IOException{
		if(fileManageService.saveByXml(formulasXml,filename)){
			response.getWriter().print("success");
		}
		else{
			response.getWriter().print("false");
		}
	}
	
	@RequestMapping(value = "/saveTasks",method=RequestMethod.POST)
	public void saveTasks(HttpServletRequest request, HttpServletResponse response,String tasksXml, String filename) throws IOException{
		if(fileManageService.saveByXml(tasksXml,filename)){
			response.getWriter().print("success");
		}
		else{
			response.getWriter().print("false");
		}
	}
	
	@RequestMapping(value = "/savePartitions",method=RequestMethod.POST)
	public void savePartitions(HttpServletRequest request, HttpServletResponse response,String partitionsXml, String filename) throws IOException{
		if(fileManageService.saveByXml(partitionsXml,filename)){
			response.getWriter().print("success");
		}
		else{
			response.getWriter().print("false");
		}
	}
	
	/**
	 * @Description 保存模块模型xml
	 * @param request
	 * @param response
	 * @param moduleXml
	 * @param filename
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/saveModuleByXml", method=RequestMethod.POST)
	public void saveModuleByXml(HttpServletRequest request,HttpServletResponse response, String moduleXml,String filename) throws IOException{
		logger.info(filename);
		logger.info(moduleXml);
		if(fileManageService.saveByXml(moduleXml, filename)){
			response.getWriter().print("success");
		}
		else{
			response.getWriter().print("fail");
		}
	}
	
	/**
	 * @Description 保存分区模型xml
	 * @param request
	 * @param response
	 * @param partitionXml
	 * @param filename
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/savePartitionByXml", method=RequestMethod.POST)
	public void savePartitionByXml(HttpServletRequest request,HttpServletResponse response, String partitionXml,String filename) throws IOException{
		logger.info(filename);
		logger.info(partitionXml);
		if(fileManageService.saveByXml(partitionXml, filename)){
			response.getWriter().print("success");
		}
		else{
			response.getWriter().print("fail");
		}
	}
	
	/**
	 * @Description 保存xml格式的任务模型
	 * @param request
	 * @param response
	 * @param taskXml
	 * @param filename
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/saveTaskByXml", method=RequestMethod.POST)
	public void saveTaskByXml(HttpServletRequest request,HttpServletResponse response, String taskXml,String filename) throws IOException{
		logger.info(filename);
		logger.info(taskXml);
		if(fileManageService.saveByXml(taskXml, filename)){
			response.getWriter().print("success");
		}
		else{
			response.getWriter().print("fail");
		}
	}
	
	
	/**
	 * @Description 打开C文件
	 * @param request
	 * @param filename
	 * @return
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/showCCode",method=RequestMethod.GET)
	public ModelAndView showCCode(HttpServletRequest request, String filename) throws IOException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("ccode");
		String code = fileManageService.getCCode(filename);
		mav.addObject("ccode",code);
		mav.addObject("filename","\""+filename+"\"");
		return mav;
	}
	
	/**
	 * @Description 打开配置文件
	 * @param request
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value = "/showConf",method=RequestMethod.GET)
	public ModelAndView showConf(HttpServletRequest request, String filename) throws IOException, DocumentException{
		logger.info(filename);
		ModelAndView mav = new ModelAndView("conf");
		String conf = fileManageService.getConf(filename);
		mav.addObject("conf",conf);
		mav.addObject("filename","\""+filename+"\"");
		return mav;
	}
	
	
	/**
	 * @Description 生成C文件
	 * @param request
	 * @param response
	 * @param filename
	 * @throws DocumentException
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value="/generateCCode",method=RequestMethod.POST)
	public void generateCCode(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		Module module = fileManageService.getModule(filename);
		codeGenerationService.generateCCode(module, filename);
		
	}
	
	/**
	 * 生成配置文件
	 * @Description
	 * @param request
	 * @param response
	 * @param filename
	 * @throws DocumentException
	 * @throws IOException
	 * @author wanglei
	 * @date 2014年7月12日
	 */
	@RequestMapping(value="/generateConf",method=RequestMethod.POST)
	public void generateConf(HttpServletRequest request,HttpServletResponse response, String filename) throws DocumentException, IOException{
		logger.info(filename);
		Module module = fileManageService.getModule(filename);
		codeGenerationService.generateConf(module, filename);
		
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
