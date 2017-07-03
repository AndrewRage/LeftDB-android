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
