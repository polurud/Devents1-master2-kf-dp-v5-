package com.dartmouth.kd.devents;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;

/**
 * Campus Event class to store all relevant information about a campus event
 * Created by kathrynflattum on 2/25/18.
 */

public class CampusEvent {

    private Long mId;
    private String Title;
    private String Location;
    private String Description;
   private Calendar Date;
    private Calendar Start;
    private Calendar End;
   private String strDate;
    private String strStart;
    private String strEnd;

    private String URL;
    private double Latitude;
    private double Longitude;
    private int Food;
    private int EventType;
    private int ProgramType;
    private int Year;
    private int Major;
    private int GreekSociety;
    private int Gender;

    public CampusEvent(){
        this.Title = "";
        this.Location = "";
        this.Description = "";

        this.strDate ="";
        this.strStart= "";
        this.strEnd= "";
        this.Date = Calendar.getInstance();
        this.Start = Calendar.getInstance();
        this.End = Calendar.getInstance();
        this.URL = "";
        double dub = 0;
        this.Latitude = dub;
        this.Longitude = dub;
        this.Food = 0;
        this.EventType = 0;
        this.ProgramType = 0;
        this.Year = 0;
        this.Major = 0;
        this.Gender = 0;
        this.GreekSociety=0;


    }

    public String getstrDate() {
        return strDate;
    }

    public void setstrDate(String Date) {
        this.strDate = Date;
    }

    public void setstrDate(int day, int month, int year) {
        //String date = String.valueOf(day) + String.valueOf(month) + String.valueOf(year);
        //this.Date = date;
        Log.d("Date.....", String.valueOf(day) + "..." + String.valueOf(month) + "..." + String.valueOf(year));
    }

    public String getstrStart() {
        return strStart;
    }

    public void setstrStart(String start) {
        strStart = start;
    }

    public String getstrEnd() {
        return strEnd;
    }

    public void setstrEnd(String end) {
        strEnd = end;
    }


    public Long getmId() {
        return mId;
    }

    public void setmId(Long id) {
        this.mId = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    @Exclude
    public Calendar getDate() {
        return Date;
    }

    public void setDate(String date) {
        SimpleDateFormat mparser = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        try {
            this.Start.setTime(mparser.parse(date));
        }catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }

    public void setDate(int year, int month, int day) {
            this.Date.set(year, month, day);
    }


    public long getDateInMillis() {
        return Date.getTimeInMillis();
    }

    public long getStartInMillis() {
        return Start.getTimeInMillis();
    }

    public long getEndInMillis(){
        return End.getTimeInMillis();
    }


    public void setStart(String start) {
        SimpleDateFormat mparser = new SimpleDateFormat("HH:mm");

        try {
            this.Start.setTime(mparser.parse(start));
        }catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    public void setStart(int hour, int minute) {
        this.Start.set(Calendar.HOUR_OF_DAY, hour);
        this.Start.set(Calendar.MINUTE, minute);
        //Log.d(Globals.TAGG, "CE Showing what time start is setStart(int,int)" + this.Start.toString());
       // Log.d(Globals.TAGG, "CE Showing what hour is " + this.Start.get(Calendar.HOUR));

    }

    @Exclude
    public Calendar getStart() {
        return Start;
    }


    public void setEnd(String end) {
       SimpleDateFormat mparser = new SimpleDateFormat("HH:mm");
        try {
            this.End.setTime(mparser.parse(end));
        }catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    public void setEnd(int hour, int minute) {
        this.End.set(Calendar.HOUR_OF_DAY, hour);
        this.End.set(Calendar.MINUTE, minute);

    }

    @Exclude
    public Calendar getEnd() {
        return End;
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getFood(){
        return Food;
    }

    public void setFood(int Food){
        this.Food = Food;
    }

    public int getEventType() {
        return EventType;
    }

    public void setEventType(int EventType) {
        this.EventType = EventType;
    }

    public int getProgramType() {
        return ProgramType;
    }

    public void setProgramType(int ProgramType) {
        this.ProgramType = ProgramType;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int Year) {
        this.Year = Year;
    }

    public int getMajor() {
        return Major;
    }

    public void setMajor(int Major) {
        this.Major = Major;
    }

    public int getGreekSociety() {
        return GreekSociety;
    }

    public void setGreekSociety(int GreekSociety) {
        this.GreekSociety = GreekSociety;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

}