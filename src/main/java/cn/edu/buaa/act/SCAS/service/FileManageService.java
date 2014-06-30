package cn.edu.buaa.act.SCAS.service;



import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;




import cn.edu.buaa.act.SCAS.po.ARINC653.Process;

@Service
public class FileManageService {
	
	

	private static Logger logger = LoggerFactory.getLogger(FileManageService.class);
	
	private String rootPath = this.getClass().getResource("/File").getPath();
	
	public String getDirectory() throws JSONException{

//		logger.info("存储工程文件的根目录："+rootPath);
		File rootDirectory = new File(rootPath);
		JSONObject jsonDir = new JSONObject();
		getJsonDir(rootDirectory, jsonDir);
//		logger.info("工程文件目录json："+jsonDir);
		return jsonDir.getJSONArray("children").toString();
	}
	
	private void getJsonDir(File dir, JSONObject jsonObject) throws JSONException {
		jsonObject.put("text", dir.getName());

		if (dir.isFile()) {
			return;
		} else {
			jsonObject.put("state", "closed");
			File files[] = dir.listFiles();
			if (files.length == 0) {
			} else {
				JSONArray ja = new JSONArray();
				for (int i = 0; i < files.length; i++) {
					JSONObject childJo = new JSONObject();
					getJsonDir(files[i], childJo);
					ja.put(childJo);
				}
				jsonObject.put("children", ja);
			}
		}
	}
	
	public Process getProcess(String filename) throws DocumentException{
		Process process = new Process();
		File file = new File(rootPath+"/"+filename);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new File(filename));
		Element root = doc.getRootElement();
		System.out.println(root.attributeValue("ID"));
		System.out.println(root.attributeValue("Name"));
		
		
		return new Process();
	}
}
