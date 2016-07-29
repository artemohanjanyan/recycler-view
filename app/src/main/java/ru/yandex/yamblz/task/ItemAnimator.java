package ru.yandex.yamblz.task;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

public class ItemAnimator extends DefaultItemAnimator {
    private static final int CHANGE_DURATION = 600;

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder,
                                 RecyclerView.ViewHolder newHolder,
                                 int fromX, int fromY, int toX, int toY) {
        AnimatorSet oldAnimator = new AnimatorSet();
        AnimatorSet newAnimator = new AnimatorSet();
        AnimatorSet animator = new AnimatorSet();


        ContentAdapter.ContentHolder castedOldHolder = (ContentAdapter.ContentHolder) oldHolder;
        castedOldHolder.bind(castedOldHolder.oldColor);
        oldHolder.itemView.setPivotX(0);
        oldAnimator.playTogether(
                ObjectAnimator.ofFloat(oldHolder.itemView, "scaleX", 1, 0)
        );
        oldAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchChangeFinished(oldHolder, true);
            }
        });

        newHolder.itemView.setPivotX(newHolder.itemView.getWidth());
        newAnimator.playTogether(
                ObjectAnimator.ofFloat(newHolder.itemView, "scaleX", 0, 1)
        );
        newAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchChangeFinished(newHolder, false);
            }
        });

        animator.playTogether(oldAnimator, newAnimator);
        animator.setDuration(CHANGE_DURATION);
        animator.start();

        return true;
    }
}
