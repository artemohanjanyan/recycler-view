package ru.yandex.yamblz.task;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final Paint backgroundPaint = new Paint();
    private ContentAdapter contentAdapter;

    public ItemTouchHelperCallback(ContentAdapter contentAdapter) {
        this.contentAdapter = contentAdapter;
        backgroundPaint.setColor(Color.RED);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition(), to = target.getAdapterPosition();
        if (from < to) {
            for (int i = from; i < to; ++i) {
                Collections.swap(contentAdapter.colors, i, i + 1);
            }
        } else {
            for (int i = from; i > to; --i) {
                Collections.swap(contentAdapter.colors, i, i - 1);
            }
        }
        contentAdapter.notifyItemMoved(from, to);
        contentAdapter.from = from;
        contentAdapter.to = to;
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        contentAdapter.colors.remove(position);
        contentAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ACTION_STATE_DRAG) {
            return;
        }

        // item is deleted if it is dragged at least by recyclerView.getWidth() * swipeThreshold
        backgroundPaint.setAlpha(
                Math.min(255, (int) (dX / recyclerView.getWidth() * 255 /
                        getSwipeThreshold(viewHolder))));

        View view = viewHolder.itemView;
        c.drawRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(),
                backgroundPaint);
    }
}