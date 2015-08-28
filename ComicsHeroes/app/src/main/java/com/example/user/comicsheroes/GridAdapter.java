package com.example.user.comicsheroes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by User on 25.08.2015.
 */
public class GridAdapter extends BaseAdapter{
    Context context;

    protected List<Hero> listHeroes;
    LayoutInflater inflater;
    public GridAdapter(Context context, List<Hero> listHeroes) {

        this.listHeroes = listHeroes;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

    }
    public int getCount() {
        return listHeroes.size();
    }

    public Hero getItem(int position) {
        return listHeroes.get(position);
    }

    public long getItemId(int position) {
        return listHeroes.get(position).hashCode();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.item_grid,
                    parent, false);

            holder.txtModel = (TextView) convertView
                    .findViewById(R.id.textHeroName);
            holder.imgHero = (SimpleDraweeView) convertView.findViewById(R.id.heroDraweeView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Hero hero = listHeroes.get(position);
        holder.txtModel.setText(hero.getHeroName());
        holder.imgHero.setImageURI(Uri.parse(hero.getHeroFace()));
        return convertView;
    }

    private class ViewHolder {
        TextView txtModel;
        SimpleDraweeView imgHero;
    }
}
