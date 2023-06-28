package com.example.mainapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> implements Filterable {

    Context context;
    private List<Song> listSong;
    private final List<Song> listSongOld;

    public SongAdapter(List<Song> listSong, Context context) {
        this.listSong = listSong;
        this.listSongOld = listSong;
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = listSong.get(position);
        if ( song == null) return;

        holder.imgSong.setImageResource(song.getImg());
        holder.txtName.setText(song.getTitle());
    }

    @Override
    public int getItemCount() {
        if (listSong != null) return listSong.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    listSong = listSongOld;
                }else{
                    List<Song> list = new ArrayList<>();
                    for ( Song song : listSongOld ){
                        if (song.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(song);
                        }
                    }
                    listSong = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listSong;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listSong = (List<Song>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgSong;
        TextView txtName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgSong);
            txtName = itemView.findViewById(R.id.txtTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, phatNhac.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ten", listSong.get(getPosition()).getTitle());
                    intent.putExtra("dulieu", bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
    public class FavoriteSongDB extends ArrayAdapter<Song> {
        Context context;
        List<Song> lsData;
        FavoriteSongDB adapter;

        public FavoriteSongDB(@NonNull Context context, List<Song> lssong) {
            super(context, 0, lssong);
            this.context = context;
            lsData = lssong;
            adapter = this;
        }

        @Override
        public int getCount() {
            return lsData.size();
        }

        @Nullable
        @Override
        public Song getItem(int position) {
            return lsData.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.layout_song,
                        parent, false);
            }
            Song song = lsData.get(position);
            //lay doi tuong cac view tren layout
            TextView tvTen = view.findViewById(R.id.tvten);
            TextView tvSDT = view.findViewById(R.id.tvava);

            ImageButton btnXoa = view.findViewById(R.id.btnXoa);
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(context);
                    dialog.setTitle("Xoa danh ba");
                    dialog.setCancelable(true);
                    dialog.setMessage("Ban co chac chan xoa danh ba: " + song.getTitle());
                    dialog.setPositiveButton("Xoa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SongDB database = new SongDB(context);
                            database.delSong(song.getFile());
                            lsData = database.getAllSong();
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Huy",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //k xoa
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = dialog.create();
                    alert.show();

                }

            });
            return view;
        }

    }

}
