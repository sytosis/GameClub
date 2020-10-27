package com.example.gameclub.Games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gameclub.Network.ClientNetwork;
import com.example.gameclub.Network.ServerNetwork;
import com.example.gameclub.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class BingoFragment extends Fragment {

    public Bingo bingoGame;
    View root;
    List<ImageView> bingoBoardImages = new ArrayList<>();
    List<TextView> bingoBoardText = new ArrayList<>();
    private Thread thread;
    private ClientNetwork client;
    private ServerNetwork server;
    private TextView networkBox;

    public void fillBingoBoardTextList() {
        bingoBoardText.add((TextView) root.findViewById(R.id.Text1));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text2));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text3));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text4));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text5));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text6));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text7));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text8));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text9));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text10));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text11));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text12));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text13));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text14));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text15));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text16));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text17));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text18));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text19));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text20));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text21));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text22));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text23));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text24));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text25));
    }

    public void fillBingoBoardImageList() {
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball1));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball2));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball3));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball4));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball5));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball6));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball7));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball8));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball9));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball10));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball11));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball12));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball13));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball14));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball15));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball16));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball17));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball18));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball19));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball20));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball21));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball22));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball23));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball24));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball25));
    }

    public void setBallColour(ImageView ball, int number) {
        if (number < 16) {
            ball.setImageResource(R.drawable.pink_ball);
        }
        else if (number < 31) {
            ball.setImageResource(R.drawable.blue_ball);
        }
        else if (number < 45) {
            ball.setImageResource(R.drawable.yellow_ball);
        }
        else if (number < 61) {
            ball.setImageResource(R.drawable.green_ball);
        }
        else {
            ball.setImageResource(R.drawable.purple_ball);
        }
    }


    public void displayBall(int number) {
        setBallColour((ImageView) root.findViewById(R.id.RollBall), number);
        ((TextView) root.findViewById(R.id.RollText)).setText(String.valueOf(number));
    }

    public void removeBall(int number) {
        // Robbie magic please
    }

    public void bingoTurn() {
        int number = bingoGame.getBall();
        displayBall(number);
        bingoGame.checkWin(number);
    }

    public void setBoardText() {

        for (int i = 0; i < 25; ++i) {
            bingoBoardText.get(i).setText(String.valueOf(bingoGame.bingoBoard.get(i)));
        }
    }

    public void setBoardColours() {

        int number;
        for (int i = 0; i < 25; ++i) {
            number = bingoGame.bingoBoard.get(i);
            setBallColour(bingoBoardImages.get(i), bingoGame.bingoBoard.get(i));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        bingoGame = ViewModelProviders.of(this).get(Bingo.class);
        root = inflater.inflate(R.layout.fragment_bingo, container, false);

        fillBingoBoardTextList();
        setBoardText();

        fillBingoBoardImageList();
        setBoardColours();

        Button rollBall = root.findViewById(R.id.roll_ball_button);

        rollBall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bingoTurn();
            }
        });

        Button resetButton = root.findViewById(R.id.new_list_button);
        final Button button1 = root.findViewById(R.id.serv);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                View newView = root.findViewById(R.id.servtext);
                server = new ServerNetwork((TextView) newView);
                thread = new Thread(server);
                thread.start();

                return;
            }
        });

        final Button button2 = root.findViewById(R.id.client);
         button2.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

                 client = new ClientNetwork();
                 thread = new Thread(client);
                 thread.start();

                 return;

             }
         });

//         Button homeButton = root.findViewById(R.id.Bingo_Home_Button);
//        homeButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//
//
//                return;
//            }
//        });

        return root;
    }
    public void clientConn(View view) {
        System.out.println("here");
        client = new ClientNetwork();
        thread = new Thread(client);
        thread.start();
        return;

    }

    public void serverConn(View view) {
        System.out.println("here");
        server = new ServerNetwork((TextView) view);
        thread = new Thread(server);
        thread.start();
        return;

    }



}
