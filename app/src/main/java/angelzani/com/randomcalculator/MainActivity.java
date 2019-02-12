package angelzani.com.randomcalculator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainActivity_constrLayoutGeoScroll; //височината му трябва да стане половината от височината на екрана
    private ConstraintLayout mainActivity_constrLayoutPkOkDkScroll; //същото
    private ConstraintLayout mainActivity_constrLayoutDMScroll; //същото

    private ArrayList<HorizontalScrollView> realScrollsMenu;
    private ArrayList<ImageButton> realScrollButtonsMenuLeft;
    private ArrayList<ImageButton> realScrollButtonsMenuRight;

    /*ConstraintLayout mainActivity_constrLayoutVektorniKoordinati; //ширината трябва да стане половината от ширината на екрана
    ConstraintLayout mainActivity_constrLayoutVektornaDuljina; //ширината трябва да стане половината от ширината на екрана*/

    private ArrayList<ConstraintLayout> menuLayouts; //тук ще отидат всички лейаути, които искам да оразмеря относно меню-то

    private ArrayList<ImageView> menuPictures; //всички картинки от меню-тата ще отидат тук (за оразмеряване)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //завъртаме ориентацията на екрана да е изправен и блокираме завъртането в landscape
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //блокираме показването на клавиатурата в началото на зареждане на активитито ако има тест

        mainActivity_constrLayoutGeoScroll = findViewById(R.id.mainActivity_constrLayoutGeoScroll);
        mainActivity_constrLayoutPkOkDkScroll = findViewById(R.id.mainActivity_constrLayoutPkOkDkScroll);
        mainActivity_constrLayoutDMScroll = findViewById(R.id.mainActivity_constrLayoutDMScroll);

        //Височината и ширината на екрана в пиксели
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        menuLayouts = new ArrayList<ConstraintLayout>();
        menuLayouts.add( (ConstraintLayout) findViewById(R.id.mainActivity_constrLayoutVektorniKoordinati) );
        menuLayouts.add( (ConstraintLayout) findViewById(R.id.mainActivity_constrLayoutVektornaDuljina) );
        menuLayouts.add( (ConstraintLayout) findViewById(R.id.mainActivity_constrLayoutBezierCoef) );
        menuLayouts.add( (ConstraintLayout) findViewById(R.id.mainActivity_constrLayoutPkOkDk) );
        menuLayouts.add( (ConstraintLayout) findViewById(R.id.mainActivity_constrLayoutMnFunkcii) );
        menuLayouts.add( (ConstraintLayout) findViewById(R.id.mainActivity_constrLayoutBool) );

        menuPictures = new ArrayList<ImageView>();
        menuPictures.add( (ImageView) findViewById(R.id.mainActivity_imageViewDesc1) );
        menuPictures.add( (ImageView) findViewById(R.id.mainActivity_imageViewDesc2) );
        menuPictures.add( (ImageView) findViewById(R.id.mainActivity_imageViewDesc4) );
        menuPictures.add( (ImageView) findViewById(R.id.mainActivity_imageViewDesc3) );
        menuPictures.add( (ImageView) findViewById(R.id.mainActivity_imageViewDesc5) );
        menuPictures.add( (ImageView) findViewById(R.id.mainActivity_imageViewDesc6) );


        //----------------------------------------------- Scrollers for menu and Buttons for scroll ------------------------------
        realScrollsMenu = new ArrayList<HorizontalScrollView>();
        realScrollsMenu.add( (HorizontalScrollView) findViewById(R.id.mainActivity_hScrollV_RealGeom) );
        realScrollsMenu.add( (HorizontalScrollView) findViewById(R.id.mainActivity_hScrollV_RealKA) );
        realScrollsMenu.add( (HorizontalScrollView) findViewById(R.id.mainActivity_hScrollV_RealDM) );

        realScrollButtonsMenuLeft = new ArrayList<ImageButton>();
        realScrollButtonsMenuLeft.add( (ImageButton) findViewById(R.id.mainActivity_IB_scrollLeft_Geom) );
        realScrollButtonsMenuLeft.add( (ImageButton) findViewById(R.id.mainActivity_IB_scrollLeft_KA) );
        realScrollButtonsMenuLeft.add( (ImageButton) findViewById(R.id.mainActivity_IB_scrollLeft_DM) );

        realScrollButtonsMenuRight = new ArrayList<ImageButton>();
        realScrollButtonsMenuRight.add( (ImageButton) findViewById(R.id.mainActivity_IB_scrollRight_Geom) );
        realScrollButtonsMenuRight.add( (ImageButton) findViewById(R.id.mainActivity_IB_scrollRight_KA) );
        realScrollButtonsMenuRight.add( (ImageButton) findViewById(R.id.mainActivity_IB_scrollRight_DM) );

        for(int i=0; i<realScrollButtonsMenuLeft.size(); i++)
        {
            realScrollButtonsMenuLeft.get(i).setOnClickListener(scrollMenuLeft);
            realScrollButtonsMenuRight.get(i).setOnClickListener(scrollMenuRight);

            realScrollButtonsMenuLeft.get(i).getLayoutParams().height = (int) (width/20);
            realScrollButtonsMenuLeft.get(i).getLayoutParams().width = realScrollButtonsMenuLeft.get(i).getLayoutParams().height;
            realScrollButtonsMenuRight.get(i).getLayoutParams().height = realScrollButtonsMenuLeft.get(i).getLayoutParams().height;
            realScrollButtonsMenuRight.get(i).getLayoutParams().width = realScrollButtonsMenuRight.get(i).getLayoutParams().height;
        }

        //-------------------------------------------------------------------------------------------------------------------------


        //Resizing
        mainActivity_constrLayoutGeoScroll.getLayoutParams().height = (int) (width/1.15);
        mainActivity_constrLayoutPkOkDkScroll.getLayoutParams().height = (int) (width/1.15);
        mainActivity_constrLayoutDMScroll.getLayoutParams().height = (int) (width/1.15);
        for(ConstraintLayout cL : menuLayouts)
        {
            cL.getLayoutParams().width = (int) (width/3)*2; //ширина
            //cL.getLayoutParams().height = (int) (height/2); //височина
            if(cL.getId() == R.id.mainActivity_constrLayoutMnFunkcii) {
                menuPictures.get(4).setOnClickListener(goToCalculateActivities); //изместваме кликъра на картинката вместо лейаута
                continue; //прескачаме кликъра за лейаута т.е. долния ред
            }
            if(cL.getId() == R.id.mainActivity_constrLayoutBool) {
                menuPictures.get(5).setOnClickListener(goToCalculateActivities); //изместваме кликъра на картинката вместо лейаута
                continue; //прескачаме кликъра за лейаута т.е. долния ред
            }
            cL.setOnClickListener(goToCalculateActivities); //listeners
        }
        for(ImageView iV : menuPictures)
        {
            iV.getLayoutParams().height = (int)(width/3);
        }

        ConstraintLayout mainActivityHeadLayout = findViewById(R.id.mainActivityHeadLayout);
        mainActivityHeadLayout.getLayoutParams().height = (int) (width/7);

        //---------------------------------------------------------- menu Parameters:
        ArrayList<TextView> menuParamTVs = new ArrayList<TextView>();
        menuParamTVs.add( (TextView) findViewById(R.id.mainActivity_TV_MnFun_Descr1) );
        menuParamTVs.add( (TextView) findViewById(R.id.mainActivity_TV_MnFun_Descr2) );
        menuParamTVs.add( (TextView) findViewById(R.id.mainActivity_TV_Bool_Descr1) );
        menuParamTVs.add( (TextView) findViewById(R.id.mainActivity_TV_Bool_Descr2) );
        for(TextView tv : menuParamTVs)
        {
            tv.getLayoutParams().width = (int) (menuLayouts.get(0).getLayoutParams().width/2.5);
        }

        ArrayList<ImageButton> menuParamIBs = new ArrayList<ImageButton>();
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_MnFun_CountX_Plus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_MnFun_CountX_Minus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_MnFun_CountF_Plus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_MnFun_CountF_Minus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_Bool_CountX_Plus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_Bool_CountX_Minus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_Bool_CountF_Plus) );
        menuParamIBs.add( (ImageButton) findViewById(R.id.mainActivity_IB_Bool_CountF_Minus) );
        for(ImageButton ib : menuParamIBs)
        {
            ib.getLayoutParams().width = (int) ((menuLayouts.get(0).getLayoutParams().width/2.5)/3);
            ib.getLayoutParams().height = (int) (ib.getLayoutParams().width/1.5);
            ib.setOnClickListener(changeParamsMenu);
        }


        //---------------------------------------------------------------------------

        //Resize text for tablets and large devices
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){

            ArrayList<TextView> textsForResize = new ArrayList<TextView>();
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_Title) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_Geom_Title) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewTitle1) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewDesc1) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewTitle2) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewDesc2) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewTitle4) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewDesc4) );

            textsForResize.add( (TextView) findViewById(R.id.mainActivity_KA_Title) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewTitle3) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewDesc3) );

            textsForResize.add( (TextView) findViewById(R.id.mainActivity_DM_Title) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewTitle5) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewDesc5) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_MnFun_Descr1) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_MnFun_Descr2) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_MnFun_CountX) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_MnFun_CountF) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewTitle6) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_textViewDesc6) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_Bool_Descr1) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_Bool_Descr2) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_Bool_CountX) );
            textsForResize.add( (TextView) findViewById(R.id.mainActivity_TV_Bool_CountF) );


            for(TextView tv : textsForResize)
            {
                float sp = (float) (tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp+8);
            }
        }else{
            // smaller device
        }

    }

    private View.OnClickListener goToCalculateActivities = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            switch(view.getId())
            {
                case R.id.mainActivity_constrLayoutVektorniKoordinati:
                    startActivity(new Intent(MainActivity.this, CoordinatesActivity.class));
                    break;
                case R.id.mainActivity_constrLayoutVektornaDuljina:
                    startActivity(new Intent(MainActivity.this, LengthActivity.class));
                    break;
                case R.id.mainActivity_constrLayoutPkOkDk:
                    startActivity(new Intent(MainActivity.this, PkOkDkActivity.class));
                    break;
                case R.id.mainActivity_imageViewDesc5: {
                    int countParam = Integer.parseInt(((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountX)).getText().toString());
                    int countFunc = Integer.parseInt(((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountF)).getText().toString());
                    Intent intent = new Intent(MainActivity.this, PulniMnojestvaActivity.class);
                    intent.putExtra("countParam", countParam);
                    intent.putExtra("countFunc", countFunc);
                    startActivity(intent);
                    break;
                }
                case R.id.mainActivity_imageViewDesc6: {
                    int countParam = Integer.parseInt(((TextView) findViewById(R.id.mainActivity_TV_Bool_CountX)).getText().toString());
                    int countFunc = Integer.parseInt(((TextView) findViewById(R.id.mainActivity_TV_Bool_CountF)).getText().toString());
                    Intent intent = new Intent(MainActivity.this, Bool1Activity.class);
                    intent.putExtra("countParam", countParam);
                    intent.putExtra("countFunc", countFunc);
                    startActivity(intent);
                    break;
                }
            }
        }
    };

    private View.OnClickListener scrollMenuLeft = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i=0; i<realScrollButtonsMenuLeft.size(); i++)
            {
                if(view.getId() == realScrollButtonsMenuLeft.get(i).getId())
                {
                    realScrollsMenu.get(i).arrowScroll(View.FOCUS_LEFT);
                    break;
                }
            }
        }
    };

    private View.OnClickListener scrollMenuRight = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i=0; i<realScrollButtonsMenuRight.size(); i++)
            {
                if(view.getId() == realScrollButtonsMenuRight.get(i).getId())
                {
                    realScrollsMenu.get(i).arrowScroll(View.FOCUS_RIGHT);
                    break;
                }
            }
        }
    };

    private View.OnClickListener changeParamsMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int current=0;
            switch (view.getId())
            {
                case R.id.mainActivity_IB_MnFun_CountX_Minus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountX)).getText().toString() );
                    if(current > 2) current --;
                    ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountX)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_MnFun_CountX_Plus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountX)).getText().toString() );
                    if(current < 3) current++;
                    ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountX)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_MnFun_CountF_Minus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountF)).getText().toString() );
                    if(current > 1) current--;
                    ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountF)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_MnFun_CountF_Plus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountF)).getText().toString() );
                    if(current < 3) current++;
                    ((TextView) findViewById(R.id.mainActivity_TV_MnFun_CountF)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_Bool_CountX_Minus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountX)).getText().toString() );
                    if(current > 2) current --;
                    ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountX)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_Bool_CountX_Plus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountX)).getText().toString() );
                    if(current < 3) current++;
                    ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountX)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_Bool_CountF_Minus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountF)).getText().toString() );
                    if(current > 1) current--;
                    ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountF)).setText(""+current);
                    break;
                case R.id.mainActivity_IB_Bool_CountF_Plus:
                    current = Integer.parseInt( ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountF)).getText().toString() );
                    if(current < 3) current++;
                    ((TextView) findViewById(R.id.mainActivity_TV_Bool_CountF)).setText(""+current);
                    break;
            }
        }
    };

}
