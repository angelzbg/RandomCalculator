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

public class CoordinatesActivity extends AppCompatActivity {

    //head
    ConstraintLayout coordActivity_constrLayout_Bar;
    ImageButton coordActivity_ImgBtn_Back;
    TextView coordActivity_textView_Title;

    //row Enter A coords
    //TextView coordActivity_textView1_2r; //2r
    EditText coordActivity_EditText_Ax; //8r
    //TextView coordActivity_textView2_1r; // 1r
    EditText coordActivity_EditText_Ay; //8r
    //TextView coordActivity_textView3_1r; //1r

    //row Enter B coords
    //TextView coordActivity_textView4_2r; //2r
    EditText coordActivity_EditText_Bx; //8r
    //TextView coordActivity_textView5_1r; // 1r
    EditText coordActivity_EditText_By; //8r
    //TextView coordActivity_textView6_1r; //1r

    TextView coordActivity_textView_Result_AB;

    //брой елементи за оразмеряване и техните тегла по дисплея
    final int r2 = 2;
    final int r1 = 4;
    final int r8 = 4;

    Button coordActivity_Button_Calculate;
    Button coordActivity_Button_Clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_coordinates);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Височината и ширината на екрана в пиксели
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int height = metrics.heightPixels;
        final int width = metrics.widthPixels;

        //header resizing first
        coordActivity_constrLayout_Bar = findViewById(R.id.coordActivity_constrLayout_Bar);
        coordActivity_ImgBtn_Back = findViewById(R.id.coordActivity_ImgBtn_Back);
        coordActivity_textView_Title = findViewById(R.id.coordActivity_textView_Title);
        coordActivity_constrLayout_Bar.getLayoutParams().height = (int) (width/7);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        coordActivity_ImgBtn_Back.getLayoutParams().height = (int) (width/8-(px*2));
        coordActivity_ImgBtn_Back.getLayoutParams().width = (int) (width/8-(px*2));
        coordActivity_ImgBtn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final int verticalPart = (int) (height/10);

        coordActivity_EditText_Ax = findViewById(R.id.coordActivity_EditText_Ax);
        coordActivity_EditText_Ay = findViewById(R.id.coordActivity_EditText_Ay);
        coordActivity_EditText_Bx = findViewById(R.id.coordActivity_EditText_Bx);
        coordActivity_EditText_By = findViewById(R.id.coordActivity_EditText_By);
        coordActivity_textView_Result_AB = findViewById(R.id.coordActivity_textView_Result_AB);

        TextView[] Rx1textViews = new TextView[r1];
        Rx1textViews[0]= findViewById(R.id.coordActivity_textView2_1r);
        Rx1textViews[1]= findViewById(R.id.coordActivity_textView3_1r);
        Rx1textViews[2]= findViewById(R.id.coordActivity_textView5_1r);
        Rx1textViews[3]= findViewById(R.id.coordActivity_textView6_1r);


        TextView[] Rx2textViews = new TextView[r2];
        Rx2textViews[0] = findViewById(R.id.coordActivity_textView1_2r);
        Rx2textViews[1] = findViewById(R.id.coordActivity_textView4_2r);

        EditText[] Rx8editTexts = new EditText[r8];
        Rx8editTexts[0] = coordActivity_EditText_Ax;
        Rx8editTexts[1] = coordActivity_EditText_Ay;
        Rx8editTexts[2] = coordActivity_EditText_Bx;
        Rx8editTexts[3] = coordActivity_EditText_By;


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
        coordActivity_Button_Calculate = findViewById(R.id.coordActivity_Button_Calculate);
        coordActivity_Button_Calculate.getLayoutParams().height = (int) (width/7.5);
        coordActivity_Button_Calculate.getLayoutParams().width = (int) (width/2-px);
        coordActivity_Button_Calculate.setOnClickListener(CalculateVectorCoords);

        coordActivity_Button_Clear = findViewById(R.id.coordActivity_Button_Clear);
        coordActivity_Button_Clear.getLayoutParams().height = (int) (width/7.5);
        coordActivity_Button_Clear.getLayoutParams().width = (int) (width/2-px);
        coordActivity_Button_Clear.setOnClickListener(Clear);

        //Resize text for tablets and large devices
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){

            float sp = (float) (coordActivity_textView_Title.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            coordActivity_textView_Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);

            ArrayList<TextView> textViewsForResize = new ArrayList<TextView>();
            for(int i=0; i<r1; i++)
            {
                textViewsForResize.add(Rx1textViews[i]);
            }
            for(int i=0; i<r2; i++)
            {
                textViewsForResize.add(Rx2textViews[i]);
            }
            textViewsForResize.add(coordActivity_textView_Result_AB);

            ArrayList<EditText> editTextsForResize = new ArrayList<EditText>();
            editTextsForResize.add(coordActivity_EditText_Ax);
            editTextsForResize.add(coordActivity_EditText_Ay);
            editTextsForResize.add(coordActivity_EditText_Bx);
            editTextsForResize.add(coordActivity_EditText_By);

            ArrayList<Button> buttonTextsForResize = new ArrayList<Button>();
            buttonTextsForResize.add(coordActivity_Button_Calculate);
            buttonTextsForResize.add(coordActivity_Button_Clear);

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

    View.OnClickListener CalculateVectorCoords = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(coordActivity_EditText_Ax.getText().toString().isEmpty() || coordActivity_EditText_Ay.getText().toString().isEmpty() || coordActivity_EditText_Bx.getText().toString().isEmpty() || coordActivity_EditText_By.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Моля въведете всички полета", Toast.LENGTH_LONG).show();
                return;
            }

            BigDecimal AX = new BigDecimal(coordActivity_EditText_Ax.getText().toString());
            BigDecimal BX = new BigDecimal(coordActivity_EditText_Bx.getText().toString());
            BigDecimal AY = new BigDecimal(coordActivity_EditText_Ay.getText().toString());
            BigDecimal BY = new BigDecimal(coordActivity_EditText_By.getText().toString());



            BX.subtract(AX);
            BY.subtract(AY);

            coordActivity_textView_Result_AB.setText("Vector AB:\nX = " + BX.subtract(AX) + "\nY = " + BY.subtract(AY));
        }
    };

    View.OnClickListener Clear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            coordActivity_EditText_Ax.setText("");
            coordActivity_EditText_Ay.setText("");
            coordActivity_EditText_Bx.setText("");
            coordActivity_EditText_By.setText("");
            coordActivity_textView_Result_AB.setText("");
        }
    };
}
