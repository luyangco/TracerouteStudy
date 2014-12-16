package cs513.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.parser.OutputParser;
import cs513.utils.Utils;

public class TooManyHops {

	int tooManyHopsCtr = 0;
	
	int [] timeOfDay = new int[24];

	Map<String, Integer> unreachableAddresses =  new HashMap<String, Integer>();

	public static void main(String[] args) {

		TooManyHops tester = new TooManyHops();
		File[] files = new File("output_trace_1").listFiles();

		
		OutputParser parser = new OutputParser();
		try {
			parser.generateOuput(files);
			tester.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void process(HashMap<String, HashMap<String, ArrayList<RoutingPath>>> m_hostmap) {
		long start = System.currentTimeMillis();
		for(Map.Entry<String,HashMap<String,ArrayList<RoutingPath>>> entry : m_hostmap.entrySet()) {
			HashMap<String,ArrayList<RoutingPath>> pathMap = entry.getValue();
			for(Map.Entry<String,ArrayList<RoutingPath>> e : pathMap.entrySet()) {
				for(RoutingPath path : e.getValue()) {
					int pathSize = path.getPath().size();
					IPaddress lasthop = path.getLastHop();
					if(pathSize == 30 && lasthop.ipToString().equals("*.*.*.*")) {
						IPaddress dest = path.getDestIP();
						if(unreachableAddresses.containsKey(dest)) {
							unreachableAddresses.put(dest.ipToString(), unreachableAddresses.get(dest) + 1);
						}
						else {
							unreachableAddresses.put(dest.ipToString(), 1);
						}
						tooManyHopsCtr++;
						timeOfDay[Utils.convertToLocalTime(path)]++;
					}
				}
			}
			
		}
		
		Set<String> reachableAddresses =  new HashSet<String>();

		for(Map.Entry<String,HashMap<String,ArrayList<RoutingPath>>> entry : m_hostmap.entrySet()) {
			HashMap<String,ArrayList<RoutingPath>> pathMap = entry.getValue();
			for(Map.Entry<String,ArrayList<RoutingPath>> e : pathMap.entrySet()) {
				for(RoutingPath path : e.getValue()) {
					if(unreachableAddresses.containsKey(path.getDestIP())) {
						if(path.getLastHop().equals(path.getDestIP())) {
							// unreachableAddresses.remove(path.getDestIP());
							System.out.println("got one!");
							reachableAddresses.add(path.getDestIP().ipToString());
						}
					}
				}
			}
		}
		
		System.out.println("Time of day pattern");
		for(int index = 0; index < timeOfDay.length; index++) {
			System.out.printf("Hour %d: %d\n", index, timeOfDay[index]);
		}

		System.out.println("Duration: " + ((System.currentTimeMillis() - start) / 1000) + " s");
		System.out.println("Too Many Hops: " + (tooManyHopsCtr));
	}

}

