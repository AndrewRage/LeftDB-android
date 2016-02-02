package com.github.andreyrage.leftdb.samplecontentprovider.activities;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andreyrage.leftdb.AsyncCall;
import com.github.andreyrage.leftdb.LeftContentProvider;
import com.github.andreyrage.leftdb.samplecontentprovider.R;
import com.github.andreyrage.leftdb.samplecontentprovider.SampleProvider;
import com.github.andreyrage.leftdb.samplecontentprovider.adapters.EntityAdapter;
import com.github.andreyrage.leftdb.samplecontentprovider.entities.SimpleEntity;
import com.github.andreyrage.leftdb.samplecontentprovider.helpers.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();

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

        getContentResolver().registerContentObserver(SampleProvider.getUri(SimpleEntity.class), true, new ContentObserver(new Handler()) {

            @Override
            public void onChange(boolean selfChange) {
                Log.d(TAG, "onChange selfChange: " + selfChange);
                super.onChange(selfChange);
            }
        });

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
                        MainActivity.this.getContentResolver().delete(SampleProvider.getUri(SimpleEntity.class), null, null);
                        return null;
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
                            MainActivity.this.getContentResolver().delete(
                                    SampleProvider.getUri(SimpleEntity.class),
                                    "id = ?",
                                    new String[]{String.valueOf(simpleEntity.getId())}
                            );
                            return null;
                        }
                    }).call();
                    mEntityList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(1, null, this);
    }

    public void addEntity() {
        if (!TextUtils.isEmpty(mEditText.getText())) {
            final SimpleEntity entity = new SimpleEntity(mEditText.getText().toString());
            AsyncCall.make(new AsyncCall.Call<Void>() {
                @Override public Void call() {
                    /*ContentValues values = new ContentValues();
                    values.put("name", entity.getEntityName());
                    values.put("properties", new Gson().toJson(entity.getProperties()));
                    Uri uri = MainActivity.this.getContentResolver().insert(SampleProvider.getUri(SimpleEntity.class), values);
                    if (uri != null) {
                        long id = Long.parseLong(uri.getLastPathSegment());
                        entity.setId(id);
                    }*/
                    Log.d(TAG, "addEntity: " + entity);
                    LeftContentProvider.add(entity);
                    Log.d(TAG, "entity added: " + entity);
                    return null;
                }
            }, new AsyncCall.Do<Void>() {
                @Override public void doNext(Void v) {
                    mEntityList.add(0, entity);
                    mAdapter.notifyItemInserted(0);
                    mLayoutManager.scrollToPosition(0);
                }
            }).call();
            mEditText.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                return new CursorLoader(
                        MainActivity.this,
                        SampleProvider.getUri(SimpleEntity.class),
                        null,
                        null,
                        null,
                        "id DESC"
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        Log.d(TAG, "onLoadFinished " + loader + " data: " + data);
        switch (loader.getId()) {
            case 1:
                List<SimpleEntity> simpleEntities = DbHelper.getInstance(this).cursorMapper(data, SimpleEntity.class);
                mEntityList.clear();
                mEntityList.addAll(simpleEntities);
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "Entity: " + mEntityList);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset " + loader);
    }
}
