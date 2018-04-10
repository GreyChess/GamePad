package com.greychess.scnycq.letsbath;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.greychess.scnycq.letsbath.customComponent.GamePad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PadActivity extends Activity {

    public Socket socketInstance;
    final Integer port = 1717;
    final String KEY_UP = "up";
    final String KEY_DOWN = "down";
    final String KEY_LEFT = "left";
    final String KEY_RIGHT = "right";
    private ExecutorService threadPool;
    private String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);
        GamePad gamePad = findViewById(R.id.gamepad);
        gamePad.setOnDirectionListener(new GamePad.OnDirectionListener() {
            @Override
            public void onUp() {
                sendMessage(KEY_UP);
            }

            @Override
            public void onDown() {
                sendMessage(KEY_DOWN);
            }

            @Override
            public void onLeft() {
                sendMessage(KEY_LEFT);
            }

            @Override
            public void onRight() {
                sendMessage(KEY_RIGHT);
            }
        });
        threadPool = Executors.newCachedThreadPool();
        final EditText editText = findViewById(R.id.editText2);
        Button btnSubmit = findViewById(R.id.button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String socketAddr = editText.getText().toString();
                            socketInstance = new Socket(socketAddr, port);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void sendMessage(String str) {
        message = str;
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socketInstance.getOutputStream();
                    outputStream.write((message+"\n").getBytes("utf-8"));
                    outputStream.flush();
                    if(message == "close"){
                        outputStream.close();
                        socketInstance.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendMessage("close");
    }
}
