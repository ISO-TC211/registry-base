package de.geoinfoffm.registry.core.model.iso19115;



/**
 * Mode in which the data is represented
 * @created 10-Sep-2013 19:43:20
 */
public enum CI_PresentationFormCode {

	/**
	 * Piece of written or printed matter that provides a record or evidence of events,
	 * an agreement, ownership, identification, etc..
	 */
	documentDigital,
	/**
	 * Representation of a map which is printed on paper, photographic material, or
	 * other media and can be interpreted directly by the human user
	 */
	documentHardcopy,
	/**
	 * Permanent record of the likeness of any natural or man-made features, objects,
	 * and activities reproduced on photographic materials.  This image can be
	 * acquired through the sensing of visual or any other segment of the
	 * electromagnetic spectrum by sensors, such as thermal infrared, and high
	 * resolution radar.
	 */
	imageDigital,
	imageHardcopy,
	mapDigital,
	mapHardcopy,
	/**
	 * Representation in three dimensions of geospatial data
	 */
	modelDigital,
	modelHardcopy,
	/**
	 * Vertical cross-section of geospatial data
	 */
	profileDigital,
	profileHardcopy,
	tableDigital,
	tableHardcopy,
	videoDigital,
	videoHardcopy,
}//end CI_PresentationFormCode