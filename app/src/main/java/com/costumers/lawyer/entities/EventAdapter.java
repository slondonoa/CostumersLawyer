package com.costumers.lawyer.entities;

import java.util.Date;

/**
 * Created by GL552VW-DM337T on 23/03/2017.
 */

public class EventAdapter {

    public  EventAdapter(){

    }

    public  EventAdapter(String Id, String TypeEvent, String Description, String Customer, String StartDate, String EndDate,
                            String Title,
                            String Executed,
                            String FullName,
                            String strStartDate,
                            String strEndDate){
        this.Id=Id;
        this.TypeEvent=TypeEvent;
        this.Description=Description;
        this.Customer=Customer;
        this.StartDate=StartDate;
        this.EndDate=EndDate;
        this.Title=Title;
        this.Executed=Executed;
        this.FullName=FullName;
        this.strStartDate=strStartDate;
        this.strEndDate=strEndDate;

    }

    public String Id;
    public String TypeEvent;
    public String Description;
    public String Customer;
    public String StartDate;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTypeEvent() {
        return TypeEvent;
    }

    public void setTypeEvent(String typeEvent) {
        TypeEvent = typeEvent;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getExecuted() {
        return Executed;
    }

    public void setExecuted(String executed) {
        Executed = executed;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getStrStartDate() {
        return strStartDate;
    }

    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public String getStrEndDate() {
        return strEndDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    public String EndDate;
    public String Title;
    public String Executed;
    public String FullName;
    public String strStartDate;
    public String strEndDate;
}
