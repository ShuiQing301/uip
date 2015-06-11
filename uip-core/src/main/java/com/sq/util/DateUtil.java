package com.sq.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间相关util.
 * User: shuiqing
 * Date: 2015/4/15
 * Time: 16:49
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class DateUtil {

    public static final String DATE_FORMAT_DAFAULT = "yyyyMMdd";

    public static final String DATE_FORMAT_MONTH = "yyyyMM";

    public static final String DATE_FORMAT_Y_M_D = "yyyy-MM-dd";

    public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_DAFAULTYMDHMS = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_YYMM = "yyMM";

    public static final String DATE_FORMAT_YY_MM = "yy-MM";

    public static final String DATE_FORMAT_MM_DD = "MM-dd";

    public static final String DATE_FORMAT_MMDD = "MMdd";

    public static final String DATE_FORMAT_DD = "dd";

    /**
     * 默认日期类型格式.
     */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DAFAULT);

    private static SimpleDateFormat stringToSdf(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf;
    }

    /**
     * 格式化日期为yyyyMMdd格式
     * @param date 需要格式化的日期
     * @return 返回格式化后的字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat.format(date);
    }

    /**
     * 根据指定格式化来格式日期.
     * 2014年1月26日 下午3:44:23 shuiqing添加此方法
     * @param date 待格式化的日期,如date为空，则返回空字符串
     * @param pattern 格式化样式或分格,如yyMMddHHmmss。如该参数为空或者不符合要求，则使用默认的yyyyMMdd格式进行转换
     * @return 字符串型日期.
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (StringUtils.isBlank(pattern)) {
            return formatDate(date);
        }
        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat(pattern);
        } catch (Exception e) {
            e.printStackTrace();
            return formatDate(date);
        }
        return simpleDateFormat.format(date);
    }

    /**
     * 根据整型日期生成Calendar
     * @param intDate 整形格式的日期；如20140124
     * @return
     */
    public static Calendar intDate2Calendar(int intDate) {
        int year = intDate / 10000;
        int month = (intDate % 10000) / 100;
        int day = intDate % 100;

        Calendar ret = Calendar.getInstance();
        ret.set(year, month - 1, day);
        return ret;
    }

    /**
     * 格式化Calendar,返回默认的日期格式字符串
     * 本方法不指定format，采用默认的format，DateUtil.DATE_FORMAT_DAFAULT；如果需要制定format，
     * 请调用formatCalendar(Calendar calendar,String fomart)
     * @param calendar 需要格式化的日历对象
     * @return
     */
    public static String formatCalendar(Calendar calendar) {
        return formatCalendar(calendar, DATE_FORMAT_DAFAULT);
    }

    /**
     * 格式化Calendar,返回指定的日期格式字符串
     * @param calendar 需要格式化的日历对象
     * @param fomart 格式化日历格式
     * @return
     */
    public static String formatCalendar(Calendar calendar,String fomart) {
        if (calendar == null) {
            return "";
        }
        return formatDate(calendar.getTime(), fomart);
    }

    /**
     * 将java.util.Date类型转换成java.util.Calendar类型
     * @param date
     * @return
     */
    public static Calendar dateToCalendar(Date date) {
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(date);
        return cdate;
    }

    /**
     * 指定日期月份的第一天
     * 2014年2月13日 下午7:57:15 shuiqing添加此方法
     * @param date
     * @return
     */
    public static Date getStartDayOfMonth(Date date) {
        Calendar cdate = dateToCalendar(date);
        cdate.set(Calendar.DAY_OF_MONTH, 1);
        return cdate.getTime();
    }

    /**
     * 计算指定日期的月最后一天
     * 2014年2月13日 下午7:45:21 shuiqing添加此方法
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * 计算两个int日期之间所有的日期
     * 2014年1月26日 下午3:02:39 shuiqing添加此方法
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> dayListBetweenDate (int startDate, int endDate) {
        if (startDate == 0 || endDate == 0) {
            return null;
        }
        if (startDate > endDate) {
            return null;
        }
        List<String> dayList = new LinkedList<String>();
        Calendar startCar = intDate2Calendar(startDate);
        Calendar endCar = intDate2Calendar(endDate);
        while (!startCar.getTime().equals(endCar.getTime())) {
            dayList.add(formatDate(startCar.getTime(), DATE_FORMAT_DAFAULT));
            startCar.add(Calendar.DATE, 1);
        }
        dayList.add(formatDate(startCar.getTime(), DATE_FORMAT_DAFAULT));

        return dayList;
    }

    /**
     * 计算两个calendar之间的所有日期
     * @param startCal 开始日期
     * @param endCal 结束日期
     * @return
     */
    public static List<Calendar> dayListSinceCal(Calendar startCal, Calendar endCal) {
        if (null == startCal || null == endCal) {
            return null;
        }
        if (endCal.before(startCal)) {
            return null;
        }
        List<Calendar> calendarList = new LinkedList<Calendar>();
        try {
            calendarList.add(DateUtil.stringToCalendar(formatDate(startCal.getTime(), DATE_FORMAT_DAFAULT),DateUtil.DATE_FORMAT_DAFAULT));
            while (startCal.getTime().before(endCal.getTime())) {
                startCal.add(Calendar.DATE, 1);
                calendarList.add(DateUtil.stringToCalendar(formatDate(startCal.getTime(), DATE_FORMAT_DAFAULT),DateUtil.DATE_FORMAT_DAFAULT));
            }
            calendarList.remove(calendarList.size() - 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendarList;
    }

    /**
     * 计算输入日期和当前日期之间的所有的日期
     * @param startCal 开始日期
     * @return
     */
    public static List<Calendar> dayListSinceCal(Calendar startCal) {
        Calendar nowCal = Calendar.getInstance();
        return dayListSinceCal(startCal,nowCal);
    }

    /**
     * 计算一个日期所在月份的所有的天数
     * @param currentDate 当前时间
     * @return
     */
    public static List<String> getAllDaysOfMonth (Date currentDate) {
        Date startDate = getStartDayOfMonth(currentDate);
        Date endDate = getLastDayOfMonth(currentDate);
        List<String> dayList = new LinkedList<String>();
        Calendar startCar = dateToCalendar(startDate);
        Calendar endCar = dateToCalendar(endDate);
        while (!startCar.getTime().equals(endCar.getTime())) {
            dayList.add(formatDate(startCar.getTime(), DATE_FORMAT_DAFAULT));
            startCar.add(Calendar.DATE, 1);
        }
        dayList.add(formatDate(startCar.getTime(), DATE_FORMAT_DAFAULT));

        return dayList;
    }

    /**
     * 把字符串转成Date
     * @param formt  指定的格式
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strDate, String formt) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(formt);
        Date date = sdf.parse(strDate);
        return date;
    }

    /**
     * 把String字符串转换成Calendar
     * @param dateTime 要转化的数据
     * @param format 数据的格式
     * @return
     * @throws ParseException
     */
    public static Calendar stringToCalendar(String dateTime, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 将日期时间从一种格式转换为另一种格式
     * @param srcTime 源串
     * @param srcPattern    源串格式
     * @param destPattern 目标串格式
     * @return String 目标串
     */
    public static String transDateTime(String srcTime, String srcPattern, String destPattern) {
        if (srcTime == null) {
            return "";
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat();
            fmt.applyPattern(srcPattern);
            Date date = fmt.parse(srcTime);
            fmt.applyPattern(destPattern);
            return fmt.format(date);
        } catch (Exception exp) {
        }
        return srcTime;
    }

    public static String getLastDayOfCurrentMonth(String sdf){
        //获取当前月最后一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return formatCalendar(calendar, sdf);
    }

    /**
     * 计算两个时间相差的天数
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获得系统当前时间
     * @param format
     * @return
     */
    public static String getCurrDateTime(String format) {
        String currTimeStr;
        try {
            java.util.Date currDate = new java.util.Date();
            SimpleDateFormat dtFmt = new SimpleDateFormat(format);
            currTimeStr = dtFmt.format(currDate);
        } catch (Exception e) {
            return "";
        }
        return currTimeStr;
    }

    /**
     * 根据提供的日期，获取一天的开始和结束时间
     * @param calendar 输入的日期
     * @return
     */
    public static Calendar[] getDayFirstAndLastCal(Calendar calendar) {
        Calendar[] calArray = new Calendar[2];
        try {
            Date inputDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date startDate = sdf.parse(sdf.format(inputDate));
            calArray[0] = dateToCalendar(startDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D + " 23:59:59");
            Date endDate = stringToSdf(DateUtil.DATE_FORMAT_YMDHMS).parse(sdf1.format(inputDate));
            calArray[1] = dateToCalendar(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calArray;
    }

    /**
     * 根据提供的日期，获取一天的开始和结束时间
     * @return
     */
    public static int[] getDayFirstAndLastInt(Calendar calendar) {
        Calendar[] calendarArray = getDayFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendarArray[1], DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一天的开始和输入时间
     * @param calendar
     * @return
     */
    public static int[] getDayFirstAndInputInt(Calendar calendar) {
        Calendar[] calendarArray = getDayFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendarArray[1], DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     *  根据提供的日期，获取一周的开始和结束时间
     * @param calendar
     * @return
     */
    public static Calendar[] getWeekFirstAndLastCal(Calendar calendar) {
        Calendar[] calArray = new Calendar[2];
        try {
            calArray[0] = getWeekFirstCal(calendar);

            int weekday1 = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE, 8 - weekday1);
            SimpleDateFormat sdf1 = new SimpleDateFormat(DateUtil.DATE_FORMAT_YMDHMS);
            Date endDate = sdf1.parse(stringToSdf(DateUtil.DATE_FORMAT_Y_M_D).format(calendar.getTime()) + " 23:59:59");
            calArray[1] = dateToCalendar(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calArray;
    }

    /**
     * 获取周的第一天
     * @param calendar
     * @return
     */
    public static Calendar getWeekFirstCal(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        try {
            int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
            cal.add(Calendar.DATE, -weekday);
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date startDate = sdf.parse(stringToSdf(DateUtil.DATE_FORMAT_Y_M_D).format(calendar.getTime()) + " 00:00:00");
            cal = dateToCalendar(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 根据提供的日期，获取一周的开始和结束时间
     * @return
     */
    public static int[] getWeekFirstAndLastInt(Calendar calendar) {
        Calendar[] calendarArray = getWeekFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendarArray[1], DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一周的开始和输入的时间
     * @param calendar
     * @return
     */
    public static int[] getWeekFirstAndInputInt(Calendar calendar) {
        Calendar[] calendarArray = getWeekFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendar, DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一月的开始和结束时间
     * @param calendar
     * @return
     */
    public static Calendar[] getMonthFirstAndLastCal(Calendar calendar) {
        Calendar[] calArray = new Calendar[2];
        try {
            Calendar cal = (Calendar) calendar.clone();
            calArray[0] = getMonthFirstCal(cal);

            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            SimpleDateFormat sdf1 = new SimpleDateFormat(DateUtil.DATE_FORMAT_YMDHMS);
            Date endDate = sdf1.parse(stringToSdf(DateUtil.DATE_FORMAT_Y_M_D).format(calendar.getTime()) + " 23:59:59");
            calArray[1] = dateToCalendar(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calArray;
    }

    /**
     * 获取月的第一天
     * @param calendar
     * @return
     */
    public static Calendar getMonthFirstCal(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        try {
            cal.set(Calendar.DATE, 1);
            Date inputDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date startDate = sdf.parse(sdf.format(inputDate));
            cal = dateToCalendar(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 根据提供的日期，获取一月的开始和结束时间
     * @return
     */
    public static int[] getMonthFirstAndLastInt(Calendar calendar) {
        Calendar[] calendarArray = getMonthFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendarArray[1], DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 月的开始时间和输入时间
     * @param calendar
     * @return
     */
    public static int[] getMonthFirstAndInputInt(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        Calendar[] calendarArray = getMonthFirstAndLastCal(cal);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendar, DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一季度的开始和结束时间
     * @param calendar
     * @return
     */
    public static Calendar[] getQuarterFirstAndLastCal(Calendar calendar) {
        Calendar[] calArray = new Calendar[2];
        try {
            calArray[0] = getQuarterFirstCal(calendar);
            calArray[1] = getQuarterEndCal(calendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calArray;
    }

    /**
     * 根据提供的日期，获取一季度的开始和结束时间
     * @return
     */
    public static int[] getQuarterFirstAndLastInt(Calendar calendar) {
        Calendar[] calendarArray = getQuarterFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendarArray[1], DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一季度的开始和输入时间
     * @param calendar
     * @return
     */
    public static int[] getQuarterFirstAndInputInt(Calendar calendar) {
        Calendar[] calendarArray = getQuarterFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendar, DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期,获取日期所在季度的第一天
     * @param calendar
     * @return
     */
    public static Calendar getQuarterFirstCal(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        try {
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            if (currentMonth >= 1 && currentMonth <= 3)
                cal.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                cal.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                cal.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                cal.set(Calendar.MONTH, 9);
            cal.set(Calendar.DATE, 1);
            Date inputDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date startDate = sdf.parse(sdf.format(inputDate));
            cal = dateToCalendar(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 根据提供的日期,获取日期所在季度的最后一天
     * @param calendar
     * @return
     */
    public static Calendar getQuarterEndCal(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        try {
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            if (currentMonth >= 1 && currentMonth <= 3) {
                cal.set(Calendar.MONTH, 2);
                cal.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                cal.set(Calendar.MONTH, 5);
                cal.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                cal.set(Calendar.MONTH,8);
                cal.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                cal.set(Calendar.MONTH, 11);
                cal.set(Calendar.DATE, 31);
            }
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date endDate = sdf.parse(stringToSdf(DateUtil.DATE_FORMAT_Y_M_D).format(cal.getTime()) + " 23:59:59");
            cal = dateToCalendar(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 根据提供的日期，获取一年的开始和结束时间
     * @param calendar
     * @return
     */
    public static Calendar[] getYearFirstAndLastCal(Calendar calendar) {
        Calendar[] calArray = new Calendar[2];
        try {
            calArray[0] = getYearFirstCal(calendar);
            calArray[1] = getYearEndCal(calendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calArray;
    }

    /**
     * 根据提供的日期，获取一年的开始和结束时间
     * @return
     */
    public static int[] getYearFirstAndLastInt(Calendar calendar) {
        Calendar[] calendarArray = getYearFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendarArray[1], DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一年的开始和输入时间
     * @param calendar
     * @return
     */
    public static int[] getYearFirstAndInputInt(Calendar calendar) {
        Calendar[] calendarArray = getYearFirstAndLastCal(calendar);
        int[] dayArray = new int[2];
        dayArray[0] = Integer.parseInt(formatCalendar(calendarArray[0], DateUtil.DATE_FORMAT_DAFAULT));
        dayArray[1] = Integer.parseInt(formatCalendar(calendar, DateUtil.DATE_FORMAT_DAFAULT));
        return dayArray;
    }

    /**
     * 根据提供的日期，获取一年的开始时间
     * @param calendar
     * @return
     */
    public static Calendar getYearFirstCal(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        try {
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DATE, 1);
            Date inputDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date startDate = sdf.parse(sdf.format(inputDate));
            cal = dateToCalendar(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 根据提供的日期，获取一年的开始时间
     * @param calendar
     * @return
     */
    public static Calendar getYearEndCal(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        try {
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DATE, 31);
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D);
            Date endDate = sdf.parse(stringToSdf(DateUtil.DATE_FORMAT_Y_M_D).format(cal.getTime()) + " 23:59:59");
            cal = dateToCalendar(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 获取一天24个时间点
     * @param calendar
     * @return
     */
    public static List<Calendar> get24Hours (Calendar calendar) {
        List<Calendar> calendarList = new ArrayList<Calendar>();
        for (int i=1;i<=24;i++) {
            Calendar clone = (Calendar) calendar.clone();
            clone.add(Calendar.HOUR_OF_DAY, i);
            calendarList.add(clone);
            System.out.println(formatCalendar(clone,DATE_FORMAT_YMDHMS));
        }
        return calendarList;
    }

    /**
     * 获取前一天的日期
     * @param calendar
     * @return
     */
    public static int getPreDay(Calendar calendar) {
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day-1);
        return Integer.parseInt(formatCalendar(calendar));
    }

    /**
     * 获取当天
     * @param calendar
     * @return
     */
    public static int getCurrDay(Calendar calendar) {
        int day = calendar.get(Calendar.DATE);
        return Integer.parseInt(formatCalendar(calendar));
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE, 0);
        List<Calendar> calendarList = DateUtil.get24Hours(cal);
    }
}