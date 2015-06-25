package it.test.horserun.model;

/**
 * Created by Savarese on 23/06/15.
 */
public class HorseTrack {
    public String getName() {
        return name;
    }

    public int position;

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int stop;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    private String name;
    private int track;

    public HorseTrack(String ht) throws Exception{
        String[] horse_track=ht.trim().split("_");
        position=0;
        stop=0;
        name=horse_track[0];
        try {
            track = Integer.parseInt(horse_track[1]);
        } catch (NumberFormatException nfe){
            throw new Exception("Wrong track number");
        }


    }


}
