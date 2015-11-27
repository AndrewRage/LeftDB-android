package com.github.andreyrage.leftdb.sample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andreyrage.leftdb.AsyncCall;
import com.github.andreyrage.leftdb.queries.SelectQuery;
import com.github.andreyrage.leftdb.sample.R;
import com.github.andreyrage.leftdb.sample.adapters.EntityAdapter;
import com.github.andreyrage.leftdb.sample.entities.SimpleEntity;
import com.github.andreyrage.leftdb.sample.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private EntityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<SimpleEntity> mEntityList = new ArrayList<>();
    private DbUtils mDbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbUtils = DbUtils.getInstance(this);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addEntity();
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                addEntity();
            }
        });

        findViewById(R.id.button_delete_all).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AsyncCall.make(new AsyncCall.Call() {
                    @Override public Object call() {
                        return mDbUtils.deleteAll(SimpleEntity.class);
                    }
                }).call();
                mEntityList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EntityAdapter(mEntityList, new EntityAdapter.OnItemClickListener() {
            @Override public void onClick(final SimpleEntity simpleEntity) {
                AsyncCall.make(new AsyncCall.Call() {
                    @Override public Object call() {
                        return mDbUtils.delete(simpleEntity);
                    }
                }).call();
                int position = mEntityList.indexOf(simpleEntity);
                mEntityList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        AsyncCall.make(new AsyncCall.Call<List<SimpleEntity>>() {
            @Override public List<SimpleEntity> call() {
                return mDbUtils.select(
                        SelectQuery.builder()
                                .entity(SimpleEntity.class)
                                .orderBy("id desc")
                                .build()
                );
            }
        }, new AsyncCall.Do<List<SimpleEntity>>() {
            @Override public void doNext(List<SimpleEntity> simpleEntities) {
                mEntityList.clear();
                mEntityList.addAll(simpleEntities);
                mAdapter.notifyDataSetChanged();
            }
        }).call();
    }

    public void addEntity() {
        if (!TextUtils.isEmpty(mEditText.getText())) {
            final SimpleEntity entity = new SimpleEntity(mEditText.getText().toString());
            AsyncCall.make(new AsyncCall.Call<Long>() {
                @Override public Long call() {
                    return mDbUtils.add(entity);
                }
            }, new AsyncCall.Do<Long>() {
                @Override public void doNext(Long aLong) {
                    mEntityList.add(0, entity);
                    mAdapter.notifyItemInserted(0);
                    mLayoutManager.scrollToPosition(0);
                }
            }).call();
            mEditText.setText("");
        }
    }
}
