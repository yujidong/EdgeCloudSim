package edu.boun.edgecloudsim.applications.sample_app5;

import edu.boun.edgecloudsim.edge_client.Task;
import edu.boun.edgecloudsim.network.NetworkModel;
import edu.boun.edgecloudsim.utils.Location;

public class MobilityNetworkModel extends NetworkModel {
    public MobilityNetworkModel(int _numberOfMobileDevices, String _simScenario) {
        super(_numberOfMobileDevices, _simScenario);
    }

    /**
     * initializes custom network model
     */
    @Override
    public void initialize() {

    }

    /**
     * calculates the upload delay from source to destination device
     *
     * @param sourceDeviceId
     * @param destDeviceId
     * @param task
     */
    @Override
    public double getUploadDelay(int sourceDeviceId, int destDeviceId, Task task) {
        return 0;
    }

    /**
     * calculates the download delay from source to destination device
     *
     * @param sourceDeviceId
     * @param destDeviceId
     * @param task
     */
    @Override
    public double getDownloadDelay(int sourceDeviceId, int destDeviceId, Task task) {
        return 0;
    }

    /**
     * Mobile device manager should inform network manager about the network operation
     * This information may be important for some network delay models
     *
     * @param accessPointLocation
     * @param destDeviceId
     */
    @Override
    public void uploadStarted(Location accessPointLocation, int destDeviceId) {

    }

    @Override
    public void uploadFinished(Location accessPointLocation, int destDeviceId) {

    }

    @Override
    public void downloadStarted(Location accessPointLocation, int sourceDeviceId) {

    }

    @Override
    public void downloadFinished(Location accessPointLocation, int sourceDeviceId) {

    }
}
