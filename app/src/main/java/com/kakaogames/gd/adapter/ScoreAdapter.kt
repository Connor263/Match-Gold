package com.kakaogames.gd.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakaogames.gd.R
import com.kakaogames.gd.data.model.main.Matches
import com.kakaogames.gd.databinding.ItemScoreBinding
import com.kakaogames.gd.interfaces.ListInterface

class ScoreAdapter(private val listener: ListInterface<Int>) :
    ListAdapter<Matches.Scorer, ScoreAdapter.ViewHolder>(
        AsyncDifferConfig.Builder(ItemCallBack()).build()
    ) {
    inner class ViewHolder(private val binding: ItemScoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Matches.Scorer) = with(binding) {
            val resources = root.context.resources

            root.setOnClickListener {
                listener.onClick(item.player?.id!!)
            }

            tvPlayerName.text = item.player?.name
            tvPlayerTeam.text = item.team?.name
            tvPlayerNationality.text = item.player?.nationality
            tvPlayerPosition.text = item.player?.position
            tvGoals.text = resources.getString(
                R.string.goals, item.numberOfGoals
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemScoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ScoreAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ItemCallBack : DiffUtil.ItemCallback<Matches.Scorer>() {
        override fun areItemsTheSame(oldItem: Matches.Scorer, newItem: Matches.Scorer): Boolean {
            return oldItem.player?.id == newItem.player?.id
        }

        override fun areContentsTheSame(oldItem: Matches.Scorer, newItem: Matches.Scorer): Boolean {
            return oldItem == newItem
        }
    }
}