package com.infoiv;

import com.infoiv.subscriber.HackthonReceiver;
import com.infoiv.subscriber.SeminarReceiver;

public class ReceiverDriver {
    public static void main(String[] args) {
        HackthonReceiver hr = new HackthonReceiver();
        SeminarReceiver sr = new SeminarReceiver();
        hr.initialize();
        sr.initialize();
        hr.receive();
        sr.receive();
        hr.destroy();
        sr.destroy();
    }
}
