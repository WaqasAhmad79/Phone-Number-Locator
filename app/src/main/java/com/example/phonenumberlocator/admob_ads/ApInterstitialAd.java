package com.example.phonenumberlocator.admob_ads;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class ApInterstitialAd {
    private InterstitialAd interstitialAd;
    StatusAd status = StatusAd.AD_INIT;

    private Boolean isPriorityAd = false;

    private String interAdIdHigh = "";
    private String interAdIdLow = "";

    public ApInterstitialAd() {
    }

    public ApInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
        status = StatusAd.AD_LOADED;
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
        status = StatusAd.AD_LOADED;
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public void setPrioritiesIds(String interAdIdHigh, String interAdIdLow) {
        this.interAdIdHigh = interAdIdHigh;
        this.interAdIdLow = interAdIdLow;
        isPriorityAd = true;
    }

    public boolean isReady() {
        if (interstitialAd != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNotReady() {
        return !isReady();
    }

    public Boolean isPriorityAd() {
        return isPriorityAd;
    }

    public void setIsPriorityAd(Boolean priorityAd) {
        isPriorityAd = priorityAd;
    }

    public String getInterAdIdHigh() {
        return interAdIdHigh;
    }

    public void setInterAdIdHigh(String interAdIdHigh) {
        this.interAdIdHigh = interAdIdHigh;
    }

    public String getInterAdIdLow() {
        return interAdIdLow;
    }

    public void setInterAdIdLow(String interAdIdLow) {
        this.interAdIdLow = interAdIdLow;
    }
}

enum StatusAd {
    AD_INIT, AD_LOADING, AD_LOADED, AD_LOAD_FAIL, AD_RENDER_SUCCESS
}