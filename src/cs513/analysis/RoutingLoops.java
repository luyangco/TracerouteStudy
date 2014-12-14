package cs513.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.parser.OutputParser;

public class RoutingLoops {

	int[] persistentLoops = new int[2];
	int[] tempLoops = new int[2];
	int totalPaths = 0;
	int numTimeouts = 0;

	public static void main(String[] args) {

		RoutingLoops tester = new RoutingLoops();
		File[] files = new File("output_trace_1").listFiles();


		OutputParser parser = new OutputParser();
		try {
			parser.showFiles(files);
			tester.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void process(
			HashMap<String, HashMap<String, ArrayList<RoutingPath>>> m_hostmap) {
		long start = System.currentTimeMillis();
		for(Map.Entry<String,HashMap<String,ArrayList<RoutingPath>>> entry : m_hostmap.entrySet()) {
			HashMap<String,ArrayList<RoutingPath>> pathMap = entry.getValue();
			for(Map.Entry<String,ArrayList<RoutingPath>> e : pathMap.entrySet()) {
				for(RoutingPath path : e.getValue()) {

					int pathSize = path.getPath().size();
					Map<String, Integer> uniqueIPs = new HashMap<String, Integer>();

					IPaddress lasthop = path.getPath().get(pathSize - 1);
					if(lasthop.ipToString().contains("*")) {
						numTimeouts++;
					}

					for(IPaddress ip : path.getPath()) {

						if (!ip.ipToString().contains("*")) {
							if(uniqueIPs.containsKey(ip.ipToString())) {
								// update the counter
								int ctr = uniqueIPs.get(ip.ipToString());
								uniqueIPs.put(ip.ipToString(), ctr + 1);
								// update the stats
								if (uniqueIPs.get(ip.ipToString()) == 3) { // has three duplicate hops
									if(lasthop.ipToString().equalsIgnoreCase(path.getDestIP().ipToString())) {
										tempLoops[1]++;
										tempLoops[0]--;
										System.out.println("Temp Src:" + path.getSrcIP() + " Dest:" + path.getDestIP() + " Timestamp:" + path.getTimestamp());
									} else { // never reaches dest
										persistentLoops[1]++;
										persistentLoops[0]--;
										System.out.println("Persis Src:" + path.getSrcIP() + " Dest:" + path.getDestIP() + " Timestamp:" + path.getTimestamp());
									}
								} else if (uniqueIPs.get(ip.ipToString()) == 2) { // has two duplicate hops
									if(lasthop.ipToString().equalsIgnoreCase(path.getDestIP().ipToString())) {
										tempLoops[0]++;
									} else { // never reaches dest
										persistentLoops[0]++;
									}
								} 
							} else {
								uniqueIPs.put(ip.ipToString(), 1);
							}
						}
					}
					totalPaths++;
				}

			}
		}
		System.out.println("Duration: " + ((System.currentTimeMillis() - start) / 1000) + " s");
		System.out.println("Total Paths: " + totalPaths);
		for (int i = 0; i < 2; i++) {
			System.out.printf("Persistent Loops with %d duplicated hops: %d\n", i + 2, persistentLoops[i]);
			System.out.printf("Temporary Loops with %d duplicated hops: %d\n", i + 2, tempLoops[i]);
		}
		System.out.println("Timeouts: " + numTimeouts);
		System.out.println("Total Loops: " + (persistentLoops[0] + tempLoops[0] + persistentLoops[1] + tempLoops[1]));
	}

}
