package cs513.model;

import java.util.ArrayList;
import java.util.Calendar;

public class RoutingPath {
	ArrayList <IPaddress> m_path;
	IPaddress m_destIP;
	Calendar m_timestamp;
	

	public RoutingPath(IPaddress dest) {
		 m_path = new ArrayList<IPaddress>();
		 m_destIP = dest;
	}
	
	public void add(IPaddress node) {
		m_path.add(node);
	}
	
	public ArrayList<IPaddress> getPath() {
		return m_path;
	}

	public IPaddress getDestIP() {
		return m_destIP;
	}

	public Calendar getTimestamp() {
		return m_timestamp;
	}

	public void setTimestamp(Calendar m_timestamp) {
		this.m_timestamp = m_timestamp;
	}
	
	public IPaddress getLastHop() {
		int size = m_path.size();
		return m_path.get(size - 1);
	}
}
