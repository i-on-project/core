package org.ionproject.core.ingestion.processor.sql

import org.ionproject.core.ingestion.processor.sql.model.RealProgrammeCoordinator
import org.ionproject.core.ingestion.processor.sql.model.RealSchoolProgramme
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface ProgrammeIngestionDao {

    @SqlQuery("""
        select * from dbo.Programme
    """)
    fun getProgrammes(): List<RealSchoolProgramme>

    @SqlBatch("""
        insert into dbo.Programme (acronym, name, termSize, department, email, uri, description) values 
        (:p.acronym, :p.name, :p.termSize, :p.department, :p.email, :p.uri, :p.description)
    """)
    @GetGeneratedKeys
    fun insertProgrammes(p: List<RealSchoolProgramme>): List<Int>

    @SqlBatch("""
        update dbo.Programme
        set name = :p.name, termSize = :p.termSize, department = :p.department, email = :p.email, uri = :p.uri, description = :p.description
        where id = :p.id
    """)
    fun updateProgrammeInformation(p: List<RealSchoolProgramme>)

    @SqlBatch("""
        update dbo.Programme
        set available = :available
        where id = :programmeId
    """)
    fun setProgrammeAvailable(programmeId: List<Int>, available: Boolean)

    @SqlQuery("""
        select * from dbo.ProgrammeCoordinators
        where programmeId in (<programmes>)
    """)
    fun getProgrammesCoordinators(@BindList("programmes") programmes: List<Int>): List<RealProgrammeCoordinator>

    @SqlBatch("""
        insert into dbo.ProgrammeCoordinators (programmeId, name)
        values (:coordinators.programmeId, :coordinators.name)
    """)
    fun insertProgrammeCoordinators(coordinators: List<RealProgrammeCoordinator>)

    @SqlUpdate("""
        delete from dbo.ProgrammeCoordinators
        where id in (<coordinators>)
    """)
    fun deleteProgrammeCoordinators(@BindList("coordinators") coordinators: List<Int>)

}