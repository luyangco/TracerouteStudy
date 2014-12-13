package cs513.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.utils.Utils;

public class SimplifyParser extends OutputParser {

	@Override
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
				} else if (line.startsWith("####")) { 
					// detect a new line
				} else {
					// merge similar hops into one hop
					IPaddress hop = string2IP(line);
					if (hop.equals(new IPaddress("*.*.*.*"))) {
						continue;
					}
					if (path.getPath().isEmpty()) {
						path.add(hop);
					} else if (!path.getLastHop().equalsLocalNetwork(hop)) {
						path.add(hop);
					}
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pathMap;
	}
}
