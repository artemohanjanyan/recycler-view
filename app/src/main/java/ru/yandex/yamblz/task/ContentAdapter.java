package ru.yandex.yamblz.task;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(createColorForPosition(position));

        holder.itemView.setOnClickListener(v -> {
            int realPosition = holder.getAdapterPosition();
            if (realPosition == RecyclerView.NO_POSITION) {
                return;
            }

            int color = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            colors.set(realPosition, color);

            holder.bind(color);
            notifyItemChanged(realPosition, new Object());
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private Integer createColorForPosition(int position) {
        while (position >= colors.size()) {
            colors.add(Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
        }
        return colors.get(position);
    }

    public ItemTouchHelper.Callback buildItemTouchHelperCallback() {
        return new ItemTouchHelperCallback();
    }

    public RecyclerView.ItemDecoration buildItemDecoration() {
        return new ItemDecoration();
    }

    static class ContentHolder extends RecyclerView.ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
        }

        void bind(Integer color) {
            itemView.setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }
    }

    private static class ItemDecoration extends RecyclerView.ItemDecoration {
        private Paint paint = new Paint();

        private ItemDecoration() {
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            final float strokeHalfWidth = parent.getChildAt(0).getWidth() / 50;
            paint.setStrokeWidth(strokeHalfWidth * 2);

            for (int i = 0; i < parent.getChildCount(); ++i) {
                final View child = parent.getChildAt(i);
                final int position = parent.getChildAdapterPosition(child);

                if (position % 2 == 0) {
                    int backgroundColor = ((ColorDrawable) child.getBackground()).getColor();
                    paint.setColor(0xFF000000 | ~backgroundColor);
                    c.drawRect(child.getLeft() + strokeHalfWidth,
                            child.getTop() + strokeHalfWidth,
                            child.getRight() - strokeHalfWidth,
                            child.getBottom() - strokeHalfWidth,
                            paint);
                }
            }
        }
    }

    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
        private final Paint backgroundPaint = new Paint();

        {
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
                    Collections.swap(ContentAdapter.this.colors, i, i + 1);
                }
            } else {
                for (int i = from; i > to; --i) {
                    Collections.swap(ContentAdapter.this.colors, i, i - 1);
                }
            }
            ContentAdapter.this.notifyItemMoved(from, to);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ContentAdapter.this.colors.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            if (actionState == ACTION_STATE_DRAG) {
                return;
            }

            // item is deleted if it is dragged at least by recyclerView.getWidth() / 2
            backgroundPaint.setAlpha(
                    Math.min(255, (int) (dX / recyclerView.getWidth() * 255 /
                            getSwipeThreshold(viewHolder))));

            View view = viewHolder.itemView;
            c.drawRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(),
                    backgroundPaint);
        }
    }

}
