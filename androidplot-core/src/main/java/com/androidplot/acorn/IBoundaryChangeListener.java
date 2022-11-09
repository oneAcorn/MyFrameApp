package com.androidplot.acorn;

/**
 * Created by acorn on 2022/11/9.
 */
public interface IBoundaryChangeListener {
    void onBoundaryChanged(boolean isDomain, Number lower, Number upper);
}
