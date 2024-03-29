package com.acorn.myframeapp.ui.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.view.View;

import java.util.regex.Pattern;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by yu on 2017/3/3.
 */

public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    /**
     * mode for direction
     */
    public static final int MODE_HORIZONTAL = 0;
    public static final int MODE_VERTICAL = 1;
    public static final int MODE_GRID = 2;

    /**
     * default decoration thick size
     */
    private final int DEFAULT_SIZE = 1;
    /**
     * default decoration color
     */
    private final String DEFAULT_COLOR = "#bdbdbd";

    /**
     * image resource id for R.java
     */
    private int drawableRid = 0;
    /**
     * decoration color
     */
    private int color = Color.parseColor(DEFAULT_COLOR);
    /**
     * decoration thick size
     */
    private int thick;
    /**
     * decoration dash with
     */
    private int dashWidth = 0;
    /**
     * decoration dash gap
     */
    private int dashGap = 0;
    /**
     * direction mode for decoration
     */
    private int recyclerviewMode;

    private Paint paint;

    private Bitmap bmp;
    private NinePatch ninePatch;
    /**
     * sign for if the resource image is a ninepatch image
     */
    private Boolean hasNinePatch = false;
    private boolean isDrawLastDivider;
    private boolean isDrawHeaderDivider;
    public RecycleViewDivider(int recyclerviewMode, Context context, int drawableRid) {
        this.recyclerviewMode = recyclerviewMode;
        this.drawableRid = drawableRid;

        this.bmp = BitmapFactory.decodeResource(context.getResources(), drawableRid);
        if (bmp.getNinePatchChunk() != null) {
            hasNinePatch = true;
            ninePatch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        }
        initPaint();

    }
    public RecycleViewDivider(int recyclerviewMode, Context context, int drawableRid, boolean isDrawLastDivider, boolean isDrawHeaderDivider) {
        this.recyclerviewMode = recyclerviewMode;
        this.drawableRid = drawableRid;
        this.isDrawHeaderDivider = isDrawHeaderDivider;
        this.isDrawLastDivider = isDrawLastDivider;
        this.bmp = BitmapFactory.decodeResource(context.getResources(), drawableRid);
        if (bmp.getNinePatchChunk() != null) {
            hasNinePatch = true;
            ninePatch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        }
        initPaint();

    }
    public RecycleViewDivider(int recyclerviewMode, int color, int thick, int dashWidth, int dashGap, boolean isDrawLastDivider, boolean isDrawHeaderDivider) {
        this.recyclerviewMode = recyclerviewMode;
        this.color = color;
        this.thick = thick;
        this.dashWidth = dashWidth;
        this.dashGap = dashGap;
        this.isDrawHeaderDivider = isDrawHeaderDivider;
        this.isDrawLastDivider = isDrawLastDivider;
        initPaint();

    }
    public RecycleViewDivider(int recyclerviewMode, int color, int thick, int dashWidth, int dashGap) {
        this.recyclerviewMode = recyclerviewMode;
        this.color = color;
        this.thick = thick;
        this.dashWidth = dashWidth;
        this.dashGap = dashGap;

        initPaint();

    }
    public RecycleViewDivider(int recyclerviewMode, String color, int thick, int dashWidth, int dashGap) {
        this.recyclerviewMode = recyclerviewMode;
        if (isColorString(color)) {
            this.color = Color.parseColor(color);
        } else {
            this.color = Color.parseColor(DEFAULT_COLOR);
        }
        this.thick = thick;
        this.dashWidth = dashWidth;
        this.dashGap = dashGap;

        initPaint();
    }
    public RecycleViewDivider(int recyclerviewMode, String color, int thick, int dashWidth, int dashGap, boolean isDrawLastDivider, boolean isDrawHeaderDivider) {
        this.recyclerviewMode = recyclerviewMode;
        if (isColorString(color)) {
            this.color = Color.parseColor(color);
        } else {
            this.color = Color.parseColor(DEFAULT_COLOR);
        }
        this.thick = thick;
        this.dashWidth = dashWidth;
        this.dashGap = dashGap;
        this.isDrawHeaderDivider = isDrawHeaderDivider;
        this.isDrawLastDivider = isDrawLastDivider;
        initPaint();
    }
    /**
     * 是否画最后一条分割线
     *
     * @param isDrawLastDivider
     */
    public void setDrawLastDivider(boolean isDrawLastDivider) {
        this.isDrawLastDivider = isDrawLastDivider;
    }

    public void setDrawHeaderDivider(boolean isDrawHeaderDivider) {
        this.isDrawHeaderDivider = isDrawHeaderDivider;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thick);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        paint.setColor(color);
        if (recyclerviewMode == MODE_HORIZONTAL) {
            drawHorinzonal(c, parent);
        } else if (recyclerviewMode == MODE_VERTICAL) {
            drawVertical(c, parent);
        } else if (recyclerviewMode == MODE_GRID) {
            drawGrid(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (recyclerviewMode == MODE_HORIZONTAL
                && parent.getChildPosition(view) != parent.getAdapter().getItemCount() - 1) {
            if (drawableRid != 0) {
                outRect.set(0, 0, 0, bmp.getHeight());
            } else {
                outRect.set(0, 0, 0, thick);
            }
            if (isDrawHeaderDivider && parent.getChildAdapterPosition(view) == 0) {
                outRect.top = thick;
            }
        } else if (recyclerviewMode == MODE_VERTICAL
                && parent.getChildPosition(view) != parent.getAdapter().getItemCount() - 1) {
            if (drawableRid != 0) {
                outRect.set(0, 0, bmp.getWidth(), 0);
            } else {
                outRect.set(0, 0, thick, 0);
            }
            if (isDrawHeaderDivider && parent.getChildAdapterPosition(view) == 0) {
                outRect.left = thick;
            }
        } else if (recyclerviewMode == MODE_GRID) {
            int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int itemSzie = parent.getAdapter().getItemCount();
            if (drawableRid != 0) {
                if (isLastRowGrid(parent.getChildPosition(view), itemSzie, columnSize)
                        && isLastGridColumn(parent.getChildPosition(view), columnSize)) {
                    outRect.set(0, 0, 0, 0);
                } else if (isLastRowGrid(parent.getChildPosition(view), itemSzie, columnSize)) {
                    outRect.set(0, 0, bmp.getWidth(), 0);
                } else if ((parent.getChildPosition(view) + 1) % columnSize != 0) {
                    outRect.set(0, 0, bmp.getWidth(), bmp.getHeight());
                } else {
                    outRect.set(0, 0, 0, bmp.getHeight());
                }
            } else {
                boolean isLastRow = isLastRowGrid(parent.getChildPosition(view), itemSzie, columnSize);
                int top = 0;
                int left;
                int right;
                int bottom;
                int eachWidth = (columnSize - 1) * thick / columnSize;
                left = parent.getChildPosition(view) % columnSize * (thick - eachWidth);
                right = eachWidth - left;
                bottom = thick;
                if (isDrawHeaderDivider && parent.getChildAdapterPosition(view) != -1 && parent.getChildAdapterPosition(view) < columnSize) {
                    top = thick;
                }
                if (isLastRow && !isDrawHeaderDivider) {
                    bottom = 0;
                }
                outRect.set(left, top, right, bottom);

            }
        }

    }

    /**
     * judge is a color string like #xxxxxx or #xxxxxxxx
     *
     * @param colorStr
     * @return
     */
    private boolean isColorString(String colorStr) {
        return Pattern.matches("^#([0-9a-fA-F]{6}||[0-9a-fA-F]{8})$", colorStr);
    }

    /**
     * draw horizonal decoration
     *
     * @param c
     * @param parent
     */
    private void drawHorinzonal(Canvas c, RecyclerView parent) {
        int childrentCount = parent.getChildCount();

        if (drawableRid != 0) {
            if (hasNinePatch) {
                if (!isDrawLastDivider) {
                    childrentCount = childrentCount - 1;
                }
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myY = childView.getBottom();

                    Rect rect = new Rect(0, myY, parent.getWidth(), myY + bmp.getHeight());
                    ninePatch.draw(c, rect);
                }
            } else {
                if (!isDrawLastDivider) {
                    childrentCount = childrentCount - 1;
                }
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myY = childView.getBottom();

                    c.drawBitmap(bmp, 0, myY, paint);
                }
            }

        } else if (dashWidth == 0 && dashGap == 0) {
            if (!isDrawLastDivider) {
                childrentCount = childrentCount - 1;
            }
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom() + thick / 2;
                c.drawLine(0, myY, parent.getWidth(), myY, paint);
            }
        } else {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, dashWidth, thick}, dashGap);
            paint.setPathEffect(effects);
            if (!isDrawLastDivider) {
                childrentCount = childrentCount - 1;
            }
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom() + thick / 2;
                Path path = new Path();
                path.moveTo(0, myY);
                path.lineTo(parent.getWidth(), myY);
                c.drawPath(path, paint);
            }
        }
    }

    /**
     * draw vertival decoration
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childrentCount = parent.getChildCount();
        if (drawableRid != 0) {
            if (hasNinePatch) {
                if (!isDrawLastDivider) {
                    childrentCount = childrentCount - 1;
                }
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    Rect rect = new Rect(myX, 0, myX + bmp.getWidth(), parent.getHeight());
                    ninePatch.draw(c, rect);
                }
            } else {
                if (!isDrawLastDivider) {
                    childrentCount = childrentCount - 1;
                }
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    c.drawBitmap(bmp, myX, 0, paint);
                }
            }
        } else if (dashWidth == 0 && dashGap == 0) {
            if (!isDrawLastDivider) {
                childrentCount = childrentCount - 1;
            }
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + thick / 2;
                c.drawLine(myX, 0, myX, parent.getHeight(), paint);
            }


        } else {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, dashWidth, thick}, dashGap);
            paint.setPathEffect(effects);
            if (!isDrawLastDivider) {
                childrentCount = childrentCount - 1;
            }
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + thick / 2;
                Path path = new Path();
                path.moveTo(myX, 0);
                path.lineTo(myX, parent.getHeight());
                c.drawPath(path, paint);
            }
        }
    }

    /**
     * 画网格分割线
     *
     * @param c
     * @param parent
     */
    private void drawGrid(Canvas c, RecyclerView parent) {

        int childrentCount = parent.getChildCount();
        int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int adapterChildrenCount = parent.getAdapter().getItemCount();

        if (drawableRid != 0) {
            if (hasNinePatch) {
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    //horizonal
                    if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        Rect rect = new Rect(0, myY, myX, myY + bmp.getHeight());
                        ninePatch.draw(c, rect);
                    }

                    //vertical
                    if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                            && !isLastGridColumn(i, columnSize)) {
                        Rect rect = new Rect(myX, childView.getTop(), myX + bmp.getWidth(), myY);
                        ninePatch.draw(c, rect);
                    } else if (!isLastGridColumn(i, columnSize)) {
                        Rect rect = new Rect(myX, childView.getTop(), myX + bmp.getWidth(), myY + bmp.getHeight());
                        ninePatch.draw(c, rect);
                    }

                }
            } else {

                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    //horizonal
                    if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        c.drawBitmap(bmp, childView.getLeft(), myY, paint);
                    }

                    //vertical
                    if (!isLastGridColumn(i, columnSize)) {
                        c.drawBitmap(bmp, myX, childView.getTop(), paint);
                    }


                }
            }
        } else if (dashWidth == 0 && dashGap == 0) {

            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + thick / 2;
                int myY = childView.getBottom() + thick / 2;

                //horizonal
                if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                    c.drawLine(childView.getLeft(), myY, childView.getRight() + thick, myY, paint);
                }

                //vertical
                if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                        && !isLastGridColumn(i, columnSize)) {
                    c.drawLine(myX, childView.getTop(), myX, childView.getBottom(), paint);
                } else if (!isLastGridColumn(i, columnSize)) {
                    c.drawLine(myX, childView.getTop(), myX, myY, paint);
                }

            }


        } else {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, dashWidth, thick}, dashGap);
            paint.setPathEffect(effects);
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + thick / 2;
                int myY = childView.getBottom() + thick / 2;

                //horizonal
                if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                    Path path = new Path();
                    path.moveTo(0, myY);
                    path.lineTo(myX, myY);
                    c.drawPath(path, paint);
                }

                //vertical
                if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                        && !isLastGridColumn(i, columnSize)) {
                    Path path = new Path();
                    path.moveTo(myX, childView.getTop());
                    path.lineTo(myX, childView.getBottom());
                    c.drawPath(path, paint);
                } else if (!isLastGridColumn(i, columnSize)) {
                    Path path = new Path();
                    path.moveTo(myX, childView.getTop());
                    path.lineTo(myX, childView.getBottom());
                    c.drawPath(path, paint);
                }

            }
        }
    }

    /**
     * check if is one of the last columns
     *
     * @param position
     * @param columnSize
     * @return
     */
    private boolean isLastGridColumn(int position, int columnSize) {
        boolean isLast = false;
        if ((position + 1) % columnSize == 0) {
            isLast = true;
        }
        return isLast;
    }

    /**
     * check if is the last row of the grid
     *
     * @param position
     * @param itemSize
     * @param columnSize
     * @return
     */
    private boolean isLastRowGrid(int position, int itemSize, int columnSize) {
        return position / columnSize == (itemSize - 1) / columnSize;
    }
}
