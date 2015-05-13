package pl.pwr.wroc.gospg2.kino.maxscreen_android.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class Converter {

	
	public static String gregToMySQLformat(GregorianCalendar c) {
		int y =  c.get(GregorianCalendar.YEAR);
		int ma =  c.get(GregorianCalendar.MONTH)+1;
		int da =  c.get(GregorianCalendar.DAY_OF_MONTH);
		String m = "";
		String d = "";
		
		if(ma<10)
			m = "0"+ma;
		else
			m = Integer.toString(ma);
		
		if(da<10)
			d = "0"+da;
		else
			d = Integer.toString(da);
		
		return y+"-" + m +"-"+d;
	}
	
	public static String hourToMySQLformat(Hour hour) {
		int ha = hour.hour;
		int ma = hour.minute;
		
		String h = ""; 
		String m = "";		
		String s = "00"; //
		
		if(ma<10)
			m = "0"+ma;
		else
			m = Integer.toString(ma);
		
		if(ha<10)
			h = "0"+ha;
		else
			h = Integer.toString(ha);
		
		return h+":" + m + ":" + s;
	}

	/**
	 * return y-m-d
	 * @param c
	 * @return
	 */
	public static String gregToString(GregorianCalendar c) {
		int y =  c.get(GregorianCalendar.YEAR);
		int m =  c.get(GregorianCalendar.MONTH)+1;
		int d =  c.get(GregorianCalendar.DAY_OF_MONTH);
		
		return y+"." + m +"."+d;
	}

	public static String gregToStringWithDay(GregorianCalendar c) {
		String s;
		switch(c.get(Calendar.DAY_OF_WEEK)) {
			default:
			case GregorianCalendar.MONDAY:
				s = "poniedzialek";
				break;

			case GregorianCalendar.TUESDAY:
				s = "wtorek";
				break;

			case GregorianCalendar.WEDNESDAY:
				s = "sroda";
				break;

			case GregorianCalendar.THURSDAY:
				s = "czwartek";
				break;

			case GregorianCalendar.FRIDAY:
				s = "piatek";
				break;

			case GregorianCalendar.SATURDAY:
				s = "sobota";
				break;

			case GregorianCalendar.SUNDAY:
				s = "niedziela";
				break;


		}

		return gregToString(c) + ", " + s;
	}
	
	public static GregorianCalendar DATETIMEformatToGreg(String s) {
		Log.d("tag","convert:" + s +"|");

		String []ar = s.split("-");
		int y = Integer.valueOf(ar[0]);
		int m = Integer.valueOf(ar[1])-1;
		int d = Integer.valueOf(ar[2].split(" ")[0]);


		
		GregorianCalendar c = new GregorianCalendar();
		c.set(GregorianCalendar.YEAR, y);
		c.set(GregorianCalendar.MONTH, m);
		c.set(GregorianCalendar.DAY_OF_MONTH, d);

		if(s.length()>=15) {
			String hour = s.substring(11, 13);
			String minute = s.substring(14, 16);
			Log.d("tag", "convertTime:" + hour + ":" + minute + "|");

			int h = Integer.valueOf(s.substring(11, 13));
			int min = Integer.valueOf(s.substring(14, 16));
			c.set(Calendar.HOUR, h);
			c.set(Calendar.MINUTE, min);
		}
		
		return c;
	}
	
	public static Hour MySQLformatToHour(String s) {
		String []ar = s.split(":");
		int h = Integer.valueOf(ar[0]);
		int m = Integer.valueOf(ar[1]);
		
		Hour hour = new Hour(h, m);
		
		return hour;
	}

	public static String timeToString(Hour t) {
		String h = "";
		String m = "";
		if (t.hour < 10)
			h = "0" + t.hour;
		else
			h = Integer.toString(t.hour);

		if (t.minute < 10)
			m = "0" + t.minute;
		else
			m = Integer.toString(t.minute);

		return h + ":" + m;
	}

	public static String getHourFromGreCale(GregorianCalendar date) {
		int h = date.get(Calendar.HOUR);
		int m = date.get(Calendar.MINUTE);

		if(m>9) {
			return h + ":" + m;
		} else {
			return h + ":0" + m;
		}
	}
}
