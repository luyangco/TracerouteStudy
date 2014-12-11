package cs513.model;

import java.util.Objects;

public class IPaddress {
	String seg_1;
	String seg_2;
	String seg_3;
	String seg_4;
	float latency;
	
	public IPaddress(String seg_1, String seg_2, String seg_3, String seg_4) {
		this.seg_1 = seg_1;
		this.seg_2 = seg_2;
		this.seg_3 = seg_3;
		this.seg_4 = seg_4;
	}
	
	public IPaddress(String wholeIP) {
		String [] segments = wholeIP.split("\\.");
		this.seg_1 = segments[0];
		this.seg_2 = segments[1];
		this.seg_3 = segments[2];
		this.seg_4 = segments[3];
	}

	public String getSeg_1() {
		return seg_1;
	}

	public String getSeg_2() {
		return seg_2;
	}

	public String getSeg_3() {
		return seg_3;
	}

	public String getSeg_4() {
		return seg_4;
	}

	public float getLatency() {
		return latency;
	}

	public void setLatency(float latency) {
		this.latency = latency;
	}

	/* toString that contains latency */
	public String toString() {
		return seg_1 + "." + seg_2 + "." + seg_3 + "." + seg_4 + " ( " + latency + "ms )";
	}
	
	public String ipToString() {
		return seg_1 + "." + seg_2 + "." + seg_3 + "." + seg_4;
	}
	
	@Override
	public boolean equals(Object ip) {
		if (!(ip instanceof IPaddress)) {
			return false;
		}
		
		if(this.ipToString().equalsIgnoreCase(ip.toString())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.ipToString());
	}
	

}
