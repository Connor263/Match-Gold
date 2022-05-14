package com.kakaogames.gd.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakaogames.gd.R
import com.kakaogames.gd.data.model.main.CompetitionInfo
import com.kakaogames.gd.databinding.ItemCompetitionBinding
import com.kakaogames.gd.interfaces.ListInterface
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CompetitionAdapter(private val listener: ListInterface<Int>) :
    ListAdapter<CompetitionInfo.Competition, CompetitionAdapter.ViewHolder>(
        AsyncDifferConfig.Builder(ItemCallBack()).build()
    ) {

    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    )
    val lastUpdatedFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        Locale.getDefault()
    )

    inner class ViewHolder(private val binding: ItemCompetitionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CompetitionInfo.Competition) = with(binding) {
            val resources = root.context.resources

            root.setOnClickListener { _ ->
                item.id?.let { it -> listener.onClick(it) }
            }


            tvAreaName.text = item.area?.name

            tvName.text = item.name

            tvStartDate.text =
                resources.getString(
                    R.string.start,
                    item.currentSeason?.startDate?.let {
                        DateFormat.getDateInstance(DateFormat.SHORT)
                            .format(dateFormat.parse(it) ?: "")
                    }
                )

            tvEndDate.text = resources.getString(
                R.string.end,
                item.currentSeason?.endDate?.let {
                    DateFormat.getDateInstance(DateFormat.SHORT).format(dateFormat.parse(it) ?: "")
                }
            )

            tvLastUpdated.text = resources.getString(R.string.last_updated,
                item.lastUpdated?.let {
                    DateFormat.getDateInstance(DateFormat.SHORT)
                        .format(lastUpdatedFormat.parse(it) ?: "")
                }
            )

            tvMatchDay.text = resources.getString(
                R.string.match_day,
                item.currentSeason?.currentMatchday
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCompetitionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CompetitionAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ItemCallBack : DiffUtil.ItemCallback<CompetitionInfo.Competition>() {
        override fun areItemsTheSame(
            oldItem: CompetitionInfo.Competition,
            newItem: CompetitionInfo.Competition
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CompetitionInfo.Competition,
            newItem: CompetitionInfo.Competition
        ): Boolean {
            return oldItem == newItem
        }
    }
}