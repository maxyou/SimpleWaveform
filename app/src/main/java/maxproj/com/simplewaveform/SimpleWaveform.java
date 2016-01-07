package maxproj.com.simplewaveform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

/**
 * Created by yhy on 2016/1/1.
 */
public class SimpleWaveform extends View {
    Context context;

    public final static int MODE_AMP_ORIGIN = 1;
    public final static int MODE_AMP_ABSOLUTE = 2;
    public int modeAmp;
    public final static int MODE_HEIGHT_PX = 1;
    public final static int MODE_HEIGHT_PERCENT = 2;
    public int modeHeight;
    public final static int MODE_ZERO_TOP = 1;
    public final static int MODE_ZERO_CENTER = 2;
    public final static int MODE_ZERO_BOTTOM = 3;
    public final static int MODE_ZERO_USR_DEFINE = 4;
    public int modeZero;
    public final static int MODE_PEAK_ORIGIN = 1;
    public final static int MODE_PEAK_PARALLEL = 2;
    public final static int MODE_PEAK_CROSS_TOP_BOTTOM = 3;
    public final static int MODE_PEAK_CROSS_BOTTOM_TOP = 4;
    public final static int MODE_PEAK_CROSS_TURN_TOP_BOTTOM = 5;
    public int modePeak;
    public final static int MODE_DIRECTION_LEFT_RIGHT = 1;
    public final static int MODE_DIRECTION_RIGHT_LEFT = 2;
    public int modeDirection;


    public boolean showPeak;
    public boolean showBar;

    public int height;
    public int width;
    public boolean haveGotWidthHeight = false;
    //    public int barWidth = 10;
    public int barGap;

    public LinkedList<Integer> dataList;
    private LinkedList<BarPoints> innerDataList = new LinkedList<BarPoints>();

    class BarPoints {
        int amplitude;//input data
        int amplitudePx;//in px
        int amplitudePxTop;//top point in px
        int amplitudePxBottom;//bottom point in px
        int amplitudePxTopCanvas;//top point in canvas, lookout that y-axis is down orientation
        int amplitudePxBottomCanvas;//bottom point in canvas
        int xOffset;//position in x axis
        public BarPoints(int amplitude) {
            this.amplitude = amplitude;
        }
    }

    public Paint barPencil = new Paint();
    public Paint peakPencil = new Paint();

    private float[] barPoints;
    private float[] peakPoints;
    private int barNum;

    public boolean clearScreen = false;
    public interface ClearScreenListener {
        void clearScreen(Canvas canvas);
    }
    public ClearScreenListener clearScreenListener;

    public interface ProgressTouch{
        void progressTouch(int progress, MotionEvent event);
    }
    public ProgressTouch progressTouch;

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

    private void getWidthLength() {

        post(new Runnable() {
            @Override
            public void run() {
                width = SimpleWaveform.this.getWidth();
                height = SimpleWaveform.this.getHeight();

                haveGotWidthHeight = true;
            }
        });


    }

