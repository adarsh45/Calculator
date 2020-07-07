package com.example.calculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView input;

    double firstNum, secondNum;
    int firstNumLength;
    String operation;
    boolean singleNumberPresent = false;

    String[] operations = {"+", "−", "×", "÷"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.edit_calculation);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickDigit(View view){
//        digit buttons -> 1,2,3... are clicked
        Button button = (Button) view;
        input.append(button.getText());
    }

    public void onClickOperation(View view){
//        operations like +,-,*,/ are clicked
        String wholeText;

        String strFirstNum = input.getText().toString();
        if (TextUtils.isEmpty(strFirstNum)){
            input.setError("Please enter a number first!");
            return;
        }

//        check if double operation signs are present like +-, *+ ,etc.
        for (String op: operations){
            if (strFirstNum.substring(strFirstNum.length()-1).equals(op)){
                input.setError("Invalid operation!");
                return;
            }
        }

        if (!singleNumberPresent){
//            no number is present in input
            firstNumLength = strFirstNum.length();
            firstNum = Double.parseDouble(strFirstNum);
            singleNumberPresent = true;
            operation = ((Button) view).getText().toString();
        } else {
//            single number is present in input and now adding operation
            wholeText = input.getText().toString();
            String strSecondNum = wholeText.substring(firstNumLength+1);
            secondNum = Double.parseDouble(strSecondNum);
//            get result directly in form of string
            String strResult = calculate(firstNum, secondNum, operation);
            input.setText(strResult);
            firstNum = Double.parseDouble(strResult);
            firstNumLength = strResult.length();
        }

        operation = ((Button) view).getText().toString();
        input.append(operation);
    }

    public void onClickResult(View view){
//        " = " button clicked
//        check if only operation is there without second number
        String wholeText = input.getText().toString();

        if (TextUtils.isEmpty(wholeText)){
            input.setError("Please enter a number first!");
            return;
        }

        for (String op: operations){
            if (wholeText.substring(wholeText.length()-1).equals(op)){
                input.setError("Invalid operation!");
                return;
            }
        }

        if (TextUtils.isDigitsOnly(wholeText)){
            input.setText(wholeText);
            return;
        }
        String strSecondNum = wholeText.substring(firstNumLength+1);
        double secondNum = Double.parseDouble(strSecondNum);
        String strResult = calculate(firstNum, secondNum, operation);
        input.setText(strResult);
        firstNum = Double.parseDouble(strResult);
        firstNumLength = strResult.length();
        singleNumberPresent = false;
    }

    public void onClickClear(View view){
        if (view.getId()==R.id.btn_del){
//            backspace btn clicked
            String wholeText = input.getText().toString();
            String backSpaceText = wholeText.substring(0,wholeText.length()-1);
            if (backSpaceText.equals("")){
                singleNumberPresent = false;
            }
            input.setText(backSpaceText);
        } else {
//        AC btn clicked
            input.setText("");
            singleNumberPresent = false;
        }
    }

    public void onClickPercent(View view){
        String wholeText = input.getText().toString();
        if (TextUtils.isEmpty(wholeText)){
            input.setError("Please enter valid number!");
            return;
        }
        if (TextUtils.isDigitsOnly(wholeText)){
//            single number is present
            double num = Double.parseDouble(wholeText);
            input.setText(String.valueOf(num/100));
        } else {
//            two numbers and one operation is present
//            we will convert second number to its percentage e.g. 80=>0.8
            String firstNum = wholeText.substring(0,firstNumLength);
            String strSecondNum = wholeText.substring(firstNumLength+1);
            double secondNum = Double.parseDouble(strSecondNum);
            String finalText = firstNum + operation + (secondNum/100);
            input.setText(finalText);
        }
    }

    public String calculate(double num1, double num2, String baseOperation){
        double result = 0;
        switch (baseOperation){
            case "+":
                result = num1 + num2;
                break;
            case "−":
                result = num1 - num2;
                break;
            case "×":
                result = num1 * num2;
                break;
            case "÷":
                result = num1 / num2;
                break;
        }
        int x = (int) result;
        String strResult;
        if (x == result){
            strResult = String.valueOf(x);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            strResult = decimalFormat.format(result);
        }

        return strResult;
    }

}