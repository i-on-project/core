package org.ionproject.core.course

import org.ionproject.core.common.mappers.CourseMapper
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Component

@Component
class CourseRepo(private val tm : TransactionManager) {
    val courseMapper : CourseMapper = CourseMapper()

     fun getCourses(page : Int, limit : Int): List<Course>? {
        val result = tm.run {
            handle -> handle.createQuery("SELECT * FROM courseWithTerm OFFSET :offset LIMIT :lim")
                .bind("offset", page*limit)
                .bind("lim", limit)
                .map(courseMapper)
                .list()
        }

         //TODO:REVIEW THIS WEIRD BLOCK IN THE FUTURE
         if(result?.size == 0) {
             if( page == 0 && limit == 5 )  //Default params 0 & 5
                 return listOf()        //Case has 0 results, but user didn't use params. then normal view with empty list
             else
                 return null            //Returns null to trigger 404 (NOT FOUND) when user tried some page/limit combination that holds no results
         } else
             return result
    }

     fun getCourseById(id: Int): Course? {
        val result = tm.run {
            handle -> handle.createQuery("SELECT * FROM courseWithTerm WHERE id=:id")
                .bind("id", id)
                .map(courseMapper)
                .one()
        }

        return result
    }
}