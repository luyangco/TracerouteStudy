package cs513.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.utils.Utils;

public class OutputParser {
	public HashMap<String, HashMap<String, ArrayList<RoutingPath>>> m_hostmap = new HashMap<String, HashMap<String, ArrayList<RoutingPath>>>();

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

	public void generateOuput(File[] files) throws IOException {
		for (File f : files) {
			if (f.isDirectory()) {
				generateOuput(f.listFiles());
			} else {
				HashMap<String, ArrayList<RoutingPath>> pathMap = generatePath(f);
				m_hostmap.put(f.getName(), pathMap);
				System.out.println(f.getName());
			}
		}
	}

	public HashMap<String, ArrayList<RoutingPath>> generatePath(File file) {

		HashMap<String, ArrayList<RoutingPath>> pathMap = new HashMap<String, ArrayList<RoutingPath>>();
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			RoutingPath path = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("Dest: ")) {
					IPaddress destIP = string2IP(line);
					path = new RoutingPath(Utils.getHostIPByName(file.getName().substring(0, file.getName().length() - 4)), destIP);
					if (pathMap.containsKey(destIP.ipToString())) {
						ArrayList<RoutingPath> pathList = pathMap.get(destIP.ipToString());
						pathList.add(path);
					} else {
						ArrayList<RoutingPath> pathList = new ArrayList<RoutingPath>();
						pathList.add(path);
						pathMap.put(destIP.ipToString(), pathList);
					}
				} else if (line.startsWith("Timestamp: ")) {
					String ts[] = line.split(" ");
					path.setTimestamp((ts[1]));
				} else if (line.startsWith("####")) { // detect a new line
					; // nothing to do here
				} else {
					path.add(string2IP(line));
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pathMap;
	}
}
