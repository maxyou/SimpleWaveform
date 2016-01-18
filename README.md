# SimpleWaveform

Waveform Widget

---

**SimpleWaveform** is a widget to show a sequence data in waveform or bar chart.

SimpleWaveform can be highly customized:<br>
(1)show original positive and negetive data, or show absolute of the data for amplitude<br>
(2)data can be px or percent of full amplitude. Will automaticly detect its width and height in px<br>
(3)choose to show bar, peak outline, x-axis<br>
(4)choose how to connect nearby peak for outline<br>
(5)x-axis can be located at top/center/bottom<br>
(6)can set bar width and gap<br>
(7)all pencil can be set include bar/outline/x-axis/background<br>
(8)two pencil with different colors to show progress<br>
(9)wave from left or from right<br>
(10)return bar position when your finger touch waveform<br>


## often used:
(1)show sound amplitude when record sound. ref to advance demo1<br>
(2)Embedded in horizontal recycler view to show a very long sound waveform. ref to advance demo2<br>

[demo apk download from github](https://github.com/maxyou/SimpleWaveform/raw/master/app/app-release.apk)<br>
[demo apk download from googleplay](https://play.google.com/store/apps/details?id=maxproj.com.simplewaveform)<br>

demo1: positive and negative data<br>
![此处输入图片的描述][1]<br>
demo2: bar chart<br>
![此处输入图片的描述][2]<br>
demo3: amplitude bar<br>
![此处输入图片的描述][3]<br>
demo4: sound wave<br>
![此处输入图片的描述][4]<br>
advance demo1: generate recorder amplitude bar<br>
![此处输入图片的描述][5]<br>
advance demo2: embedded in horizontal recycler view<br>
![此处输入图片的描述][6]<br>

## usage:

Install by gradle<br>    

    dependencies {
        compile 'com.maxproj.simplewaveform:app:1.0.0'
    }

Define a SimpleWaveform in layout<br>

    <com.maxproj.simplewaveform.SimpleWaveform
        android:id="@+id/simplewaveform"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:layout_margin="10dp" />
 
SimpleWaveformDemo.java include demo1~4 and advance demo1~2 in. Let's use demo3 as an example. please notice the comments:<br>

    private void demo3() {
        
        //restore default setting, you can omit all following setting and goto the final refresh() show
        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for (int i = 0; i < 80; i++) {
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);//input data to show

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
        xAxisPencil.setColor(0x88ffffff);//the first 0x88 is transparency, the next 0xffffff is color
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
        simpleWaveform.firstPartNum = 20;//first 20 bars will be draw by first pencil

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        // set touch listener
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;//set touch position back to its progress
                simpleWaveform.refresh();
            }
        };
        //show...
        simpleWaveform.refresh();

        demo_introduce.setText("demo3: amplitude bar");
    }




## License<br>
under [MIT License](http://www.opensource.org/licenses/MIT).

  [1]: https://raw.githubusercontent.com/maxyou/SimpleWaveform/master/demo1.PNG
  [2]: https://raw.githubusercontent.com/maxyou/SimpleWaveform/master/demo2.PNG
  [3]: https://raw.githubusercontent.com/maxyou/SimpleWaveform/master/demo3.PNG
  [4]: https://raw.githubusercontent.com/maxyou/SimpleWaveform/master/demo4.PNG
  [5]: https://raw.githubusercontent.com/maxyou/SimpleWaveform/master/advancedemo1.gif
  [6]: https://raw.githubusercontent.com/maxyou/SimpleWaveform/master/advancedemo2.gif
