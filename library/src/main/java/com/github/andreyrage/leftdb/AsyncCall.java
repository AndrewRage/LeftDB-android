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

    public static <Result> AsyncCall make(Call<Result> call) {
        return new AsyncCall<>(call, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Result doInBackground(Void... params) {
        try {
            return mCall.call();
        } catch (Exception e) {
            return (Result) new MonitorClass(e);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result instanceof MonitorClass) {
            Exception originException = ((MonitorClass) result).getException();
            IllegalStateException newException =
                    new IllegalStateException("An error occurred while doing in background: "
                            + originException.getMessage());
            newException.setStackTrace(originException.getStackTrace());
            throw newException;
        }
        if (mDoNext != null) {
            mDoNext.doNext(result);
        }
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

    private static class MonitorClass {
        private Exception mException;

        public MonitorClass(Exception exception) {
            mException = exception;
        }

        public Exception getException() {
            return mException;
        }

        public void setException(Exception exception) {
            mException = exception;
        }
    }

}