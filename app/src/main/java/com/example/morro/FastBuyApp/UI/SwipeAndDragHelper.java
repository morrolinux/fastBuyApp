package com.example.morro.FastBuyApp.UI;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

/**
 * Handle the gestures by calling adapter's ActionCompletionContract methods
 */
public class SwipeAndDragHelper extends Callback {

    private ActionCompletionContract contract;      // passed object which will implement the interface

    /** Constructor */
    public SwipeAndDragHelper(ActionCompletionContract contract) {
        this.contract = contract;
    }

    /** Define flags for IDLE, DRAG and SWIPE */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /** gets called when a view is dragged from its position to other positions */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //contract.onViewMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());   //we don't really need this feature as of now
        return true;
    }

    /** let's disable item dragging since we won't need it. */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /** gets called when a view is completely swiped out */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        contract.onViewSwiped(viewHolder.getAdapterPosition());
    }

    /* Action completion contract interface definition */
    public interface ActionCompletionContract {
        void onViewMoved(int oldPosition, int newPosition);
        void onViewSwiped(int position);
    }
}
