package maxproj.com.simplewaveform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

/**
 * Created by yhy on 2016/1/1.
 */
public class SimpleWaveform extends View {
    Context context;

    public int heightMode;
    public final static int HEIGHT_MODE_PX = 1;
    public final static int HEIGHT_MODE_PERCENT = 2;

    public int height;
    public int width;
    public boolean haveGotWidthHeight = false;
    public int barWidth = 10;
    public int barGap = 10;
    public int barColorWithMask = 0xff901f5f;
    public LinkedList<Integer> dataList = new LinkedList<Integer>();

    boolean clearScreen = false;

    Paint mForeground = new Paint();
    private float[] mPoints;


    public int dp2Px(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static int getPxFromDimen(Context context, int dimen_id) {
        return context.getResources().getDimensionPixelSize(dimen_id);
    }

    public SimpleWaveform(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SimpleWaveform(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    private void getWidthLength(){

        post(new Runnable() {
            @Override
            public void run() {
                width = SimpleWaveform.this.getWidth();
                height = SimpleWaveform.this.getHeight();

                haveGotWidthHeight = true;
            }
        });


    }
    private void init() {

        getWidthLength();

    }


    public void setDataList(LinkedList<Integer> ampList) {
        this.dataList = ampList;
    }

    public void refresh(){
        this.invalidate();
    }
    public void clearScreen(){
        clearScreen = true;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Log.d("", "normal view: onDraw()");

        if(clearScreen){
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            clearScreen = false;
            return;
        }

        drawWaveList(canvas);

    }

    private float calculateBarHeight(int input){

        float barInPx;

        switch (heightMode){
            case HEIGHT_MODE_PERCENT:
                barInPx = (input * height) / 100;
                break;
            case HEIGHT_MODE_PX:
                barInPx = input;
                break;
            default:
                barInPx = 0;
        }

        return barInPx;
    }

    public void drawWaveList(Canvas canvas) {
        if (canvas == null) {
            return;
        }


        mForeground.setStrokeWidth(barWidth);//这里设置信号柱的宽度
        mForeground.setColor(barColorWithMask);
        mForeground.setAntiAlias(false);

        /**
         * 画前面所有竖线
         */

        mPoints = new float[dataList.size() * 4];
        for (int i = 0; i < dataList.size(); i++) {
            float barHeight = calculateBarHeight(dataList.get(i));
            mPoints[i * 4] = i * (barWidth + barGap);
            mPoints[i * 4 + 1] = (height - barHeight) / 2;
            mPoints[i * 4 + 2] = i * (barWidth + barGap);
            mPoints[i * 4 + 3] = (height + barHeight) / 2;
        }

        /**
         * 清屏
         */
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.drawColor(MyConfig.win_wave_backgrand);

        /**
         * 画波形图
         */
        canvas.drawLines(mPoints, mForeground);

    }


}
