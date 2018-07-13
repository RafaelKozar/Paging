package bakeapp.rako.paginglibrary_example

/**
 * Created by rako on 13/07/2018.
 */
data class Movies(val results: List<MyMovie>, val total_pages : String)
data class MyMovie(val vote_count: String?,
                   val id: String?,
                   val video: String?,
                   val vote_average: String?,
                   val title: String?,
                   val popularity: String?,
                   val poster_path: String?,
                   val original_language: String?,
                   val original_title: String?,
                   val genre_ids: List<String>?,
                   val backdrop_path: String?,
                   val adult: String?,
                   val overview: String?,
                   var release_date: String?)