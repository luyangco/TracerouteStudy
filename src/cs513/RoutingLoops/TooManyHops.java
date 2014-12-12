package cs513.RoutingLoops;

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

public class TooManyHops {

	int tooManyHopsCtr = 0;

	Map<String, Integer> unreachableAddresses =  new HashMap<String, Integer>();

	public static void main(String[] args) {

		TooManyHops tester = new TooManyHops();
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
		System.out.println("Too Many Hops: " + (tester.tooManyHopsCtr));
	}

	private void process(HashMap<String, ArrayList<RoutingPath>> m_hostmap) {

		for(Map.Entry<String, ArrayList<RoutingPath>> entry : m_hostmap.entrySet()) {
			for(RoutingPath path : entry.getValue()) {

				int pathSize = path.getPath().size();
				Map<String, Integer> uniqueIPs = new HashMap<String, Integer>();

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
				}
			}
		}
		
		Set<String> reachableAddresses =  new HashSet<String>();

		for(Map.Entry<String, ArrayList<RoutingPath>> entry : m_hostmap.entrySet()) {
			for(RoutingPath path : entry.getValue()) {
				if(unreachableAddresses.containsKey(path.getDestIP())) {
					if(path.getLastHop().equals(path.getDestIP())) {
						// unreachableAddresses.remove(path.getDestIP());
						System.out.println("got one!");
						reachableAddresses.add(path.getDestIP().ipToString());
					}
				}
			}

		}

		for(Map.Entry<String, Integer> entry : unreachableAddresses.entrySet()) {
			System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());

		}
		
		for(String addr : reachableAddresses) {
			System.out.println("Addr: " + addr);
		}

	}

}

