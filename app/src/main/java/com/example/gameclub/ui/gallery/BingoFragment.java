package com.example.gameclub.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gameclub.R;
import com.example.gameclub.Network.ServerNetwork;
import com.example.gameclub.Network.ClientNetwork;

public class BingoFragment extends Fragment {

    private BingoViewModel bingoViewModel;
    private Thread thread;
    private ClientNetwork client;
    private ServerNetwork server;
    private TextView networkBox;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bingoViewModel =
                ViewModelProviders.of(this).get(BingoViewModel.class);
        root = inflater.inflate(R.layout.fragment_bingo, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        bingoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Button rollBall = root.findViewById(R.id.roll_ball_button);
        Button resetButton = root.findViewById(R.id.new_list_button);
        final TextView ballNumberText = root.findViewById(R.id.ball_number);
        rollBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = bingoViewModel.callBall();
                ballNumberText.setText(String.valueOf(number));
                if (bingoViewModel.checkBall(number)) {
                    textView.setText(bingoViewModel.getBingoBoard().toString());
                }

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoViewModel.resetBingoBackground();
                textView.setText(bingoViewModel.getBingoBoard().toString());
            }
        });
        return root;
    }
    public void clientConn(View view) {
        System.out.println("here");
        client = new ClientNetwork();
        thread = new Thread(client);
        thread.start();
        //showMessage("Connected to Server...");
        root.findViewById(R.id.cli).setVisibility(View.GONE);
        return;

    }

    public void serverConn(View view) {
        System.out.println("here");
        server = new ServerNetwork((TextView) view);
        thread = new Thread(server);
        thread.start();
        showMessage("Connected to Server...");
        root.findViewById(R.id.serv).setVisibility(View.GONE);
        return;

    }


    public void showMessage(final String message) {

        networkBox.setText(message);

    }
}