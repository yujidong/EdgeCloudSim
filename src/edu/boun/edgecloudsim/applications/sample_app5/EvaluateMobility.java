package edu.boun.edgecloudsim.applications.sample_app5;

import edu.boun.edgecloudsim.mobility.MobilityModel;
import edu.boun.edgecloudsim.utils.Location;
import edu.boun.edgecloudsim.utils.SimLogger;
import org.apache.commons.math3.distribution.ExponentialDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EvaluateMobility extends MobilityModel {
    private int numOfFogNodes;
    private double fogCoverageRadius;
    private List<Map<Double, Double>> deviceSpeeds; // k: time    v: value
    private List<MobilityModel> mobilityModels;
    private List<TreeMap<Double, Location>> treeMapArray;

    public EvaluateMobility(int _numberOfMobileDevices, double _simulationTime) {
        super(_numberOfMobileDevices, _simulationTime);
    }

    public EvaluateMobility(int _numberOfMobileDevices, double _simulationTime, double averageSpeed, double standardSpeed,
                            double fogCoverageRadius) {
        this(_numberOfMobileDevices, _simulationTime);
    }

    public EvaluateMobility(int _numberOfMobileDevices, double _simulationTime, int numOfFogNodes, double fogCoverageRadius,
                            List<Map<Double, Double>> deviceSpeeds) {
        this(_numberOfMobileDevices, _simulationTime);
        this.numOfFogNodes = numOfFogNodes;
        this.fogCoverageRadius = fogCoverageRadius;
        this.deviceSpeeds = deviceSpeeds;
    }


    @Override
    public void initialize() {
        treeMapArray = new ArrayList<TreeMap<Double, Location>>();
    }

    @Override
    public Location getLocation(int deviceId, double time) {
        TreeMap<Double, Location> treeMap = treeMapArray.get(deviceId);

        Map.Entry<Double, Location> e = treeMap.floorEntry(time);

        if(e == null){
            SimLogger.printLine("impossible is occured! no location is found for the device '" + deviceId + "' at " + time);
            System.exit(0);
        }

        return e.getValue();
    }

    public static void main(String[] args) {
        ExponentialDistribution expRng;
        expRng = new ExponentialDistribution(200);
        System.out.println(expRng.sample());
    }
}
