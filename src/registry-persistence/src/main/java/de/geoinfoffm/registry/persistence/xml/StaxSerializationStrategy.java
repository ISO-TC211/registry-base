package de.geoinfoffm.registry.persistence.xml;

public class StaxSerializationStrategy implements XmlSerializationStrategy 
{
	private boolean isRoot;
	
	public StaxSerializationStrategy(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isRoot() {
		return isRoot;
	}
	
	public StaxSerializationStrategy asNonRoot() {
		return new StaxSerializationStrategy(false);
	}
	
}
