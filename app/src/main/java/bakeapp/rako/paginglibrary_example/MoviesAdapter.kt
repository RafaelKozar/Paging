package bakeapp.rako.paginglibrary_example

import android.arch.paging.PagedListAdapter

import android.support.v7.widget.RecyclerView
import android.support.v7.util.DiffUtil

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Created by rako on 13/07/2018.
 */
class MoviesAdapter(val retryCallBack: () -> Unit)
    : PagedListAdapter<MyMovie, MoviesAdapter.MovieViewHolder>(COMPARATOR) {


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent)
    }


    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(

         LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)) {
        fun bindTo(item: MyMovie?) {
            Glide.with(itemView.context)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w500"+item?.poster_path)
                    .into(itemView.img_movie)
        }
    }


    companion object {

        val COMPARATOR = object  : DiffUtil.ItemCallback<MyMovie>(){

            override fun areItemsTheSame(oldItem: MyMovie?, newItem: MyMovie?): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MyMovie?, newItem: MyMovie?): Boolean {
                return oldItem?.id == newItem?.id
            }

        }

        /*private val PAYLOAD_SCORE = Any()
        val COMPARATOR = object : DiffUtil.ItemCallback<MyMovie>() {
            override fun areContentsTheSame(oldItem: MyMovie, newItem: MyMovie): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: MyMovie, newItem: MyMovie): Boolean =
                    oldItem.id == newItem.id

            override fun getChangePayload(oldItem: MyMovie, newItem: MyMovie): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: MyMovie, newItem: MyMovie): Boolean {
            // DON'T do this copy in a real app, it is just convenient here for the demo :)
            // because reddit randomizes scores, we want to pass it as a payload to minimize
            // UI updates between refreshes
            return oldItem.copy(id = newItem.vote_average) == newItem
        }*/
    }
}
