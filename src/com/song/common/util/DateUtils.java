package com.song.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.util.Assert;

public class DateUtils {
	/**
  * 
  */
	public static final String MATRIX_NULL_FLAG = "/";
	public static final long MILSEC_PER_DAY = 24 * 3600000;
	public static final FastDateFormat FAST_DAY_FORMATTER = FastDateFormat
			.getInstance("yyyy-MM-dd");
	public static final FastDateFormat FAST_MINUTE_FORMATTER = FastDateFormat
			.getInstance("yyyy-MM-dd HH:mm");

	public static final FastDateFormat FAST_HOUR_FORMATTER = FastDateFormat
			.getInstance("yyyy-MM-dd HH");

	public static final FastDateFormat FAST_SECOND_FORMATETR = FastDateFormat
			.getInstance("yyyy-MM-dd HH:mm:ss");
	public static final FastDateFormat FAST_SHORT_FORMATETR = FastDateFormat
			.getInstance("yyyyMMdd");
	public static final FastDateFormat FAST_ONLY_MINUTE_FORMATETR = FastDateFormat
			.getInstance("HH:mm");

	public static String getLastDate(int year, int month) {
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			monthDay[1] = 29;
		}
		int monthDayNum = monthDay[month - 1];
		String end = year + "-" + month + "-" + monthDayNum;
		return end;
	}

	/* 格式化日期为短形式 */
	public static String getShortDate(Date myDate) {
		return FAST_SHORT_FORMATETR.format(myDate);
	}

	/* 格式化日期为标准形式 */
	public static String formatDateTime(Date myDate, String pattern) {
		FastDateFormat format = FastDateFormat.getInstance(pattern);
		return format.format(myDate);
	}

	/* 判断myDate是否为null */
	public static Date isDate(Date myDate) {
		if (myDate == null)
			return new Date();
		return myDate;
	}

	public static Date currentDate() {
		return currentDate(null);
	}

	/**
	 * 
	 * @return
	 */
	public static Date currentDate(DateFormat format) {
		Date today = new Date();
		if (format != null) {
			String string = format.format(today);
			today = parse(string, format);
		}
		return today;
	}

    protected static Calendar convert(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    /**
     * 当前月末日期的
     * @return
     */
    public static String getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date lastDate = calendar.getTime();
        return format(lastDate);
    }
	/**
	 * 查询当天的前n天的具体时间
	 * 
	 * @param n
	 * @return
	 */
	public static String getCurrentDateBefore(int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, -n);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = sdf.format(day.getTime());
		return result;
	}

	/**
	 * 日期差(time1 - time2，返回负数，若time1在time2之前)
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuot(Date time1, Date time2) {
		long quot = 0;
		try {
			Date date1 = time1;
			Date date2 = time2;
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * 日期差(忽略时间部分，time1 - time2，返回负数，若time1在time2之前)
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuoteIgnoreTime(Date time1, Date time2) {
		return getQuot(org.apache.commons.lang.time.DateUtils.truncate(time1,
				Calendar.DATE),
				org.apache.commons.lang.time.DateUtils.truncate(time2,
						Calendar.DATE));
	}

	// 判断日期为星期几,1为星期日com.vnvtrip.util,依此类推
	public static int dayOfWeek(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x;
	}

	public static String dayOfWeek2(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeekByDayNum(x);
	}

	public static String dayOfWeekByDayNum(int x) {
		String str = "周日";
		if (x == 7) {
			str = "周六";
		} else if (x == 6) {
			str = "周五";
		} else if (x == 5) {
			str = "周四";
		} else if (x == 4) {
			str = "周三";
		} else if (x == 3) {
			str = "周二";
		} else if (x == 2) {
			str = "周一";
		}
		return str;
	}

	// 根据当前一个星期中的第几天得到它的日期
	public static Date getDateOfCurrentWeek(int day) {
		Calendar aCalendar = Calendar.getInstance();
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		aCalendar.add(Calendar.DAY_OF_WEEK, day - (x - 1));
		return aCalendar.getTime();
	}

	// 某一天在一个月中的第几周
	public static int getWeekOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	public static Date addSomeDay(Date oldDate, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.add(Calendar.DATE, num);
		return calendar.getTime();
	}

	// 把日期“2006-09-01”转化成20060901
	public static String Dateformat(String date) {
		int i = date.length();
		StringBuffer newdate = new StringBuffer(date.substring(0, 4));
		if (i == 8) {
			newdate.append(0);
			newdate.append(date.substring(5, 6));
			newdate.append(0);
			newdate.append(date.substring(7, 8));
		} else if (i == 9) {
			if (date.substring(7, 8).equalsIgnoreCase("-")) {
				newdate.append(date.substring(5, 7));
				newdate.append(0);
				newdate.append(date.substring(8, 9));
			} else {
				newdate.append(0);
				newdate.append(date.substring(5, 6));
				newdate.append(date.substring(7, 9));
			}
		} else {
			newdate.append(date.substring(5, 7));
			newdate.append(date.substring(8, 10));
		}
		return newdate.toString();
	}

	/* 新增static方法 */
	/* 格式话日期为yyyy-MM-dd形式 */
	public static String formatDate(Date myDate) {
		return FAST_DAY_FORMATTER.format(myDate);
	}

	/* 格式话日期为yyyy-MM-dd HH:mm形式 */
	public static String formatDateMinutes(Date myDate) {
		return FAST_MINUTE_FORMATTER.format(myDate);
	}

	/* 格式话日期为yyyy-MM-dd HH:mm:ss形式 */
	public static String formatDateTime(Date myDate) {
		return FAST_SECOND_FORMATETR.format(myDate);
	}

	/* 格式话日期为yyyy-MM-dd HH:mm:ss形式 */
	public static String formatDateMinutesTime(Date myDate) {
		return FAST_ONLY_MINUTE_FORMATETR.format(myDate);
	}

	/* 将字符串转换成日期 */
	public static Date getDateByString(String rq) {
		DateFormat df = new SimpleDateFormat();
		Date d = null;
		try {
			d = df.parse(rq);
		} catch (Exception e) {
		}
		return d;
	}

	public static Date getDateByString(String str, String pattern) {
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat(pattern);
			return sdf.parse(str);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 比较时间是否相同
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean equals(Date start, Date end) {
		if (start != null && end != null && start.getTime() == end.getTime()) {
			return true;
		}
		return false;
	}

	public static final Date convertStringToDate(String aMask, String strDate) {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		try {
			date = df.parse(strDate);
		} catch (Exception pe) {
		}
		return (date);
	}

	/**
	 * 当前月份第一天
	 * 
	 * @return
	 */
	public static Date getCurrentMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 得到当前系统日期
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Date myDate = new Date(System.currentTimeMillis());
		return formatDateTime(myDate);
	}

	public static boolean isSameDay(Date c1, Date c2) {
		return formatDate(c1).equals(formatDate(c2));
	}

	public static Calendar string2Cal(String arg) {
		SimpleDateFormat sdf = null;
		String trimString = arg.trim();
		if (trimString.length() > 14)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(trimString);
		} catch (ParseException e) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal;
	}

	/**
	 * 匹配是否在某个时间段中
	 * 
	 * @param timePeriod
	 *            "00:00-06:00"格式
	 * @param time
	 *            "07:30"
	 * @return
	 */
	public static boolean isInPeriod(String timePeriod, String time) {
		String startTime = timePeriod.substring(0, 5);
		String endTime = timePeriod.substring(6);
		String timeTime = time;
		// 和时间段的开始或者结束刚好相等的时候
		if (startTime.equalsIgnoreCase(timeTime)
				|| endTime.equalsIgnoreCase(timeTime)) {
			return true;
		}
		SimpleDateFormat d = new SimpleDateFormat("HH:mm");
		try {
			Date startDate = d.parse(startTime);
			Date endDate = d.parse(endTime);
			Date timeDate = d.parse(timeTime);
			if (timeDate.after(startDate) && timeDate.before(endDate)) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isBetween(Date date, Date from, Date to) {
		Assert.notNull(date, "date cannot be null.");
		Assert.notNull(from, "from cannot be null.");
		Assert.notNull(to, "to cannot be null.");
		Assert.isTrue(!from.after(to), "from cannot be after to.");
		return !date.before(from) && !date.after(to);
	}

	public static Date ifNull(Date date, Date defaultDate) {
		return date != null ? date : defaultDate;
	}

	public static String format(Date date, DateFormat df) {
		if (date == null) {
			return "";
		} else if (df != null) {
			return df.format(date).toLowerCase();
		} else {
			return FAST_DAY_FORMATTER.format(date);
		}
	}

	public static String format(Date date) {
		return format(date, null);
	}

	public static Date parseUseDefaultFormat(String date) {
		return parse(date, getDayFormatter());
	}

	public static Date parse(String date, DateFormat df) {
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("parse date [" + date
					+ "] failed in use [" + getDayFormatter() + "]", e);
		}
	}

	// 增加或减少几个月
	public static Date addMonth(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.MONTH, num);
		return startDT.getTime();
	}

	// 增加或减少天数
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	public static List<Date> splitDays(Date start, Date end) {
		return splitDays(start, end, null);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param dayOfWeeks
	 *            周日(1), 周一(2), ..., 周六(7)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Date> splitDays(Date start, Date end, int[] dayOfWeeks) {
		List<Date> dates = new ArrayList<Date>();
		for (Date date = start; !date.after(end); date = addDay(date, 1)) {
			if (ArrayUtils.isEmpty(dayOfWeeks)
					|| ArrayUtils.contains(dayOfWeeks, date.getDay() + 1)) {
				dates.add(date);
			}
		}
		return dates;
	}

	/**
	 * 取得时间距阵
	 * 
	 * @param dayStart
	 * @param dayEnd
	 * @return
	 */
	public static List<String> getDayPeriodFullMatrix(String dayStart,
			String dayEnd) {
		List<String> retList = new ArrayList<String>();
		Calendar calStart = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
		String str = null;
		try {
			calStart.setTime(getDayFormatter().parse(dayStart));
			calEnd.setTime(getDayFormatter().parse(dayEnd));
			calEnd.add(Calendar.DATE, 1); // 包含最后一天
			// 前端补足
			int dayOfWeek = calStart.get(Calendar.DAY_OF_WEEK);
			for (; dayOfWeek > 1; dayOfWeek--) {
				retList.add(MATRIX_NULL_FLAG);
			}
			// 中间部分
			for (; calStart.before(calEnd); calStart.add(Calendar.DATE, 1)) {
				str = FAST_DAY_FORMATTER.format(calStart.getTime());
				retList.add(str);
			}
			// 后端补足
			dayOfWeek = calEnd.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != 1) {
				for (; dayOfWeek != 1 && dayOfWeek <= 7; dayOfWeek++) {
					retList.add(MATRIX_NULL_FLAG);
				}
			}
			if (retList.size() < 42) {
				int length = 42 - retList.size();
				for (int i = 0; i < length; i++) {
					retList.add(MATRIX_NULL_FLAG);
					System.out.println(i);
				}
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return retList;
	}

	/**
	 * 取得时间距阵
	 * 
	 * @param dayStart
	 * @param dayEnd
	 * @return
	 */
	public static List<String> getDayPeriodFullMatrix(Date dayStart, Date dayEnd) {
		return getDayPeriodFullMatrix(format(dayStart), format(dayEnd));
	}

	/**
	 * <li>SimpleDateFormat is not thread saft, so when you need, you should
	 * create it</li>
	 */
	public static SimpleDateFormat getDayFormatter() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	public static SimpleDateFormat getMinuteFormatter() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	public static SimpleDateFormat getMonthFormatter() {
		return new SimpleDateFormat("yyyy-MM");
	}

	public static SimpleDateFormat getSecondFormatter() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串格式时间转换到对象时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date string2DateTime(String str) {
		SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = fo.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回date时间 只到天 2008-05-20 00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNoHHMMDate(Date date) {
		return org.apache.commons.lang.time.DateUtils.truncate(date,
				Calendar.DATE);
	}

	/**
	 * 增加天
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addDays(Date date, int i) {
		return org.apache.commons.lang.time.DateUtils.addDays(date, i);
	}

	public static long diff(Date d1, Date d2, String field) {
		long d1t = d1.getTime();
		long d2t = d2.getTime();
		if ("middleNight".equalsIgnoreCase(field)) { // 计算间夜，先除后减
			return d1t / MILSEC_PER_DAY - d2t / MILSEC_PER_DAY;
		} else {
			return d2t - d1t;
		}
	}

	/**
	 * 获得正时正分格式 HHMM00
	 * 
	 * @param myDate
	 * @return
	 */
	public static String getLikeTimePointCode(Date myDate) {
		String myTime = formatDateMinutesTime(myDate);
		String[] myTimeList = myTime.split(":");
		return myTimeList[0] + myTimeList[1] + "00";
	}

	/**
	 * 筛选日期(400系统)
	 * 
	 * @param start
	 * @param end
	 * @param dayOfWeeks
	 * @return
	 */
	public static List<Date> getDates(Date start, Date end, Integer[] dayOfWeeks) {
		List<Date> list = new ArrayList<Date>();
		Date date = start;
		for (int i = 1; i <= 7; i++) {
			if (ArrayUtils.contains(dayOfWeeks, dayOfWeek(date))) {
				while (date.compareTo(end) <= 0) {
					list.add(date);
					date = addDays(date, 7);
				}
				date = addDays(start, i);
			} else {
				date = addDays(date, 1);
			}
		}
		return list;
	}

	/**
	 * 比较日期
	 */
	public static boolean constractDate(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		Date date1 = string2DateTime(formatDate(d1));
		Date date2 = string2DateTime(formatDate(d2));
		if (date1.equals(date2))
			return true;
		return date1.before(date2);
	}

	/**
	 * 星期几
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 比较两日期是否相等
	 */
	public static boolean equalDate(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		Date date1 = string2DateTime(formatDate(d1));
		Date date2 = string2DateTime(formatDate(d2));
		return date1.equals(date2);
	}

	/**
	 * 将某个日期转换成业务逻辑上面的星期几 calendar: 周一：2；周二：3；周三：4；周四：5；周五：6；周六：7；周日：1 业务逻辑 ：
	 * 周一：1；周二：2；周三：3；周四：4；周五：5；周六：6；周日：7
	 * 
	 * @param day
	 * @return
	 */
	public static String explainDayOfWeek(Date date) {
		int departDay = dayOfWeek(date);
		if (departDay == 1) {
			return "7";
		} else if (departDay == 2) {
			return "1";
		} else {
			return String.valueOf(departDay - 1);
		}
	}

	/**
	 * 比较 是否当前日子以前的日子(不包含当前天)
	 */
	public static boolean gtAfter(Date date) {
		return date.before(addDay(new Date(), -1));
	}

	/**
	 * 获得当前日期字符串yyyy-mm-dd
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(new Date());
	}

	public static String getCurrentFullDate() {
		return FAST_SECOND_FORMATETR.format(new Date());
	}

	/**
	 * 获得今日的毫秒数
	 * 
	 * @return
	 */
	public static Long getCurrentDateMillis() {

		return getDateMillis(getCurrentDate());
	}

	/**
	 * 获得指定日的毫秒
	 * 
	 * @param d
	 *            日期串格式yyyy-MM-dd
	 * @return
	 */
	public static Long getDateMillis(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date date = sdf.parse(d);

			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return 数据格式:2014-14-19 12:30:30
	 */
	public static String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	/**
	 * 获取指定日期的季节
	 * 
	 * @param date
	 * @return
	 * @Description:
	 */
	public static String getSeaSon(Date date) {
		String[] monSeaSon = { "冬", "冬", "春", "春", "春", "夏", "夏", "夏", "秋",
				"秋", "秋", "冬" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return monSeaSon[calendar.get(Calendar.MONTH)];
	}

	/**
	 * 获得当前年份yyyy
	 * 
	 * @return
	 */
	public static String getCurrentYear() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}

	/**
	 * 获取需要替换的年份
	 * 
	 * @param date
	 * @return
	 * @Description:
	 */
	public static String getFromYear() {
		return new SimpleDateFormat("yyyy").format(addDays(new Date(), -31));
	}

	/**
	 * 获取需要替换成的年份
	 * 
	 * @return
	 */
	public static String getToYear() {
		return new SimpleDateFormat("yyyy").format(addDays(new Date(), 31));
	}

	public static void main(String[] args) {

		System.out.println(DateUtils.getCurrentDate());
		System.out.println(DateUtils.getLastDayOfMonth());
	}
}