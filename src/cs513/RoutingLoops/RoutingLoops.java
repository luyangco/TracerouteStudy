package cs513.RoutingLoops;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.parser.OutputParser;

public class RoutingLoops {

	int persistentLoops = 0;
	int tempLoops = 0;
	int totalPaths = 0;
	int numTimeouts = 0;

	public static void main(String[] args) {

		RoutingLoops tester = new RoutingLoops();
		File[] files = new File("output_trace_1").listFiles();

		long start = System.currentTimeMillis();
		OutputParser parser = new OutputParser();
		try {
			parser.showFiles(files);
			tester.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Duration: " + ((System.currentTimeMillis() - start) / 1000) + " s");
		System.out.println("Total Paths: " + tester.totalPaths);
		System.out.println("Persistent Loops: " + tester.persistentLoops);
		System.out.println("Temporary Loops: " + tester.tempLoops);
		System.out.println("Timeouts: " + tester.numTimeouts);
		System.out.println("Total Loops: " + (tester.persistentLoops + tester.tempLoops));

	}

	private void process(
			HashMap<String, ArrayList<RoutingPath>> m_hostmap) {

		for(Map.Entry<String, ArrayList<RoutingPath>> entry : m_hostmap.entrySet()) {
			for(RoutingPath path : entry.getValue()) {

				int pathSize = path.getPath().size();
				Map<String, Integer> uniqueIPs = new HashMap<String, Integer>();
				
				IPaddress lasthop = path.getPath().get(pathSize - 1);
				if(lasthop.ipToString().contains("*")) {
					numTimeouts++;
				}
				
				for(IPaddress ip : path.getPath()) {
					
					if(uniqueIPs.containsKey(ip.ipToString()) && !ip.ipToString().contains("*")) {
						if(lasthop.ipToString().equalsIgnoreCase(path.getDestIP().ipToString())) {
							tempLoops++;
						} else { // never reaches dest
								persistentLoops++;
						}
						System.out.println("Dest: " + path.getDestIP().ipToString() + " Time: " + path.getTimestamp().getTime().toLocaleString());
						break;
					} else {
						uniqueIPs.put(ip.ipToString(), 1);
					}
				}
				totalPaths++;
			}
		}
	}

}
