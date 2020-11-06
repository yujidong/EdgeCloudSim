/*
 * Title:        EdgeCloudSim - Location
 * 
 * Description:  Location class used in EdgeCloudSim
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 * Copyright (c) 2017, Bogazici University, Istanbul, Turkey
 */

package edu.boun.edgecloudsim.utils;

import java.util.*;

public class Location {
	class Position {
		double xPos;
		double yPos;

		public Position(double xPos, double yPos) {
			this.xPos = xPos;
			this.yPos = yPos;
		}
	}

	private static Map<Integer, LinkedList<Integer>> neighbourFog;
	private static Map<Integer, Position> fogPositions;
	static {
		neighbourFog = new HashMap<Integer, LinkedList<Integer>>();
		fogPositions = new HashMap<Integer, Position>();
	}
	private boolean isFogNode = true;
	private double xPos;
	private double yPos;
	private int servingWlanId;
	private double radius = 10;
	private Position fogPosition;
	private int placeTypeIndex;

	public Location(int _placeTypeIndex, int _servingWlanId, double _xPos, double _yPos){
		servingWlanId = _servingWlanId;
		placeTypeIndex=_placeTypeIndex;
		xPos = _xPos;
		yPos = _yPos;
	}

	public Location(int _placeTypeIndex, int _servingWlanId, double _xPos, double _yPos, double _radius){
		this(_placeTypeIndex, _servingWlanId, _xPos, _yPos);
		fogPosition = new Position(_xPos, _yPos);
		radius = _radius;
		if(!neighbourFog.containsKey(_servingWlanId)) {
			neighbourFog.put(_servingWlanId, new LinkedList<Integer>());
		}
		if(!fogPositions.containsKey(_servingWlanId)) {
			fogPositions.put(_servingWlanId, fogPosition);
		}
	}

	public Location(int _placeTypeIndex, int _servingWlanId, double _xPos, double _yPos,
					double _xFogPos, double _yFogPos, double _radius){
		this(_placeTypeIndex, _servingWlanId, _xPos, _yPos);
		fogPosition = new Position(_xFogPos, _yFogPos);
		isFogNode = false;
		radius = _radius;
		if(!neighbourFog.containsKey(servingWlanId)) {
			neighbourFog.put(servingWlanId, new LinkedList<Integer>());
		}
		if(!fogPositions.containsKey(servingWlanId)) {
			fogPositions.put(servingWlanId, fogPosition);
		}
	}
	
	@Override
	public boolean equals(Object other){
		boolean result = false;
	    if (other == null) return false;
	    if (!(other instanceof Location))return false;
	    if (other == this) return true;
	    
	    Location otherLocation = (Location)other;
	    if(this.xPos == otherLocation.xPos && this.yPos == otherLocation.yPos)
	    	result = true;

	    return result;
	}

	public int getServingWlanId(){
		return servingWlanId;
	}
	
	public int getPlaceTypeIndex(){
		return placeTypeIndex;
	}
	
	public double getXPos(){
		return xPos;
	}
	
	public double getYPos(){
		return yPos;
	}

	public double getFogxPos() {
		return fogPosition.xPos;
	}

	public double getFogyPos() {
		return fogPosition.yPos;
	}

	public double getFogxPos(int servingWlanId) {
		return fogPositions.get(servingWlanId).xPos;
	}

	public double getFogyPos(int servingWlanId) {
		return fogPositions.get(servingWlanId).yPos;
	}


	public boolean addFogNeighbourIfItIs(Location anotherFogNode) {
		if(anotherFogNode != this && getBetweenFogDistance(anotherFogNode) < 2 * radius) {
			LinkedList neighbours = neighbourFog.get(servingWlanId);
			neighbours.add(anotherFogNode.getServingWlanId());
			neighbourFog.put(servingWlanId, neighbours);
			return true;
		} else {
			return false;
		}
	}

	public double getDistance(Location anotherNode) {
		return Math.sqrt(Math.pow(anotherNode.getXPos()-this.getXPos(),2) + Math.pow(anotherNode.getYPos()-this.getYPos(),2));
	}

	public double getFogDistance(Location anotherNode) {
		if(anotherNode == this)
			return getFogDistance();
		else
			return Math.sqrt(Math.pow(anotherNode.getFogxPos()-this.getXPos(),2) + Math.pow(anotherNode.getFogyPos()-this.getYPos(),2));
	}

	public double getBetweenFogDistance(Location anotherNode) {
		if(anotherNode == this)
			return 0;
		else
			return Math.sqrt(Math.pow(anotherNode.getFogxPos()-this.getFogxPos(),2) + Math.pow(anotherNode.getFogyPos()-this.getFogyPos(),2));
	}

	public double getFogDistance() {
		if(isFogNode)
			return 0;
		else
			return Math.sqrt(Math.pow(this.getFogxPos()-this.getXPos(),2) + Math.pow(this.getFogyPos()-this.getYPos(),2));
	}

	public List<Integer> getNeighbourFogs() {
		return neighbourFog.get(servingWlanId);
	}

	public void updateNodePosition(double xPos, double yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public boolean withinCurrentFog() {
		if(getFogDistance() < radius)
			return true;
		else
			return false;
	}

	public boolean noResponseFog() {
		double distanceToNeighbourFog;
		for(Integer fogIndex: getNeighbourFogs()) {
			distanceToNeighbourFog = Math.sqrt(Math.pow(xPos-fogPositions.get(fogIndex).xPos,2) +
					Math.pow(yPos-fogPositions.get(fogIndex).yPos,2));
			if(distanceToNeighbourFog <= radius) {
				return false;
			}
		}
		if(withinCurrentFog()) {
			return false;
		}
		return true;
	}

	public boolean updateNewFog() {
		if(!withinCurrentFog()) {
			double shortestDistance = radius + 10;
			double distanceToNeighbourFog;
			int newFogID = servingWlanId;
			for(Integer fogIndex : getNeighbourFogs()) {
				distanceToNeighbourFog = Math.sqrt(Math.pow(xPos-fogPositions.get(fogIndex).xPos,2) +
						Math.pow(yPos-fogPositions.get(fogIndex).yPos,2));
				if( distanceToNeighbourFog < shortestDistance) {
					shortestDistance = distanceToNeighbourFog;
					newFogID = fogIndex;
					fogPosition.xPos = fogPositions.get(fogIndex).xPos;
					fogPosition.yPos = fogPositions.get(fogIndex).yPos;
				}
			}
			if(shortestDistance < radius) {
				this.servingWlanId = newFogID;
				return true;
			}
		}
		return false;
	}

	public Location copyNewLocation() {
		return new Location(placeTypeIndex, servingWlanId, xPos, yPos, fogPosition.xPos, fogPosition.yPos, radius);
	}
}
