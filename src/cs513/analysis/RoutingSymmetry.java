package cs513.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.parser.OutputParser;
import cs513.parser.SimplifyParser;
import cs513.utils.Utils;

public class RoutingSymmetry {

	private int[] symmetricPath = new int[30];
	private int totalPath = 0;
	
	private static final String DATASET = "trace_1";

	public void process(HashMap<String, HashMap<String, ArrayList<RoutingPath>>> hostmap) {
		for(Map.Entry<String, HashMap<String, ArrayList<RoutingPath>>> entry : hostmap.entrySet()) {
			for(Map.Entry<String, ArrayList<RoutingPath>> e : entry.getValue().entrySet()) {
				ArrayList<RoutingPath> pathList = e.getValue();
				for (RoutingPath path: pathList) {
					totalPath++;
					RoutingPath reversePath = Utils.reversePath(hostmap, path);
					if (reversePath != null) {
						// if two paths are exactly the same
						ArrayList<IPaddress> reverseHops = reversePath.getPath();
						ArrayList<IPaddress> hops = path.getPath();
						int endHop = reverseHops.size() - 1;
						boolean isPathSymmtric = true;
						
						List<IPaddress> common = new ArrayList<IPaddress>(reverseHops);
						common.retainAll(hops);
						int diffNum = Math.min(reverseHops.size(), hops.size()) - common.size();
						if (diffNum == -1){
							System.out.println("");
						}
						symmetricPath[diffNum]++;
						
						
//						if ( reverseHops.size() == hops.size()) {
//							for (int i = 0; i < reverseHops.size(); i++) {
//								if (!reverseHops.get(endHop - i).equalsLocalNetwork(hops.get(i))) {
//									isPathSymmtric = false;
//									break;
//								}
//							}
//							if (isPathSymmtric) {
//								symmetricPath[0]++;
//							}
//						} else if ( Math.abs(reverseHops.size() - hops.size()) == 1) { // One hop difference
//							List<IPaddress> common = new ArrayList<IPaddress>(reverseHops);
//							common.retainAll(hops);
//							if (common.size() == Math.min(reverseHops.size(), hops.size()) - 1) {
//								symmetricPath[1]++;
//							}
//						}
					}
				}
			}
		}
		
		System.out.println("The total number of paths is: " + totalPath);

		for (int i = 0; i < symmetricPath.length; i++) {
			System.out.printf("The number of symmetric paths with %d is: %d\n", i, symmetricPath[i] / 2);
		}
		
		
	}

	public static void main(String[] args) {
		RoutingSymmetry routSym = new RoutingSymmetry();
		File[] files = new File("output_" + DATASET).listFiles();

		SimplifyParser parser = new SimplifyParser();
		try {
			parser.showFiles(files);
			routSym.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
