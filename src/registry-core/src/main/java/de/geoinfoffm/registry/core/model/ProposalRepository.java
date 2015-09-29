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
	@Query("SELECT p FROM Proposal p WHERE p.uuid IN :uuids")
	Page<Proposal> findByUuids(@Param("uuids") Collection<UUID> uuids, Pageable pageable);

	List<Proposal> findByDateSubmittedIsNotNullAndIsConcludedIsFalse();

	List<Proposal> findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsTrue();
	
	List<Proposal> findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse();
	Page<Proposal> findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse(Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.dateSubmitted IS NOT NULL AND p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse(@Param("search") String search, Pageable pageable);

	Page<Proposal> findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsTrue(Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.dateSubmitted IS NOT NULL AND p.parent IS NULL AND p.isConcluded = true AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsTrue(@Param("search") String search, Pageable pageable);

	List<Proposal> findBySponsorAndStatusAndDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor, String status);

	List<Proposal> findByParentIsNullAndIsConcludedIsFalse();
	Page<Proposal> findByParentIsNullAndIsConcludedIsFalse(Pageable pageable);

	List<Proposal> findByParentIsNullAndIsConcludedIsFalseAndUuidIn(Collection<UUID> uuids);
	Page<Proposal> findByParentIsNullAndIsConcludedIsFalseAndUuidIn(Collection<UUID> uuids, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.uuid IN (:uuids) AND p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByParentIsNullAndIsConcludedIsFalseAndUuidIn(@Param("uuids") Collection<UUID> uuids, @Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE (p.sponsor = :sponsor AND p.dateSubmitted IS NULL AND p.parent IS NULL) OR (p.uuid IN (:uuids) AND p.parent IS NULL AND p.isConcluded = false)")
	Page<Proposal> findResponsibleRepresentativeProposals(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("uuids") Collection<UUID> uuids, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE TYPE(p) NOT IN :classes AND ((p.sponsor = :sponsor AND p.dateSubmitted IS NULL AND p.parent IS NULL) OR (p.uuid IN (:uuids) AND p.parent IS NULL AND p.isConcluded = false))")
	Page<Proposal> findResponsibleRepresentativeProposals(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("uuids") Collection<UUID> uuids, @Param("classes") List<Class<?>> excludedClasses, Pageable pageable);
	
	@Query("SELECT p FROM Proposal p WHERE (p.sponsor = :sponsor AND p.dateSubmitted IS NULL AND (LOWER(p.title) LIKE LOWER(:search)) AND p.parent IS NULL) OR (p.uuid IN (:uuids) AND p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search)))")
	Page<Proposal> findResponsibleRepresentativeProposals(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("uuids") Collection<UUID> uuids, @Param("search") String search, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE TYPE(p) NOT IN :classes AND ((p.sponsor = :sponsor AND p.dateSubmitted IS NULL AND (LOWER(p.title) LIKE LOWER(:search)) AND p.parent IS NULL) OR (p.uuid IN (:uuids) AND p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))))")
	Page<Proposal> findResponsibleRepresentativeProposals(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("uuids") Collection<UUID> uuids, @Param("search") String search, @Param("classes") List<Class<?>> excludedClasses, Pageable pageable);

	List<Proposal> findBySponsorAndParentIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor);
	Page<Proposal> findBySponsorAndParentIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByParentIsNullAndIsConcludedIsFalse(@Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE p.sponsor = :sponsor AND p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findBySponsorAndParentIsNullAndIsConcludedIsFalse(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE p.parent.uuid = :groupUuid AND p.isConcluded = false")
	Page<Proposal> findByGroupAndIsConcludedIsFalse(@Param("groupUuid") UUID groupUuid, Pageable pageable);

	@Query("SELECT p FROM Proposal p WHERE p.parent.uuid = :groupUuid AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByGroupAndIsConcludedIsFalse(@Param("groupUuid") UUID groupUuid, @Param("search") String search, Pageable pageable);

	@Query("SELECT p FROM SimpleProposal p WHERE p.sponsor = :sponsor AND p.proposalManagementInformation.item.itemClass = :itemClass AND p.parent IS NULL AND p.isConcluded = false AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<SimpleProposal> findSimpleProposalBySponsorAndItemClassAndParentIsNullAndIsConcludedIsFalse(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("itemClass") RE_ItemClass itemClass, @Param("search") String search, Pageable pageable);
	
	Page<Proposal> findBySponsorAndStatusAndParentIsNull(RE_SubmittingOrganization sponsor, String status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.sponsor = :sponsor AND p.status = :status AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findBySponsorAndStatusAndParentIsNull(@Param("sponsor") RE_SubmittingOrganization sponsor, @Param("status") String status, @Param("search") String search, Pageable pageable);

	List<Proposal> findByStatus(String status);
	Page<Proposal> findByStatus(String status, Pageable pageable);

	List<Proposal> findByStatusAndParentIsNull(String status);
	Page<Proposal> findByStatusAndParentIsNull(String status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.parent IS NULL AND p.isConcluded = false AND p.status = :status AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByStatusAndParentIsNull(@Param("status") String status, @Param("search") String search, Pageable pageable);

	List<Proposal> findByStatusIn(Collection<String> status);
	Page<Proposal> findByStatusIn(Collection<String> status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.isConcluded = false AND p.status IN (:status) AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByStatusIn(@Param("status") Collection<String> status, @Param("search") String search, Pageable pageable);

	List<Proposal> findByStatusInAndParentIsNull(Collection<String> status);
	Page<Proposal> findByStatusInAndParentIsNull(Collection<String> status, Pageable pageable);
	@Query("SELECT p FROM Proposal p WHERE p.parent IS NULL AND p.isConcluded = false AND p.status IN (:status) AND (LOWER(p.title) LIKE LOWER(:search))")
	Page<Proposal> findByStatusInAndParentIsNull(@Param("status") Collection<String> status, @Param("search") String search, Pageable pageable);
	
}