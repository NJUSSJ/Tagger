package com.nju.DataDservice;

import com.nju.Domain.Paint;

/**
 * @Author shisj
 * @Date: 2018/9/9 14:30
 */
public interface PaintDataService {
    boolean savePaintAsTxt(Paint paint);
    Paint getSavedPaint();
}
