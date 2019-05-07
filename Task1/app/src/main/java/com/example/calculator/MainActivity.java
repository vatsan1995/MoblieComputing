package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.text.format.Formatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private Button zero;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button add;
    private Button sub;
    private Button mul;
    private Button div;
    private Button equ;
    private Button clr;
    private Button dec;
    private TextView info;
    private TextView res;
    private final char ADDITION = '+';
    private final char SUBTRACTION = '-';
    private final char MULTIPLICATION = '*';
    private final char DIVISION = '/';
    private final char EQUAL = 0;
    private double val1 = Double.NaN;
    private double val2;
    private char OPERATION;
    NumberFormat formatter = new DecimalFormat("##.###");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetupUIViews();

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "0");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "6");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewCompute();
                info.setText(info.getText().toString() + "9");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!info.getText().toString().isEmpty()) || ((!res.getText().toString().isEmpty()) && ((info.getText().toString().isEmpty())))) {
                    res.setText(null);
                    compute();
                    OPERATION = ADDITION;
                    info.setText(String.valueOf(formatter.format(val1)) + "+");
                }
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OPERATION == MULTIPLICATION) {
                    res.setText("Cannot * by -ve value.");
                }
                else if(OPERATION == DIVISION){
                    res.setText("Cannot / by -ve value.");
                }
                else {
                    if ((res.getText().toString().isEmpty()) && (info.getText().toString().isEmpty())) {
                        info.setText("-");
                    } else {
                        res.setText(null);
                        compute();
                        OPERATION = SUBTRACTION;
                        info.setText(String.valueOf(formatter.format(val1)) + "-");
                    }
                }
            }
        });

        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!info.getText().toString().isEmpty()) || ((!res.getText().toString().isEmpty()) && ((info.getText().toString().isEmpty())))) {
                    res.setText(null);
                    compute();
                    OPERATION = MULTIPLICATION;
                    info.setText(String.valueOf(formatter.format(val1)) + "*");
                }
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!info.getText().toString().isEmpty()) || ((!res.getText().toString().isEmpty()) && ((info.getText().toString().isEmpty())))) {
                    res.setText(null);
                    compute();
                    OPERATION = DIVISION;
                    info.setText(String.valueOf(formatter.format(val1)) + "/");
                }
            }
        });

        equ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compute();
                if((OPERATION == DIVISION) && (val2==0)) {
                    res.setText("Cannot divide by 0");
                    val1 = Double.NaN;
                    val2 = Double.NaN;
                }
                else {
                    res.setText(String.valueOf(formatter.format(val1)));
                }
                info.setText(null);
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getText().length() > 0){
                    CharSequence name = info.getText().toString();
                    info.setText(name.subSequence(0,name.length()-1));
                }
                else {
                    val1 = Double.NaN;
                    val2 = Double.NaN;
                    info.setText(null);
                    res.setText(null);
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!info.getText().toString().isEmpty()) {
                    CharSequence name = info.getText();
                    String bar = name.toString();
                    String lastChar = bar.substring(bar.length() - 1, bar.length());
                    String dec = ".";
                    if (!lastChar.equals(dec)) {
                        checkNewCompute();
                        info.setText(info.getText().toString() + ".");
                    }
                }
            }
        });
    }

    private void SetupUIViews() {
        zero = findViewById(R.id.btn0);
        one = findViewById(R.id.btn1);
        two = findViewById(R.id.btn2);
        three = findViewById(R.id.btn3);
        four = findViewById(R.id.btn4);
        five = findViewById(R.id.btn5);
        six = findViewById(R.id.btn6);
        seven = findViewById(R.id.btn7);
        eight = findViewById(R.id.btn8);
        nine = findViewById(R.id.btn9);
        add = findViewById(R.id.btnadd);
        sub = findViewById(R.id.btnsub);
        mul = findViewById(R.id.btnmul);
        div = findViewById(R.id.btndiv);
        equ = findViewById(R.id.btneql);
        clr = findViewById(R.id.btnclr);
        dec = findViewById(R.id.btndec);
        info = findViewById(R.id.tvopn);
        res = findViewById(R.id.tvres);

    }

    private void compute() {
        String[] values;
        if(!Double.isNaN(val1)) {
            if(!info.getText().toString().isEmpty()) {
                if(val1 > 0) {
                    values = info.getText().toString().split("[\\-+*/]");
                }
                else {
                    values = info.getText().toString().substring(1).split("[\\-+*/]");
                }
                val2 = Double.parseDouble(values[1]);

                switch (OPERATION) {
                    case ADDITION:
                        val1 = val1 + val2;
                        break;
                    case SUBTRACTION:
                        val1 = val1 - val2;
                        break;
                    case MULTIPLICATION:
                        val1 = val1 * val2;
                        break;
                    case DIVISION:
                        val1 = val1 / val2;
                        break;
                    case EQUAL:
                        break;
                }
            }
        }
        else {
            val1 = Double.parseDouble(info.getText().toString());
        }
    }

    private void checkNewCompute() {
        if(info.getText().toString().isEmpty()) {
            if(!res.getText().toString().isEmpty()) {
                res.setText(null);
                val1 = Double.NaN;
                val2 = Double.NaN;
            }
        }
    }
}
