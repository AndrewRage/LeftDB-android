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