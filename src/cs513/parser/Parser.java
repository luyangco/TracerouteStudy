package cs513.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;

public class Parser {
	
	public static IPaddress extractIP(String line) {
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
        	if (line.contains("* * *")) {
        		return new IPaddress("*.*.*.*");
        	}
            return new IPaddress("0.0.0.0");
        }
	}
	
	public static void showFiles(File[] files, BufferedWriter bw) throws IOException {
		for (File f : files) {
			if (f.isDirectory()) {
				FileWriter fstream = null;
				BufferedWriter out = null;
				if (!f.getName().equalsIgnoreCase("trace_1")) {
					fstream = new FileWriter(f.getName()+".txt");
					out = new BufferedWriter(fstream);
				}
				long startTime = System.currentTimeMillis();
				System.out.println("Directory: " + f.getName());
				showFiles(f.listFiles(), out);
				out.close();
				long endTime = System.currentTimeMillis();
				System.out.println("enduration: " + (endTime - startTime) / 1000 + " s");
			} else {
				extractFile(f, bw);
			}
		}
	}
	
	public static void extractFile(File file, BufferedWriter bw) {
		
		String TIMESTAMP_PATTERN = "(?:(\\d{4})-(\\d{2})-(\\d{2})\\.(\\d{2})-(\\d{2})-(\\d{2})?)";
		Pattern pattern = Pattern.compile(TIMESTAMP_PATTERN);
		Matcher matcher = pattern.matcher(file.getName());
		String timestamp = null;
        if (matcher.find()) {
        	timestamp = matcher.group();
        }
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			RoutingPath path = null;
			int lineNum = 1;
			while ((line = bufferedReader.readLine()) != null) {
				if (lineNum == 1) {
					path = new RoutingPath(extractIP(line)); 
				} else {
					path.add(extractIP(line));
					stringBuffer.append(line);
					stringBuffer.append("\n");
				}
				lineNum++;
			}
			fileReader.close();
//			System.out.println("Contents of file:");
//			System.out.println(stringBuffer.toString());

//			System.out.println("DestIP: " + path.getDestIP());
			int pathSize = path.getPath().size();
			IPaddress lastNode = path.getPath().get(pathSize - 1);
			
			if (path.getDestIP().ipToString().equalsIgnoreCase(lastNode.ipToString()) &&
					path.getPath().size() == 1) {
				return;
			}
			bw.write("Dest: " + path.getDestIP());
			bw.newLine();
			bw.write("Timestamp: " + timestamp);
			bw.newLine();
			for (IPaddress ip: path.getPath()) {
				if (ip.toString().contains("0.0.0.0")) {
					System.out.println("Couldn't recognize ip: " + file.getName());
				}
				bw.write(ip.toString());
				bw.newLine();
			}
			bw.write("####");
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		File[] files = new File("c:\\Program Files (x86)\\cygwin64\\home\\ylu5\\output\\trace_1\\").listFiles();
		try {
			showFiles(files, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
