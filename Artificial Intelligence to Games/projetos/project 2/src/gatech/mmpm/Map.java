/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

//import java.util.ArrayList;
import gatech.mmpm.util.XMLWriter;

import java.util.List;



public abstract class Map {
		
	public static final int DIRECTION_X_NEG = 3;	// west
	public static final int DIRECTION_X_POS = 1; 	// east
	public static final int DIRECTION_Y_NEG = 0;	// north
	public static final int DIRECTION_Y_POS = 2;	// south
	public static final int DIRECTION_Z_NEG = 4;	// up
	public static final int DIRECTION_Z_POS = 5;	// down

	protected float cell_size[];	
	protected int numberOfDimensions;
	
	public Map(int dimensionCount)
	{
		numberOfDimensions = dimensionCount;	
		cell_size = new float[dimensionCount];
		for(int i = 0;i<dimensionCount;i++) cell_size[i]=1;
	}
	
	public float getCellSizeInDimension(int d) 
	{
		return cell_size[d];
	}
	
	public int getNumberOfDimensions()
	{
		return numberOfDimensions;
	}
		
	public abstract void printMap();
	public abstract PhysicalEntity getCellLocation(int cellCoords[]);
	public abstract PhysicalEntity getCellLocation(float coords[]);
	public abstract boolean setCellLocation(char mapCharacter, int cellCoords[], gatech.mmpm.IDomain idomain);
	public abstract boolean setCellLocation(char mapCharacter, float coords[], gatech.mmpm.IDomain idomain);
	public abstract void addEntity(PhysicalEntity pe);
		
	public abstract Object clone();
	public abstract Object cloneWithSameEntities();  // This function only clones the map, 
													 // but copies all the entities form the original map.
	public abstract int size();
	public abstract PhysicalEntity get(int i);
	
	public abstract float[] toCoords(int pos1);
	public abstract float[] toCoords(int cellcoords[]);

	public abstract int[] toCellCoords(int pos1);
	public abstract void toCellCoords(int pos1,int []cellCoords);

	public abstract int[] toCellCoords(float coords[]);
	public  int[] toCellCoords(PhysicalEntity e)
	{
		return toCellCoords(e.get_Coords());
	}
	
	public abstract void toCellCoords(float coords_in[],int coords_out[]);
	public void toCellCoords(PhysicalEntity e,int coords_out[])
	{
		toCellCoords(e.get_Coords(),coords_out);
	}
	
	public abstract int toCell(float coords[]);
	public abstract int toCell(int cellCoords[]);
	public int toCell(PhysicalEntity e)
	{
		return toCell(e.get_Coords());
	}
	
	public abstract double distance(int cellCoords1[],int cellCoords2[]);
	public abstract double distance(float coords1[],float coords2[]);
	public double distance(PhysicalEntity e1,PhysicalEntity e2)
	{
		return distance(e1.get_Coords(),e2.get_Coords());
	}
	
	public abstract float squareDistance(int cellCoords1[],int cellCoords2[]);	// distance returned is in "cells"
	public abstract float squareDistance(float coords1[],float coords2[]);		// distance returned is raw
	public double squareDistance(PhysicalEntity e1,PhysicalEntity e2)
	{
		return distance(e1.get_Coords(),e2.get_Coords());
	}
	
	public abstract boolean areNeighbors(int cellCoords1[],int cellCoords2[]);
	public abstract boolean areNeighbors(float coords1[],float coords2[]);
	public boolean areNeighbors(PhysicalEntity e1,PhysicalEntity e2)
	{
		return areNeighbors(e1.get_Coords(),e2.get_Coords());
	}
	
	public abstract String toString();
	
	public abstract void deleteEntity(String id);
	public abstract void deleteEntity(PhysicalEntity e);	
	public abstract List<PhysicalEntity> getCollisionsOf(PhysicalEntity e);

	public abstract void writeToXML(XMLWriter w);
	public abstract void writeDifferenceToXML(XMLWriter w, Map previousMap);
	
} // class Map
