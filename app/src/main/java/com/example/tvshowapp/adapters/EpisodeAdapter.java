package com.example.tvshowapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowapp.R;
import com.example.tvshowapp.databinding.ItemContainerEpisodeBinding;
import com.example.tvshowapp.models.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>{

    private List<Episode> episodes;
    private LayoutInflater layoutInflater;

    public EpisodeAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull

    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
       if (layoutInflater==null){
           layoutInflater=LayoutInflater.from(parent.getContext());
       }
       ItemContainerEpisodeBinding itemContainerEpisodeBinding= DataBindingUtil.inflate(
               layoutInflater, R.layout.item_container_episode,parent,false
       );
        return new EpisodeViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull  EpisodeAdapter.EpisodeViewHolder holder, int position) {

        holder.bindEpisode(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodeViewHolder( ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }

        public void bindEpisode(Episode episode){
            String title="S";
            String season=episode.getSeason();
            if (season.length() == 1){
                season="0".concat(season);
            }
            String episodeeNumber=episode.getEpisode();
            if (episodeeNumber.length() ==1){
                episodeeNumber="0".concat(episodeeNumber);
            }
            episodeeNumber="E".concat(episodeeNumber);
            title=title.concat(season).concat(episodeeNumber);
            itemContainerEpisodeBinding.setTitle(title);
            itemContainerEpisodeBinding.setName(episode.getName());
            itemContainerEpisodeBinding.setAirDate(episode.getAirDate());
        }
    }
}
