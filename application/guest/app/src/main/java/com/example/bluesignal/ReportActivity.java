package com.example.bluesignal;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class ReportActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup radioGroup1,radioGroup2,radioGroup3,radioGroup4,radioGroup5,radioGroup6,radioGroup7,radioGroup8,radioGroup9,radioGroup10,radioGroup11;

    public String[] check=new String[12];
    public String[] equal={"no1","no2","no3","no4","no5","no6","no7","no8","no9","no10","no11","complete"};
    String blank="blank";
    public String[] reason=new String[11];
    GuestInfo guestInfo = GuestInfo.getInstance();
    ArrayList<String> list = new ArrayList<String>();
    private TextView question1,question2,question3,question4,question5,question6,question7,question8,question9,question10,question11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        question1=(TextView)findViewById(R.id.question1);
        question2=(TextView)findViewById(R.id.question2);
        question3=(TextView)findViewById(R.id.question3);
        question4=(TextView)findViewById(R.id.question4);
        question5=(TextView)findViewById(R.id.question5);
        question6=(TextView)findViewById(R.id.question6);
        question7=(TextView)findViewById(R.id.question7);
        question8=(TextView)findViewById(R.id.question8);
        question9=(TextView)findViewById(R.id.question9);
        question10=(TextView)findViewById(R.id.question10);
        question11=(TextView)findViewById(R.id.question11);



        radioGroup1=(RadioGroup)findViewById((R.id.checkitem_radiogroup1));
        radioGroup2=(RadioGroup)findViewById((R.id.checkitem_radiogroup2));
        radioGroup3=(RadioGroup)findViewById((R.id.checkitem_radiogroup3));
        radioGroup4=(RadioGroup)findViewById((R.id.checkitem_radiogroup4));
        radioGroup5=(RadioGroup)findViewById((R.id.checkitem_radiogroup5));
        radioGroup6=(RadioGroup)findViewById((R.id.checkitem_radiogroup6));
        radioGroup7=(RadioGroup)findViewById((R.id.checkitem_radiogroup7));
        radioGroup8=(RadioGroup)findViewById((R.id.checkitem_radiogroup8));
        radioGroup9=(RadioGroup)findViewById((R.id.checkitem_radiogroup9));
        radioGroup10=(RadioGroup)findViewById((R.id.checkitem_radiogroup10));
        radioGroup11=(RadioGroup)findViewById((R.id.checkitem_radiogroup11));



        radioGroup1.setOnCheckedChangeListener(this);
        radioGroup2.setOnCheckedChangeListener(this);
        radioGroup3.setOnCheckedChangeListener(this);
        radioGroup4.setOnCheckedChangeListener(this);
        radioGroup5.setOnCheckedChangeListener(this);
        radioGroup6.setOnCheckedChangeListener(this);
        radioGroup7.setOnCheckedChangeListener(this);
        radioGroup8.setOnCheckedChangeListener(this);
        radioGroup9.setOnCheckedChangeListener(this);
        radioGroup10.setOnCheckedChangeListener(this);
        radioGroup11.setOnCheckedChangeListener(this);

    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    String currentDateandTime = sdf.format(new Date());
    public void onClick(View view){

        if(Arrays.equals(check,equal)){Response.Listener<String> responseListener=new Response.Listener<String>() {//volley
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
                    boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
                    if (success) {//회원등록 성공한 경우
                        Intent intent  = new Intent(ReportActivity.this,VisitCardActivity.class);
                        guestInfo.setReport(currentDateandTime);
                        startActivity(intent);
                    }
                    else{//회원등록 실패한 경우
                        Toast.makeText(getApplicationContext(),"회원 등록 실패",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
            //서버로 volley를 이용해서 요청을 함
            ReportRequest registerRequest=new ReportRequest(guestInfo.getId(),currentDateandTime,responseListener);
            RequestQueue queue= Volley.newRequestQueue(ReportActivity.this);
            queue.add(registerRequest);

        }
        else if(Arrays.asList(check).contains(blank)){
            Toast.makeText(getApplicationContext(),"미기입", Toast.LENGTH_LONG).show();
        }
        else if(!Arrays.equals(check,equal)){
           /* for(String s:reason){
                if(!s.equals(""))
                    list.add(s);
            }
            Toast.makeText(getApplicationContext(),list.toString(), Toast.LENGTH_LONG).show();*/
            AlertDialog.Builder nopop=new AlertDialog.Builder(this);
            nopop.setMessage("건물출입이 불가능합니다.");
            nopop.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent nointent= new Intent(ReportActivity.this,MainActivity.class);
                    startActivity(nointent);
                }
            });

            nopop.show();
        }



    }


    public void onCheckedChanged(RadioGroup group,int checkId) {
        if(group==radioGroup1){
            if( checkId==R.id.checkitem_radioButtonno1){
                check[0]="no1";
                reason[0]="";

            }
            else if( checkId==R.id.checkitem_radioButtonyes1){
                check[0]="yes1";
                reason[0]=question1.getText().toString();;


            }
        }

        if(group==radioGroup2){
            if( checkId==R.id.checkitem_radioButtonno2){
                check[1]="no2";
                reason[1]="";

            }
            else if( checkId==R.id.checkitem_radioButtonyes2){
                check[1]="yes2";
                reason[1]=question2.getText().toString();;

            }
        }


        if(group==radioGroup3){
            if( checkId==R.id.checkitem_radioButtonno3){
                check[2]="no3";
                reason[2]="";

            }
            else if( checkId==R.id.checkitem_radioButtonyes3){
                reason[2]=question3.getText().toString();;
                check[2]="yes3";
            }
        }
        if(group==radioGroup4){
            if( checkId==R.id.checkitem_radioButtonno4){
                check[3]="no4";
                reason[3]="";

            }
            else if( checkId==R.id.checkitem_radioButtonyes4){
                check[3]="yes4";
                reason[3]=question4.getText().toString();;

            }
        }
        if(group==radioGroup5){
            if( checkId==R.id.checkitem_radioButtonno5){
                reason[4]="";
                check[4]="no5";

            }
            else if( checkId==R.id.checkitem_radioButtonyes5){
                reason[4]=question5.getText().toString();;
                check[4]="yes5";

            }
        }
        if(group==radioGroup6){
            if( checkId==R.id.checkitem_radioButtonno6){
                reason[5]="";
                check[5]="no6";

            }
            else if( checkId==R.id.checkitem_radioButtonyes6){
                check[5]="yes6";
                reason[5]=question6.getText().toString();;

            }
        }
        if(group==radioGroup7){
            if( checkId==R.id.checkitem_radioButtonno7){
                reason[6]="";
                check[6]="no7";

            }
            else if( checkId==R.id.checkitem_radioButtonyes7){
                check[6]="yes7";
                reason[6]=question7.getText().toString();;

            }
        }
        if(group==radioGroup8){
            if( checkId==R.id.checkitem_radioButtonno8){
                reason[7]="";
                check[7]="no8";

            }
            else if( checkId==R.id.checkitem_radioButtonyes8){
                check[7]="yes8";
                reason[7]=question8.getText().toString();


            }
        }
        if(group==radioGroup9){
            if( checkId==R.id.checkitem_radioButtonno9){
                check[8]="no9";
                reason[8]="";

            }
            else if( checkId==R.id.checkitem_radioButtonyes9){
                check[8]="yes9";
                reason[8]=question9.getText().toString();

            }
        }
        if(group==radioGroup10){
            if( checkId==R.id.checkitem_radioButtonno10){
                check[9]="no10";
                reason[9]="";

            }
            else if( checkId==R.id.checkitem_radioButtonyes10){
                check[9]="yes10";
                reason[9]=question10.getText().toString();

            }
        }
        if(group==radioGroup11){
            if( checkId==R.id.checkitem_radioButtonno11){
                reason[10]="";
                check[10]="no11";

            }
            else if( checkId==R.id.checkitem_radioButtonyes11){
                check[10]="yes11";
                reason[10]=question11.getText().toString();

            }
        }
        if(radioGroup1.getCheckedRadioButtonId()==-1 || radioGroup2.getCheckedRadioButtonId()==-1||radioGroup3.getCheckedRadioButtonId()==-1
                ||radioGroup4.getCheckedRadioButtonId()==-1||radioGroup5.getCheckedRadioButtonId()==-1||radioGroup6.getCheckedRadioButtonId()==-1||
                radioGroup7.getCheckedRadioButtonId()==-1||radioGroup8.getCheckedRadioButtonId()==-1||radioGroup9.getCheckedRadioButtonId()==-1||
                radioGroup10.getCheckedRadioButtonId()==-1||radioGroup11.getCheckedRadioButtonId()==-1) {
            check[11] = "blank";
        }else{
            check[11]="complete";
        }
    }

}