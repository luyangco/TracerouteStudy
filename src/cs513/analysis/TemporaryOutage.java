package cs513.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.utils.Utils;

public class TemporaryOutage {
	int outageCtr = 0;

	int [] timeOfDay = new int[24];
	int [] outageMapping = new int[30];

	public void process(HashMap<String, HashMap<String, ArrayList<RoutingPath>>> m_hostmap) {
		for(Map.Entry<String,HashMap<String,ArrayList<RoutingPath>>> entry : m_hostmap.entrySet()) {
			HashMap<String,ArrayList<RoutingPath>> pathMap = entry.getValue();
			for(Map.Entry<String,ArrayList<RoutingPath>> e : pathMap.entrySet()) {
				for(RoutingPath path : e.getValue()) {
					IPaddress lasthop = path.getLastHop();
					if (lasthop.equals(path.getDestIP())) {
						int numOfStar = 0;
						for (IPaddress ip : path.getPath()) {
							if (ip.ipToString().equalsIgnoreCase("*.*.*.*")) {
								numOfStar++;
							}
						}
						outageMapping[numOfStar]++;
						if (numOfStar > 0) {
							timeOfDay[Utils.convertToLocalTime(path)]++;
							outageCtr++;
						}
					}
				}
			}

		}


		System.out.println("Time of day pattern");
		for(int index = 0; index < timeOfDay.length; index++) {
			System.out.printf("Hour %2d: %d\n", index, timeOfDay[index]);
		}

		System.out.println("Outage Mapping");
		for(int index = 0; index < outageMapping.length; index++) {
			System.out.printf("Number of outage %2d: %d\n", index, outageMapping[index]);
		}
	}

}
