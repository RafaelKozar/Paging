package bakeapp.rako.paginglibrary_example

/**
 * Created by rako on 13/07/2018.
 */
interface MovieRepository {
    fun movies(type: String, pageSize: Int): Listing<MyMovie>
}