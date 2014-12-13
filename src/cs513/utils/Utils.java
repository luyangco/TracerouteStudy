package cs513.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import cs513.model.IPaddress;
import cs513.model.RoutingPath;

public class Utils {
	
	private static HashMap<String, IPaddress> m_hostToIP = new HashMap<String, IPaddress>();
	static {
		m_hostToIP.put("planetlab2.csee.usf.edu", new IPaddress("131.247.2.242"));
		m_hostToIP.put("planetlab1.csuohio.edu", new IPaddress("137.148.16.10"));
		m_hostToIP.put("planetlab1.acis.ufl.edu", new IPaddress("128.227.150.11"));
		m_hostToIP.put("ricepl-1.cs.rice.edu", new IPaddress("128.42.142.41"));
		m_hostToIP.put("planetlab1.cs.pitt.edu", new IPaddress("192.52.240.213"));
		m_hostToIP.put("pl3.cs.unm.edu", new IPaddress("198.83.85.46"));
		m_hostToIP.put("planetlab2.cis.upenn.edu", new IPaddress("158.130.6.253"));
		m_hostToIP.put("planetlab1.clemson.edu", new IPaddress("130.127.39.152"));
		m_hostToIP.put("righthand.eecs.harvard.edu", new IPaddress("140.247.60.126"));
		m_hostToIP.put("planetlab1.utdallas.edu", new IPaddress("129.110.125.51"));
		m_hostToIP.put("planetlab-3.cmcl.cs.cmu.edu", new IPaddress("128.2.211.115"));
		m_hostToIP.put("75-130-96-13.static.oxfr.ma.charter.com", new IPaddress("75.130.96.13"));
		m_hostToIP.put("planetlab1.cs.du.edu", new IPaddress("130.253.21.121"));
		m_hostToIP.put("pl-node-1.csl.sri.com", new IPaddress("192.12.33.102"));
		m_hostToIP.put("planetlab-4.eecs.cwru.edu", new IPaddress("129.22.150.29"));
		m_hostToIP.put("planetlab4.ie.cuhk.edu.hk", new IPaddress("137.189.98.209"));
		m_hostToIP.put("planet-lab4.uba.ar", new IPaddress("157.92.44.104"));
		m_hostToIP.put("plonk.cs.uwaterloo.ca", new IPaddress("129.97.74.14"));
		m_hostToIP.put("planetlab2.koganei.itrc.net", new IPaddress("133.69.32.133"));
		m_hostToIP.put("planetlab3.cesnet.cz", new IPaddress("195.113.161.84"));
		m_hostToIP.put("planetlab1.dojima.wide.ad.jp", new IPaddress("203.178.133.11"));
		m_hostToIP.put("planetlab1.netlab.uky.edu", new IPaddress("128.163.142.20"));
		m_hostToIP.put("planetlab1.postel.org", new IPaddress("206.117.37.4"));
		m_hostToIP.put("planetlab1.cti.espol.edu.ec", new IPaddress("200.10.150.252"));
		m_hostToIP.put("planetlab1.cs.otago.ac.nz", new IPaddress("139.80.206.132"));
		m_hostToIP.put("planet-plc-1.mpi-sws.org", new IPaddress("139.19.158.225"));
		m_hostToIP.put("pl1.eng.monash.edu.au", new IPaddress("130.194.252.8"));
		m_hostToIP.put("planetlab2.cs.okstate.edu", new IPaddress("139.78.141.245"));
		m_hostToIP.put("planetlab1.ustc.edu.cn", new IPaddress("202.141.161.43"));
		m_hostToIP.put("planetlab-2.sjtu.edu.cn", new IPaddress("202.112.28.100"));
		m_hostToIP.put("planetlab-01.vt.nodes.planet-lab.org", new IPaddress("198.82.160.238"));
		m_hostToIP.put("pl1.6test.edu.cn", new IPaddress("219.243.208.60"));
		m_hostToIP.put("pli1-pa-6.hpl.hp.com", new IPaddress("204.123.28.57"));
		m_hostToIP.put("planetlab3.inf.ethz.ch", new IPaddress("192.33.90.68"));
		m_hostToIP.put("planetlab2.cs.unc.edu", new IPaddress("204.85.191.11"));
		m_hostToIP.put("planetlab-1.scie.uestc.edu.cn", new IPaddress("222.197.180.137"));
	}
	
	private static HashMap<String, Integer> m_IPToTimezone = new HashMap<String, Integer>();
	