    public void init() {

        if(!haveGotWidthHeight) {
            getWidthLength();
        }

        // set default value
        barGap = 40;

        barPencil.setStrokeWidth(barGap / 2);//这里设置信号柱的宽度
        barPencil.setColor(0xff901f5f);
        peakPencil.setStrokeWidth(barGap / 6);//这里峰线的宽度
        peakPencil.setColor(0xfffe2f3f);

        modeAmp = MODE_AMP_ABSOLUTE;
        modeHeight = MODE_HEIGHT_PERCENT;
        modeZero = MODE_ZERO_CENTER;
        modePeak = MODE_PEAK_CROSS_TOP_BOTTOM;

        showPeak = true;
        showBar = true;

        dataList = null;
        clearScreenListener = null;

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(progressTouch != null) {
                    if (barGap != 0) {
                        if(modeDirection == MODE_DIRECTION_LEFT_RIGHT) {
                            progressTouch.progressTouch((int) (event.getX() / barGap) + 1, event);
                        }else{
                            progressTouch.progressTouch((int) ((width - event.getX()) / barGap) + 1, event);
                        }
                    }
                }
                return true;
            }

        });
    }

    public void setDataList(LinkedList<Integer> ampList) {
        this.dataList = ampList;
    }

    public void refresh() {
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("", "normal view: onDraw()");

        if (clearScreenListener != null) {
            clearScreenListener.clearScreen(canvas);
        } else {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }

        if (clearScreen) {
            clearScreen = false;
            return;
        }

        drawWaveList(canvas);

    }

    private void drawWaveList(Canvas canvas) {

        if(!haveGotWidthHeight){
            return;
        }

        if(dataList == null || dataList.size() == 0){
            return;
        }
        innerDataList.clear();

        barNum = (width / barGap) + 2;
        if (barNum > dataList.size()) {
            barNum = dataList.size();
        }

        if (showBar) {
            this.barPoints = new float[barNum * 4];
        }
        if (showPeak) {
            if (modePeak == MODE_PEAK_PARALLEL) {
                this.peakPoints = new float[barNum * 8];
            } else {
                this.peakPoints = new float[barNum * 4];
            }
        }

        for (int i = 0; i < barNum; i++) {

            BarPoints barPoints = new BarPoints(dataList.get(i));

            if(modeDirection == MODE_DIRECTION_LEFT_RIGHT){
                barPoints.xOffset = i * barGap;
            }else{
                barPoints.xOffset = width - i * barGap;
            }

            if (modeHeight == MODE_HEIGHT_PERCENT) {
                barPoints.amplitudePx = (barPoints.amplitude * height) / 100;
            } else {
                barPoints.amplitudePx = barPoints.amplitude;
            }

            if (modeAmp == MODE_AMP_ABSOLUTE) {
                barPoints.amplitudePxTop = Math.abs(barPoints.amplitudePx);
                barPoints.amplitudePxBottom = -Math.abs(barPoints.amplitudePx);
//                if (barPoints.amplitudePx > 0) {
//                    barPoints.amplitudePxTop = barPoints.amplitudePx;
//                    barPoints.amplitudePxBottom = -barPoints.amplitudePx;
//                } else {
//                    barPoints.amplitudePxTop = -barPoints.amplitudePx;
//                    barPoints.amplitudePxBottom = barPoints.amplitudePx;
//                }
            } else {
                if (barPoints.amplitudePx > 0) {
                    barPoints.amplitudePxTop = barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = 0;
                } else {
                    barPoints.amplitudePxTop = 0;
                    barPoints.amplitudePxBottom = barPoints.amplitudePx;
                }
            }

            /**
             * before here, amplitudePxTop/amplitudePxBottom is on normal x-y axis
             * this mean y is up orientation
             * in next step, we will invert the y axis ant set where is 'y == 0'
             */
            switch (modeZero) {
                case MODE_ZERO_TOP:
                    barPoints.amplitudePxTopCanvas = -barPoints.amplitudePxTop;
                    barPoints.amplitudePxBottomCanvas = -barPoints.amplitudePxBottom;
                    break;
                case MODE_ZERO_CENTER:
                    barPoints.amplitudePxTopCanvas = -barPoints.amplitudePxTop + height / 2;
                    barPoints.amplitudePxBottomCanvas = -barPoints.amplitudePxBottom + height / 2;
                    break;
                case MODE_ZERO_BOTTOM:
                    barPoints.amplitudePxTopCanvas = -barPoints.amplitudePxTop + height;
                    barPoints.amplitudePxBottomCanvas = -barPoints.amplitudePxBottom + height;
                    break;
                case MODE_ZERO_USR_DEFINE:
                    break;
            }

            //自此得到可直接显示的数据
            innerDataList.addLast(barPoints);

            if (showBar) {
                this.barPoints[i * 4] = barPoints.xOffset;
                this.barPoints[i * 4 + 1] = barPoints.amplitudePxTopCanvas;
                this.barPoints[i * 4 + 2] = barPoints.xOffset;
                this.barPoints[i * 4 + 3] = barPoints.amplitudePxBottomCanvas;
            }

            if (showPeak) {

                if (i > 0) {
                    BarPoints barPoints1_last = innerDataList.get(i - 1);

                    switch (modePeak) {
                        case MODE_PEAK_ORIGIN:
                            this.peakPoints[i * 4] = barPoints.xOffset;
                            if (barPoints.amplitudePxTop != 0) {
                                this.peakPoints[i * 4 + 1] = barPoints.amplitudePxTopCanvas;
                            } else {
                                this.peakPoints[i * 4 + 1] = barPoints.amplitudePxBottomCanvas;
                            }
                            this.peakPoints[i * 4 + 2] = barPoints1_last.xOffset;
                            if (barPoints1_last.amplitudePxTop != 0) {
                                this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxTopCanvas;
                            } else {
                                this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxBottomCanvas;
                            }
                            break;
                        case MODE_PEAK_CROSS_BOTTOM_TOP:
                            peakCrossBottomTop(i, barPoints, barPoints1_last);
                            break;
                        case MODE_PEAK_CROSS_TOP_BOTTOM:
                            peakCrossTopBottom(i, barPoints, barPoints1_last);
                            break;
                        case MODE_PEAK_CROSS_TURN_TOP_BOTTOM:
                            if (i % 2 == 0) {
                                peakCrossBottomTop(i, barPoints, barPoints1_last);
                            } else {
                                peakCrossTopBottom(i, barPoints, barPoints1_last);
                            }
                            break;
                        case MODE_PEAK_PARALLEL:
                            //draw top outline
                            this.peakPoints[i * 8] = barPoints.xOffset;
                            this.peakPoints[i * 8 + 1] = barPoints.amplitudePxTopCanvas;
                            this.peakPoints[i * 8 + 2] = barPoints1_last.xOffset;
                            this.peakPoints[i * 8 + 3] = barPoints1_last.amplitudePxTopCanvas;
                            //draw bottom outline
                            this.peakPoints[i * 8 + 4] = barPoints.xOffset;
                            this.peakPoints[i * 8 + 5] = barPoints.amplitudePxBottomCanvas;
                            this.peakPoints[i * 8 + 6] = barPoints1_last.xOffset;
                            this.peakPoints[i * 8 + 7] = barPoints1_last.amplitudePxBottomCanvas;
                            break;
                    }
                }

            }
        }

        /**
         * 画波形图
         */
        if (showBar) {
            canvas.drawLines(barPoints, barPencil);
        }
        if (showPeak) {
            canvas.drawLines(peakPoints, peakPencil);
        }

    }

    private void peakCrossTopBottom(int i, BarPoints barPoints, BarPoints barPoints1_last) {
        this.peakPoints[i * 4] = barPoints.xOffset;
        this.peakPoints[i * 4 + 1] = barPoints.amplitudePxBottomCanvas;
        this.peakPoints[i * 4 + 2] = barPoints1_last.xOffset;
        this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxTopCanvas;
    }

    private void peakCrossBottomTop(int i, BarPoints barPoints, BarPoints barPoints1_last) {
        this.peakPoints[i * 4] = barPoints.xOffset;
        this.peakPoints[i * 4 + 1] = barPoints.amplitudePxTopCanvas;
        this.peakPoints[i * 4 + 2] = barPoints1_last.xOffset;
        this.peakPoints[i * 4 + 3] = barPoints1_last.amplitudePxBottomCanvas;
    }

}
