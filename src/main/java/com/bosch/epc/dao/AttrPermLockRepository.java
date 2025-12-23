package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.AttrPermLock;

/**
 * @author VFE1COB
 *
 */
@Repository
public interface AttrPermLockRepository extends JpaRepository<AttrPermLock, Integer> {

    /**
     * @return
     */
    @Query("select a from AttrPermLock a where a.isRequestPresent <> 0")
    List<AttrPermLock> findAllOpenRequest();

    /**
     * @param attrStringId
     * @param i
     * @param wiType
     * @param wiStatus
     * @param wiResolution
     * @param wiStatusGroup
     * @return
     */
    @Query("select case when count(a) > 0 then true else false end from AttrPermLock a " +
        "where a.attrStringId = :attrStringId " +
        "and a.paRoleId = :paRoleId " +
        "and a.wiType = :wiType " +
        "and a.wiStatus = :wiStatus " +
        "and a.wiResolution = :wiResolution " +
        "and a.wiStatusGroup = :wiStatusGroup")
 boolean existsForRequest(
         @Param("attrStringId") String attrStringId,
         @Param("paRoleId") int id,
         @Param("wiType") String wiType,
         @Param("wiStatus") String wiStatus,
         @Param("wiResolution") String wiResolution,
         @Param("wiStatusGroup") String wiStatusGroup
 );

}
