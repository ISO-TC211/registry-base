package de.geoinfoffm.registry.core.model.iso19103;

/**
 * 
 * @created 10-Sep-2013 20:18:45
 */
public final class DatePrecision
{
	public CharacterString _precision;
	public CharacterString _determinationMethod;

	public DatePrecision(CharacterString precision, CharacterString determinationMethod) {
		_precision = precision;
		_determinationMethod = determinationMethod;
	}

}// end DatePrecision