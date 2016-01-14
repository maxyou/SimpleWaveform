package com.maxproj.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.LinkedList;
import java.util.Random;


import android.widget.TextView;

import com.maxproj.simplewaveform.SimpleWaveform;

public class SimpleWaveformDemo extends AppCompatActivity {

    Button button_switch_simple_demos;
    Button button_switch_advance_demos;

    TextView demo_introduce;

    int simple_demo_loop = 0;
    int advance_demo_loop = 0;

    SimpleWaveform simpleWaveform;
    RecyclerView recycler_view;
    LinearLayoutManager linearLayoutManager;

    Paint barPencilFirst = new Paint();
    Paint barPencilSecond = new Paint();
    Paint peakPencilFirst = new Paint();
    Paint peakPencilSecond = new Paint();

    Paint xAxisPencil = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_waveform_demo);

        demo_introduce = (TextView)findViewById(R.id.demo_introduce);

        simpleWaveform = (SimpleWaveform) findViewById(R.id.simplewaveform);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);

        button_switch_simple_demos = (Button) findViewById(R.id.button_switch_simple_demos);
        button_switch_simple_demos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleWaveform.setVisibility(View.VISIBLE);
                recycler_view.setVisibility(View.GONE);

                advance_demo_loop = 0;
                simple_demo_loop++;
                if (simple_demo_loop > 4) {
                    simple_demo_loop = 1;
                }
                switch (simple_demo_loop) {
                    case 1:
                        demo1();
                        break;
                    case 2:
                        demo2();
                        break;
                    case 3:
                        demo3();
                        break;
                    case 4:
                        demo4();
                        break;
                }
            }
        });
        button_switch_advance_demos = (Button) findViewById(R.id.button_switch_advance_demos);
        button_switch_advance_demos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simple_demo_loop = 0;
                advance_demo_loop++;
                if (advance_demo_loop > 2) {
                    advance_demo_loop = 1;
                }
                switch (advance_demo_loop) {
                    case 1:
                        simpleWaveform.setVisibility(View.VISIBLE);
                        recycler_view.setVisibility(View.GONE);
                        demoAdvance1();
                        break;
                    case 2:
                        simpleWaveform.setVisibility(View.GONE);
                        recycler_view.setVisibility(View.VISIBLE);
                        demoAdvance2();
                        break;
                }

            }
        });

        simple_demo_loop = 1;
        demo1();

    }

    private void demo1() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for (int i = 0; i < 80; i++) {
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 50;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ORIGIN;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_ORIGIN;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(15);
        barPencilFirst.setColor(0xff1dcf0f);
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(15);
        barPencilSecond.setColor(0xff1dcfcf);
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 10;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };

        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;
                simpleWaveform.refresh();
            }
        };

        //show...
        simpleWaveform.refresh();

        demo_introduce.setText("demo1: positive and negative data");
    }


    private void demo2() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for (int i = 0; i < 80; i++) {
            ampList.add(randomInt(0, 100));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 100;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_BOTTOM;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(75);
        barPencilFirst.setColor(0xff1dcf0f);
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(75);
        barPencilSecond.setColor(0xff1dcfcf);
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 5;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;
                simpleWaveform.refresh();
            }
        };
        //show...
        simpleWaveform.refresh();

        demo_introduce.setText("demo2: bar chart");
    }


    private void demo3() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for (int i = 0; i < 80; i++) {
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 30;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(15);
        barPencilFirst.setColor(0xff1dcf0f);
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(15);
        barPencilSecond.setColor(0xff1dcfcf);
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 20;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;
                simpleWaveform.refresh();
            }
        };
        //show...
        simpleWaveform.refresh();

        demo_introduce.setText("demo3: amplitude bar");
    }


    private void demo4() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for (int i = 0; i < 80; i++) {
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 50;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_CROSS_TOP_BOTTOM;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(5);
        barPencilFirst.setColor(0xff1dcf0f);
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(5);
        barPencilSecond.setColor(0xff1dcfcf);
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 10;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;
                simpleWaveform.refresh();
            }
        };
        //show...
        simpleWaveform.refresh();

        demo_introduce.setText("demo4: sound wave");
    }

    private void demoAdvance1() {

        simpleWaveform.init();

        final LinkedList<Integer> ampList = new LinkedList<>();
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 30;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(15);
        barPencilFirst.setColor(0xff1dcf0f);
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(15);
        barPencilSecond.setColor(0xff1dcfcf);
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 20;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;
                simpleWaveform.refresh();
            }
        };
        //loop
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                    }

                    if (advance_demo_loop != 1) {
                        break;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ampList.addFirst(randomInt(-50, 50));
                            if (ampList.size() > simpleWaveform.width / simpleWaveform.barGap + 2) {
                                ampList.removeLast();
                                Log.d("", "SimpleWaveform: ampList remove last node, total " + ampList.size());
                            }
                            simpleWaveform.refresh();
                        }
                    });
                }
            }
        }).start();

        demo_introduce.setText("advance demo1: generate recorder amplitude bar");

    }

    private void demoAdvance2() {

        LinkedList<LinkedList<Integer>> amp_list_list = new LinkedList();
        for (int i = 0; i < 6; i++) {

            LinkedList<Integer> ampList = new LinkedList<>();
            amp_list_list.add(ampList);

            for (int j = 0; j < 200; j++) {
                ampList.add(randomInt(-50, 50));
            }

        }


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter waveAdapter = new RecyclerViewAdapter(amp_list_list);
        recycler_view.setAdapter(waveAdapter);

//        recycler_view.scrollBy(100, 10);
        recycler_view.scrollToPosition(2);
//        recycler_view.smoothScrollBy(100, 10);

        demo_introduce.setText("advance demo2: embedded in horizontal recycler view");
    }

    private int randomInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    private class RecyclerViewAdapter extends
            RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        // List<LvRowFile> listItems;
        LinkedList<LinkedList<Integer>> amp_list_list;

        public RecyclerViewAdapter(LinkedList<LinkedList<Integer>> amp_list_list) {
            this.amp_list_list = amp_list_list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public SimpleWaveform simpleWaveform;

            public ViewHolder(View itemView) {
                super(itemView);
                this.simpleWaveform = (SimpleWaveform) itemView
                        .findViewById(R.id.simplewaveform_row);
//                this.simpleWaveform.clearScreen();
            }
        }


        @Override
        public int getItemCount() {
            Log.d("","SimpleWaveform: amp_list_list.size() "+amp_list_list.size());
            return amp_list_list.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d("", "SimpleWaveform: position " + position);
            holder.simpleWaveform.setDataList(amp_list_list.get(position));

            holder.simpleWaveform.barPencilSecond.setStrokeWidth(15);
            holder.simpleWaveform.barPencilSecond.setColor(0xff1dcfcf);

            holder.simpleWaveform.peakPencilSecond.setStrokeWidth(5);
            holder.simpleWaveform.peakPencilSecond.setColor(0xfffeef3f);

            //show x-axis
            holder.simpleWaveform.showXAxis = true;
            holder.simpleWaveform.xAxisPencil.setStrokeWidth(1);
            holder.simpleWaveform.xAxisPencil.setStrokeWidth(0x88ffffff);

            holder.simpleWaveform.refresh();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View view = View.inflate(parent.getContext(),
            // R.layout.gridview_pic, null);
            View view = View.inflate(parent.getContext(),
                    R.layout.row_recycler, null);
            ViewHolder holder = new ViewHolder(view);
            Log.d("","SimpleWaveform: onCreateViewHolder ");
            return holder;
        }

    }

}
