package cs513.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RoutingPath {
	ArrayList <IPaddress> m_path;
	IPaddress m_srcIP;
	IPaddress m_destIP;
	String m_timestamp;
	

	public RoutingPath(IPaddress src, IPaddress dest) {
		 m_path = new ArrayList<IPaddress>();
		 m_srcIP = src;
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

	public String getTimestamp() {
		return m_timestamp;
	}

	public void setTimestamp(String m_timestamp) {
		this.m_timestamp = m_timestamp;
	}

	public IPaddress getSrcIP() {
		return m_srcIP;
	}
	
	public boolean within2Hrs(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss");
		sdf.setLenient(false);

		// if not valid, it will throw ParseException
		Date date, currDate;
		try {
			date = sdf.parse(timestamp);
			currDate = sdf.parse(m_timestamp);


			// current date after 1 hour
			Calendar currentDateAfter1Hr = Calendar.getInstance();
			currentDateAfter1Hr.setTime(currDate);
			currentDateAfter1Hr.add(Calendar.HOUR, 1);

			// current date before 1 hour
			Calendar currentDateBefore1Hr = Calendar.getInstance();
			currentDateBefore1Hr.setTime(currDate);
			currentDateBefore1Hr.add(Calendar.HOUR, -1);

			if (date.before(currentDateAfter1Hr.getTime())
					&& date.after(currentDateBefore1Hr.getTime())) {

				//ok everything is fine, date in range
				return true;

			} else {

				return false;

			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public IPaddress getLastHop() {
		int size = m_path.size();
		return m_path.get(size - 1);
	}
}
