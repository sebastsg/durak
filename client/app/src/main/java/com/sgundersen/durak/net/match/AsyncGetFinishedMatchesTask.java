package com.sgundersen.durak.net.match;

import com.sgundersen.durak.core.net.match.FinishedMatchItemInfoList;
import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.match.RecordedMatchTable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncGetFinishedMatchesTask extends AsyncHttpTask<String, Void, FinishedMatchItemInfoList> {

    private final RecordedMatchTable recordedMatchTable;

    @Override
    protected FinishedMatchItemInfoList doInBackground(String... strings) {
        return gson.fromJson(get("history/list"), FinishedMatchItemInfoList.class);
    }

    @Override
    protected void onPostExecute(FinishedMatchItemInfoList items) {
        if (items != null) {
            recordedMatchTable.setFinishedMatchList(items);
        }
    }


}
