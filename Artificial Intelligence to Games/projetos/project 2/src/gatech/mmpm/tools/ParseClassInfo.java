/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools;

import gatech.mmpm.util.Pair;

import java.util.ArrayList;


public class ParseClassInfo {
	
	String className = new String();
	String superClassName= new String();
	ArrayList <Pair<String,String>> features = new ArrayList <Pair<String,String>> ();
	
	public ParseClassInfo(String className, String superClassName) 
	{
		this.className = className;
		this.superClassName = superClassName;
	}
	
	public ArrayList<Pair<String,String>> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Pair<String,String>> features) {
		this.features = features;
	}
	
	public Pair<String,String> hasFeature(String featureName)
	{
		for(Pair<String,String> f:features) if (f.getFirst().equals(featureName)) return f;
		return null;
	}
		
}
