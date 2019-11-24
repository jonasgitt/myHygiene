package org.frieling.myHygiene;


import java.util.logging.LogManager;

public class customDistanceCalculator  {



    public static final String TAG = "CurveFittedDistanceCalculator";
    private double mCoefficient1;
    private double mCoefficient2;
    private double mCoefficient3;

    /**
     * Construct a calculator with coefficients specific for the device's signal vs. distance
     *
     * @param coefficient1
     * @param coefficient2
     * @param coefficient3
     */
    public customDistanceCalculator(double coefficient1, double coefficient2, double coefficient3) {
        mCoefficient1 = coefficient1;
        mCoefficient2 = coefficient2;
        mCoefficient3 = coefficient3;
    }

    /**
     * Calculated the estimated distance in meters to the beacon based on a reference rssi at 1m
     * and the known actual rssi at the current location
     *
     * @param txPower
     * @param rssi
     * @return estimated distance
     */
//    @Override
//    public double calculateDistance(int txPower, double rssi) {
//        if (rssi == 0) {
//            return -1.0; // if we cannot determine accuracy, return -1.
//        }
//        txPower = -63; //4dbm
//        //LogManager.d(TAG, "calculating distance based on mRssi of %s and txPower of %s", rssi, txPower);
//
//
//        double ratio = rssi*1.0/txPower;
//        double distance;
//        if (ratio < 1.0) {
//            distance =  Math.pow(ratio,10);
//        }
//        else {
//            distance =  (mCoefficient1)*Math.pow(ratio,mCoefficient2) + mCoefficient3;
//        }
//        LogManager.d(TAG, "mRssi: %s distance: %s", rssi, distance);
//        return distance;
//    }
}
