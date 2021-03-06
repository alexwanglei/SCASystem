package cn.edu.buaa.act.SCAS.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.buaa.act.SCAS.po.ARINC653.Module;
import cn.edu.buaa.act.SCAS.po.ARINC653.Partition;
import cn.edu.buaa.act.SCAS.po.ARINC653.Port;

@Service
public class CodeGenerationService {
	private static Logger logger = LoggerFactory.getLogger(CodeGenerationService.class);
	
	private String rootPath = this.getClass().getResource("/File").getPath();
	
	private final String headFileCode = "#include \"copyright_wrs.h\"\n" +
			"#include \"vxWorks.h\"\n" +
			"#include \"apex/apexLib.h\"\n" +
			"#include \"configRecordLib.h\"\n" +
			"#include \"stdio.h\"\n" +
			"#include \"string.h\"\n" +
			"#include \"taskLib.h\"\n" +
			"#include \"math.h\"\n" +
			"#define CHECK_CODE(msg, code){ if(code!=NO_ERROR) printf(\"%s error : %s\\n\",msg,codeToStr(retCode));}\n\n\n";
	
	private final String codeToStr ="char * codeToStr (RETURN_CODE_TYPE retCode)\n" +
		"{\n" +
		"	switch (retCode)\n" +
		"	{\n" +
		"		case NO_ERROR:\n" +
		"			return \"NO_ERROR\";\n" +
		"			break;\n" +
		"		case NO_ACTION:\n" +
		"			return \"NO_ACTION\";\n" +
		"			break;\n" +
		"		case NOT_AVAILABLE:\n" +
		"			return \"NOT_AVAILABLE\";\n" +
		"			break;\n" +
		"		case INVALID_PARAM:\n" +
		"			return \"INVALID_PARAM\";\n" +
		"			break;\n" +
		"		case INVALID_CONFIG:\n" +
		"			return \"INVALID_CONFIG\";\n" +
		"			break;\n" +
		"		case INVALID_MODE:\n" +
		"			return \"INVALID_MODE\";\n" +
		"			break;\n" +
		"		case TIMED_OUT:\n" +
		"			return \"TIMED_OUT\";\n" +
		"			break;\n" +
		"	}\n" +
		"	return \"Unknown code\";\n" +
		"}\n\n\n";
	
	
	public void generateCCode(Module module, String filepath) throws IOException{
		logger.info(filepath);
		File codeFolder = new File(rootPath+"/"+filepath.substring(0,filepath.indexOf('/')) + "/Code");
		logger.info(codeFolder.getPath());
		if(!codeFolder.exists())
			codeFolder.mkdir();
		for(Partition p : module.getPartitions()){
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(codeFolder.getPath()+"/"+p.getName()+".c")));
			bw.write(headFileCode);
			bw.write(codeToStr);
			bw.write(p.defLocals());
			bw.write("\n\n");
			bw.write(p.defTaskFunc());
			bw.write("\n\n");
			bw.write(p.defProcessTable());
			bw.write("\n\n");
			bw.write(p.initPartition());
			bw.write("\n\n");
			bw.write("void usrAppInit(void) {\n" +
					"	Init();\n" +
					"}\n");
			bw.flush();
			bw.close();
			
		}
	}
	
	
	public void generateConf(Module module, String filepath) throws DocumentException, IOException{
		File codeFolder = new File(rootPath+"/"+filepath.substring(0,filepath.indexOf('/')) + "/Code");
		logger.info(codeFolder.getPath());
		if(!codeFolder.exists())
			codeFolder.mkdir();
		
		String confTemplate = this.getClass().getResource("/Conf/wrSbc834x_default_template.xml").getPath();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(confTemplate));
		Element root = document.getRootElement();
		List<Element> content = root.elements();
		
		content.add(1,module.generateAppEle(root));
		
		content.add(4,module.generatePartsEle(root));

		content.add(5,module.generateScheduleEle(root));

		content.add(5,module.generateConnectionEle(root));

		content.add(module.generatePayloadsEle(root));
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(codeFolder.getPath()+"/wrSbc834x_default.xml"));
		
		XMLWriter out = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        out = new XMLWriter(bw, format);
        out.write(document);
        
        bw.close();
		
	}
	

}
