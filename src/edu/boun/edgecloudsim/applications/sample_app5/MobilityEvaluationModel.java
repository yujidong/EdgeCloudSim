package edu.boun.edgecloudsim.applications.sample_app5;

import edu.boun.edgecloudsim.mobility.MobilityModel;
import edu.boun.edgecloudsim.utils.Location;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MobilityEvaluationModel {
    private int numOfDevices;
    private int numOfFogNodes;
    private double fogCoverageRadius;
    private List<Map<Double, Double>> deviceSpeeds; // k: time    v: value
    private MobilityNetworkModel mobilityNetworkModel;
    private List<Application> applications;
    private List<MobilityScenarioFactory> mobilityScenarioFactories;
    private List<MobilityModel> mobilityModels;
    private double simulationTime;

    public MobilityEvaluationModel(int numOfDevices, int numOfFogNodes, double fogCoverageRadius,
                                   List<Map<Double, Double>> deviceSpeeds, MobilityNetworkModel mobilityNetworkModel) {
        this.numOfDevices = numOfDevices;
        this.numOfFogNodes = numOfFogNodes;
        this.fogCoverageRadius = fogCoverageRadius;
        this.deviceSpeeds = deviceSpeeds;
        this.mobilityNetworkModel = mobilityNetworkModel;
        this.applications = new ArrayList<Application>();
        this.mobilityScenarioFactories = new ArrayList<MobilityScenarioFactory>();
        this.mobilityModels = new ArrayList<MobilityModel>();
    }

    public void addMobilityModel() {
       mobilityModels.add(new MobilityModel(numOfDevices, simulationTime) {
           @Override
           public void initialize() {

           }

           @Override
           public Location getLocation(int deviceId, double time) {
               return null;
           }
       });
    }

    public MobilityModel generateMobilityModel(int numOfDevices, int numOfFogNodes, double fogCoverageRadius, double averageSpeed,
                                               double standardSpeedDeviation) {
        return null;
    }


    // Mobility effects on Application Layer





    // Mobility effects on Networking Layer





    // Mobility effects on Device Layer



    private void generateApplicationXML() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }


}
