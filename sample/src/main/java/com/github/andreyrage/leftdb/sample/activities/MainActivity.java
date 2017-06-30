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
import com.github.andreyrage.leftdb.sample.helpers.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private EntityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<SimpleEntity> mEntityList = new ArrayList<>();
    private DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = DbHelper.getInstance(this);

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
                        return mDbHelper.deleteAll(SimpleEntity.class);
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
                int position = mEntityList.indexOf(simpleEntity);
                if (position >= 0) {
                    AsyncCall.make(new AsyncCall.Call() {
                        @Override
                        public Object call() {
                            return mDbHelper.delete(simpleEntity);
                        }
                    }).call();
                    mEntityList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        AsyncCall.make(new AsyncCall.Call<List<SimpleEntity>>() {
            @Override public List<SimpleEntity> call() {
                return mDbHelper.select(
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
                    return mDbHelper.add(entity);
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
