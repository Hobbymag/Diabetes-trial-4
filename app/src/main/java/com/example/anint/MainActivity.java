package com.example.anint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText inputNumber, inputNumber1,inputNumber2, inputNumber3, inputNumber4,inputNumber5, inputNumber6;
    Button inferButton;
    TextView outputNumber;
    Interpreter tflite;
    Float iN, iN1, iN2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNumber= (EditText) findViewById(R.id.inputNumber);
        inputNumber1= (EditText) findViewById(R.id.inputNumber1);
        inputNumber2= (EditText) findViewById(R.id.inputNumber2);
        inputNumber3= (EditText) findViewById(R.id.inputNumber3);
        inputNumber4= (EditText) findViewById(R.id.inputNumber4);
        inputNumber5= (EditText) findViewById(R.id.inputNumber5);
        inputNumber6= (EditText) findViewById(R.id.inputNumber6);
        inferButton = (Button) findViewById(R.id.inferButton);
        outputNumber= (TextView) findViewById(R.id.outputNumber);

        float iN1= Float.parseFloat(inputNumber1.getText().toString());
        float iN2= Float.parseFloat(inputNumber2.getText().toString());
        float iN= Float.parseFloat(inputNumber.getText().toString());
        float iN3= Float.parseFloat(inputNumber3.getText().toString());
        float iN4= Float.parseFloat(inputNumber4.getText().toString());
        float iN5= Float.parseFloat(inputNumber5.getText().toString());
        float iN6= Float.parseFloat(inputNumber6.getText().toString());



        try{
            tflite = new Interpreter((loadModelFile()));

        } catch (Exception ex){
            ex.printStackTrace();
        }
        inferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float prediction= doInference(inputNumber.getText().toString());
                outputNumber.setText(Float.toString(prediction));
                System.out.println("model loaded successfully");
            }

        });
    }

    public float doInference(String inputString){

        //float[] inputVal= new float[1];
        float[] inputVal = {iN1,iN2};
       inputVal[0]= Float.valueOf(inputString);
        float[][] outputval= new float[1][1];
        tflite.run(inputVal, outputval );
        //System.out.println("running successfully");
        float inferredValue = outputval[0][0];
        return inferredValue;


    }

        private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd( "converted_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset, declaredLength);
    }


}
