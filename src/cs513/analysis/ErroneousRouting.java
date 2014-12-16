package cs513.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.utils.Utils;

public class ErroneousRouting {
	
	private int m_errPath = 0;
	
	
	public void process(HashMap<String, HashMap<String, ArrayList<RoutingPath>>> hostmap) {
		for(Map.Entry<String, HashMap<String, ArrayList<RoutingPath>>> entry : hostmap.entrySet()) {
			for(Map.Entry<String, ArrayList<RoutingPath>> e : entry.getValue().entrySet()) {
				ArrayList<RoutingPath> pathList = e.getValue();
				for (RoutingPath path: pathList) {
					for(IPaddress ip : path.getPath()) {
						// if an erroneous path detected, we want to see if this node is reachable by other hosts 
						if (ip.ipToString().equalsIgnoreCase("0.0.0.0") && path.getPath().size() == 1) {
							m_errPath++;
							System.out.println("Found a erroneous Path (srcIp=" + path.getSrcIP() + 
									" destIp=" + path.getDestIP() + " Timestamp=" + path.getTimestamp() + ")");
							RoutingPath oppoPath = Utils.reversePath(hostmap, path);
							if (oppoPath != null && oppoPath.getLastHop().equals(oppoPath.getDestIP())) {
								System.out.println("But path from opposite direction could reach the destination Path(SrcIP=" + 
										oppoPath.getSrcIP() + " DestIP=" + oppoPath.getDestIP() + " Timestamp=" + oppoPath.getTimestamp() + ")");
							} else {
								System.out.println("Path from opposite direction also can't reach the destination within 2 hrs");
							}

						}
					}
				}
			}
		}
		System.out.println("Number of Erroneous Path: " + m_errPath);
	}

}
