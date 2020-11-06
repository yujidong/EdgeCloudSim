package edu.boun.edgecloudsim.applications.sample_app5;

import edu.boun.edgecloudsim.core.SimSettings;
import edu.boun.edgecloudsim.mobility.MobilityModel;
import edu.boun.edgecloudsim.utils.Direction;
import edu.boun.edgecloudsim.utils.Location;
import edu.boun.edgecloudsim.utils.SimLogger;
import edu.boun.edgecloudsim.utils.SimUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class ManhattanGridMobility extends MobilityModel {
    private List<TreeMap<Double, Location>> treeMapArray;
    private List<Location> fogLocations;
    private Map<Integer, List<Integer>> fogNeighbourMap;

    /**
     * Number of blocks on x-axis.
     */
    protected int xblocks = 9;
    /**
     * Number of blocks on y-axis.
     */
    protected int yblocks = 9;

    /**
     * Distance interval in which to possibly update the mobile's speed [m].
     */
    protected double updateDist = 5.0;
    /**
     * Probability for the mobile to turn at a crossing.
     */
    protected double turnProb = 0.5;
    /**
     * Probability for the mobile to change its speed (every updateDist m).
     */
    protected double speedChangeProb = 0.2;
    /**
     * Mobile's mean speed [m/s].
     */
    protected double meanSpeed = 1.0;
    /**
     * Mobile's minimum speed [m/s].
     */
    protected double minSpeed = 0.5;
    /**
     * Standard deviation of normally distributed random speed [m/s].
     */
    protected double speedStdDev = 0.2;
    /**
     * Probability for the mobile to pause (every updateDist m), given it does not change it's speed.
     */
    protected double pauseProb = 0.0;
    /**
     * Maximum pause time [s].
     */
    protected double maxPause = 120.0;

    /**
     *
     */
    protected double radius = 50 * 1.6;

    /**
     * Size of a block on x-axis. [m]
     */
    protected double xdim = 50;
    /**
     * Size of a block on y-axis. [m]
     */
    protected double ydim = 50;

    protected double xBoundary = xdim * xblocks; // x-boundary
    protected double yBoundary = ydim * yblocks; // y-boundary

    public ManhattanGridMobility(int _numberOfMobileDevices, double _simulationTime) {
        super(_numberOfMobileDevices, _simulationTime);
    }

    public ManhattanGridMobility(int _numberOfMobileDevices, double _simulationTime, int xblocks, int yblocks, double updateDist,
                                 double turnProb, double speedChangeProb, double meanSpeed, double minSpeed, double speedStdDev,
                                 double pauseProb, double maxPause) {
        this(_numberOfMobileDevices, _simulationTime);
        this.xblocks = xblocks;
        this.yblocks = yblocks;
        this.updateDist = updateDist;
        this.turnProb = turnProb;
        this.speedChangeProb = speedChangeProb;
        this.meanSpeed = meanSpeed;
        this.minSpeed = minSpeed;
        this.speedStdDev = speedStdDev;
        this.pauseProb = pauseProb;
        this.maxPause = maxPause;
    }

    @Override
    public void initialize() {
        treeMapArray = new ArrayList<TreeMap<Double, Location>>();

        Document doc = SimSettings.getInstance().getEdgeDevicesDocument();
        NodeList fogList = doc.getElementsByTagName("datacenter");
        fogLocations = new ArrayList<Location>();

        for (int i = 0; i < fogList.getLength(); i++) {
            Node fogNode = fogList.item(i);
            Element datacenterElement = (Element) fogNode;
            Element location = (Element) datacenterElement.getElementsByTagName("location").item(0);
            String attractiveness = location.getElementsByTagName("attractiveness").item(0).getTextContent();
            int placeTypeIndex = Integer.parseInt(attractiveness);
            int wlan_id = Integer.parseInt(location.getElementsByTagName("wlan_id").item(0).getTextContent());
            double x_pos = Double.parseDouble(location.getElementsByTagName("x_pos").item(0).getTextContent()) * xdim;
            double y_pos = Double.parseDouble(location.getElementsByTagName("y_pos").item(0).getTextContent()) * ydim;
            fogLocations.add(i, new Location(placeTypeIndex, wlan_id, x_pos, y_pos, radius));
//            SimLogger.printLine("Fog node: " + i + "; wlan_id: " + wlan_id + "; x_pos: " + x_pos + "; y_pos: " + y_pos);
        }

        for (int i = 0; i < fogLocations.size(); i++) {
            for (int j = 0; j < fogLocations.size(); j++) {
                if (i != j) {
                    fogLocations.get(i).addFogNeighbourIfItIs(fogLocations.get(j));

                }
            }
        }

//        for(Location location : fogLocations) {
//            SimLogger.print("Fog node: " + location.getServingWlanId() + " - ");
//            for(Integer neighbour : location.getNeighbourFogs()) {
//                SimLogger.print(neighbour + " ");
//            }
//            SimLogger.printLine(";");
//        }
        for (int i = 0; i < numberOfMobileDevices; i++) {
            treeMapArray.add(i, new TreeMap<Double, Location>());
            int randDatacenterId = SimUtils.getRandomNumber(0, SimSettings.getInstance().getNumOfEdgeDatacenters() - 1);
//            SimLogger.print(randDatacenterId + " ");
            Location fogLocation = fogLocations.get(randDatacenterId);
            Location mobileDeviceLocation;
            double xPos = fogLocation.getFogxPos();
            double yPos = fogLocation.getFogyPos();
            int randRoad = SimUtils.getRandomNumber(1, 4); // 1: x - xdim/2  ; 2: x + xdim/2 ; 3: y - ydim/2  ; 4: y + ydim/2
            double roadPosition = SimUtils.getRandomDoubleNumber(-Math.sqrt(Math.pow(radius, 2) - Math.pow(xdim / 2, 2)),
                    Math.sqrt(Math.pow(radius, 2) - Math.pow(xdim / 2, 2)));
            switch (randRoad) {
                case 1:
                    mobileDeviceLocation = new Location(fogLocation.getPlaceTypeIndex(), fogLocation.getServingWlanId(),
                            xPos - 25, yPos + roadPosition, fogLocation.getFogxPos(), fogLocation.getFogyPos(), radius);
                    break;
                case 2:
                    mobileDeviceLocation = new Location(fogLocation.getPlaceTypeIndex(), fogLocation.getServingWlanId(),
                            xPos + 25, yPos + roadPosition, fogLocation.getFogxPos(), fogLocation.getFogyPos(), radius);
                    break;
                case 3:
                    mobileDeviceLocation = new Location(fogLocation.getPlaceTypeIndex(), fogLocation.getServingWlanId(),
                            xPos + roadPosition, yPos - 25, fogLocation.getFogxPos(), fogLocation.getFogyPos(), radius);
                    break;
                case 4:
                    mobileDeviceLocation = new Location(fogLocation.getPlaceTypeIndex(), fogLocation.getServingWlanId(),
                            xPos + roadPosition, yPos + 25, fogLocation.getFogxPos(), fogLocation.getFogyPos(), radius);
                    break;
                default:
                    SimLogger.printLine("error: the position is not initialized correctly.");
                    mobileDeviceLocation = new Location(0, 0, 0, 0);
            }
//            SimLogger.print("Mobile device: " + i + " xPos: " + mobileDeviceLocation.getXPos() +
//                    " yPos: " + mobileDeviceLocation.getYPos() +
//                    " Fog node: " + mobileDeviceLocation.getServingWlanId() +
//                    " Fog xPos: " + mobileDeviceLocation.getFogxPos() +
//                    " Fog yPos: " + mobileDeviceLocation.getFogyPos() + " Neighbour fogs: ");
//            for(Integer neighbourFog : mobileDeviceLocation.getNeighbourFogs()) {
//                SimLogger.print(neighbourFog + " ");
//            }
//            SimLogger.printLine(".");
            treeMapArray.get(i).put(SimSettings.CLIENT_ACTIVITY_START_TIME, mobileDeviceLocation);
        }

        for (int i = 0; i < numberOfMobileDevices; i++) {
            TreeMap<Double, Location> treeMap = treeMapArray.get(i);

            Direction direction = Direction.up;

            if (treeMap.lastEntry().getValue().getXPos() % xdim < 0.0001) {
                if (SimUtils.getRandomDoubleNumber() < 0.5) {
                    direction = Direction.up;
                } else {
                    direction = Direction.down;
                }
            } else {
                if (SimUtils.getRandomDoubleNumber() < 0.5) {
                    direction = Direction.right;
                } else {
                    direction = Direction.left;
                }
            }


            while (treeMap.lastKey() < SimSettings.getInstance().getSimulationTime()) {
                double speed = meanSpeed;
                double changeFogTime = 0;
                double distance = updateDist;
                pauseProb += 0.01;



                Location source = treeMap.lastEntry().getValue();

                Location destination;

//                SimLogger.printLine("Node: " + i + " Time: " + treeMap.lastKey() + " xPos: " + source.getXPos() + " yPos: " + source.getYPos() +
//                        " Direction: " + direction + " FogIndex: " + source.getServingWlanId() );
                do {
                    destination = getNewPos(source, distance, direction);
                    if (outOfBounds(destination)) { // if the car drives out of bounds, it will come back
                        switch (direction) {
                            case up:
                                destination.updateNodePosition(destination.getXPos(), yBoundary*2 - destination.getYPos());
                                direction = Direction.down;
                                break;
                            case down:
                                destination.updateNodePosition(destination.getXPos(), -destination.getYPos());
                                direction = Direction.up;
                                break;
                            case right:
                                destination.updateNodePosition(xBoundary*2 - destination.getXPos(), destination.getYPos());
                                direction = Direction.left;
                                break;
                            case left:
                                destination.updateNodePosition(-destination.getXPos(), destination.getYPos());
                                direction = Direction.right;
                                break;
                            default:
                                destination = null;
                                break;
                        }
                    } else if (willMeetCross(source, distance, direction) && SimUtils.getRandomDoubleNumber() < turnProb) { // the car turn right/left at the cross
                        if (SimUtils.getRandomDoubleNumber() < 0.5) { // turn left
                            switch (direction) {
                                case up:
                                    destination.updateNodePosition(destination.getXPos() - (destination.getYPos() % ydim), (int) (destination.getYPos() / ydim) * ydim);
                                    direction = Direction.left;
                                    break;
                                case down:
                                    destination.updateNodePosition(destination.getXPos() + (source.getYPos() % ydim) - distance, (int) (source.getYPos() / ydim) * ydim);
                                    direction = Direction.right;
                                    break;
                                case right:
                                    destination.updateNodePosition((int) (destination.getXPos() / xdim) * xdim, destination.getYPos() + (destination.getXPos() % xdim));
                                    direction = Direction.up;
                                    break;
                                case left:
                                    destination.updateNodePosition((int) (source.getXPos() / xdim) * xdim, destination.getYPos() + (source.getYPos() % xdim) - distance);
                                    direction = Direction.down;
                                    break;
                                default:
                                    destination = null;
                                    break;
                            }
                        } else {  // turn right
                            switch (direction) {
                                case up:
                                    destination.updateNodePosition(destination.getXPos() + (destination.getYPos() % ydim), (int) (destination.getYPos() / ydim) * ydim);
                                    direction = Direction.right;
                                    break;
                                case down:
                                    destination.updateNodePosition(destination.getXPos() - (source.getYPos() % ydim) + distance, (int) (source.getYPos() / ydim) * ydim);
                                    direction = Direction.left;
                                    break;
                                case right:
                                    destination.updateNodePosition((int) (destination.getXPos() / xdim) * xdim, destination.getYPos() - (destination.getXPos() % xdim));
                                    direction = Direction.down;
                                    break;
                                case left:
                                    destination.updateNodePosition((int) (source.getXPos() / xdim) * xdim, destination.getYPos() - (source.getYPos() % xdim) + distance);
                                    direction = Direction.up;
                                    break;
                                default:
                                    destination = null;
                                    break;
                            }
                        }
                    }
                    speed = (SimUtils.getRandomGaussian() * speedStdDev) + meanSpeed;
                    if(speed < minSpeed) {
                        speed = minSpeed;
                    }
                    double tt = treeMap.lastKey()+changeFogTime;
                    changeFogTime += distance/speed;
//                    if(SimUtils.getRandomDoubleNumber() < pauseProb) {
//                        changeFogTime += SimUtils.getRandomDoubleNumber() * maxPause;
//                    }
//                    SimLogger.printLine("Node: " + i + " Time: " + tt + " xPos: " + destination.getXPos() + " yPos: " + destination.getYPos() +
//                             " Direction: " + direction + " FogIndex: " + destination.getServingWlanId() );
                    source = destination;
                } while (destination.withinCurrentFog());
                destination.updateNewFog();
                double t = treeMap.lastKey()+changeFogTime;
//                SimLogger.printLine("Node: " + i + " Time: " + t + " xPos: " + destination.getXPos() + " yPos: " + destination.getYPos() +
//                        " Direction: " + direction + " FogIndex: " + destination.getServingWlanId() );
                treeMap.put(treeMap.lastKey()+changeFogTime, destination);
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(SimUtils.getRandomDoubleNumber());
    }

    public boolean willMeetCross(Location source, double distance, Direction dir) {
        switch ( dir) {
            case up:
                if((source.getYPos() + distance)%ydim < distance)
                    return true;
                else
                    return false;
            case down:
                if(source.getYPos()%ydim < distance)
                    return true;
                else
                    return false;
            case left:
                if(source.getXPos()%xdim < distance)
                    return true;
                else
                    return false;
            case right:
                if((source.getXPos() + distance)%xdim < distance)
                    return true;
                else
                    return false;
            default:
                return false;
        }
    }

    public boolean outOfBounds(Location location) {
        return ((location.getXPos() < 0.0) || (location.getYPos() < 0.0) ||
                (location.getXPos() > xBoundary || (location.getYPos() > yBoundary)));
    }

    public Location getNewPos(Location source, double distance, Direction dir) {
        Location destination = source.copyNewLocation();
        switch (dir) {
            case up:
                destination.updateNodePosition(source.getXPos(), source.getYPos() + distance);
                break;
            case down :
                destination.updateNodePosition(source.getXPos(), source.getYPos() - distance);
                break;
            case right :
                destination.updateNodePosition(source.getXPos() + distance, source.getYPos());
                break;
            case left :
                destination.updateNodePosition(source.getXPos() - distance, source.getYPos());
                break;
            default:
                destination = null;
                break;
        }
        return destination;
    }



    @Override
    public Location getLocation(int deviceId, double time) {
        TreeMap<Double, Location> treeMap = treeMapArray.get(deviceId);

        Map.Entry<Double, Location> e = treeMap.floorEntry(time);

        if (e == null) {
            SimLogger.printLine("impossible is occured! no location is found for the device '" + deviceId + "' at " + time);
            System.exit(0);
        }

        return e.getValue();
    }
}
