package com.wordpress.gatarblog.dzwonnik.States;


import java.io.Serializable;

public class VolumeValues implements Serializable{
    private int volumeRingtone;
    private int volumeNotification;
    private int volumeSystem;
    private int volumeMedia;

    public VolumeValues() {
    }

    public VolumeValues(int volumeRingtone) {
        this.volumeRingtone = volumeRingtone;
        this.volumeNotification = volumeRingtone;
        this.volumeSystem = volumeRingtone;
        this.volumeMedia = volumeRingtone;
    }

    public VolumeValues(int volumeRingtone, int volumeNotification, int volumeSystem, int volumeMedia) {
        this.volumeRingtone = volumeRingtone;
        this.volumeNotification = volumeNotification;
        this.volumeSystem = volumeSystem;
        this.volumeMedia = volumeMedia;
    }

    public int getVolumeRingtone() {
        return volumeRingtone;
    }

    public void setVolumeRingtone(int volumeRingtone) {
        this.volumeRingtone = volumeRingtone;
    }

    public int getVolumeNotification() {
        return volumeNotification;
    }

    public void setVolumeNotification(int volumeNotification) {
        this.volumeNotification = volumeNotification;
    }

    public int getVolumeSystem() {
        return volumeSystem;
    }

    public void setVolumeSystem(int volumeSystem) {
        this.volumeSystem = volumeSystem;
    }

    public int getVolumeMedia() {
        return volumeMedia;
    }

    public void setVolumeMedia(int volumeMedia) {
        this.volumeMedia = volumeMedia;
    }
}