	static {
		m_IPToTimezone.put("131.247.2.242", -5);
		m_IPToTimezone.put("137.148.16.10", -5);
		m_IPToTimezone.put("128.227.150.11", -5);
		m_IPToTimezone.put("128.42.142.41", -6);
		m_IPToTimezone.put("192.52.240.213", -5);
		m_IPToTimezone.put("198.83.85.46", -7);
		m_IPToTimezone.put("158.130.6.253", -5);
		m_IPToTimezone.put("130.127.39.152", -5);
		m_IPToTimezone.put("140.247.60.126", -5);
		m_IPToTimezone.put("129.110.125.51", -6);
		m_IPToTimezone.put("128.2.211.115", -5);
		m_IPToTimezone.put("75.130.96.13", -5);
		m_IPToTimezone.put("130.253.21.121", -7);
		m_IPToTimezone.put("192.12.33.102", -6);
		m_IPToTimezone.put("129.22.150.29", -5);
		m_IPToTimezone.put("137.189.98.209", 8);
		m_IPToTimezone.put("157.92.44.104", -3);
		m_IPToTimezone.put("129.97.74.14", -5);
		m_IPToTimezone.put("133.69.32.133", 9);
		m_IPToTimezone.put("195.113.161.84", 1);
		m_IPToTimezone.put("203.178.133.11", 9);
		m_IPToTimezone.put("128.163.142.20", -5);
		m_IPToTimezone.put("206.117.37.4", -8);
		m_IPToTimezone.put("200.10.150.252", -5);
		m_IPToTimezone.put("139.80.206.132", 13);
		m_IPToTimezone.put("139.19.158.225", 1);
		m_IPToTimezone.put("130.194.252.8", 11);
		m_IPToTimezone.put("139.78.141.245", -6);
		m_IPToTimezone.put("202.141.161.43", 8);
		m_IPToTimezone.put("202.112.28.100", 8);
		m_IPToTimezone.put("198.82.160.238", -5);
		m_IPToTimezone.put("219.243.208.60", 8);
		m_IPToTimezone.put("204.123.28.57", -8);
		m_IPToTimezone.put("192.33.90.68", 1);
		m_IPToTimezone.put("204.85.191.11", -5);
		m_IPToTimezone.put("222.197.180.137", 8);


	}
	
	
	public static String getKeyByValue(IPaddress ip) {
		for (Entry<String, IPaddress> entry : m_hostToIP.entrySet()) {
			if (ip.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static IPaddress getHostIPByName(String filename) {
		return m_hostToIP.get(filename);
	}
	
	public static RoutingPath reversePath(HashMap<String, HashMap<String, ArrayList<RoutingPath>>> hostmap, RoutingPath path) {
		if (path.getDestIP().equals(new IPaddress("0.0.0.0"))) {
			return null;
		}
		String filename = getKeyByValue(path.getDestIP()) + ".txt";
		HashMap<String, ArrayList<RoutingPath>> pathMap = hostmap.get(filename);
		ArrayList<RoutingPath> pathList = pathMap.get(path.getSrcIP().ipToString());
		if (pathList == null) {
			return null;
		}
		for (RoutingPath p : pathList) {
			if (path.within2Hrs(p.getTimestamp())) {
				if (!p.getSrcIP().equals(p.getDestIP())) {
					return p;
				}
			}
		}
		return null;
	}
	
	public static int convertToLocalTime(RoutingPath path) {
		int dstTimezoneDiff = m_IPToTimezone.get(path.getDestIP().ipToString());
		int srcTimezoneDiff = m_IPToTimezone.get(path.getSrcIP().ipToString());
		
		int resultInHr = (dstTimezoneDiff + srcTimezoneDiff) / 2;
		int resutlInMin;
		if ((dstTimezoneDiff + srcTimezoneDiff) % 2 == 1) {
			resutlInMin = 30;
		} else if ((dstTimezoneDiff + srcTimezoneDiff) % 2 == -1) {
			resutlInMin = -30;
		} else {
			resutlInMin = 0;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss");
		sdf.setLenient(false);

		// if not valid, it will throw ParseException
		Date date;
		try {
			date = sdf.parse(path.getTimestamp());
			
			// current date after 1 hour
			Calendar localDate = Calendar.getInstance();
			localDate.setTime(date);
			localDate.add(Calendar.HOUR, resultInHr);
			localDate.add(Calendar.MINUTE, resutlInMin);
			
			return localDate.get(Calendar.HOUR_OF_DAY);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	
	// test
	public static void main(String[] args) {
		RoutingPath path = new RoutingPath(new IPaddress("128.163.142.20"), new IPaddress("129.110.125.51"));
		path.setTimestamp("2014-12-13.00-00-00");
		System.out.println(convertToLocalTime(path));
	}
}
