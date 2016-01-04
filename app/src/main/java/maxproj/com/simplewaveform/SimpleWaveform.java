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

    public final static int MODE_AMP_ORIGIN = 1;
    public final static int MODE_AMP_ABSOLUTE = 2;
    public int modeAmp = MODE_AMP_ABSOLUTE;
    public final static int MODE_HEIGHT_PX = 1;
    public final static int MODE_HEIGHT_PERCENT = 2;
    public int modeHeight = MODE_HEIGHT_PERCENT;
    public final static int MODE_ZERO_TOP = 1;
    public final static int MODE_ZERO_CENTER = 2;
    public final static int MODE_ZERO_BOTTOM = 3;
    public final static int MODE_ZERO_USR_DEFINE = 4;
    public int modeZero = MODE_ZERO_CENTER;
    public final static int MODE_PEAK_ORIGIN = 1;
    public final static int MODE_PEAK_PARALLEL = 2;
    public final static int MODE_PEAK_CROSS_TOP_BOTTOM = 3;
    public final static int MODE_PEAK_CROSS_BOTTOM_TOP = 4;
    public final static int MODE_PEAK_CROSS_TURN_TOP_BOTTOM = 5;
    public int modePeak = MODE_PEAK_CROSS_TOP_BOTTOM;
    public boolean showPeak = true;
    public boolean showBar = true;

    public int height;
    public int width;
    public boolean haveGotWidthHeight = false;
    public int barWidth = 10;
    public int barGap = 40;
    public int barColorWithMask = 0xff901f5f;
    public int peakColorWithMask = 0xff307f9f;
    public LinkedList<Integer> dataList = new LinkedList<Integer>();
    private LinkedList<BarPoints> innerDataList = new LinkedList<BarPoints>();

    class BarPoints{
        int amplitude;
        int amplitudePx;
        int amplitudePxTop;
        int amplitudePxBottom;
        int amplitudePxTopAbs;
        int amplitudePxBottomAbs;

        public BarPoints(int amplitude){
            this.amplitude = amplitude;
        }
    }

    boolean clearScreen = false;

    Paint barPencil = new Paint();
    Paint peakPencil = new Paint();
    private float[] barPoints;
    private float[] peakPoints;
    public int barNum;


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

    public void drawWaveList(Canvas canvas) {
        if (canvas == null) {
            return;
        }

        barPencil.setStrokeWidth(barWidth);//这里设置信号柱的宽度
        barPencil.setColor(barColorWithMask);
        barPencil.setAntiAlias(false);

        peakPencil.setStrokeWidth(barWidth);//这里设置信号柱的宽度
        peakPencil.setColor(peakColorWithMask);
        peakPencil.setAntiAlias(false);

        barNum = (width / (barWidth + barGap)) + 1;
        if(barNum > dataList.size()){
            barNum = dataList.size();
        }

        if(showBar) {
            this.barPoints = new float[barNum * 4];
        }
        if(showPeak){
            if(modePeak == MODE_PEAK_PARALLEL) {
                this.peakPoints = new float[barNum * 8];
            }else{
                this.peakPoints = new float[barNum * 4];
            }
        }


        for (int i = 0; i < barNum; i++) {

            BarPoints barPoints = new BarPoints(dataList.get(i));
            if(modeHeight == MODE_HEIGHT_PERCENT) {
                barPoints.amplitudePx = (barPoints.amplitude * height) / 100;
            }else{
                barPoints.amplitudePx = barPoints.amplitude;
            }

            if(modeAmp == MODE_AMP_ABSOLUTE){
                if(barPoints.amplitudePx > 0) {
                    barPoints.amplitudePxTop = barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = -barPoints.amplitudePx;
                }else{
                    barPoints.amplitudePxTop = -barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = barPoints.amplitudePx;
                }
            }else{
                if(barPoints.amplitudePx > 0) {
                    barPoints.amplitudePxTop = barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = 0;
                }else{
                    barPoints.amplitudePxTop = 0;
                    barPoints.amplitudePxBottom = barPoints.amplitudePx;
                }
            }


            switch (modeZero){
                case MODE_ZERO_TOP:
                    barPoints.amplitudePxTopAbs = -barPoints.amplitudePxTop;
                    barPoints.amplitudePxBottomAbs = -barPoints.amplitudePxBottom;
                    break;
                case MODE_ZERO_CENTER:
                    barPoints.amplitudePxTopAbs = -barPoints.amplitudePxTop + height / 2;
                    barPoints.amplitudePxBottomAbs = -barPoints.amplitudePxBottom + height / 2;
                    break;
                case MODE_ZERO_BOTTOM:
                    barPoints.amplitudePxTopAbs = -barPoints.amplitudePxTop + height;
                    barPoints.amplitudePxBottomAbs = -barPoints.amplitudePxBottom + height;
                    break;
                case MODE_ZERO_USR_DEFINE:
                    break;
            }
            innerDataList.addLast(barPoints);//得到可直接显示的数据

            if(showBar) {
                this.barPoints[i * 4] = i * (barWidth + barGap);
                this.barPoints[i * 4 + 1] = barPoints.amplitudePxTopAbs;
                this.barPoints[i * 4 + 2] = i * (barWidth + barGap);
                this.barPoints[i * 4 + 3] = barPoints.amplitudePxBottomAbs;
            }

            if(showPeak){

                if(i > 0) {
                    BarPoints barPoints1_last = innerDataList.get(i - 1);

                    switch (modePeak) {
                        case MODE_PEAK_ORIGIN:
                            this.peakPoints[i * 4] = i * (barWidth + barGap);
                            if(barPoints.amplitudePxTop != 0) {
                                this.peakPoints[i * 4 + 1] = barPoints.amplitudePxTopAbs;
                            }else{
                                this.peakPoints[i * 4 + 1] = barPoints.amplitudePxBottomAbs;
                            }
                            this.peakPoints[i * 4 + 2] = (i-1) * (barWidth + barGap);
                            if(barPoints1_last.amplitudePxTop != 0) {
                                this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxTopAbs;
                            }else{
                                this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxBottomAbs;
                            }
                            break;
                        case MODE_PEAK_CROSS_BOTTOM_TOP:
                            peakCrossBottomTop(i, barPoints, barPoints1_last);
                            break;
                        case MODE_PEAK_CROSS_TOP_BOTTOM:
                            peakCrossTopBottom(i, barPoints, barPoints1_last);
                            break;
                        case MODE_PEAK_CROSS_TURN_TOP_BOTTOM:
                            if(i%2 == 0){
                                peakCrossBottomTop(i, barPoints, barPoints1_last);
                            }else{
                                peakCrossTopBottom(i, barPoints, barPoints1_last);
                            }
                            break;
                        case MODE_PEAK_PARALLEL:
                            //draw top outline
                            this.peakPoints[i * 8] = i * (barWidth + barGap);
                            this.peakPoints[i * 8 + 1] = barPoints.amplitudePxTopAbs;
                            this.peakPoints[i * 8 + 2] = (i-1) * (barWidth + barGap);
                            this.peakPoints[i * 8 + 3] = barPoints1_last.amplitudePxTopAbs;
                            //draw bottom outline
                            this.peakPoints[i * 8 + 4] = i * (barWidth + barGap);
                            this.peakPoints[i * 8 + 5] = barPoints.amplitudePxBottomAbs;
                            this.peakPoints[i * 8 + 6] = (i-1) * (barWidth + barGap);
                            this.peakPoints[i * 8 + 7] = barPoints1_last.amplitudePxBottomAbs;
                            break;
                    }
                }

            }
        }


        /**
         * 清屏
         */
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.drawColor(MyConfig.win_wave_backgrand);

        /**
         * 画波形图
         */
        if(showBar) {
            canvas.drawLines(barPoints, barPencil);
        }
        if(showPeak) {
            canvas.drawLines(peakPoints, peakPencil);
        }

    }

    private void peakCrossTopBottom(int i, BarPoints barPoints, BarPoints barPoints1_last) {
        this.peakPoints[i * 4] = i * (barWidth + barGap);
        this.peakPoints[i * 4 + 1] = barPoints.amplitudePxBottomAbs;
        this.peakPoints[i * 4 + 2] = (i-1) * (barWidth + barGap);
        this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxTopAbs;
    }

    private void peakCrossBottomTop(int i, BarPoints barPoints, BarPoints barPoints1_last) {
        this.peakPoints[i * 4] = i * (barWidth + barGap);
        this.peakPoints[i * 4 + 1] = barPoints.amplitudePxTopAbs;
        this.peakPoints[i * 4 + 2] = (i-1) * (barWidth + barGap);
        this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxBottomAbs;
    }

}
