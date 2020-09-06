package com.example.gameclub.Games.Bingo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gameclub.R;

import java.util.ArrayList;
import java.util.List;

public class BingoFragment extends Fragment {

    private Bingo bingoGame;
    List<ImageView> bingoBoardImages = new ArrayList<>(25);

    public void fillBingoBoardImageList(View in) {
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball1));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball2));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball3));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball4));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball5));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball6));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball7));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball8));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball9));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball10));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball11));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball12));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball13));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball14));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball15));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball16));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball17));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball18));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball19));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball20));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball21));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball22));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball23));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball24));
        bingoBoardImages.add((ImageView) in.findViewById(R.id.Ball25));
    }

    public void setBoardColours() {

//        bingoBoardImages.get(0).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.red_ball, null));

//        int number;
//        for (int i = 0; i < 25; ++i) {
//            number = bingoGame.bingoBoard.get(i);
//
//            System.out.println("LOOP");
//
//            if (number < 16) {
//                bingoBoardImages.get(i).setImageDrawable();
//            } else if (number < 31) {
//                bingoBoardImages.get(i).setImageResource(R.drawable.blue_ball);
//            } else if (number < 45) {
//                bingoBoardImages.get(i).setImageResource(R.drawable.yellow_ball);
//            } else if (number < 61) {
//                bingoBoardImages.get(i).setImageResource(R.drawable.green_ball);
//            } else {
//                bingoBoardImages.get(i).setImageResource(R.drawable.purple_ball);
//            }
//        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        bingoGame = ViewModelProviders.of(this).get(Bingo.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        fillBingoBoardImageList(root);
        setBoardColours();

        Button rollBall = root.findViewById(R.id.roll_ball_button);
        Button resetButton = root.findViewById(R.id.new_list_button);

        return root;
    }
}
