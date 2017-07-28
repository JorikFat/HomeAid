package ru.jorik.homeaid;

import java.util.Date;

/**
 * Created by 111 on 26.07.2017.
 */

public class Medicine {

    private long id;
    private String name;
    private Date dateStart;
    private Date dateOver;
    private int warrantyMonth;

    public Medicine(String name) {
        this.name = name;
    }

    public Medicine(String name, Date dateOver) {
        this.name = name;
        this.dateOver = dateOver;
    }

    public Medicine(long id, String name, Date dateOver) {
        this.id = id;
        this.name = name;
        this.dateOver = dateOver;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateOver() {
        return dateOver;
    }

    public int getWarrantyMonth() {
        return warrantyMonth;
    }
}
