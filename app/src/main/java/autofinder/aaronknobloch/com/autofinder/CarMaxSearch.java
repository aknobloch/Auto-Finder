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
 * Created by aaron on 8/6/15.
 */
public class CarMaxSearch extends AsyncTask<Void, Void, Integer> {

    private CarList list;
    private Context mainContext;
    private ProgressDialog progDialog;
    private String carMaxLink = "http://www.carmax.com/search?ASc=5&D=50&zip=30097"
            + "&N=285+283&sP=0-12000&sM=NA-60000&sY=2011-2016&Q=436b1a02-9739-47c4-a39f-836beef387db"
            + "&Ep=search:results:results%20page";
    Integer count = 1;


    public CarMaxSearch(Context inContext, CarList inList) {
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

            URL carMaxURL = new URL(carMaxLink);
            HttpURLConnection connection = (HttpURLConnection) carMaxURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            String carInfo = "";

            while((line = in.readLine()) != null) {

                if(line.contains("photo")) {

                    carInfo += line + "\n";

                    for(int i = 0; i < 60; i++) {

                        carInfo += in.readLine() + "\n";

                    }

                    parseInfo(carInfo);
                    count++;
                    carInfo = "";
                }
            }

        } catch(IOException ie) {
            ie.printStackTrace();

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
        String link;
        String picLink;

        // do not want versas or fiats
        if(carInfo.contains("Versa") || carInfo.contains("Fiat")) return;

        // get pic link
        carInfo = carInfo.substring(carInfo.indexOf("src") + 5);
        picLink = carInfo.substring(0, carInfo.indexOf("\""));

        // get link
        carInfo = carInfo.substring(carInfo.indexOf("href=\"") + 6);
        link = "http://www.carmax.com" + carInfo.substring(0, carInfo.indexOf(";"));

        // parse to correct place, get model year
        carInfo = carInfo.substring(carInfo.indexOf("h1") + 3);
        modelYear = carInfo.substring(0, carInfo.indexOf(" "));

        // parse to correct place, get car name
        carInfo = carInfo.substring(carInfo.indexOf(" ") + 1);
        car = carInfo.substring(0, carInfo.indexOf("<")) + " (CM)";

        // parse to correct place, get mileage
        carInfo = carInfo.substring(carInfo.indexOf("dd") + 3);
        mileage = carInfo.substring(0, carInfo.indexOf("K")) + ",000";

        // parse to correct place, get price
        carInfo = carInfo.substring(carInfo.indexOf("$") + 1);
        price = carInfo.substring(0, carInfo.indexOf("<"));

        // price usually followed by * indicating no-haggle-price
        price = price.replaceAll("\\*", "");

        list.addCar(modelYear, car, price, mileage, link, picLink);

    }

}