package autofinder.aaronknobloch.com.autofinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private CarList list = new CarList();
    private boolean alreadySearched = false;
    private boolean connectedToInternet;
    public final static String RESULTS_HTML = "Unique Key, usually package name";
    public static int finishedTasks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

        // set welcome message font
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Typeface surferFont = Typeface.createFromAsset(getAssets(), "fonts/Surfing Capital.ttf");
        welcomeMessage.setTypeface(surferFont);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchCars(final View view) {

        if (hasInternet() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Error connecting to the internet.");
            builder.setCancelable(false);
            builder.setPositiveButton("Retry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            searchCars(view);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        if(alreadySearched) {
            // if the user searches, then goes back, no need to search again.
            Intent intent = new Intent(this, ResultsPage.class);
            intent.putExtra(MainActivity.RESULTS_HTML, list.getHTML());
            startActivity(intent);
        }

        else {
            // search through websites. the tasks open new activity when all are complete
            AutoTraderSearch autoTraderTask = new AutoTraderSearch(this, list);
            CarMaxSearch carMaxTask = new CarMaxSearch(this, list);
            CarsSearch carsTask = new CarsSearch(this, list);

            autoTraderTask.execute();
            carMaxTask.execute();
            carsTask.execute();

            alreadySearched = true;
        }

    }

    public static boolean allTasksFinished() {
        // this method is executed at the end of the AsyncTask lifecycles
        // determines whether ready to open results activity or not
        finishedTasks++;
        if(finishedTasks == 3) return true;
        else return false;
    }

    private boolean hasInternet() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }


}
