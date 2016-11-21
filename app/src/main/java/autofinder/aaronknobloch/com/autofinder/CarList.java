package autofinder.aaronknobloch.com.autofinder;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by aaron on 8/5/15.
 */
public class CarList {

    private int count = 0;
    private String htmlPage;

    public CarList() {

        htmlPage += "<!DOCTYPE html>" + "\n" +
                "<html>" + "\n" +
                "<body bgcolor=\"#000000\">" + "\n" +
                "<body link=\"#474773\">" + "\n" +
                "<p align=\"center\">" + "\n" + "\n" + "\n" +
                "<font color = \"white\">";

    }

    public synchronized void addCar(String modelYear, String car, String price, String mileage, String adLink, String picLink) {

        WarrantyCalculator calcWarranty = new WarrantyCalculator(modelYear, car, mileage);

        // dont want vehicles less than 12 months on warranty
        if(calcWarranty.monthsLeftOnWarranty() < 12) return;

        // write pic line
        htmlPage += "<a href=\"" + adLink + "\"target=\"_blank\">" + "\n"
                + "<img src=\"" + picLink +"\" alt=\"Car Picture\" style=\"width:225px;height:150px;\""
                + "<br>" + "<br>" + "\n" + "</a>" + "\n";

        // write car title line
        htmlPage += "<font size=\"4\">" + "<b>" + modelYear + " " + car +
                "</font>" + "</b>" + "<br>" + "\n";

        // write warranty line
        htmlPage += calcWarranty.monthsLeftOnWarranty() + " months left on warranty." + "<br>" + "\n";

        // write mileage line
        htmlPage += mileage + " miles" + "<br>" + "\n";

        // write price line
        htmlPage += "$" + price + "<br>" + "<br>" + "<br>" + "\n" + "\n";



        count++;

    }

    public int getCount() {
        return this.count;
    }

    public String getHTML() {
        return this.htmlPage;
    }

}
