package jp.ac.gifu_u.ido.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MyView extends View {

    private HashMap<Integer, ArrayList<Float>> pointerXMap;
    private HashMap<Integer, ArrayList<Float>> pointerYMap;

    private Bitmap bitmap;
    private Bitmap resizedBitmap;  // リサイズ済みビットマップ

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pointerXMap = new HashMap<>();
        pointerYMap = new HashMap<>();

        // ビットマップ画像の読み込み（res/drawable に cat.png を置いておくこと）
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);

        if (bitmap != null) {
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE); // 背景を白に塗りつぶし

        if (resizedBitmap != null) {
            canvas.drawBitmap(resizedBitmap, 50, 50, null); // 左上に画像を表示
        }

        // 線のスタイル
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);

        for (Integer pointerId : pointerXMap.keySet()) {
            ArrayList<Float> xList = pointerXMap.get(pointerId);
            ArrayList<Float> yList = pointerYMap.get(pointerId);

            if (xList == null || yList == null) continue;

            for (int i = 1; i < xList.size(); i++) {
                Float x1 = xList.get(i - 1);
                Float y1 = yList.get(i - 1);
                Float x2 = xList.get(i);
                Float y2 = yList.get(i);

                if (x1 == null || x2 == null || y1 == null || y2 == null) continue;

                canvas.drawLine(x1, y1, x2, y2, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!pointerXMap.containsKey(pointerId)) {
                    pointerXMap.put(pointerId, new ArrayList<>());
                    pointerYMap.put(pointerId, new ArrayList<>());
                }

                pointerXMap.get(pointerId).add(null); // 線の切れ目を表現
                pointerYMap.get(pointerId).add(null);
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    float x = event.getX(i);
                    float y = event.getY(i);

                    if (!pointerXMap.containsKey(id)) {
                        pointerXMap.put(id, new ArrayList<>());
                        pointerYMap.put(id, new ArrayList<>());
                        pointerXMap.get(id).add(null);
                        pointerYMap.get(id).add(null);
                    }

                    pointerXMap.get(id).add(x);
                    pointerYMap.get(id).add(y);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                // 終了時は何もしない（軌跡は残す）
                break;
        }

        invalidate(); // 再描画
        return true;
    }
}
