package org.ionproject.core.course

import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.mappers.CourseMapper
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.statement.Query
import org.springframework.stereotype.Component

@Component
class CourseRepo(private val tm : TransactionManager) {
    val courseMapper : CourseMapper = CourseMapper()

     fun getCourses(page : Int, limit : Int, flagDefaultValues : Boolean): List<Course> {
        val result = tm.run {
            handle ->
            {
                val q: Query
                if(flagDefaultValues) {
                    q = handle.createQuery("SELECT * FROM courseWithTerm")
                } else {
                    q = handle.createQuery("SELECT * FROM courseWithTerm OFFSET :offset LIMIT :lim")
                            .bind("offset", page * limit)
                            .bind("lim", limit)
                }

                q.map(courseMapper).list()
            }()
        }

         if(result?.size == 0) {
             if(flagDefaultValues)
                 return listOf()      //Case has 0 results, but user didn't use params. then normal view with empty list
             else
                 throw ResourceNotFoundException("There is no course at the page $page with limit $limit.")
             //User requested a resource specifing query parameters and such held no result. e.g. /v0/courses?page=5&limit=100
         } else
            return result as List<Course>  //Either had default values or not, there was a result...
    }

     fun getCourseById(id: Int): Course {
        val result = tm.run {
            handle -> handle.createQuery("SELECT * FROM courseWithTerm WHERE id=:id")
                .bind("id", id)
                .map(courseMapper)
                .findOne()
        }

         if(result?.isEmpty == false)
             return result.get()
         else
             throw ResourceNotFoundException("The course with id=$id was not found.")
    }
}