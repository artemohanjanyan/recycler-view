package ru.yandex.yamblz.ui.fragments;

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

public class ContentFragment extends BaseFragment {

    private static final String COLUMN_N = "column number";

    @BindView(R.id.rv)
    RecyclerView rv;

    private GridLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        new ItemTouchHelper(adapter.buildItemTouchHelperCallback()).attachToRecyclerView(rv);
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
        }
        layoutManager.requestLayout();
        rv.getAdapter().notifyItemRangeChanged(layoutManager.findFirstVisibleItemPosition(), 0);
        return true;
    }
}
