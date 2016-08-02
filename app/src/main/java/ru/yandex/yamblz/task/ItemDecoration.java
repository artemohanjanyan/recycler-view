package ru.yandex.yamblz.task;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint = new Paint();
    private ContentAdapter adapter;
    private boolean isDecorated = false;

    public ItemDecoration(ContentAdapter adapter) {
        paint.setStyle(Paint.Style.STROKE);
        this.adapter = adapter;
    }

    /**
     * Invert color but preserve alpha.
     *
     * @param color color to invert.
     * @return inverted color.
     */
    private static int invertColor(int color) {
        return (0xFF000000 & color) | ~(0x00FFFFFF & color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final float strokeHalfWidth = parent.getChildAt(0).getWidth() / 50;
        paint.setStrokeWidth(strokeHalfWidth * 2);

        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position % 2 == 0 && isDecorated()) {
                int backgroundColor = ((ColorDrawable) child.getBackground()).getColor();
                paint.setColor(invertColor(backgroundColor));
                c.drawRect(child.getLeft() + strokeHalfWidth,
                        child.getTop() + strokeHalfWidth,
                        child.getRight() - strokeHalfWidth,
                        child.getBottom() - strokeHalfWidth,
                        paint);
            }
            if (position == adapter.from || position == adapter.to) {
                paint.setColor(Color.RED);
                c.drawLine(child.getLeft() + strokeHalfWidth * 3, child.getTop() + strokeHalfWidth * 2,
                        child.getLeft() + strokeHalfWidth * 3, child.getBottom() - strokeHalfWidth * 2,
                        paint);
            }
        }
    }

    public boolean isDecorated() {
        return isDecorated;
    }

    public void setDecorated(boolean decorated) {
        isDecorated = decorated;
    }
}
