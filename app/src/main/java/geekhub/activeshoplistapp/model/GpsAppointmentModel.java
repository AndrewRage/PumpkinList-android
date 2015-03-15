package geekhub.activeshoplistapp.model;

/**
 * Created by rage on 3/15/15.
 */
public class GpsAppointmentModel {
    private long purchaseId;
    private double latitude;
    private double longitude;
    private float maxDistance;
    private boolean isAlert;

    public GpsAppointmentModel(long purchaseId, double latitude, double longitude, float maxDistance, boolean isAlert) {
        this.purchaseId = purchaseId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxDistance = maxDistance;
        this.isAlert = isAlert;
    }

    public long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setIsStartAlertPosition(boolean isStartAlertPosition) {
        this.isAlert = isStartAlertPosition;
    }
}
