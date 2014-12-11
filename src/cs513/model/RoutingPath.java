package cs513.model;

import java.util.ArrayList;

public class RoutingPath {
	ArrayList <IPaddress> m_path;
	IPaddress m_destIP;
	

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
	
	
	
	

}
