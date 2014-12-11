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
					  path = new RoutingPath(string2IP(line)); 
				} else if (line.startsWith("Timestamp: ")) {
					String ts[] = line.split(" ");
					path.setTimestamp(convertToCalendar(ts[1]));
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
