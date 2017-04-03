package ecse321.group12.tamas.androidtamas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Date;

import ecse321.group12.tamas.controller.TamasController;
import ecse321.group12.tamas.model.Applicant;
import ecse321.group12.tamas.model.Application;
import ecse321.group12.tamas.model.ResourceManager;
import ecse321.group12.tamas.persistence.PersistenceXStream;

public class CurrentApplicationActivity extends AppCompatActivity {

    private ResourceManager rm;
    private String fileName;
    String error = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_application);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener
                (
                        view -> Snackbar.make(view, "DONE?", Snackbar.LENGTH_LONG)
                                .setAction(
                                        "LOGOUT", v ->
                                        {
                                            TamasController tc = new TamasController(rm);
                                            tc.logOut();
                                            moveTo(LoginActivity.class,null);
                                        }
                                ).show()
                );
        Button done = (Button) findViewById(R.id.current_application_button_return);
        done.setOnClickListener(v -> moveTo(HomeActivity.class, null));

        fileName = getFilesDir().getAbsolutePath() + "/tamas_data.xml";
        rm = PersistenceXStream.initializeModelManager(fileName);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

       int applicationNumber=0;

       for (Application a: ((Applicant) rm.getLoggedIn()).getApplications())
       {
            applicationNumber++;
       }
        LinearLayout parent= (LinearLayout)findViewById(R.id.current_application_linearlayout_inflation_target);
        if (applicationNumber==0)
        {
            TextView tv = (TextView) findViewById(R.id.current_application_tv_no_applications);
            parent.removeView(tv);
        }
        else
        {
            for (int i = 0; i < applicationNumber; i++)
            {
                if (((Applicant) rm.getLoggedIn()).getApplication(i).getIsOffered() || ((Applicant) rm.getLoggedIn()).getApplication(i).getJob().getDeadline().before(currentDate()))
                {
                    View child = getLayoutInflater().inflate(R.layout.content_current_application_decision, null);
                    child.setId(i);
                    parent.addView(child);
                    Button childButton = (Button) child.findViewById(R.id.fragment_current_applications_button_decision);
                    childButton.setOnClickListener(v -> moveTo(ApplicationActivity.class, null));
                    drawField(((Applicant) rm.getLoggedIn()).getApplication(i).getJob().getCourse().getName(), R.id.fragment_current_applications_decision_tv_application_position_and_type);

                }
                else
                {
                    View child = getLayoutInflater().inflate(R.layout.content_current_application_pending, null);
                    child.setId(i);
                    parent.addView(child);
                    Button childButton = (Button) child.findViewById(R.id.fragment_current_applications_button_edit);
                    childButton.setOnClickListener(v -> moveTo(ApplicationActivity.class, null));
                    drawField(((Applicant) rm.getLoggedIn()).getApplication(i).getJob().getCourse().getName(), R.id.fragment_current_applications_tv_application_position_and_type);
                }
            }
        }
    }
    private void drawField(String aString, int viewID)
    {
        TextView tv = (TextView) findViewById(viewID);
        tv.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(aString), null, null, null);
        tv.setCompoundDrawablePadding(aString.length()*10);
    }
    private Date currentDate()
    {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    private Bundle bundleData(int applicationIndex)
    {
        Intent i = new Intent(getApplicationContext(), CurrentApplicationActivity.class);
        i.putExtra("applicationIndex",applicationIndex);
        Bundle ret = i.getExtras();
        return ret;
    }
    private void moveTo(Class target, Intent i)
    {
        if (i==null)
        {
            i = new Intent(getApplicationContext(), target);
        }
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        moveTo(HomeActivity.class, null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction()
    {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
