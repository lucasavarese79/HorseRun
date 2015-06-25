package it.test.horserun;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.test.horserun.model.Buca;
import it.test.horserun.model.HorseTrack;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.HorseInterface {

    Buca[] holes=new Buca[20];


    public HorseTrack[] getHorsesTracks() {
        return horsesTracks;
    }

    public void setHorsesTracks(HorseTrack[] horsesTracks) {
        this.horsesTracks = horsesTracks;
    }

    public Buca[] getHoles() {
        return holes;
    }


    HorseTrack[] horsesTracks=new HorseTrack[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        holes[0]=new Buca('H',2);
        holes[1]=new Buca('F',5);
        holes[2]=new Buca('S');
        holes[3]=new Buca('P',10);
        holes[4]=new Buca('F',5);

        holes[5]=new Buca('F',5);
        holes[6]=new Buca('S');
        holes[7]=new Buca('F',10);
        holes[8]=new Buca('F',10);
        holes[9]=new Buca('H',3);

        holes[10]=new Buca('S');
        holes[11]=new Buca('F',10);
        holes[12]=new Buca('F',20);
        holes[13]=new Buca('P',5);
        holes[14]=new Buca('F',20);

        holes[15]=new Buca('S');
        holes[16]=new Buca('F',40);
        holes[17]=new Buca('F',60);
        holes[18]=new Buca('H',1);
        holes[19]=new Buca('P',4);
        setContentView(R.layout.activity_main);


     //   H 2, F 5, S, P 10, F 5, F 5, S, F 10, F 10, H 3, S, F 10, F 20, P 5, F 20, S, F 40, F 60, H 1, P 4
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
