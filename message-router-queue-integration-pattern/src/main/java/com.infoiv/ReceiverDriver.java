package com.infoiv;

import com.infoiv.subscriber.HackthonReceiver;
import com.infoiv.subscriber.SeminarReceiver;

import java.util.Timer;
import java.util.TimerTask;

public class ReceiverDriver {
    public static void main(String[] args) {
        HackthonReceiver hr = new HackthonReceiver();
        SeminarReceiver sr = new SeminarReceiver();
       // hr.initialize();
        //sr.initialize();

        Timer t=new Timer();
        t.scheduleAtFixedRate(hr, 0,5*1000);
        t.scheduleAtFixedRate(sr, 0,1000);
       // hr.receive();
        //sr.receive();
       // hr.destroy();
        //sr.destroy();
    }
}
