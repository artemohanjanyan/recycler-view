package ru.yandex.yamblz.task;

import android.animation.Animator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class NewItemsScrollListener extends RecyclerView.OnScrollListener {

    private AnimatorBuilder animatorBuilder;
    private LinearLayoutManager layoutManager;
    private int firstNotAnimated = 0;

    public NewItemsScrollListener(AnimatorBuilder animatorBuilder, RecyclerView recyclerView) {
        this.animatorBuilder = animatorBuilder;
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        animateNewItems(recyclerView);
    }

    public void animateNewItems(RecyclerView recyclerView) {
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        for (firstNotAnimated =
                     Math.max(firstNotAnimated, layoutManager.findFirstVisibleItemPosition());
             firstNotAnimated <= lastVisiblePosition;
             ++firstNotAnimated) {

            RecyclerView.ViewHolder viewHolder =
                    recyclerView.findViewHolderForAdapterPosition(firstNotAnimated);
            if (viewHolder != null) {
                animatorBuilder.buildAnimator(viewHolder.itemView)
                        .start();
            }
        }
    }

    public int getFirstNotAnimated() {
        return firstNotAnimated;
    }

    public void setFirstNotAnimated(int firstNotAnimated) {
        this.firstNotAnimated = firstNotAnimated;
    }

    public interface AnimatorBuilder {
        Animator buildAnimator(View view);
    }
}
