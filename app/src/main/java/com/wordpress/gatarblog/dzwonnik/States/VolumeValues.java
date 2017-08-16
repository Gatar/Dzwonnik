package com.wordpress.gatarblog.dzwonnik.States;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolumeValues implements Serializable {

    private static final long serialVersionUID = 3241261L;

    private int volumeRingtone;
    private int volumeNotification;
    private int volumeSystem;
    private int volumeMedia;

    public VolumeValues(int volumeRingtone) {
        this.volumeRingtone = volumeRingtone;
        this.volumeNotification = volumeRingtone;
        this.volumeSystem = volumeRingtone;
        this.volumeMedia = volumeRingtone;
    }

}
