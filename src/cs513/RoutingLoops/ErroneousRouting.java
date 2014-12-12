package cs513.RoutingLoops;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;
import cs513.parser.OutputParser;

public class ErroneousRouting {
	
	private int m_errPath = 0;
	
	private static HashMap<String, IPaddress> m_mapping = new HashMap<String, IPaddress>();
	static {
		m_mapping.put("planetlab2.csee.usf.edu", new IPaddress("131.247.2.242"));
		m_mapping.put("planetlab1.csuohio.edu", new IPaddress("137.148.16.10"));
		m_mapping.put("planetlab1.acis.ufl.edu", new IPaddress("128.227.150.11"));
		m_mapping.put("ricepl-1.cs.rice.edu", new IPaddress("128.42.142.41"));
		m_mapping.put("planetlab1.cs.pitt.edu", new IPaddress("192.52.240.213"));
		m_mapping.put("pl3.cs.unm.edu", new IPaddress("198.83.85.46"));
		m_mapping.put("planetlab2.cis.upenn.edu", new IPaddress("158.130.6.253"));
		m_mapping.put("planetlab1.clemson.edu", new IPaddress("130.127.39.152"));
		m_mapping.put("righthand.eecs.harvard.edu", new IPaddress("140.247.60.126"));
		m_mapping.put("planetlab1.utdallas.edu", new IPaddress("129.110.125.51"));
		m_mapping.put("planetlab-3.cmcl.cs.cmu.edu", new IPaddress("128.2.211.115"));
		m_mapping.put("75-130-96-13.static.oxfr.ma.charter.com", new IPaddress("75.130.96.13"));
		m_mapping.put("planetlab1.cs.du.edu", new IPaddress("130.253.21.121"));
		m_mapping.put("pl-node-1.csl.sri.com", new IPaddress("192.12.33.102"));
		m_mapping.put("planetlab-4.eecs.cwru.edu", new IPaddress("129.22.150.29"));
		m_mapping.put("planetlab4.ie.cuhk.edu.hk", new IPaddress("137.189.98.209"));
		m_mapping.put("planet-lab4.uba.ar", new IPaddress("157.92.44.104"));
		m_mapping.put("plonk.cs.uwaterloo.ca", new IPaddress("129.97.74.14"));
		m_mapping.put("planetlab2.koganei.itrc.net", new IPaddress("133.69.32.133"));
		m_mapping.put("planetlab3.cesnet.cz", new IPaddress("195.113.161.84"));
		m_mapping.put("planetlab1.dojima.wide.ad.jp", new IPaddress("203.178.133.11"));
		m_mapping.put("planetlab1.netlab.uky.edu", new IPaddress("128.163.142.20"));
		m_mapping.put("planetlab1.postel.org", new IPaddress("206.117.37.4"));
		m_mapping.put("planetlab1.cti.espol.edu.ec", new IPaddress("200.10.150.252"));
		m_mapping.put("planetlab1.cs.otago.ac.nz", new IPaddress("139.80.206.132"));
		m_mapping.put("planet-plc-1.mpi-sws.org", new IPaddress("139.19.158.225"));
		m_mapping.put("pl1.eng.monash.edu.au", new IPaddress("130.194.252.8"));
		m_mapping.put("planetlab2.cs.okstate.edu", new IPaddress("139.78.141.245"));
		m_mapping.put("planetlab1.ustc.edu.cn", new IPaddress("202.141.161.43"));
		m_mapping.put("planetlab-2.sjtu.edu.cn", new IPaddress("202.112.28.100"));
		m_mapping.put("planetlab-01.vt.nodes.planet-lab.org", new IPaddress("198.82.160.238"));
		m_mapping.put("pl1.6test.edu.cn", new IPaddress("219.243.208.60"));
		m_mapping.put("pli1-pa-6.hpl.hp.com", new IPaddress("204.123.28.57"));
		m_mapping.put("planetlab3.inf.ethz.ch", new IPaddress("192.33.90.68"));
		m_mapping.put("planetlab2.cs.unc.edu", new IPaddress("204.85.191.11"));
		m_mapping.put("planetlab-1.scie.uestc.edu.cn", new IPaddress("222.197.180.137"));
	}
	
	public String getKeyByValue(IPaddress ip) {
		for (Entry<String, IPaddress> entry : m_mapping.entrySet()) {
			if (ip.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public RoutingPath reversePath(HashMap<String, HashMap<String, ArrayList<RoutingPath>>> hostmap, RoutingPath path) {
		String filename = getKeyByValue(path.getSrcIP()) + ".txt";
		HashMap<String, ArrayList<RoutingPath>> pathMap = hostmap.get(filename);
		ArrayList<RoutingPath> pathList = pathMap.get(path.getSrcIP().ipToString());
		for (RoutingPath p : pathList) {
			if (path.within2Hrs(p.getTimestamp())) {
				if (!p.getSrcIP().equals(p.getDestIP())) {
					return p;
				}
			}
		}
		return null;
	}
	
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
							RoutingPath oppoPath = reversePath(hostmap, path);
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
	
	public static void main(String[] args) {

		ErroneousRouting tester = new ErroneousRouting();
		File[] files = new File("output_trace_1").listFiles();

		OutputParser parser = new OutputParser();
		try {
			parser.showFiles(files);
			tester.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
