package cc.guoxingnan.myblog.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * Created by mixinan on 2016/5/29.
 */
public class SpaceItemDecoration extends ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
    }
}
