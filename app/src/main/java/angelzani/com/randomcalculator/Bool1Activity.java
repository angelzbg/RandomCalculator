package angelzani.com.randomcalculator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class Bool1Activity extends AppCompatActivity {

    private int countParam = 3;
    private int countFunc = 3;

    //head
    private ConstraintLayout bool1Activity_constrLayout_Bar;
    private ImageButton bool1Activity_ImgBtn_Back;
    private TextView bool1Activity_textView_Title;

    //Scroll field
    private LinearLayout bool1Activity_LinearTop;
    private LinearLayout bool1Activity_LinearBottom;

    //Параметри и функции
    EditText[][] paramArray;
    EditText[][] funcArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bool1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Височината и ширината на екрана в пиксели
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        //header resizing first
        bool1Activity_constrLayout_Bar = findViewById(R.id.bool1Activity_constrLayout_Bar);
        bool1Activity_ImgBtn_Back = findViewById(R.id.bool1Activity_ImgBtn_Back);
        bool1Activity_textView_Title = findViewById(R.id.bool1Activity_textView_Title);
        bool1Activity_constrLayout_Bar.getLayoutParams().height = (int) width/7;
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        bool1Activity_ImgBtn_Back.getLayoutParams().height = (int) (width/8-(px*2));
        bool1Activity_ImgBtn_Back.getLayoutParams().width = (int) (width/8-(px*2));
        bool1Activity_ImgBtn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Intent intent = getIntent();
        if (intent != null) {
            countParam=intent.getIntExtra("countParam", 3);
            countFunc=intent.getIntExtra("countFunc", 3);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        bool1Activity_LinearTop = findViewById(R.id.bool1Activity_LinearTop);
        bool1Activity_LinearBottom = findViewById(R.id.bool1Activity_LinearBottom);

        //Start
        ConstraintLayout cLHead = new ConstraintLayout(getApplicationContext());
        cLHead.setId(View.generateViewId());
        bool1Activity_LinearTop.addView(cLHead);
        TextView[] headTexts = new TextView[countParam+countFunc];
        for(int i=0; i<headTexts.length; i++)
        {
            headTexts[i] = new TextView(getApplicationContext());
            headTexts[i].setId(View.generateViewId());
            if(i<countParam) headTexts[i].setText("X" + (i + 1));
            else headTexts[i].setText("f" + (i + 1 - countParam));

            cLHead.addView(headTexts[i]);
            headTexts[i].getLayoutParams().width = (int) (width / (countParam + countFunc));
            headTexts[i].setGravity(Gravity.CENTER);
            headTexts[i].setTextColor(Color.parseColor("#000000"));
            headTexts[i].setTextSize(14);

            if(i==0) continue; //подреждане в лейаута по ред
            ConstraintSet set = new ConstraintSet();
            set.clone(cLHead);
            set.connect(headTexts[i].getId(), ConstraintSet.LEFT, headTexts[i-1].getId(), ConstraintSet.RIGHT, 0);
            set.applyTo(cLHead);
        }

        ConstraintLayout[] cLRows = new ConstraintLayout[(int) (Math.pow(2, countParam))];
        paramArray = new EditText[(int) (Math.pow(2, countParam))][countParam]; //aka 2^2 = 4 2^3 = 8 2^4 = 16 aka everything alright
        funcArray = new EditText[(int) (Math.pow(2, countParam))][countFunc];

        for(int i=0; i<cLRows.length; i++)//за всеки ред от таблицата
        {
            cLRows[i] = new ConstraintLayout(getApplicationContext());
            cLRows[i].setId(View.generateViewId());
            bool1Activity_LinearTop.addView(cLRows[i]);
            for(int j=0; j<countParam; j++) {
                paramArray[i][j] = new EditText(getApplicationContext());
                paramArray[i][j].setId(View.generateViewId());
                cLRows[i].addView(paramArray[i][j]);
                paramArray[i][j].getLayoutParams().width = (int) (width / (countParam + countFunc));
                paramArray[i][j].setGravity(Gravity.CENTER);
                paramArray[i][j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.mn_func_back_texts));
                paramArray[i][j].setInputType(InputType.TYPE_CLASS_NUMBER);
                paramArray[i][j].setTextColor(Color.parseColor("#262626")); //green3
                paramArray[i][j].setTextSize(14);
                paramArray[i][j].setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                paramArray[i][j].setSelectAllOnFocus(true);

                if(j==0) continue;
                ConstraintSet set = new ConstraintSet();
                set.clone(cLRows[i]);
                set.connect(paramArray[i][j].getId(), ConstraintSet.LEFT, paramArray[i][j-1].getId(), ConstraintSet.RIGHT, 0);
                set.applyTo(cLRows[i]);
            }

            for(int j=0; j<countFunc; j++) {
                funcArray[i][j] = new EditText(getApplicationContext());
                funcArray[i][j].setId(View.generateViewId());
                cLRows[i].addView(funcArray[i][j]);
                funcArray[i][j].getLayoutParams().width = (int) (width / (countParam + countFunc));
                funcArray[i][j].setGravity(Gravity.CENTER);
                funcArray[i][j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.mn_func_back_texts));
                funcArray[i][j].setInputType(InputType.TYPE_CLASS_NUMBER);
                funcArray[i][j].setTextColor(Color.parseColor("#7cbc48")); //green3
                funcArray[i][j].setTextSize(14);
                funcArray[i][j].setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                funcArray[i][j].setSelectAllOnFocus(true);

                if(j==0) {
                    ConstraintSet set = new ConstraintSet();
                    set.clone(cLRows[i]);
                    set.connect(funcArray[i][j].getId(), ConstraintSet.LEFT, paramArray[i][countParam-1].getId(), ConstraintSet.RIGHT, 0);
                    set.applyTo(cLRows[i]);
                }
                else {
                    ConstraintSet set = new ConstraintSet();
                    set.clone(cLRows[i]);
                    set.connect(funcArray[i][j].getId(), ConstraintSet.LEFT, funcArray[i][j - 1].getId(), ConstraintSet.RIGHT, 0);
                    set.applyTo(cLRows[i]);
                }
            }

        }

        //Sample
        if(countParam == 2) {
            /*for (int i = 0; i < (int) (Math.pow(2, countParam)); i++) {
                for (int j = 0; j < countParam; j++) {

                }
            }*/
            paramArray[0][0].setText(""+0); paramArray[0][1].setText(""+0);
            paramArray[1][0].setText(""+0); paramArray[1][1].setText(""+1);
            paramArray[2][0].setText(""+1); paramArray[2][1].setText(""+0);
            paramArray[3][0].setText(""+1); paramArray[3][1].setText(""+1);
        } else if(countParam == 3) {
            paramArray[0][0].setText(""+0); paramArray[0][1].setText(""+0); paramArray[0][2].setText(""+0);
            paramArray[1][0].setText(""+0); paramArray[1][1].setText(""+0); paramArray[1][2].setText(""+1);
            paramArray[2][0].setText(""+0); paramArray[2][1].setText(""+1); paramArray[2][2].setText(""+0);
            paramArray[3][0].setText(""+0); paramArray[3][1].setText(""+1); paramArray[3][2].setText(""+1);
            paramArray[4][0].setText(""+1); paramArray[4][1].setText(""+0); paramArray[4][2].setText(""+0);
            paramArray[5][0].setText(""+1); paramArray[5][1].setText(""+0); paramArray[5][2].setText(""+1);
            paramArray[6][0].setText(""+1); paramArray[6][1].setText(""+1); paramArray[6][2].setText(""+0);
            paramArray[7][0].setText(""+1); paramArray[7][1].setText(""+1); paramArray[7][2].setText(""+1);
        }


        //Buttons
        Button bool1_Button_Try = findViewById(R.id.bool1_Button_Try); bool1_Button_Try.setOnClickListener(Try);
        Button bool1_Button_Clear = findViewById(R.id.bool1_Button_Clear); bool1_Button_Clear.setOnClickListener(Clear);


        //Resize text for tablets and large devices
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){

            float sp = (float) (bool1Activity_textView_Title.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            bool1Activity_textView_Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);

            ArrayList<TextView> textViewsForResize = new ArrayList<TextView>();
            for(int i=0; i<headTexts.length; i++) textViewsForResize.add(headTexts[i]);
            textViewsForResize.add((TextView) findViewById(R.id.bool1Activity_func1_answ));
            textViewsForResize.add((TextView) findViewById(R.id.bool1Activity_func2_answ));
            textViewsForResize.add((TextView) findViewById(R.id.bool1Activity_func3_answ));

            ArrayList<EditText> editTextsForResize = new ArrayList<EditText>();
            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) for(int j=0; j<countParam; j++) editTextsForResize.add(paramArray[i][j]);
            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) for(int j=0; j<countFunc; j++) editTextsForResize.add(funcArray[i][j]);

            ArrayList<Button> buttonTextsForResize = new ArrayList<Button>();
            buttonTextsForResize.add(bool1_Button_Try);
            buttonTextsForResize.add(bool1_Button_Clear);

            for(TextView tv : textViewsForResize)
            {
                sp = (float) (tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);
            }
            for(EditText et : editTextsForResize)
            {
                sp = (float) (et.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                et.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);
            }
            for(Button btn : buttonTextsForResize)
            {
                sp = (float) (btn.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);
            }
        }else{
            // smaller device
        }
    }


    View.OnClickListener Try = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ClearUp();

            int[][] param = new int[(int) (Math.pow(2, countParam))][countParam];
            int[][] func = new int[(int) (Math.pow(2, countParam))][countFunc];

            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) {
                for (int j = 0; j < countParam; j++) {
                    try {
                        param[i][j] = Integer.parseInt(paramArray[i][j].getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Моля въведете всички полета", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(param[i][j] > 1) {
                        Toast.makeText(getApplicationContext(), "Моля въвеждайте само 0 и 1", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) {
                for (int j = 0; j < countFunc; j++) {
                    try {
                        func[i][j] = Integer.parseInt(funcArray[i][j].getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Моля въведете всички полета", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(func[i][j] > 1) {
                        Toast.makeText(getApplicationContext(), "Моля въвеждайте само 0 и 1", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            TextView[] Answer = new TextView[countFunc];
            Answer[0] = (TextView) findViewById(R.id.bool1Activity_func1_answ);
            if(countFunc == 2) Answer[1] = (TextView) findViewById(R.id.bool1Activity_func2_answ);
            else if(countFunc == 3) {
                Answer[1] = (TextView) findViewById(R.id.bool1Activity_func2_answ);
                Answer[2] = (TextView) findViewById(R.id.bool1Activity_func3_answ);
            }

            String[] result = new String[countFunc];
            if(countParam == 2) {
                for(int i=0; i<countFunc; i++) {
                    boolean isFirst = true;
                    result[i] = "";
                    for(int j=0; j<(int) (Math.pow(2, countParam)); j++) {
                        if(func[j][i] == 1) {

                            if(!isFirst) result[i]+="\u2228"; // V

                            if(param[j][0] == 0) result[i]+="x\u0304\u2081";
                            else result[i]+="x\u2081";
                            if(param[j][1] == 0) result[i]+=".x\u0304\u2082";
                            else result[i]+="x\u2082";
                            isFirst = false;
                        }
                    }
                    Answer[i].setText("f" + (i+1) + " = " + result[i]);
                    if (result[i].isEmpty()) Answer[i].setText("f" + (i+1) + " = ???");
                }
            } else if(countParam == 3) {
                for(int i=0; i<countFunc; i++) {
                    boolean isFirst = true;
                    result[i] = "";
                    for(int j=0; j<(int) (Math.pow(2, countParam)); j++) {
                        if(func[j][i] == 1) {

                            if(!isFirst) result[i]+="\u2228"; // V

                            if(param[j][0] == 0) result[i]+="x\u0304\u2081";
                            else result[i]+="x\u2081";
                            if(param[j][1] == 0) result[i]+=".x\u0304\u2082";
                            else result[i]+="x\u2082";
                            if(param[j][2] == 0) result[i]+=".x\u0304\u2083";
                            else result[i]+="x\u2082";
                            isFirst = false;
                        }
                    }
                    Answer[i].setText("f" + (i+1) + " = " + result[i]);
                    if (result[i].isEmpty()) Answer[i].setText("f" + (i+1) + " = ???");
                }
            }


            ScrollDown();

        }
    };

    private void ClearUp() {
        ((TextView) findViewById(R.id.bool1Activity_func1_answ)).setText("");
        ((TextView) findViewById(R.id.bool1Activity_func2_answ)).setText("");
        ((TextView) findViewById(R.id.bool1Activity_func3_answ)).setText("");
        ScrollUp();
    }

    private View.OnClickListener Clear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ClearUp();
        }
    };

    private void ScrollDown(){
        final Handler handler;
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                ((ScrollView) findViewById(R.id.bool1Activity_ScrollMain)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        handler.postDelayed(r, 250);
    }

    private void ScrollUp(){
        final Handler handler;
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                ((ScrollView) findViewById(R.id.bool1Activity_ScrollMain)).fullScroll(ScrollView.FOCUS_UP);
            }
        };
        handler.postDelayed(r, 250);
    }
}
