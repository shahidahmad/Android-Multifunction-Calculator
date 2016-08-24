package com.example.tgk.integration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

/**
 *  The code in this file is based on the project FragmentBasicsM25 that
 *  Todd Kelly has given us through his personal communication.
 * Created by Xing on 4/10/2015.
 */
public class MyCursorAdapter extends SimpleCursorAdapter {
    public interface OnDeleteTaskListener {
        public void onTaskDeleted(long id);
    }

    private OnDeleteTaskListener onDeleteTaskListener;

    public MyCursorAdapter(OnDeleteTaskListener onDeleteTaskListener, Context context, int layout,
                           Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.onDeleteTaskListener = onDeleteTaskListener;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        final long itemId = cursor.getLong(cursor.getColumnIndex("_id"));
        final Context ctx = context;
        ImageButton btnDeleteRecipient = (ImageButton) view.findViewById(R.id.imageButtonDel);

        btnDeleteRecipient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                long selectedId = itemId;
                MyCursorAdapter.this.deleteTaskById(ctx, selectedId);
            }
        });

        super.bindView(view, context, cursor);
    }

    private void deleteTaskById(Context context, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (onDeleteTaskListener != null) {
                    onDeleteTaskListener.onTaskDeleted(id);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();

    }
}
