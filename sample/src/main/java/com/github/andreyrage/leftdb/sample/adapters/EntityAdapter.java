/*
 * Copyright 2017 Andrii Horishnii
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.andreyrage.leftdb.sample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.andreyrage.leftdb.sample.R;
import com.github.andreyrage.leftdb.sample.entities.SimpleEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by rage on 11/27/15.
 */
public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.ViewHolder> {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private List<SimpleEntity> mEntityList;
    private OnItemClickListener mClickListener;

    public EntityAdapter(List<SimpleEntity> entityList, OnItemClickListener clickListener) {
        mEntityList = entityList;
        mClickListener = clickListener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleEntity simpleEntity = mEntityList.get(position);
        holder.setItem(simpleEntity, mClickListener);
    }

    @Override public int getItemCount() {
        return mEntityList != null ? mEntityList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewName;
        private TextView mTextViewDescription;
        private Button mButtonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.text_view_name);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.text_view_description);
            mButtonDelete = (Button) itemView.findViewById(R.id.button_delete);
        }

        public void setItem(final SimpleEntity simpleEntity, final OnItemClickListener clickListener) {
            mTextViewName.setText(simpleEntity.getEntityName());
            if (simpleEntity.getProperties() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                String date = dateFormat.format(simpleEntity.getProperties().getCreatedAt());
                mTextViewDescription.setText(String.format("id = %d, created at %s", simpleEntity.getId(), date));
            } else {
                mTextViewDescription.setText(String.format("id = %d", simpleEntity.getId()));
            }
            if (clickListener != null) {
                mButtonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        clickListener.onClick(simpleEntity);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(SimpleEntity simpleEntity);
    }
}
