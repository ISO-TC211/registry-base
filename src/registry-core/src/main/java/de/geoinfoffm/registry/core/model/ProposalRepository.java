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
package de.geoinfoffm.registry.core.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * The interface ProposalRepository.
 *
 * @author Florian Esser
 */
public interface ProposalRepository extends EntityRepository<Proposal>
{
	List<Proposal> findByDateSubmittedIsNotNullAndIsConcludedIsFalse();

	List<Proposal> findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsTrue();
	
	List<Proposal> findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse();
	Page<Proposal> findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse(Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.dateSubmitted IS NOT NULL AND p.group IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse(@Param("search") String search, Pageable pageable);
	
	List<Proposal> findBySponsorAndStatusAndDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor, String status);

	List<Proposal> findByGroupIsNullAndIsConcludedIsFalse();
	Page<Proposal> findByGroupIsNullAndIsConcludedIsFalse(Pageable pageable);

	List<Proposal> findByGroupIsNullAndIsConcludedIsFalseAndUuidIn(Collection<UUID> uuids);
	Page<Proposal> findByGroupIsNullAndIsConcludedIsFalseAndUuidIn(Collection<UUID> uuids, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.uuid IN (:uuids) AND p.group IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByGroupIsNullAndIsConcludedIsFalseAndUuidIn(@Param("uuids") Collection<UUID> uuids, @Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE (p.sponsor = :sponsor AND p.dateSubmitted IS NULL) OR (p.uuid IN (:uuids) AND p.group IS NULL AND p.isConcluded = false)")
	Page<Proposal> findResponsibleRepresentativeProposals(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("uuids") Collection<UUID> uuids, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE (p.sponsor = :sponsor AND p.dateSubmitted IS NULL AND (LOWER(p.title) LIKE LOWER(:search))) OR (p.uuid IN (:uuids) AND p.group IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search)))")
	Page<Proposal> findResponsibleRepresentativeProposals(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("uuids") Collection<UUID> uuids, @Param("search") String search, Pageable pageable);

	List<Proposal> findBySponsorAndGroupIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor);
	Page<Proposal> findBySponsorAndGroupIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE p.group IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByGroupIsNullAndIsConcludedIsFalse(@Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE p.sponsor = :sponsor AND p.group IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findBySponsorAndGroupIsNullAndIsConcludedIsFalse(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM SimpleProposal p WHERE p.sponsor = :sponsor AND p.proposalManagementInformation.item.itemClass = :itemClass AND p.group IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<SimpleProposal> findSimpleProposalBySponsorAndItemClassAndGroupIsNullAndIsConcludedIsFalse(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("itemClass") RE_ItemClass itemClass, @Param("search") String search, Pageable pageable);
	
	Page<Proposal> findBySponsorAndStatusAndGroupIsNull(RE_SubmittingOrganization sponsor, String status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.sponsor = :sponsor AND p.status = :status AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findBySponsorAndStatusAndGroupIsNull(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("status") String status, @Param("search") String search, Pageable pageable);
	
	List<Proposal> findByStatusAndGroupIsNull(String status);
	Page<Proposal> findByStatusAndGroupIsNull(String status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.group IS NULL AND p.isConcluded = false AND p.status = :status AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByStatusAndGroupIsNull(@Param("status") String status, @Param("search") String search, Pageable pageable);

	List<Proposal> findByStatusInAndGroupIsNull(Collection<String> status);
	Page<Proposal> findByStatusInAndGroupIsNull(Collection<String> status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.group IS NULL AND p.isConcluded = false AND p.status IN (:status) AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByStatusInAndGroupIsNull(@Param("status") Collection<String> status, @Param("search") String search, Pageable pageable);
}