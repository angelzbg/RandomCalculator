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
import java.math.BigInteger;
import java.util.ArrayList;

public class PkOkDkActivity extends AppCompatActivity {

    //head
    ConstraintLayout pravKodActivity_constrLayout_Bar;
    ImageButton pravKodActivity_ImgBtn_Back;
    TextView pravKodActivity_textView_Title;

    Button pravKodActivity_Button_Calculate;
    Button pravKodActivity_Button_Clear;

    EditText pravKodActivity_Number_ET;
    EditText pravKodActivity_Razrqd_ET;

    TextView pravKodActivity_textView_Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pk_ok_dk);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Височината и ширината на екрана в пиксели
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        //header resizing first
        pravKodActivity_constrLayout_Bar = findViewById(R.id.pravKodActivity_constrLayout_Bar);
        pravKodActivity_ImgBtn_Back = findViewById(R.id.pravKodActivity_ImgBtn_Back);
        pravKodActivity_textView_Title = findViewById(R.id.pravKodActivity_textView_Title);
        pravKodActivity_constrLayout_Bar.getLayoutParams().height = (int) (width/7);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        pravKodActivity_ImgBtn_Back.getLayoutParams().height = (int) (width/8-(px*2));
        pravKodActivity_ImgBtn_Back.getLayoutParams().width = (int) (width/8-(px*2));
        pravKodActivity_ImgBtn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final int verticalPart = (int) (height/10);

        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        pravKodActivity_Button_Calculate = findViewById(R.id.pravKodActivity_Button_Calculate);
        pravKodActivity_Button_Calculate.getLayoutParams().height = (int) (width/7.5);
        pravKodActivity_Button_Calculate.getLayoutParams().width = (int) (width/2-px);
        pravKodActivity_Button_Calculate.setOnClickListener(Calculate);

        pravKodActivity_Button_Clear = findViewById(R.id.pravKodActivity_Button_Clear);
        pravKodActivity_Button_Clear.getLayoutParams().height = (int) (width/7.5);
        pravKodActivity_Button_Clear.getLayoutParams().width = (int) (width/2-px);
        pravKodActivity_Button_Clear.setOnClickListener(Clear);


        pravKodActivity_Number_ET = findViewById(R.id.pravKodActivity_Number_ET);
        pravKodActivity_Number_ET.getLayoutParams().width = (int) (width/1.5);
        pravKodActivity_Number_ET.getLayoutParams().height = (int) (width/7.5);
        pravKodActivity_Razrqd_ET = findViewById(R.id.pravKodActivity_Razrqd_ET);
        pravKodActivity_Razrqd_ET.getLayoutParams().width = (int) (width/3);
        pravKodActivity_Razrqd_ET.getLayoutParams().height = (int) (width/7.5);

        pravKodActivity_textView_Result = findViewById(R.id.pravKodActivity_textView_Result);



        //Resize text for tablets and large devices
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){

            float sp = (float) (pravKodActivity_textView_Title.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            pravKodActivity_textView_Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+10);

            ArrayList<TextView> textViewsForResize = new ArrayList<TextView>();

            textViewsForResize.add(pravKodActivity_textView_Result);

            ArrayList<EditText> editTextsForResize = new ArrayList<EditText>();
            editTextsForResize.add(pravKodActivity_Number_ET);
            editTextsForResize.add(pravKodActivity_Razrqd_ET);

            ArrayList<Button> buttonTextsForResize = new ArrayList<Button>();
            buttonTextsForResize.add(pravKodActivity_Button_Calculate);
            buttonTextsForResize.add(pravKodActivity_Button_Clear);

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

    View.OnClickListener Calculate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(pravKodActivity_Number_ET.getText().toString().isEmpty() || pravKodActivity_Razrqd_ET.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Моля въведете всички полета", Toast.LENGTH_LONG).show();
                return;
            }

            int integer1 = 0;

            try {
                integer1 = Integer.parseInt(pravKodActivity_Number_ET.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Моля въведете Integer за число", Toast.LENGTH_LONG).show();
                return;
            }

            if(integer1==0)
            {
                Toast.makeText(getApplicationContext(), "Въведете отрицателно число", Toast.LENGTH_LONG).show();
                return;
            }
            else if(integer1>0)
            {
                integer1 = -integer1;
                pravKodActivity_Number_ET.setText(""+integer1);
            }

            int razrqd = 0;

            try {
                razrqd = Integer.parseInt(pravKodActivity_Razrqd_ET.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Моля въведете Integer за число", Toast.LENGTH_LONG).show();
                return;
            }

            if(razrqd>32){
                razrqd = 32;
                pravKodActivity_Razrqd_ET.setText(""+razrqd);
            }

            char[] charArray = Integer.toBinaryString(integer1).toCharArray();
            String DK = "";
            for(int i=0; i<razrqd; i++)
            {
                DK = charArray[charArray.length-(1+i)] + DK;
            }

            String PK = Integer.toBinaryString(-integer1);

            int loop = razrqd - PK.length() - 1;

            for(int i=0; i<loop; i++)
            {
                PK = "0" + PK;
            }
            PK = "1" + PK;

            String PKCopy = PK;
            PKCopy = PKCopy.replace('0', '*');
            PKCopy = PKCopy.replace('1', '0');
            PKCopy = PKCopy.replace('*', '1');
            char[] OK = PKCopy.toCharArray();
            OK[0] = '1';

            if(PK.length()>DK.length())
            {
                pravKodActivity_textView_Result.setText("Изчисление за (" + integer1 + ") има нужда от разрядност >= " + PK.length());
            }
            else
            {
                pravKodActivity_textView_Result.setText("ПК: " + PK + "\nОК: " + String.copyValueOf(OK) + "\nДК: " + DK);
            }
        }
    };

    View.OnClickListener Clear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pravKodActivity_Number_ET.setText("");
            pravKodActivity_Razrqd_ET.setText("");
            pravKodActivity_textView_Result.setText("");
        }
    };
}
