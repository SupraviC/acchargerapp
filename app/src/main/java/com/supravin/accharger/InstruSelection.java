package com.supravin.accharger;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.supravin.accharger.Storage.SPlanguageCP1;

/**
 * Created by User on 29-01-2018.
 */

public class InstruSelection extends AppCompatActivity {
    private TextView txt_instrumorqu,textInstruction;

    private SPlanguageCP1 sPlanguageCP1;
    private Button btn_submitmode;
    private RadioGroup rg_mode;
    private RadioButton rb_mannual,rb_auto;
    private TextView txt_VersionNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.insruction_layout);
        sPlanguageCP1 = new SPlanguageCP1(InstruSelection.this);

        txt_VersionNo=findViewById(R.id.txt_VersionNo);

        try {
            txt_VersionNo.setVisibility(View.VISIBLE);
            txt_VersionNo.setText("V"+getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            txt_VersionNo.setVisibility(View.INVISIBLE);
        }
        rb_auto = findViewById(R.id.rb_auto);
        rb_mannual = findViewById(R.id.rb_mannual);
        rg_mode = findViewById(R.id.rg_mode);
        btn_submitmode = findViewById(R.id.btn_submitmode);
        txt_instrumorqu = findViewById(R.id.txt_instrumorqu);
        textInstruction = findViewById(R.id.textInstruction);

        setLanguage();
        txt_instrumorqu.setSelected(true);
        btn_submitmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedRadioButtonID = rg_mode.getCheckedRadioButtonId();

                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                    String selectedRadioButtonText = selectedRadioButton.getText().toString().trim();
                    String selectedRadioButtonHint = selectedRadioButton.getHint().toString();
                    String selectedRadioid = String.valueOf(selectedRadioButton.getId());

                    //Toast.makeText(LangSelection.this, "Text : "+selectedRadioButtonText + "\nid = "+selectedRadioid+"\n Hint :"+selectedRadioButtonHint, Toast.LENGTH_SHORT).show();
                    if (selectedRadioButtonHint.equals("a"))/*{

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                InstruSelection.this);
                        builder.setTitle("OCPP 1.6");
                        builder.setMessage("NTPC's server has not been configured yet, please try another option");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                        builder.show();
                    }*/{
                        goToOCPP();
                    }else if (selectedRadioButtonHint.equals("m")){
                      //  goToManual();
                    }
                }
                else{
                    Toast.makeText(InstruSelection.this, "Please select mode", Toast.LENGTH_SHORT).show();

                }
            }
        });

        goToOCPP();
    }


    public void goToOCPP(){
        startActivity(new Intent(InstruSelection.this, InitiatingActivityOcpp.class));
        finish();
    }
   /* public void goToManual(){
        startActivity(new Intent(InstruSelection.this, InitiatingActivityMannual.class));
        finish();
    }*/
    public void toastShow(){
        Toast.makeText(this, "Work in progress", Toast.LENGTH_SHORT).show();
    }
    public void setLanguage(){
        if (sPlanguageCP1.getlanguageCP1().equals("en")){
            textInstruction.setText(LangString.s18_E);
            rb_auto.setText(LangString.s19_E);
            rb_mannual.setText(LangString.s20_E);
            txt_instrumorqu.setText("Select mode here       Select mode here       Select mode here       Select mode here       Select mode here       Select mode here       Select mode here       ");
        }else if (sPlanguageCP1.getlanguageCP1().equals("hi")){
            textInstruction.setText(LangString.s18_H);
            rb_auto.setText(LangString.s19_H);
            rb_mannual.setText(LangString.s20_H);
            txt_instrumorqu.setText("कृपया यहां विकल्प चुनें       कृपया यहां विकल्प चुनें      कृपया यहां विकल्प चुनें       कृपया यहां विकल्प चुनें        कृपया यहां विकल्प चुनें       कृपया यहां विकल्प चुनें       कृपया यहां विकल्प चुनें       ");

        }
        else if (sPlanguageCP1.getlanguageCP1().equals("ma")){
            textInstruction.setText(LangString.s18_M);
            rb_auto.setText(LangString.s19_M);
            rb_mannual.setText(LangString.s20_M);
            txt_instrumorqu.setText("कृपया येथे पर्याय निवडा       कृपया येथे पर्याय निवडा       कृपया येथे पर्याय निवडा       कृपया येथे पर्याय निवडा       कृपया येथे पर्याय निवडा       कृपया येथे पर्याय निवडा       कृपया येथे पर्याय निवडा      ");

        }
    }
}