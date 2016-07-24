package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
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

class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(createColorForPosition(position));
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

    ItemTouchHelper.Callback buildItemTouchHelperCallback() {
        return new ItemTouchHelperCallback();
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

    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
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
    }

}
