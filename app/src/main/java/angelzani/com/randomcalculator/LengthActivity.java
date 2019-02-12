package angelzani.com.randomcalculator;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

public class LengthActivity extends AppCompatActivity {

    //head
    ConstraintLayout lenActivity_constrLayout_Bar;
    ImageButton lenActivity_ImgBtn_Back;
    TextView lenActivity_textView_Title;


    //row Enter coords
    //TextView lenActivity_textView1_2r; //2r
    EditText lenActivity_EditText_Ax; //8r
    //TextView lenActivity_textView2_1r; // 1r
    EditText lenActivity_EditText_Ay; //8r
    //TextView lenActivity_textView3_1r; //1r

    TextView lenActivity_textView_Result_LenAB;

    //брой елементи за оразмеряване и техните тегла по дисплея
    final int r2 = 1;
    final int r1 = 2;
    final int r8 = 2;

    Button lenActivity_Button_Calculate;
    Button lenActivity_Button_Clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_length);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Височината и ширината на екрана в пиксели
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int height = metrics.heightPixels;
        final int width = metrics.widthPixels;

        //header resizing first
        lenActivity_constrLayout_Bar = findViewById(R.id.lenActivity_constrLayout_Bar);
        lenActivity_ImgBtn_Back = findViewById(R.id.lenActivity_ImgBtn_Back);
        lenActivity_textView_Title = findViewById(R.id.lenActivity_textView_Title);
        lenActivity_constrLayout_Bar.getLayoutParams().height = (int) (width/7);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        lenActivity_ImgBtn_Back.getLayoutParams().height = (int) (width/8-(px*2));
        lenActivity_ImgBtn_Back.getLayoutParams().width = (int) (width/8-(px*2));
        lenActivity_ImgBtn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lenActivity_EditText_Ax = findViewById(R.id.lenActivity_EditText_Ax);
        lenActivity_EditText_Ay = findViewById(R.id.lenActivity_EditText_Ay);

        lenActivity_Button_Calculate = findViewById(R.id.lenActivity_Button_Calculate);
        lenActivity_Button_Clear = findViewById(R.id.lenActivity_Button_Clear);

        lenActivity_textView_Result_LenAB = findViewById(R.id.lenActivity_textView_Result_LenAB);


        final int verticalPart = (int) (height/10);

        TextView[] Rx1textViews = new TextView[r1];
        Rx1textViews[0]= findViewById(R.id.lenActivity_textView2_1r);
        Rx1textViews[1]= findViewById(R.id.lenActivity_textView3_1r);


        TextView[] Rx2textViews = new TextView[r2];
        Rx2textViews[0] = findViewById(R.id.lenActivity_textView1_2r);

        EditText[] Rx8editTexts = new EditText[r8];
        Rx8editTexts[0] = lenActivity_EditText_Ax;
        Rx8editTexts[1] = lenActivity_EditText_Ay;

        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
        final int part20 = (int) ((width-px)/20);

        for(int i=0; i<r1; i++)
        {
            Rx1textViews[i].getLayoutParams().width = part20;
            Rx1textViews[i].getLayoutParams().height = (int) (width/7.5);
        }

        for(int i=0; i<r2; i++)
        {
            Rx2textViews[i].getLayoutParams().width=part20*2;
            Rx2textViews[i].getLayoutParams().height = (int) (width/7.5);
        }

        for(int i=0; i<r8; i++)
        {
            Rx8editTexts[i].getLayoutParams().width=part20*8;
            Rx8editTexts[i].getLayoutParams().height = (int) (width/7.5);
        }

        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        lenActivity_Button_Calculate = findViewById(R.id.lenActivity_Button_Calculate);
        lenActivity_Button_Calculate.getLayoutParams().height = (int) (width/7.5);
        lenActivity_Button_Calculate.getLayoutParams().width = (int) (width/2-px);
        lenActivity_Button_Calculate.setOnClickListener(CalculateVectorLength);

        lenActivity_Button_Clear = findViewById(R.id.lenActivity_Button_Clear);
        lenActivity_Button_Clear.getLayoutParams().height = (int) (width/7.5);
        lenActivity_Button_Clear.getLayoutParams().width = (int) (width/2-px);
        lenActivity_Button_Clear.setOnClickListener(Clear);


        //Resize text for tablets and large devices
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){

            float sp = (float) (lenActivity_textView_Title.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            lenActivity_textView_Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);

            ArrayList<TextView> textViewsForResize = new ArrayList<TextView>();
            for(int i=0; i<r1; i++)
            {
                textViewsForResize.add(Rx1textViews[i]);
            }
            for(int i=0; i<r2; i++)
            {
                textViewsForResize.add(Rx2textViews[i]);
            }
            textViewsForResize.add(lenActivity_textView_Result_LenAB);

            ArrayList<EditText> editTextsForResize = new ArrayList<EditText>();
            editTextsForResize.add(lenActivity_EditText_Ax);
            editTextsForResize.add(lenActivity_EditText_Ay);

            ArrayList<Button> buttonTextsForResize = new ArrayList<Button>();
            buttonTextsForResize.add(lenActivity_Button_Calculate);
            buttonTextsForResize.add(lenActivity_Button_Clear);

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

    View.OnClickListener CalculateVectorLength = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(lenActivity_EditText_Ax.getText().toString().isEmpty() || lenActivity_EditText_Ay.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Моля въведете стойност за X и Y", Toast.LENGTH_LONG).show();
                return;
            }

            double AX = 0d;
            double AY = 0d;

            try {
                AX = Double.parseDouble(lenActivity_EditText_Ax.getText().toString());
                AY = Double.parseDouble(lenActivity_EditText_Ay.getText().toString());
            } catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Опитайте с по-малки числа или по-малка точност", Toast.LENGTH_LONG).show();
                return;
            }

            double result = Math.sqrt((AX*AX+AY*AY));

            lenActivity_textView_Result_LenAB.setText("Vector length = " + result);
        }
    };

    View.OnClickListener Clear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            lenActivity_EditText_Ax.setText("");
            lenActivity_EditText_Ay.setText("");
            lenActivity_textView_Result_LenAB.setText("");
        }
    };

    private static BigDecimal sqrtSQRT(BigDecimal x) {
        return BigDecimal.valueOf(StrictMath.sqrt(x.doubleValue()));
    }
}
