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
	
	public IPaddress getLastHop() {
		return m_path.get(m_path.size() - 1);
	}

	public boolean within2Hrs(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd.hh-mm-ss");
		sdf.setLenient(false);

		// if not valid, it will throw ParseException
		Date date;
		try {
			date = sdf.parse(timestamp);


			// current date after 1 hour
			Calendar currentDateAfter1Hr = Calendar.getInstance();
			currentDateAfter1Hr.add(Calendar.HOUR, 1);

			// current date before 1 hour
			Calendar currentDateBefore1Hr = Calendar.getInstance();
			currentDateBefore1Hr.add(Calendar.HOUR, -1);

			/*************** verbose ***********************/
			System.out.println("\n\ncurrentDate : "
					+ Calendar.getInstance().getTime());
			System.out.println("currentDateAfter3Months : "
					+ currentDateAfter1Hr.getTime());
			System.out.println("currentDateBefore3Months : "
					+ currentDateBefore1Hr.getTime());
			System.out.println("dateToValidate : " + timestamp);
			/************************************************/

			if (date.before(currentDateAfter1Hr.getTime())
					&& date.after(currentDateBefore1Hr.getTime())) {

				//ok everything is fine, date in range
				return true;

			} else {

				return false;

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
