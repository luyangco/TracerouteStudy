package cs513.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.parser.SimplifyParser;
import cs513.utils.Utils;

public class RoutingSymmetry {

	private int[] symmetricPath = new int[30];
	private int totalPath = 0;
	
	// Select the dataset to be analysed, either "trace_1" or "trace_2"
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
						
						List<IPaddress> common = new ArrayList<IPaddress>(reverseHops);
						common.retainAll(hops);
						int diffNum = Math.min(reverseHops.size(), hops.size()) - common.size();
						if (diffNum == -1){
							System.out.println("");
						}
						symmetricPath[diffNum]++;
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
			parser.generateOuput(files);
			routSym.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
