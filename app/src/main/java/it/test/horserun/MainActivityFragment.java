package it.test.horserun;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import it.test.horserun.model.Buca;
import it.test.horserun.model.HorseTrack;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int MAX = 70;
    boolean victory=false;
    Activity a;
    String head;
    HorseInterface horseInterface;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StringBuffer[] buf=null;
        try {
            buf=parseFile();
        } catch (Exception e){
            Log.e(this.getClass().getName(), "EXCEPTION: " + e.getMessage());
            Toast.makeText(a.getBaseContext(),
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }
        View v=inflater.inflate(R.layout.fragment_main, container, false);
        ((TextView) v.findViewById(R.id.lista)).setText(buf[0].toString());
        ((TextView) v.findViewById(R.id.listaStep)).setText(buf[1].toString());
        ((TextView) v.findViewById(R.id.results)).setText(buf[2].toString());
        ((TextView) v.findViewById(R.id.initial)).setText(head);
        return v;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
             a=activity;
            horseInterface = (HorseInterface) activity;
            
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement HorseInterface");
        }
    }



    public StringBuffer[] parseFile() throws Exception {
        String str="";
        StringBuffer buf = new StringBuffer();
        StringBuffer bufSteps = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.raw.board);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int line=0;
        if (is!=null) {
            while ((str = reader.readLine()) != null && !victory) {
                if (line==0){
                    setHorsesNames(str);
                    //bufSteps.append(str+"\n");
                    head=str;
                } else
                    if (!str.trim().equalsIgnoreCase("NEW_ROUND") && !str.trim().equalsIgnoreCase("END_ROUND"))
                        try {

                            bufSteps.append(parseStep(str));
                        } catch (Exception e){
                            Log.e(this.getClass().getName(),"Skipping line "+line+": "+e.getMessage());

                        }
                    else {
                        if (str.trim().equalsIgnoreCase("NEW_ROUND"))
                            releaseHaltedHorses();
                        else
                            if (str.trim().equalsIgnoreCase("END_ROUND"))
                                for (int i=0;i<horseInterface.getHorsesTracks().length;i++) {
                                    HorseTrack ht=horseInterface.getHorsesTracks()[i];
                                    buf.append(ht.getTrack()+" "+ht.getName()+": "+ht.getPosition()+"\n");
                                    checkVictory();
                                }
                        bufSteps.append(str + "\n");
                        buf.append(str+"\n");
                    }
                line++;
            }
        }
        is.close();
        /*
        for (int i=0;i<horseInterface.getHorsesTracks().length;i++) {
            HorseTrack ht=horseInterface.getHorsesTracks()[i];
            buf.append(ht.getTrack()+" "+ht.getName()+": "+ht.getPosition()+"\n");

        }
*/
        sortArray();
        StringBuffer bufFinal=new StringBuffer();
        for (int i=0;i<horseInterface.getHorsesTracks().length;i++) {
            HorseTrack ht=horseInterface.getHorsesTracks()[i];
            bufFinal.append(i+1+": " +ht.getName()+ " - meters: " +ht.getPosition()+" - track: "+ ht.getTrack() + "\n");
        }

        StringBuffer[] aBuf=new StringBuffer[3];
        aBuf[0]=buf;
        aBuf[1]=bufSteps;
        aBuf[2]=bufFinal;
        return aBuf;

    }// PlayWithSDFiles

    private void checkVictory() {
       for (int i=0;i<horseInterface.getHorsesTracks().length;i++)
       {
           if (horseInterface.getHorsesTracks()[i].position>=MAX) {
               victory = true;
               break;
           }
       }
    }

    private void sortArray() {
        Arrays.sort(horseInterface.getHorsesTracks(), new Comparator<HorseTrack>() {
            @Override
            public int compare(HorseTrack horse1, HorseTrack horse2) {
                if (horse1.position > horse2.position) return -1;
                else if (horse1.position < horse2.position) return 1;
                else return 0;
            }
        });
    }
    private void releaseHaltedHorses() {
        HorseTrack[] aHorses=horseInterface.getHorsesTracks();
        for (int i=0;i<aHorses.length;i++){
            if (aHorses[i].stop>0)
                aHorses[i].stop-=1;
        }
    }

    private String parseStep(String str) throws Exception{
        Log.d(this.getClass().getName(),"STEP: "+str);
        if (str==null)
            throw new Exception ("Missing step");
        String[] steps=str.trim().split(" ");
        if (steps.length!=2)
            throw new Exception("Wrong number of parameters in current step");
        int track_number=-1;
        try {
            track_number=Integer.parseInt(steps[0]);
        } catch (Exception e){
            throw new Exception("Wrong track format");

        }
        int hole_number=-1;
        try {
            hole_number=Integer.parseInt(steps[1]);
        } catch (Exception e) {
            throw new Exception("Wrong hole format");
        }
        Buca currHole=horseInterface.getHoles()[hole_number];
        switch (currHole.getType()){
            case 'H':
                haltHorseInTrack(track_number,currHole.getValue());
                break;
            case 'F':
                moveHorseInTrack(track_number,currHole.getValue());
                break;
            case 'P':
                moveBackHorseInTrack(track_number,currHole.getValue());
                break;
            case 'S':
                switchHorseInTrack(track_number);
                break;
            default:
                throw new Exception("Wrong instruction");

        }
        return track_number+": "+currHole.getType()+currHole.getValue()+"\n";

    }

    private void switchHorseInTrack(int track_number) {
        int next_track_number;
        if (track_number==horseInterface.getHorsesTracks().length)
            next_track_number=1;
        else
            next_track_number=track_number+1;
        HorseTrack horse1=null;
        HorseTrack horse2=null;
        int position1=-1;
        int position2=-1;
        for (int i=0; i<horseInterface.getHorsesTracks().length;i++)
        {
            HorseTrack horse=horseInterface.getHorsesTracks()[i];
            if (horse.getTrack()==track_number) {
                horse1 = horse;
                position1=horse.position;
            }
            if (horse.getTrack()==next_track_number) {
                horse2 = horse;
                position2=horse.position;
            }
        }

        horse1.setTrack(next_track_number);
        //horse1.setPosition(position2);
        horse2.setTrack(track_number);
        //horse2.setPosition(position1);
    }

    private void moveBackHorseInTrack(int track_number, int value) {
        if (track_number==1)
            track_number=horseInterface.getHorsesTracks().length;
        else
            track_number=track_number-1;
        for (int i=0; i<horseInterface.getHorsesTracks().length;i++)
        {
            HorseTrack horse=horseInterface.getHorsesTracks()[i];
            if (horse.getTrack()==track_number && horse.stop==0)
                horse.position-=value;
        }
    }

    private void moveHorseInTrack(int track_number, int value) {
        for (int i=0; i<horseInterface.getHorsesTracks().length;i++)
        {
            HorseTrack horse=horseInterface.getHorsesTracks()[i];
            if (horse.getTrack()==track_number && horse.stop==0)
                horse.position+=value;
        }

    }

    private void haltHorseInTrack(int track_number, int value) {
        for (int i=0;i<horseInterface.getHorsesTracks().length;i++){

            HorseTrack horse=horseInterface.getHorsesTracks()[i];
            if (horse.getTrack()==track_number && horse.stop==0)
                horse.stop=value;
        }

    }

    private void setHorsesNames(String h) throws Exception{
        
        if (h==null)
            throw new Exception("Missing headline");
        String[] horses=h.trim().split(" ");
        if (horses.length!=7)
            throw new Exception("Wrong number of Horses");
        for (int i=0;i<horses.length;i++){
            horseInterface.getHorsesTracks()[i]=new HorseTrack(horses[i]);    
        }
    }
    
    public interface HorseInterface{
        
        public HorseTrack[] getHorsesTracks();

        public Buca[] getHoles();

    }
}
