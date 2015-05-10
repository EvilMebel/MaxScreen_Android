package pl.pwr.wroc.gospg2.kino.maxscreen_android.utils;

public class Hour {
	public int hour;
	public int minute;
	
	
	
	public Hour() {
		super();
	}



	public Hour(int hour, int minute) {
		super();
		this.hour = hour;
		this.minute = minute;
	}



	public void forward(int h, int m) {
		hour+=h;
		minute+=m;
		
		if(minute>=60) {
			hour++;
			minute-=60;
		}
	}
	
	@Override
	public String toString() {
		return Converter.timeToString(this);
	}
	
	

}
