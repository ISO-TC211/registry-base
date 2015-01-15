package de.geoinfoffm.registry.core.model.iso19103;

/**
 * 
 * @created 10-Sep-2013 20:18:59
 */
public interface ClockTime
{
	public CharacterString getHour();
	public CharacterString getMinute();
	public CharacterString getSecond();
	public CharacterString getTimeZone();

}// end ClockTime