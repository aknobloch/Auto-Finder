package autofinder.aaronknobloch.com.autofinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aaron on 8/5/15.
 */

public class CarsSearch extends AsyncTask<Void, Void, Integer> {

    private CarList list;
    private Context mainContext;
    private ProgressDialog progDialog;
    private String carsLink = "http://www.cars.com/for-sale/searchresults.action?feedSegId=28705"
            + "&transTypeId=28112&sf2Nm=price&requestorTrackingInfo=RTB_SEARCH&yrId=51683&yrId=56007"
            + "&yrId=34923&yrId=39723&yrId=47272&sf1Nm=miles&sf2Dir=DESC&PMmt=0-0-0&zc=30097&rd=100"
            + "&mlgId=28863&prMn=0&sf1Dir=ASC&prMx=12000&searchSource=UTILITY&crSrtFlds=feedSegId-pseudoPrice"
            + "&pgId=2102&rpp=250";
    Integer count = 1;


    public CarsSearch(Context inContext, CarList inList) {
        this.list = inList;
        this.mainContext = inContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = new ProgressDialog(mainContext);
        progDialog.setMessage("Searching for cars...");
        progDialog.setIndeterminate(true);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setCancelable(false);
        progDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {

            URL carsURL = new URL(carsLink);
            HttpURLConnection connection = (HttpURLConnection) carsURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            String line;
            String carInfo = "";

            while((line = in.readLine()) != null) {
                // parse out the current car and all relevant info
                if(line.contains("detPhoto" + count)) {

                    carInfo += line;

                    for(int i = 0; i < 90; i++) {
                        carInfo += in.readLine() + "\n";
                    }
                    // send info to method to parse and enter into database
                    parseInfo(carInfo);
                    // go to next car and clear carInfo
                    count++;
                    carInfo = "";
                }
            }

            in.close();

        } catch(IOException ie) {
            ie.printStackTrace();
            Log.d("Cars Error", "Error accessing Cars.com");
        } finally {

            return count;

        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        progDialog.dismiss();

        if(MainActivity.allTasksFinished()) {
            Intent intent = new Intent(mainContext, ResultsPage.class);
            intent.putExtra(MainActivity.RESULTS_HTML, list.getHTML());
            mainContext.startActivity(intent);
        }
    }

    private void parseInfo(String carInfo) {

        String modelYear;
        String car;
        String price;
        String mileage;
        String adLink;
        String picLink;


        // get car link
        adLink = carInfo.substring(carInfo.indexOf("href") + 6, carInfo.indexOf(">") - 1);
        adLink = "http://www.cars.com/" + adLink;

        // get photo link
        carInfo = carInfo.substring(carInfo.indexOf("data-def-src") + 14);
        picLink = carInfo.substring(0, carInfo.indexOf("\""));

        // get model year
        // truncate carInfo to begin at relevant point
        carInfo = carInfo.substring(carInfo.indexOf("modelYearSort") + 15);
        modelYear = carInfo.substring(0, carInfo.indexOf("<"));

        // get car
        carInfo = carInfo.substring(carInfo.indexOf("mmtSort") + 9);
        car = carInfo.substring(0, carInfo.indexOf("<")) + " (C)";

        // do not want fiats or versas
        String copyOfCar = car.toLowerCase();
        if(copyOfCar.contains("fiat") || copyOfCar.contains("versa")) return;

        // get price
        carInfo = carInfo.substring(carInfo.indexOf("priceSort") + 12);
        price = carInfo.substring(0, carInfo.indexOf("<"));

        // get mileage
        carInfo = carInfo.substring(carInfo.indexOf("milesSort") + 11);
        mileage = carInfo.substring(0, carInfo.indexOf("<"));
        if(mileage.contains("mi.")) {
            mileage = mileage.substring(0,mileage.length()-4);
        }


        list.addCar(modelYear, car, price, mileage, adLink, picLink);

    }

}

