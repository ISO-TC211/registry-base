/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
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