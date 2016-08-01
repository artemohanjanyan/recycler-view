package ru.yandex.yamblz.task;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint = new Paint();

    public ItemDecoration() {
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Invert color but preserve alpha.
     *
     * @param color color to invert.
     * @return inverted color.
     */
    private static int invertColor(int color) {
        return (0xFF000000 & color) | ~color;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final float strokeHalfWidth = parent.getChildAt(0).getWidth() / 50;
        paint.setStrokeWidth(strokeHalfWidth * 2);

        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position % 2 == 0) {
                int backgroundColor = ((ColorDrawable) child.getBackground()).getColor();
                paint.setColor(invertColor(backgroundColor));
                c.drawRect(child.getLeft() + strokeHalfWidth,
                        child.getTop() + strokeHalfWidth,
                        child.getRight() - strokeHalfWidth,
                        child.getBottom() - strokeHalfWidth,
                        paint);
            }
        }
    }
}
