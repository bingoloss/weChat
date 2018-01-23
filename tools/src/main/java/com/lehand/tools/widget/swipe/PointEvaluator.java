package com.lehand.tools.widget.swipe;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by bingo on 2018-1-23.
 */

public class PointEvaluator implements TypeEvaluator<PointF> {
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x = startValue.x + fraction * (endValue.x - startValue.x);
        float y = startValue.y + fraction * (endValue.y - startValue.y);
        return new PointF(x, y);
    }
}
