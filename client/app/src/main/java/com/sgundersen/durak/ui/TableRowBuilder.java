package com.sgundersen.durak.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class TableRowBuilder {

    private final Context context;
    private final int resourceId;
    private TableRow row;

    public TableRowBuilder(Context context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
        reset();
    }

    public TableRowBuilder reset() {
        row = (TableRow) LayoutInflater.from(context).inflate(resourceId, null);
        return this;
    }

    public TableRowBuilder set(int id, String text) {
        ((TextView) row.findViewById(id)).setText(text);
        return this;
    }

    public TableRowBuilder set(int id, int stringId) {
        return set(id, context.getResources().getString(stringId));
    }

    public TableRowBuilder bold() {
        for (int childIndex = 0; childIndex < row.getChildCount(); childIndex++) {
            View child = row.getChildAt(childIndex);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }
        return this;
    }

    public TableRowBuilder backgroundColor(int color) {
        row.setBackgroundColor(color);
        return this;
    }

    public TableRowBuilder tag(Object tag) {
        row.setTag(tag);
        return this;
    }

    public TableRowBuilder click(View.OnClickListener listener) {
        row.setOnClickListener(listener);
        return this;
    }

    public TableRow build() {
        TableRow result = row;
        reset();
        return result;
    }

}
