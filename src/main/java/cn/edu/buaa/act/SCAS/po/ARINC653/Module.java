package cn.edu.buaa.act.SCAS.po.ARINC653;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

public class Module {
	private int id;
	
	private String name;
	
	private String xmlModule;
	
	private ArrayList<Partition> partitions = new ArrayList<Partition>();
	
	private ArrayList<InterPartitionCom> interCom = new ArrayList<InterPartitionCom>();
	
	private ArrayList<Channel> channels = new ArrayList<Channel>();
	
	private ArrayList<PartitionWindow> schedule = new ArrayList<PartitionWindow>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(ArrayList<Partition> partitions) {
		this.partitions = partitions;
	}

	public ArrayList<InterPartitionCom> getInterCom() {
		return interCom;
	}

	public void setInterCom(ArrayList<InterPartitionCom> interCom) {
		this.interCom = interCom;
	}

	public ArrayList<Channel> getChannels() {
		return channels;
	}

	public void setChannels(ArrayList<Channel> channels) {
		this.channels = channels;
	}

	public ArrayList<PartitionWindow> getSchedule() {
		return schedule;
	}

	public void setSchedule(ArrayList<PartitionWindow> schedule) {
		this.schedule = schedule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXmlModule() {
		return xmlModule;
	}

	public void setXmlModule(String xmlModule) {
		this.xmlModule = xmlModule;
	}
	
	public Element generateAppEle(Element root){
		Element applicationsEle = DocumentHelper.createElement(QName.get("Applications",root.getNamespace()));

		for(Partition part : this.partitions)
		{
			Element applicationEle = applicationsEle.addElement("Application");
			applicationEle.addAttribute("Name", part.getName());
			Element appDescriptionEle = applicationEle.addElement("ApplicationDescription");
			Element memorysizeEle = appDescriptionEle.addElement("MemorySize");
			memorysizeEle.addAttribute("MemorySizeBss", "0x"+Integer.toHexString(part.getMemorySizeBss()));
			memorysizeEle.addAttribute("MemorySizeText", "0x"+Integer.toHexString(part.getMemorySizeText()));
			memorysizeEle.addAttribute("MemorySizeData", "0x"+Integer.toHexString(part.getMemorySizeData()));
			memorysizeEle.addAttribute("MemorySizeRoData", "0x"+Integer.toHexString(part.getMemorySizeRoData()));
			memorysizeEle.addAttribute("MemorySizePersistentData", "0x"+Integer.toHexString(part.getMemorySizePersistentData()));
			memorysizeEle.addAttribute("MemorySizePersistentBss", "0x"+Integer.toHexString(part.getMemorySizePersistentBss()));
			
			Element portsEle = appDescriptionEle.addElement("Ports");
			for(Port p : part.getPorts()){
				p.genPortXML(portsEle);
			}
		}
		return applicationsEle;
	}
	
	public Element generatePartsEle(Element root) throws DocumentException
	{
		Element partitionsEle = DocumentHelper.createElement(QName.get("Partitions",root.getNamespace()));
		for(Partition part : this.partitions)
		{
			
			Element partitionEle = partitionsEle.addElement("Partition");
			partitionEle.addAttribute("Name", part.getName());

			partitionEle.addAttribute("Id", Integer.toString(part.getId()));
			Element partDescEle = partitionEle.addElement("PartitionDescription");
			Element appEle = partDescEle.addElement("Application");
			appEle.addAttribute("NameRef", part.getName());
			Element shareLibRegionEle = partDescEle.addElement("SharedLibraryRegion");
			shareLibRegionEle.addAttribute("NameRef", "vxSysLib");
			Element settingsEle = partDescEle.addElement("Settings");
			settingsEle.addAttribute("RequiredMemorySize", "0x200000");
			settingsEle.addAttribute("PartitionHMTable", "part1Hm");
			settingsEle.addAttribute("watchDogDuration", "0");
			settingsEle.addAttribute("allocDisable", "0");
			settingsEle.addAttribute("numStackGuardPages", "0xffffffff");
			settingsEle.addAttribute("numWorkerTasks", "0");
			settingsEle.addAttribute("isrStackSize", "0xffffffff");
			settingsEle.addAttribute("selSvrQSize", "0xffffffff");
			settingsEle.addAttribute("maxEventQStallDuration", "INFINITE_TIME");
			settingsEle.addAttribute("fpExcEnable", "1");
			settingsEle.addAttribute("syscallPermissions", "0xffffffff");
			settingsEle.addAttribute("numFiles", "0xffffffff");
			settingsEle.addAttribute("maxGlobalFDs", "10");
			settingsEle.addAttribute("numDrivers", "0xffffffff");
			settingsEle.addAttribute("numLogMsgs", "0xffffffff");
			
//			Document settingDoc = DocumentHelper.parseText("<Settings RequiredMemorySize=\"0x200000\" "
//					+ "PartitionHMTable=\"part1Hm\" "
//					+ "watchDogDuration=\"0\" "
//					+ "allocDisable=\"0\" "
//					+ "numStackGuardPages=\"0xffffffff\" "
//					+ "numWorkerTasks=\"0\" "
//					+ "isrStackSize=\"0xffffffff\" "
//					+ "selSvrQSize=\"0xffffffff\" "
//					+ "maxEventQStallDuration=\"INFINITE_TIME\" "
//					+ "fpExcEnable=\"1\" "
//					+ "syscallPermissions=\"0xffffffff\" "
//					+ "numFiles=\"0xffffffff\" "
//					+ "maxGlobalFDs=\"10\" "
//					+ "numDrivers=\"0xffffffff\" "
//					+ "numLogMsgs=\"0xffffffff\"/>");
//			partDescEle.add(settingDoc);
			
		}
		return partitionsEle;
	}
	
	public Element generateScheduleEle(Element root)
	{
		Element schedulesEle = DocumentHelper.createElement(QName.get("Schedules",root.getNamespace()));
		Element scheduleEle = schedulesEle.addElement("Schedule");
		scheduleEle.addAttribute("Id", "0");
		for(PartitionWindow pw : this.schedule){
			Element partitionWinEle = scheduleEle.addElement("PartitionWindow");
			partitionWinEle.addAttribute("PartitionNameRef", pw.getPartName());
			partitionWinEle.addAttribute("Duration", Double.toString(pw.getDuration()));
			partitionWinEle.addAttribute("ReleasePoint",Double.toString(pw.getReleasePoint()));
		}
		return schedulesEle;
	}
	
	public Element generateConnectionEle(Element root)
	{
		Element connectionEle = DocumentHelper.createElement(QName.get("Connections",root.getNamespace()));
		
		for(Channel  c: this.channels)
		{
			Element channelEle = connectionEle.addElement("Channel");
			channelEle.addAttribute("Id", Integer.toString(c.getId())); 
			Element sourceEle = channelEle.addElement("Source");
			sourceEle.addAttribute("PartitionNameRef", c.getSrcPartition());
			sourceEle.addAttribute("PortNameRef", c.getSrcPort());
			
			for(int i=0; i<c.getDstPartitions().size(); i++)
			{
				Element desEle = channelEle.addElement("Destination");
				desEle.addAttribute("PartitionNameRef", c.getDstPartitions().get(i));
				desEle.addAttribute("PortNameRef", c.getDstPorts().get(i));
			}
		}
		return connectionEle;
	}
	
	
	public Element generatePayloadsEle(Element root)
	{
		Element payloadsEle = DocumentHelper.createElement(QName.get("Payloads",root.getNamespace()));
		payloadsEle.addElement("CoreOSPayload");
		Element shareLibPLEle = payloadsEle.addElement("SharedLibraryPayload");
		shareLibPLEle.addAttribute("NameRef","vxSysLib");
		Element confRPLEle = payloadsEle.addElement("ConfigRecordPayload");
		confRPLEle.addAttribute("NameRef","configRecord");
		for(Partition part : this.partitions)
		{
			Element partPLEle = payloadsEle.addElement("PartitionPayload");
			partPLEle.addAttribute("NameRef", part.getName());
		}
		return payloadsEle;
	}

}
