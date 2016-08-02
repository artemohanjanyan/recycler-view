package ru.yandex.yamblz.ui.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.task.ContentAdapter;
import ru.yandex.yamblz.task.ItemDecoration;
import ru.yandex.yamblz.task.ItemTouchHelperCallback;
import ru.yandex.yamblz.task.NewItemsScrollListener;

public class ContentFragment extends BaseFragment {

    private static final String COLUMN_N = "column number";
    private static final String FIRST_NOT_ANIMATED = "first not animated";
    private static final String IS_DECORATED = "is decorated";
    private static final String HAS_SCROLL_LISTENER = "has scroll listener";

    @BindView(R.id.rv)
    RecyclerView rv;

    private GridLayoutManager layoutManager;
    private ItemDecoration itemDecoration;

    private NewItemsScrollListener scrollListener;
    private boolean hasScrollListener = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int columnN = 1;
        if (savedInstanceState != null) {
            columnN = savedInstanceState.getInt(COLUMN_N, 1);
        }
        layoutManager = new GridLayoutManager(getContext(), columnN);
        rv.setLayoutManager(layoutManager);

        ContentAdapter adapter = new ContentAdapter();
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        new ItemTouchHelper(new ItemTouchHelperCallback(adapter)).attachToRecyclerView(rv);
        itemDecoration = new ItemDecoration(adapter);
        if (savedInstanceState != null) {
            itemDecoration.setDecorated(savedInstanceState.getBoolean(IS_DECORATED, false));
        }
        rv.addItemDecoration(itemDecoration);

        scrollListener = new NewItemsScrollListener(itemView -> {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(itemView, "scaleX", 0.5f, 1f),
                    ObjectAnimator.ofFloat(itemView, "scaleY", 0.5f, 1f)
            );
            animatorSet.setDuration(500);
            return animatorSet;
        }, rv);
        if (savedInstanceState != null) {
            scrollListener.setFirstNotAnimated(savedInstanceState.getInt(FIRST_NOT_ANIMATED, 0));
            hasScrollListener = savedInstanceState.getBoolean(HAS_SCROLL_LISTENER, false);
        }
        setScrollListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLUMN_N, layoutManager.getSpanCount());
        outState.putInt(FIRST_NOT_ANIMATED, scrollListener.getFirstNotAnimated());
        outState.putBoolean(IS_DECORATED, itemDecoration.isDecorated());
        outState.putBoolean(HAS_SCROLL_LISTENER, hasScrollListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                layoutManager.setSpanCount(layoutManager.getSpanCount() + 1);
                break;
            case R.id.menu_item_remove:
                layoutManager.setSpanCount(Math.max(1, layoutManager.getSpanCount() - 1));
                break;
            case R.id.menu_item_decorations:
                itemDecoration.setDecorated(!itemDecoration.isDecorated());
                break;
            case R.id.menu_item_animations:
                hasScrollListener = !hasScrollListener;
                setScrollListener();
                break;
        }
        layoutManager.requestLayout();
        rv.getAdapter().notifyItemRangeChanged(layoutManager.findFirstVisibleItemPosition(), 0);
        return true;
    }

    private void setScrollListener() {
        if (hasScrollListener) {
            scrollListener.animateNewItems(rv);
            rv.addOnScrollListener(scrollListener);
        } else {
            rv.removeOnScrollListener(scrollListener);
        }
    }
}
