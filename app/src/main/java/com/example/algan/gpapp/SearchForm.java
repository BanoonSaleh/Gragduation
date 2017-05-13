package com.example.algan.gpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchForm extends AppCompatActivity {
    EditText coursebox;
    String Course, both1="", both2="", both3="" , both4="";
    String quali, mode, time1,time2, date, time3,time4,both5,both6,both7,both8,both9,both10;
    String mode2="", mode3="",mode4="", modeboth1="",modeboth2="";
    Spinner sp_quali, sp_mode, sp_time, sp_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);
        coursebox= (EditText) findViewById(R.id.course);
        final Button search=(Button) findViewById(R.id.go);


    }

    public void userLogin (View view)
    {
        // Course = coursebox.getText().toString();
        String method = "";

        sp_quali = (Spinner) findViewById(R.id.spinner1);
        sp_mode = (Spinner) findViewById(R.id.spinner2);
        sp_time = (Spinner) findViewById(R.id.spinner3);
        sp_date = (Spinner) findViewById(R.id.spinner4);

        quali = String.valueOf(sp_quali.getSelectedItem());
        mode = String.valueOf(sp_mode.getSelectedItem());
        time1 = String.valueOf(sp_time.getSelectedItem());
        date = String.valueOf(sp_date.getSelectedItem());


        if (time1.equals("1 year")) {
            time2 = "12 months";
            time3= "length "+time1;
            time4= "length "+time2;
        }
        if (time1.equals("2 years")) {
            time2 = "24 months";
            time3= "length "+time1;
            time4= "length "+time2;
        }



        if (mode.equals("Part Time")){
            mode="part time:";
            mode2 = "mode of study: part time";
            mode3= "study modes:part-time";
            mode4= "part-time:";

            modeboth1="mode of study: full time/part time";
            modeboth2= "study modes: full-time, part-time";

            both1="duration: "+time1+" part-time";
            both2= time1+" part-time";
            both3="part time "+time1;
            both4=time1+" part time";
            both5="duration/mode: "+time1+" part time";
            both6="part time: "+time1;
            both7="part time course";
            both8="part time program";
            both9="part-time course";
            both10="part-time program";

        }

        else if (mode.equals("Full Time")) {
            mode="full time:";
            mode2 = "mode of study: full time";
            mode3= "study modes: full-time";
            mode4="full-time:";

            modeboth1="mode of study: full time/part time";
            modeboth2= "study modes: full-time, part-time";

            both1="duration: "+time1+" full-time";
            both2= time1+" full-time";
            both3="full time "+time1;
            both4=time1+" full time";
            both5="duration/mode: "+time1+" full time";
            both6="full time: "+time1;
            both7="full time course";
            both8="full time program";
            both9="full-time course";
            both10="full-time program";

        }

        else if(mode.equals("Online")){
            mode="online";
            mode2="online distance learning";
            mode3="online learning";
            mode4="distance learning";

            modeboth1="online distance learning";
            modeboth2="online learning";

            both1="distance learning";
            both2= "distance learning";
            both3="distance learning";
            both4="online distance learning";
            both5="online distance learning";
            both6="online distance learning";
            both7="online distance learning";
            both8="online distance learning";
            both9="online distance learning";
            both10="online distance learning";

        }


        if (quali.equals("Taught"))
            method = "taughtCourse";
        else
            method = "researchCourse";




        if(coursebox.getText().toString().length()==0){
            coursebox.setError("Course Name is Required");
            coursebox.requestFocus();
        }


        else {
            Course = coursebox.getText().toString();
            SearchTask backgroundTask = new SearchTask(this);
            backgroundTask.execute(method, Course);

            Bundle options = new Bundle();
            options.putString("mode", mode);
            options.putString("mode2", mode2);
            options.putString("mode3", mode3);
            options.putString("mode4", mode4);
            options.putString("modeboth1", modeboth1);
            options.putString("modeboth2", modeboth2);

            options.putString("time1", time1);
            options.putString("time2", time2);
            options.putString("time3", time3);
            options.putString("time4", time4);

            options.putString("date", date);

            options.putString("both1", both1);
            options.putString("both2", both2);
            options.putString("both3", both3);
            options.putString("both4", both4);
            options.putString("both5", both5);
            options.putString("both6", both6);
            options.putString("both7", both7);
            options.putString("both8", both8);
            options.putString("both9", both9);
            options.putString("both10", both10);

            Intent intent = new Intent(SearchForm.this, Showall.class);
            intent.putExtras(options);
            startActivity(intent);
            finish();

        }

    }
}

