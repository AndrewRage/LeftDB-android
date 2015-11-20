package com.github.andreyrage.leftdb;

import android.os.AsyncTask;

/**
 * Created by Vlad on 11/19/15.
 */
public class AsyncCall<Result> extends AsyncTask<Void, Void, Result> {

	private Call<Result> mCall;
	private Do<Result> mDoNext;

	private AsyncCall(Call<Result> call, Do<Result> doNext) {
		mCall = call;
		mDoNext = doNext;
	}

	public static <Result> AsyncCall make(Call<Result> call, Do<Result> doNext) {
		return new AsyncCall<>(call, doNext);
	}

	@Override protected Result doInBackground(Void... params) {
		return mCall.call();
	}

	@Override protected void onPostExecute(Result result) {
		mDoNext.doNext(result);
	}

	public interface Call<Result> {
		Result call();
	}

	public interface Do<Result> {
		void doNext(Result result);
	}

	public void call() {
		execute();
	}

}