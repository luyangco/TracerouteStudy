package cs513.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;

public class OutputParser {
	public HashMap<String, ArrayList<RoutingPath>> m_hostmap = new HashMap<String, ArrayList<RoutingPath>>();
	
	private static HashMap<String, IPaddress> m_mapping = new HashMap<String, IPaddress>();
	static {
		m_mapping.put("planetlab2.csee.usf.edu", new IPaddress("131.247.2.242"));
		m_mapping.put("planetlab1.csuohio.edu", new IPaddress("137.148.16.10"));
		m_mapping.put("planetlab1.acis.ufl.edu", new IPaddress("128.227.150.11"));
		m_mapping.put("ricepl-1.cs.rice.edu", new IPaddress("128.42.142.41"));
		m_mapping.put("planetlab1.cs.pitt.edu", new IPaddress("192.52.240.213"));
		m_mapping.put("pl3.cs.unm.edu", new IPaddress("198.83.85.46"));
		m_mapping.put("planetlab2.cis.upenn.edu", new IPaddress("158.130.6.253"));
		m_mapping.put("planetlab1.clemson.edu", new IPaddress("130.127.39.152"));
		m_mapping.put("righthand.eecs.harvard.edu", new IPaddress("140.247.60.126"));
		m_mapping.put("planetlab1.utdallas.edu", new IPaddress("129.110.125.51"));
		m_mapping.put("planetlab-3.cmcl.cs.cmu.edu", new IPaddress("128.2.211.115"));
		m_mapping.put("75-130-96-13.static.oxfr.ma.charter.com", new IPaddress("75.130.96.13"));
		m_mapping.put("planetlab1.cs.du.edu", new IPaddress("130.253.21.121"));
		m_mapping.put("pl-node-1.csl.sri.com", new IPaddress("192.12.33.102"));
		m_mapping.put("planetlab-4.eecs.cwru.edu", new IPaddress("129.22.150.29"));
		m_mapping.put("planetlab4.ie.cuhk.edu.hk", new IPaddress("137.189.98.209"));
		m_mapping.put("planet-lab4.uba.ar", new IPaddress("157.92.44.104"));
		m_mapping.put("plonk.cs.uwaterloo.ca", new IPaddress("129.97.74.14"));
		m_mapping.put("planetlab2.koganei.itrc.net", new IPaddress("133.69.32.133"));
		m_mapping.put("planetlab3.cesnet.cz", new IPaddress("195.113.161.84"));
		m_mapping.put("planetlab1.dojima.wide.ad.jp", new IPaddress("203.178.133.11"));
		m_mapping.put("planetlab1.netlab.uky.edu", new IPaddress("128.163.142.20"));
		m_mapping.put("planetlab1.postel.org", new IPaddress("206.117.37.4"));
		m_mapping.put("planetlab1.cti.espol.edu.ec", new IPaddress("200.10.150.252"));
		m_mapping.put("planetlab1.cs.otago.ac.nz", new IPaddress("139.80.206.132"));
		m_mapping.put("planet-plc-1.mpi-sws.org", new IPaddress("139.19.158.225"));
		m_mapping.put("pl1.eng.monash.edu.au", new IPaddress("130.194.252.8"));
		m_mapping.put("planetlab2.cs.okstate.edu", new IPaddress("139.78.141.245"));
		m_mapping.put("planetlab1.ustc.edu.cn", new IPaddress("202.141.161.43"));
		m_mapping.put("planetlab-2.sjtu.edu.cn", new IPaddress("202.112.28.100"));
		m_mapping.put("planetlab-01.vt.nodes.planet-lab.org", new IPaddress("198.82.160.238"));
		m_mapping.put("pl1.6test.edu.cn", new IPaddress("219.243.208.60"));
		m_mapping.put("pli1-pa-6.hpl.hp.com", new IPaddress("204.123.28.57"));
		m_mapping.put("planetlab3.inf.ethz.ch", new IPaddress("192.33.90.68"));
		m_mapping.put("planetlab2.cs.unc.edu", new IPaddress("204.85.191.11"));
		m_mapping.put("planetlab-1.scie.uestc.edu.cn", new IPaddress("222.197.180.137"));
	}
	
	public Calendar convertToCalendar(String date) {
		
		String TIMESTAMP_PATTERN = "(?:(\\d{4})-(\\d{2})-(\\d{2})\\.(\\d{2})-(\\d{2})-(\\d{2})?)";
		Pattern pattern = Pattern.compile(TIMESTAMP_PATTERN);
		Matcher matcher = pattern.matcher(date);
		
		Calendar cal = Calendar.getInstance();
		
		if (matcher.find()) {
			int year = Integer.parseInt(matcher.group(1));
			int month = Integer.parseInt(matcher.group(2));
			int day = Integer.parseInt(matcher.group(3));
			int hour = Integer.parseInt(matcher.group(4));
			int minute = Integer.parseInt(matcher.group(5));
			int second = Integer.parseInt(matcher.group(6));
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DAY_OF_MONTH, day);	
			cal.set(Calendar.HOUR, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
		}
		
		return cal;
	}
	
	
	public IPaddress string2IP(String line) {
		String IPADDRESS_PATTERN = 
		        "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		
		String LATENCY_PATTERN = 
				"(?:[0-9]+.[0-9]+ ms?)";

		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
        	IPaddress ip = new IPaddress(matcher.group());
        	Pattern latency = Pattern.compile(LATENCY_PATTERN);
        	Matcher latencyMatcher = latency.matcher(line);
        	if (latencyMatcher.find()) {
        		String latencyString[] = latencyMatcher.group().split(" ");
        		ip.setLatency(Float.parseFloat(latencyString[0]));
        	}
            return ip;
        }
        else{
        	if (line.contains("*")) {
        		return new IPaddress("*.*.*.*");
        	}
            return new IPaddress("0.0.0.0");
        }
	}
	
	public void showFiles(File[] files) throws IOException {
		for (File f : files) {
			if (f.isDirectory()) {
				showFiles(f.listFiles());
			} else {
				ArrayList<RoutingPath> pathList = generatePath(f);
				m_hostmap.put(f.getName(), pathList);
				System.out.println(f.getName() + ": size=" + pathList.size());
			}
		}
	}
	
	public ArrayList<RoutingPath> generatePath(File file) {

		ArrayList<RoutingPath> pathList = new ArrayList<RoutingPath>();
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			RoutingPath path = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("Dest: ")) {
					  path = new RoutingPath(m_mapping.get(file.getName().substring(0, file.getName().length() - 4)), string2IP(line)); 
				} else if (line.startsWith("Timestamp: ")) {
					String ts[] = line.split(" ");
					path.setTimestamp((ts[1]));
				} else if (line.startsWith("####")) { // detect a new line
					pathList.add(path);
				} else {
					path.add(string2IP(line));
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return pathList;
	}

	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		File[] files = new File("output_trace_1").listFiles();
		OutputParser parser = new OutputParser();
		try {
			parser.showFiles(files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Duration: " + ((System.currentTimeMillis() - start) / 1000) + " s");
		
	}	
}
