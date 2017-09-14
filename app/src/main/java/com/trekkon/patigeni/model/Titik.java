package com.trekkon.patigeni.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Titik {

    @SerializedName("hot_spot_longitude")
    @Expose
    private String hotSpotLongitude;
    @SerializedName("hot_spot_latitude")
    @Expose
    private String hotSpotLatitude;
    @SerializedName("hotspot_id")
    @Expose
    private String hotspotId;
    @SerializedName("PIC")
    @Expose
    private String pIC;
    @SerializedName("tingkat_kepercayaan")
    @Expose
    private String tingkatKepercayaan;
    @SerializedName("timestamp_original")
    @Expose
    private String timestampOrginal;
    @SerializedName("timestamp_received")
    @Expose
    private String timestampReceived;
    private String byHuman;
    @SerializedName("status")
    @Expose
    private String status;


    public String getHotSpotLatitude() {
        return hotSpotLatitude;
    }

    public void setHotSpotLatitude(String hotSpotLatitude) {
        this.hotSpotLatitude = hotSpotLatitude;
    }

    public String getHotSpotLongitude() {
        return hotSpotLongitude;
    }

    public void setHotSpotLongitude(String hotSpotLongitude) {
        this.hotSpotLongitude = hotSpotLongitude;
    }

    public String getHotspotId() {
        return hotspotId;
    }

    public void setHotspotId(String hotspotId) {
        this.hotspotId = hotspotId;
    }

    public String getPIC() {
        return pIC;
    }

    public void setPIC(String pIC) {
        this.pIC = pIC;
    }

    public String getTingkatKepercayaan() {
        return tingkatKepercayaan;
    }

    public void setTingkatKepercayaan(String tingkatKepercayaan) {
        this.tingkatKepercayaan = tingkatKepercayaan;
    }

    public String getTimestampOrginal() {
        return timestampOrginal;
    }

    public void setTimestampOrginal(String timestampOrginal) {
        this.timestampOrginal = timestampOrginal;
    }

    public String getTimestampReceived() {
        return timestampReceived;
    }

    public void setTimestampReceived(String timestampReceived) {
        this.timestampReceived = timestampReceived;
    }

    public String getByHuman() {
        return byHuman;
    }

    public void setByHuman(String byHuman) {
        this.byHuman = byHuman;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}