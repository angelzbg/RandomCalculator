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

import java.util.ArrayList;
import java.util.Random;

public class PulniMnojestvaActivity extends AppCompatActivity {

    private int countParam = 3;
    private int countFunc = 3;

    //head
    private ConstraintLayout pulniMnActivity_constrLayout_Bar;
    private ImageButton pulniMnActivity_ImgBtn_Back;
    private TextView pulniMnActivity_textView_Title;

    //Scroll field
    private LinearLayout pulniMnActivity_LinearTop;
    private LinearLayout pulniMnActivity_LinearBottom;

    //Параметри и функции
    EditText[][] paramArray;
    EditText[][] funcArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pulni_mnojestva);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Височината и ширината на екрана в пиксели
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        //header resizing first
        pulniMnActivity_constrLayout_Bar = findViewById(R.id.pulniMnActivity_constrLayout_Bar);
        pulniMnActivity_ImgBtn_Back = findViewById(R.id.pulniMnActivity_ImgBtn_Back);
        pulniMnActivity_textView_Title = findViewById(R.id.pulniMnActivity_textView_Title);
        pulniMnActivity_constrLayout_Bar.getLayoutParams().height = (int) width/7;
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        pulniMnActivity_ImgBtn_Back.getLayoutParams().height = (int) (width/8-(px*2));
        pulniMnActivity_ImgBtn_Back.getLayoutParams().width = (int) (width/8-(px*2));
        pulniMnActivity_ImgBtn_Back.setOnClickListener(new View.OnClickListener() {
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

        pulniMnActivity_LinearTop = findViewById(R.id.pulniMnActivity_LinearTop);
        pulniMnActivity_LinearBottom = findViewById(R.id.pulniMnActivity_LinearBottom);

        //Start
        ConstraintLayout cLHead = new ConstraintLayout(getApplicationContext());
        cLHead.setId(View.generateViewId());
        pulniMnActivity_LinearTop.addView(cLHead);
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
            pulniMnActivity_LinearTop.addView(cLRows[i]);
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

        for(int i=0; i<(int) (Math.pow(2, countParam)); i++)
        {
            funcArray[i][0].setText(""+0);
            if(countFunc>1) funcArray[i][1].setText(""+1);
            if(countFunc>2) {
                Random random = new Random();
                funcArray[i][2].setText("" + random.nextInt(2));
            }
        }


        //Buttons
        Button pulniMn_Button_Try = findViewById(R.id.pulniMn_Button_Try); pulniMn_Button_Try.setOnClickListener(Try);
        Button pulniMn_Button_Clear = findViewById(R.id.pulniMn_Button_Clear); pulniMn_Button_Clear.setOnClickListener(Clear);


        //Resize text for tablets and large devices
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){

            float sp = (float) (pulniMnActivity_textView_Title.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            pulniMnActivity_textView_Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);

            ArrayList<TextView> textViewsForResize = new ArrayList<TextView>();
            for(int i=0; i<headTexts.length; i++) textViewsForResize.add(headTexts[i]);
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_T0_Yes));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_T0_No));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_T1_Yes));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_T1_No));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_S_Yes));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_S_No));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_M_Yes));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_M_No));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_L_Yes));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_L_No));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_Final));
            textViewsForResize.add((TextView) findViewById(R.id.pulniMn_TV_Jigalkin));

            ArrayList<EditText> editTextsForResize = new ArrayList<EditText>();
            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) for(int j=0; j<countParam; j++) editTextsForResize.add(paramArray[i][j]);
            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) for(int j=0; j<countFunc; j++) editTextsForResize.add(funcArray[i][j]);

            ArrayList<Button> buttonTextsForResize = new ArrayList<Button>();
            buttonTextsForResize.add(pulniMn_Button_Try);
            buttonTextsForResize.add(pulniMn_Button_Clear);

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

            boolean t0 = false, t1 = false, s = false, m = false, l = false; // трябва всички за да true за да бъде пълно множеството

            //Проверка за Т0 и Т1
            int rowT0 = -1, rowT1 = -1;
            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) {
                if (countParam == 2) {
                    if (param[i][0] == 0 && param[i][1] == 0) rowT0 = i;
                    if (param[i][0] == 1 && param[i][1] == 1) rowT1 = i;
                }
                if (countParam == 3) {
                    if (param[i][0] == 0 && param[i][1] == 0 && param[i][2] == 0) rowT0 = i;
                    if (param[i][0] == 1 && param[i][1] == 1 && param[i][2] == 1) rowT1 = i;
                }
            }

            if(rowT0 > -1){
                String functionsYes = "";
                String functionsNo = "";
                for(int i=0; i<countFunc; i++)
                {
                    if(func[rowT0][i] == 0){
                        functionsYes = functionsYes + "f" + (i+1) + "  ";
                    } else {
                        functionsNo = functionsNo + "f" + (i+1) + "  ";
                        t0 = true;
                    }
                }
                if(!functionsYes.isEmpty()) {
                    ((TextView) findViewById(R.id.pulniMn_TV_T0_Yes)).setText(functionsYes + "\u2208 T\u2080");
                }
                else {
                    //((TextView) findViewById(R.id.pulniMn_TV_T0_Yes)).setText("Никоя функции не принадлежи на множеството T\u2080");
                }
                if(!functionsNo.isEmpty()) {
                    ((TextView) findViewById(R.id.pulniMn_TV_T0_No)).setText(functionsNo + "\u2209 T\u2080");
                }
                else {
                    //((TextView) findViewById(R.id.pulniMn_TV_T0_No)).setText("Всички функции принадлежат на множеството T\u2080");
                    //от тук правя текста на отговора да стане, че множеството е непълно и return от метода
                    String functions = "";
                    for(int i = 1; i<= countFunc; i++) functions += "f" + i + "  ";
                    ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2282 T\u2080 \u21D2 множеството е непълно");
                    ScrollDown();
                    return;
                }
            } else {
                ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("Не намирам ред X1=X2=X3=0!");
                ScrollDown();
                return;
            }

            if(rowT1 > -1){
                String functionsYes = "";
                String functionsNo = "";
                for(int i=0; i<countFunc; i++)
                {
                    if(func[rowT1][i] == 1){
                        functionsYes = functionsYes + "f" + (i+1) + "  ";
                    } else {
                        functionsNo = functionsNo + "f" + (i+1) + "  ";
                        t1 = true;
                    }
                }
                if(!functionsYes.isEmpty()) {
                    ((TextView) findViewById(R.id.pulniMn_TV_T1_Yes)).setText(functionsYes + "\u2208 T\u2081");
                }
                else {
                    //((TextView) findViewById(R.id.pulniMn_TV_T1_Yes)).setText("Никоя функция не принадлежи на множеството T\u2081");
                }
                if(!functionsNo.isEmpty()) {
                    ((TextView) findViewById(R.id.pulniMn_TV_T1_No)).setText(functionsNo + "\u2209 T\u2081");
                }
                else {
                    //((TextView) findViewById(R.id.pulniMn_TV_T1_No)).setText("Всички функции принадлежат на множеството T\u2081");
                    //от тук правя текста на отговора да стане, че множеството е непълно и return от метода
                    String functions = "";
                    for(int i = 1; i<= countFunc; i++) functions += "f" + i + "  ";
                    ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2282 T\u2081 \u21D2 множеството е непълно");
                    ScrollDown();
                    return;
                }
            } else {
                ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("Не намирам ред X1=X2=X3=1!");
                ScrollDown();
                return;
            }

            int row00=-1, row01=-1, row10=-1, row11=-1;
            int row000 = -1, row001 = -1, row010 = -1, row011 = -1, row100 = -1, row101 = -1, row110 = -1, row111 = -1;

            for(int i=0; i<(int) countFunc; i++) {
                for (int j = 0; j < (int) (Math.pow(2, countParam)); j++) {
                    if (countParam == 2) {
                        if(param[j][0] == 0 && param[j][1] == 0) row00 = j;
                        if(param[j][0] == 0 && param[j][1] == 1) row01 = j;
                        if(param[j][0] == 1 && param[j][1] == 0) row10 = j;
                        if(param[j][0] == 1 && param[j][1] == 1) row11 = j;
                    }
                    if (countParam==3) {
                        if(param[j][0] == 0 && param[j][1] == 0 && param[j][2] == 0) row000 = j;
                        if(param[j][0] == 0 && param[j][1] == 0 && param[j][2] == 1) row001 = j;
                        if(param[j][0] == 0 && param[j][1] == 1 && param[j][2] == 0) row010 = j;
                        if(param[j][0] == 0 && param[j][1] == 1 && param[j][2] == 1) row011 = j;
                        if(param[j][0] == 1 && param[j][1] == 0 && param[j][2] == 0) row100 = j;
                        if(param[j][0] == 1 && param[j][1] == 0 && param[j][2] == 1) row101 = j;
                        if(param[j][0] == 1 && param[j][1] == 1 && param[j][2] == 0) row110 = j;
                        if(param[j][0] == 1 && param[j][1] == 1 && param[j][2] == 1) row111 = j;
                    }
                }

                if (countParam == 2) {
                    if(row00==-1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=X2=0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row01==-1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=0 X2=1", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row10==-1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=1 X2=0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row11==-1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=X2=1", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (countParam==3) {
                    if(row000 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=X2=X3=0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row001 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=0 X2=0 X3=1", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row010 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=0 X2=1 X3=0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row011 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=0 X2=1 X3=1", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row100 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=1 X2=0 X3=0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row101 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=1 X2=0 X3=1", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row110 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=1 X2=1 X3=0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(row111 == -1) {
                        ClearUp();
                        Toast.makeText(getApplicationContext(), "Не намирам ред X1=X2=X3=1", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            int[][] paramOrdered = new int[(int) (Math.pow(2, countParam))][countParam];
            int[][] funcOrdered = new int[(int) (Math.pow(2, countParam))][countFunc];
            int[] IndexesByRow = new int[(int) (Math.pow(2, countParam))];

            if(countParam == 2) {
                IndexesByRow[0] = row00;
                IndexesByRow[1] = row01;
                IndexesByRow[2] = row10;
                IndexesByRow[3] = row11;
            } else if (countParam == 3) {
                IndexesByRow[0] = row000;
                IndexesByRow[1] = row001;
                IndexesByRow[2] = row010;
                IndexesByRow[3] = row011;
                IndexesByRow[4] = row100;
                IndexesByRow[5] = row101;
                IndexesByRow[6] = row110;
                IndexesByRow[7] = row111;
            }

            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) {
                for(int j=0; j<countParam; j++) {
                    paramOrdered[i][j] = param[IndexesByRow[i]][j];
                }
            }

            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) {
                for(int j=0; j<countFunc; j++) {
                    funcOrdered[i][j] = func[IndexesByRow[i]][j];
                }
            }

            //Проверка за самодвойни функции
            int[][] Sfunc = new int[(int) (Math.pow(2, countParam))][countFunc];

            for(int i=0; i<(int) (Math.pow(2, countParam)); i++) {
                for (int j = 0; j < countFunc; j++) {
                    if(funcOrdered[((int) (Math.pow(2, countParam)))-1-i][j] == 0) Sfunc[i][j] = 1;
                    else Sfunc[i][j] = 0;
                }
            }
            String functionsSYes = "";
            String functionsSNo = "";

            for(int i=0; i<(int) countFunc; i++) {
                boolean currentFunc = true;
                for (int j = 0; j < (int) (Math.pow(2, countParam)); j++) {
                    if(Sfunc[j][i] != funcOrdered[j][i]){
                        s = true;
                        currentFunc = false;
                    }
                }
                if(currentFunc) functionsSYes = functionsSYes + "f" + (i+1) + "  ";
                else functionsSNo = functionsSNo + "f" + (i+1) + "  ";
            }

            if(!functionsSYes.isEmpty()) {
                ((TextView) findViewById(R.id.pulniMn_TV_S_Yes)).setText(functionsSYes + "\u2208 S");
            }
            else {
                //((TextView) findViewById(R.id.pulniMn_TV_S_Yes)).setText("Никоя от функциите не принадлежи на множеството S");
            }
            if(!functionsSNo.isEmpty()) {
                ((TextView) findViewById(R.id.pulniMn_TV_S_No)).setText(functionsSNo + "\u2209 S");
            }
            else {
                //((TextView) findViewById(R.id.pulniMn_TV_S_No)).setText("Всички функции принадлежат на множеството S");
                //от тук правя текста на отговора да стане, че множеството е непълно и return от метода
                String functions = "";
                for(int i = 1; i<= countFunc; i++) functions += "f" + i + "  ";
                ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2282 S \u21D2 множеството е непълно");
                ScrollDown();
                return;
            }

            //Проверка за монотонни функции
            String functionsMYes = "";
            String functionsMNo = "";
            for(int i=0; i<(int) countFunc; i++) {
                int indexFirst1 = -1;
                int indexLineTwo = -1;
                boolean currentMonotonna = true;
                for (int j = 0; j < (int) (Math.pow(2, countParam)); j++) {
                    if(indexFirst1 == -1) {
                        if (funcOrdered[j][i] == 1) indexFirst1 = j;
                    }
                    else {
                        if(func[j][i] == 0) {
                            if (countParam == 2) {
                                if(paramOrdered[indexFirst1][0] <= paramOrdered[j][0] && paramOrdered[indexFirst1][1] <= paramOrdered[j][1])
                                {
                                    //намерихме немонотонна функция
                                    m = true;
                                    currentMonotonna = false;
                                    indexLineTwo = j;
                                    break;
                                }
                            }
                            if (countParam == 3) {
                                if(paramOrdered[indexFirst1][0] <= paramOrdered[j][0] && paramOrdered[indexFirst1][1] <= paramOrdered[j][1] && paramOrdered[indexFirst1][2] <= paramOrdered[j][2])
                                {
                                    //намерихме немонотонна функция
                                    m = true;
                                    currentMonotonna = false;
                                    indexLineTwo = j;
                                    break;
                                }
                            }
                        }
                    }
                }
                if(currentMonotonna) functionsMYes = functionsMYes + "f" + (i+1) + "  ";
                else functionsMNo = functionsMNo + "f" + (i+1) + " (ред " + (indexFirst1+1) + " и " + (indexLineTwo+1) + ")  ";
            }
            if(!functionsMYes.isEmpty()) {
                ((TextView) findViewById(R.id.pulniMn_TV_M_Yes)).setText(functionsMYes + "\u2208 M");
            }
            else {
                //((TextView) findViewById(R.id.pulniMn_TV_M_Yes)).setText("Никоя от функциите не принадлежи на множеството M");
            }
            if(!functionsMNo.isEmpty()) {
                ((TextView) findViewById(R.id.pulniMn_TV_M_No)).setText(functionsMNo + "\u2209 M");
            }
            else {
                //((TextView) findViewById(R.id.pulniMn_TV_M_No)).setText("Всички функции принадлежат на множеството M");
                //от тук правя текста на отговора да стане, че множеството е непълно и return от метода
                String functions = "";
                for(int i = 1; i<= countFunc; i++) functions += "f" + i + "  ";
                ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2282 M \u21D2 множеството е непълно");
                ScrollDown();
                return;
            }

            //Проверка за линейни функции

            String[] result = new String[countFunc];
            for(int i=0; i<countFunc; i++) result[i] = "";

            String functionsLYes = "";
            String functionsLNo = "";
            for(int i=0; i<(int) countFunc; i++) {
                boolean currentLineina = true;
                int a, b1, b2, b3, c, c1, c2, c3, d;

                if (countParam == 2) {
                    c = func[row00][i];
                    if(func[row01][i] == 0 && c == 0) b2 = 0;
                    else if(func[row01][i] == 1 && c == 1) b2 = 0;
                    else b2 = 1;
                    if(func[row10][i] == 0 && c == 0) b1 = 0;
                    else if(func[row10][i] == 1 && c == 1) b1 = 0;
                    else b1 = 1;
                    if((c+b2+b1)%2 == 0 && func[row11][i] == 0) a = 0;
                    else if((c+b2+b1)%2 == 0 && func[row11][i] == 1) a = 1;
                    else if((c+b2+b1)%2 != 0 && func[row11][i] == 0) a = 1;
                    else a = 0;
                    if(a == 1) {
                        l = true;
                        currentLineina = false;
                    }

                    result[i] = "\nf" + (i+1) + ": \nc = " + c + "\nb2 = " + b2 + "\nb1 = " + b1 + "\na = " + a;
                    result[i]+= "\nf" + (i+1) + " = ";
                    if(a==1) result[i]+="X1.X2";
                    if(b1==1) result[i]+="+X1";
                    if(b2==1) result[i]+="+X2";
                    if(c==1) result[i]+="+1";
                    result[i]+="\n\n";
                }
                if (countParam==3) {
                    d = func[row000][i];
                    if(func[row001][i] == 0 && d == 0) c3 = 0;
                    else if(func[row001][i] == 1 && d == 1) c3 = 0;
                    else c3 = 1;
                    if(func[row010][i] == 0 && d == 0) c2 = 0;
                    else if(func[row010][i] == 1 && d == 1) c2 = 0;
                    else c2 = 1;
                    if((d+c2+c3)%2 == 0 && func[row011][i] == 0) b1 = 0;
                    else if((d+c2+c3)%2 == 0 && func[row011][i] == 1) b1 = 1;
                    else if((d+c2+c3)%2 != 0 && func[row011][i] == 0) b1 = 1;
                    else b1 = 0;
                    if(func[row100][i] == 0 && d == 0) c1 = 0;
                    else if(func[row100][i] == 1 && d == 1) c1 = 0;
                    else c1 = 1;
                    if((d+c1+c3)%2 == 0 && func[row101][i] == 0) b2 = 0;
                    else if((d+c1+c3)%2 == 0 && func[row101][i] == 1) b2 = 1;
                    else if((d+c1+c3)%2 != 0 && func[row101][i] == 0) b2 = 1;
                    else b2 = 0;
                    if((d+c1+c2)%2 == 0 && func[row110][i] == 0) b3 = 0;
                    else if((d+c1+c2)%2 == 0 && func[row110][i] == 1) b3 = 1;
                    else if((d+c1+c2)%2 != 0 && func[row110][i] == 0) b3 = 1;
                    else b3 = 0;
                    if((d+b1+b2+b3+c1+c3)%2 == 0 && func[row111][i] == 0) a = 0;
                    else if((d+b1+b2+b3+c1+c3)%2 == 0 && func[row111][i] == 1) a = 1;
                    else if((d+b1+b2+b3+c1+c3)%2 != 0 && func[row111][i] == 0) a = 1;
                    else a = 0;
                    if(a == 1 || b1 == 1 || b2 == 1 || b3 == 1) {
                        l = true;
                        currentLineina = false;
                    }

                    result[i] = "f" + (i+1) + ": \nd = " + d + "\nc3 = " + c3 + "\nc2 = " + c2 + "\nb1 = " + b1 + "\nc1 = " + c1 + "\nb2 = " + b2 + "\nb3 = " + b3 + "\na = " + a;
                    result[i]+= "\nf" + (i+1) + " = ";
                    if(a==1) result[i]+="X1.X2.X3";
                    if(b1==1) result[i]+="+X2.X3";
                    if(b2==1) result[i]+="+X1.X3";
                    if(b3==1) result[i]+="+X1.X2";
                    if(c1==1) result[i]+="+X1";
                    if(c2==1) result[i]+="+X2";
                    if(c3==1) result[i]+="+X3";
                    if(d==1) result[i]+="+1";
                    result[i]+="\n\n";
                }


                if(currentLineina) functionsLYes = functionsLYes + "f" + (i+1) + "  ";
                else functionsLNo = functionsLNo + "f" + (i+1) + "  ";
            }

            //Показване разписан полинома на жигалкин:

            String blegh = "";
            if(countParam == 2) blegh = "\nf(X1,X2) = a.X1.X2 + b1.X1 + b2.X2 + c\n";
            else if(countParam == 3) blegh = "\n\nf(X1,X2,X3) = a.X1.X2.X3 + b1.X2.X3 + b2.X1.X3 + b3.X1.X2 + c1.X1 + c2.X2 + c3.X3 + d\n";
            if (countFunc == 1) {
                ((TextView) findViewById(R.id.pulniMn_TV_Jigalkin)).setText(blegh + result[0]);
            } else if(countFunc == 2) {
                ((TextView) findViewById(R.id.pulniMn_TV_Jigalkin)).setText(blegh + result[0]+result[1]);
            } else if(countFunc == 3) {
                ((TextView) findViewById(R.id.pulniMn_TV_Jigalkin)).setText(blegh + result[0]+result[1]+result[2]);
            }

            if(!functionsLYes.isEmpty()) {
                ((TextView) findViewById(R.id.pulniMn_TV_L_Yes)).setText(functionsLYes + "\u2208 L");
            }
            else {
                //((TextView) findViewById(R.id.pulniMn_TV_L_Yes)).setText("Никоя от функциите не принадлежи на множеството L");
            }
            if(!functionsLNo.isEmpty()) {
                ((TextView) findViewById(R.id.pulniMn_TV_L_No)).setText(functionsLNo + "\u2209 L");
            }
            else {
                //((TextView) findViewById(R.id.pulniMn_TV_L_No)).setText("Всички функции принадлежат на множеството L");
                //от тук правя текста на отговора да стане, че множеството е непълно и return от метода
                String functions = "";
                for(int i = 1; i<= countFunc; i++) functions += "f" + i + "  ";
                ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2282 L \u21D2 множеството е непълно");
                ScrollDown();
                return;
            }


            String functions = "";
            for(int i = 1; i<= countFunc; i++) functions += "f" + i + "  ";
            if(t0 && t1 && s && m && l) {
                if(countFunc == 1) ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2284 T\u2080  &  F \u2284 T\u2081  &  F \u2284 S  &  F \u2284 M  &  F \u2284 L \n \u21D2 функцията е шеферова");
                else ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("F {  "+functions+"}\n F \u2284 T\u2080  &  F \u2284 T\u2081  &  F \u2284 S  &  F \u2284 M  &  F \u2284 L \n \u21D2 множеството е пълно");
            }
            ScrollDown();

        }
    };

    private void ClearUp() {
        ((TextView) findViewById(R.id.pulniMn_TV_T0_Yes)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_T0_No)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_T1_Yes)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_T1_No)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_S_Yes)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_S_No)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_M_Yes)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_M_No)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_L_Yes)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_L_No)).setText("");
        ((TextView) findViewById(R.id.pulniMn_TV_Final)).setText("");
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
                ((ScrollView) findViewById(R.id.pulniMnActivity_ScrollMain)).pageScroll(ScrollView.FOCUS_DOWN); //.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        handler.postDelayed(r, 250);
    }

    private void ScrollUp(){
        final Handler handler;
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                ((ScrollView) findViewById(R.id.pulniMnActivity_ScrollMain)).fullScroll(ScrollView.FOCUS_UP);
            }
        };
        handler.postDelayed(r, 250);
    }
}
