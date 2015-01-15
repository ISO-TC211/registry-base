package de.geoinfoffm.registry.core.model.iso19135;



/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_Reference</Class>
 * <Attribute>similarity</Attribute>
 * <Type>RE_SimilarityToSource</Type>
 * <UsedBy>
 * @created 09-Sep-2013 19:14:23
 */
public enum RE_SimilarityToSource {

	identical,
	restyled,
	contextAdded,
	generalization,
	specialization,
	unspecified;
}//end RE_SimilarityToSource