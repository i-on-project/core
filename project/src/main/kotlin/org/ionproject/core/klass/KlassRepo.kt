package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.stereotype.Component
import java.lang.Exception

class ClassNotInDbException : Exception()

@Component
class KlassRepo {
    fun get(acr: String, calendarTerm: String): FullKlass {
        val dataSource = PGSimpleDataSource()
        dataSource.databaseName = System.getenv("CORE_DB_NAME")
        dataSource.user = System.getenv("CORE_DB_ROLE")
        //dataSource.password = System.getenv("CORE_DB_PASS")
        val j = Jdbi.create(dataSource).installPlugin(SqlObjectPlugin()).open()

        val acrUpper = acr.toUpperCase()

        val klass = j.createQuery("select CR.acronym, C.term from dbo.Class as C join dbo.Course as CR on C.courseid=CR.id where CR.acronym=:acr and C.term=:term")
            .bind("acr", acrUpper)
            .bind("term", calendarTerm)
            .map { ro, _ -> Klass(ro.getString("acronym"), ro.getString("term")) }
            .findOne()

        if (klass.isEmpty) {
            throw ClassNotInDbException()
        }

        val sections = j.createQuery("select CR.acronym, C.term, CS.id from dbo.Class as C join dbo.ClassSection as CS on C.courseid=CS.courseid and C.term=CS.term join dbo.Course as CR on CR.id=C.courseid where CR.acronym=:acr and C.term=:term;")
            .bind("acr", acrUpper)
            .bind("term", calendarTerm)
            .map { ro, _ -> ClassSection(ro.getString("acronym"), ro.getString("term"), ro.getString("id")) }
            .list()

        return FullKlass(klass.get().course, klass.get().calendarTerm, sections)
    }

    fun getPage(acr: String, page: Int, size: Int): List<Klass> {
        val dataSource = PGSimpleDataSource()
        dataSource.databaseName = "core" //System.getenv("CORE_DB_NAME")
        dataSource.user = "bob" //System.getenv("CORE_DB_ROLE")
        //dataSource.password = //System.getenv("CORE_DB_PASS")
        val j = Jdbi.create(dataSource).installPlugin(SqlObjectPlugin()).open()

        val acrUpper = acr.toUpperCase()

        return j.createQuery("select CR.acronym, C.term from dbo.Class as C join dbo.Course as CR on C.courseid=CR.id where CR.acronym=:acr;")
            .bind("acr", acrUpper)
            .map { ro, _ -> Klass(ro.getString("acronym"), ro.getString("term")) }
            .list()
    }
}