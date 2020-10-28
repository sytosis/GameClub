package com.example.gameclub.Ui.Gallery;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.Network.ChessGame;
import com.example.gameclub.Network.ClientNetwork;
import com.example.gameclub.Network.ServerNetwork;
import com.example.gameclub.R;

import java.util.ArrayList;
import java.util.List;


public class ChessFragment extends Fragment {
    Button resetBoardButton;
    private ChessViewModel chessViewModel;
    Button boardButton;
    Button chatCloseButton;
    Button chatOpenButton;
    Button sendChat;
    Button homeButton;
    LinearLayout wholeChatBox;
    EditText text;
    LinearLayout chatChessBox;
    View rootSave;
    ScrollView scrollViewChat;
    Boolean onWhite = true;
    private ChessGame chessGame;
    private Thread thread;
    private ClientNetwork client;
    private ServerNetwork server;
    private TextView networkBox;
    int knightMoves[][] = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        chessViewModel =
                ViewModelProviders.of(this).get(ChessViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_chess, container, false);
        rootSave = root;
        final Observer<List<Integer>> chessMove = new Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable final List<Integer> newMove) {
                // Update the UI, in this case, a TextView.
                System.out.println(newMove.toString());
                chessViewModel.selectChessPiece(newMove.get(0),newMove.get(1));
                chessViewModel.moveSelectedPiece(newMove.get(2),newMove.get(3));
                redrawBoard();
                onWhite = !onWhite;
            }

        };
        chessViewModel.getMove().observe(getViewLifecycleOwner(), chessMove);
        wholeChatBox = root.findViewById(R.id.whole_chat_box);
        resetBoardButton = root.findViewById(R.id.reset_board_button);
        chatCloseButton = root.findViewById(R.id.close_chat_button);
        chatOpenButton = root.findViewById(R.id.message_button);
        homeButton = root.findViewById(R.id.home_button);
        sendChat = root.findViewById(R.id.send_chat_button);
        chatChessBox = root.findViewById(R.id.chess_chat_box);
        scrollViewChat = root.findViewById(R.id.scrollview_chat);
        text = root.findViewById(R.id.chess_chat_id);
        chessGame = new ChessGame(chessViewModel, root);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChessFragment.this).navigate((R.id.action_nav_chess_to_nav_home));
            }
        });
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendChat = (MainActivity.currentUser.getFirstName() + ": " + text.getText());
                text.getText().clear();
                TextView tv = new TextView(getContext());
                tv.setText(sendChat);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                chatChessBox.addView(tv);
                scrollViewChat.fullScroll(View.FOCUS_DOWN);
            }
        });
        chatOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wholeChatBox.setVisibility(View.VISIBLE);
            }
        });
        chatCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wholeChatBox.setVisibility(View.INVISIBLE);
            }
        });
        serverConn();
        resetBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chessViewModel.setChessBoard();
                String[][] currentBoard = chessViewModel.getChessBoard();
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        final int newX = x;
                        final int newY = y;
                        String boardId = "board" + x + y;
                        int id = getResources().getIdentifier(boardId, "id", getActivity().getPackageName());
                        boardButton = root.findViewById(id);
                        boardButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //move the piece
                                if (AppCompatResources.getDrawable(getContext(),R.drawable.red).
                                        getConstantState().
                                        equals(((LayerDrawable)view.getBackground()).
                                                getDrawable(0).getConstantState())) {

                                    chessViewModel.moveSelectedPiece(newX,newY);
                                    redrawBoard();
                                    onWhite = !onWhite;

                                    ArrayList<Integer> array = new ArrayList<>();
                                    synchronized (chessGame) {
                                        array.add(0, chessViewModel.getSelectedPiece()[0]);
                                        array.add(1, chessViewModel.getSelectedPiece()[1]);
                                        array.add(2, newX);
                                        array.add(3, newY);
                                        chessGame.setGame(array);
                                        chessGame.notify();
                                    }
                                } else {
                                    redrawBoard();

                                    String id = view.getResources().getResourceEntryName(view.getId());
                                    System.out.println(id);
                                    id = id.substring(5);
                                    System.out.println(id);
                                    int x = Integer.parseInt(id.substring(0, 1));
                                    int y = Integer.parseInt(id.substring(1));
                                    String boardId = "board" + x + y;
                                    int boardFinder = getResources().getIdentifier(boardId, "id", getActivity().getPackageName());
                                    Button selectedBoard = root.findViewById(boardFinder);
                                    if (chessViewModel.selectChessPiece(x, y) && (onWhite && chessViewModel.getChessBoard()[x][y].contains("w") || !onWhite && chessViewModel.getChessBoard()[x][y].contains("b"))) {
                                        Drawable[] layers = new Drawable[2];
                                        try {
                                            LayerDrawable tempBackground = (LayerDrawable) selectedBoard.getBackground();
                                            layers[1] =  tempBackground.getDrawable(1);
                                        } catch (Exception e) {
                                            layers = new Drawable[1];
                                        }
                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.green);
                                        LayerDrawable pieceImage = new LayerDrawable(layers);
                                        selectedBoard.setBackground(pieceImage);

                                        String piece = chessViewModel.getChessBoard()[x][y];
                                        if (piece.contains("n")) {

                                            for (int i = 0; i < 8; i++) {
                                                int newX = x + knightMoves[i][0];
                                                int newY = y + knightMoves[i][1];
                                                if (newX < 8 && newX > -1 && newY < 8 && newY > -1) {
                                                    //check if the piece is same team and if so dont add it.
                                                    if (chessViewModel.getChessBoard()[newX][newY] != null) {
                                                        if (chessViewModel.getChessBoard()[x][y].contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("w")) {
                                                            continue;
                                                        } else if (chessViewModel.getChessBoard()[x][y].contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("b")) {
                                                            continue;
                                                        }
                                                    }

                                                    String newBoardId = "board" + newX + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            }
                                        } else if (piece.contains("wp")) {
                                            int newY = y + 1;
                                            if (y == 1) {
                                                //if its on the starting point
                                                if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                    String newBoardId = "board" + x + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                    newY = newY + 1;
                                                    if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                        newBoardId = "board" + x + newY;
                                                        newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    }
                                                }
                                            } else {
                                                //if its not on the starting point
                                                if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                    String newBoardId = "board" + x + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            }
                                            //check diagonals
                                            if (x != 7 && chessViewModel.getChessBoard()[x + 1][y + 1] != null) {
                                                if (!chessViewModel.getChessBoard()[x + 1][y + 1].contains("w")) {
                                                    int dX = x + 1;
                                                    int dY = y + 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            } else if (x != 0 && chessViewModel.getChessBoard()[x - 1][y + 1] != null) {
                                                if (!chessViewModel.getChessBoard()[x - 1][y + 1].contains("w")) {
                                                    int dX = x - 1;
                                                    int dY = y + 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            }
                                        } else if (piece.contains("bp")) {
                                            int newY = y - 1;
                                            if (y == 6) {
                                                //if its on the starting point
                                                if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                    String newBoardId = "board" + x + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                    newY = newY - 1;
                                                    if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                        newBoardId = "board" + x + newY;
                                                        newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    }
                                                }
                                            } else {
                                                //if its not on the starting point
                                                if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                    String newBoardId = "board" + x + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            }
                                            //check diagonals
                                            if (x != 0 && chessViewModel.getChessBoard()[x + 1][y - 1] != null) {
                                                if (chessViewModel.getChessBoard()[x + 1][y - 1].contains("w")) {
                                                    int dX = x + 1;
                                                    int dY = y - 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            } else if (x != 0 && chessViewModel.getChessBoard()[x - 1][y - 1] != null) {
                                                if (chessViewModel.getChessBoard()[x - 1][y - 1].contains("w")) {
                                                    int dX = x - 1;
                                                    int dY = y - 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    layers = new Drawable[2];
                                                    try {
                                                        LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                        layers[1] =  tempBackground.getDrawable(1);
                                                    } catch (Exception e) {
                                                        layers = new Drawable[1];
                                                    }
                                                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                    pieceImage = new LayerDrawable(layers);
                                                    newSelectedBoard.setBackground(pieceImage);
                                                }
                                            }
                                        } else if (piece.contains("k")) {
                                            for (int tempX = -1; tempX < 2; tempX++) {
                                                for (int tempY = -1; tempY < 2; tempY++) {
                                                    //continue if its the same position as the king already is
                                                    if (tempX == 0 && tempY == 0) {
                                                        continue;
                                                    } else {
                                                        int newX = x + tempX;
                                                        int newY = y + tempY;
                                                        if (newX < 8 && newX > -1 && newY < 8 && newY > -1) {
                                                            //continue if the potential square is occupied by a team.
                                                            if (chessViewModel.getChessBoard()[newX][newY] != null) {
                                                                if (chessViewModel.getChessBoard()[x][y].contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("b")) {
                                                                    continue;
                                                                } else if (chessViewModel.getChessBoard()[x][y].contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("w")) {
                                                                    continue;
                                                                }
                                                            }

                                                            String newBoardId = "board" + newX + newY;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (piece.contains("r")) {
                                            for (int tempX = 1; tempX < 8; tempX++) {
                                                int newX = x + tempX;
                                                if (newX < 8 ) {
                                                    if (chessViewModel.getChessBoard()[newX][y] == null && newX != x) {
                                                        String newBoardId = "board" + newX + y;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][y] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][y].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[newX][y].contains("w"))) {
                                                            String newBoardId = "board" + newX + y;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);

                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            for (int tempX = -1; tempX > -8 ; tempX--) {
                                                int newX = x + tempX;
                                                if (newX > -1) {
                                                    if (chessViewModel.getChessBoard()[newX][y] == null && newX != x) {
                                                        String newBoardId = "board" + newX + y;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][y] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][y].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[newX][y].contains("w"))) {
                                                            String newBoardId = "board" + newX + y;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);

                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            for (int tempY = 1; tempY < 8 ; tempY++) {
                                                int newY = y + tempY;
                                                if (newY < 8) {
                                                    if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                        String newBoardId = "board" + x + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[x][newY].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[x][newY].contains("w"))) {
                                                            String newBoardId = "board" + x + newY;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);

                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            for (int tempY = -1; tempY > -8 ; tempY--) {
                                                int newY = y + tempY;
                                                if (newY > -1) {
                                                    if (chessViewModel.getChessBoard()[x][newY] == null && newY != y) {
                                                        String newBoardId = "board" + x + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[x][newY].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[x][newY].contains("w"))) {
                                                            String newBoardId = "board" + x + newY;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);
                                                        }
                                                        break;
                                                    }

                                                }
                                            }
                                        }
                                        //bishops, checking all four diagonals
                                        else if (piece.contains("i")) {
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x + temp;
                                                int newY = y + temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x + temp;
                                                int newY = y - temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x - temp;
                                                int newY = y + temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x - temp;
                                                int newY = y - temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else if (piece.contains("q")) {
                                            //check diagonals and horizontals
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x + temp;
                                                int newY = y + temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x + temp;
                                                int newY = y - temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x - temp;
                                                int newY = y + temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int temp = 1; temp < 8; temp++) {
                                                int newX = x - temp;
                                                int newY = y - temp;
                                                if (newX > -1 && newX < 8 && newY > -1 && newY < 8) {
                                                    if (chessViewModel.getChessBoard()[newX][newY] == null && (newY != y && newX != x)) {
                                                        String newBoardId = "board" + newX + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][newY] == null) {
                                                            continue;
                                                        } else {
                                                            if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][newY].contains("b")) ||
                                                                    (piece.contains("b") && chessViewModel.getChessBoard()[newX][newY].contains("w"))) {
                                                                String newBoardId = "board" + newX + newY;
                                                                int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                                Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                                layers = new Drawable[2];
                                                                try {
                                                                    LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                    layers[1] =  tempBackground.getDrawable(1);
                                                                } catch (Exception e) {
                                                                    layers = new Drawable[1];
                                                                }
                                                                layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                                pieceImage = new LayerDrawable(layers);
                                                                newSelectedBoard.setBackground(pieceImage);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            for (int tempX = 1; tempX < 8; tempX++) {
                                                int newX = x + tempX;
                                                if (newX < 8 ) {
                                                    if (chessViewModel.getChessBoard()[newX][y] == null && newX != x) {
                                                        String newBoardId = "board" + newX + y;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][y] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][y].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[newX][y].contains("w"))) {
                                                            String newBoardId = "board" + newX + y;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);

                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            for (int tempX = -1; tempX > -8 ; tempX--) {
                                                int newX = x + tempX;
                                                if (newX > -1) {
                                                    if (chessViewModel.getChessBoard()[newX][y] == null && newX != x) {
                                                        String newBoardId = "board" + newX + y;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[newX][y] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[newX][y].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[newX][y].contains("w"))) {
                                                            String newBoardId = "board" + newX + y;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);

                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            for (int tempY = 1; tempY < 8 ; tempY++) {
                                                int newY = y + tempY;
                                                if (newY < 8) {
                                                    if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                        String newBoardId = "board" + x + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[x][newY].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[x][newY].contains("w"))) {
                                                            String newBoardId = "board" + x + newY;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);

                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            for (int tempY = -1; tempY > -8 ; tempY--) {
                                                int newY = y + tempY;
                                                if (newY > -1) {
                                                    if (chessViewModel.getChessBoard()[x][newY] == null && newY != y) {
                                                        String newBoardId = "board" + x + newY;
                                                        int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                        layers = new Drawable[2];
                                                        try {
                                                            LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                            layers[1] =  tempBackground.getDrawable(1);
                                                        } catch (Exception e) {
                                                            layers = new Drawable[1];
                                                        }
                                                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                        pieceImage = new LayerDrawable(layers);
                                                        newSelectedBoard.setBackground(pieceImage);
                                                    } else {
                                                        //if pieces are opposite teams, mark it as red too.
                                                        if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                            continue;
                                                        }
                                                        if ((piece.contains("w") && chessViewModel.getChessBoard()[x][newY].contains("b")) ||
                                                                (piece.contains("b") && chessViewModel.getChessBoard()[x][newY].contains("w"))) {
                                                            String newBoardId = "board" + x + newY;
                                                            int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                            Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                            layers = new Drawable[2];
                                                            try {
                                                                LayerDrawable tempBackground = (LayerDrawable) newSelectedBoard.getBackground();
                                                                layers[1] =  tempBackground.getDrawable(1);
                                                            } catch (Exception e) {
                                                                layers = new Drawable[1];
                                                            }
                                                            layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.red);
                                                            pieceImage = new LayerDrawable(layers);
                                                            newSelectedBoard.setBackground(pieceImage);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });

                        //draw
                        if (currentBoard[x][y] != null) {
                            redrawBoard();
                        }
                    }
                }
            }
        });
        return root;
    }

    public void redrawBoard() {
        Boolean wKing = false;
        Boolean bKing = false;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                String boardId = "board" + x + y;
                int id = getResources().getIdentifier(boardId, "id", getActivity().getPackageName());
                boardButton = rootSave.findViewById(id);

                if (chessViewModel.getChessBoard()[x][y] != null) {
                    String currentPiece = chessViewModel.getChessBoard()[x][y];
                    if (currentPiece == "wk") {
                        wKing = true;
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.wking);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "bk") {
                        bKing = true;
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.bking);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "bp") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.bpawn);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "wp") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.wpawn);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "bn") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.bknight);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "wn") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.wknight);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "bi") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.bbishop);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "wi") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.wbishop);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "bq") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.bqueen);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "wq") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.wqueen);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "wr") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.wrook);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    } else if (currentPiece == "br") {
                        Drawable[] layers = new Drawable[2];
                        layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                        layers[1] =  AppCompatResources.getDrawable(getContext(),R.drawable.brook);
                        LayerDrawable pieceImage = new LayerDrawable(layers);
                        boardButton.setBackground(pieceImage);
                    }

                } else {
                    Drawable[] layers = new Drawable[1];
                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                    LayerDrawable pieceImage = new LayerDrawable(layers);
                    boardButton.setBackground(pieceImage);
                }
                Drawable[] layers = new Drawable[2];
                if ((y - x) % 2 == 0) {
                    try {
                        LayerDrawable tempBackground = (LayerDrawable) boardButton.getBackground();
                        layers[1] =  tempBackground.getDrawable(1);
                    } catch (Exception e) {
                        layers = new Drawable[1];
                    }
                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.black);
                    LayerDrawable pieceImage = new LayerDrawable(layers);
                    boardButton.setBackground(pieceImage);
                } else {
                    try {
                        LayerDrawable tempBackground = (LayerDrawable) boardButton.getBackground();
                        layers[1] =  tempBackground.getDrawable(1);
                    } catch (Exception e) {
                        layers = new Drawable[1];
                    }
                    layers[0] = AppCompatResources.getDrawable(getContext(),R.drawable.white);
                    LayerDrawable pieceImage = new LayerDrawable(layers);
                    boardButton.setBackground(pieceImage);
                }
            }
        }
        if (!bKing) {
            blackLoss();
        }

        if(!wKing) {
            whiteLoss();
        }
    }

    public void blackLoss() {ArrayList<Integer> array = new ArrayList<>();
        synchronized (chessGame) {
            array.add(0, -1);
            array.add(1, -1);
            array.add(2, -1);
            array.add(3, -1);
            chessGame.setGame(array);
            chessGame.notify();
        }


    }

    public void whiteLoss() {
        ArrayList<Integer> array = new ArrayList<>();
        synchronized (chessGame) {
            array.add(0, -1);
            array.add(1, -1);
            array.add(2, -1);
            array.add(3, -1);
            chessGame.setGame(array);
            chessGame.notify();
        }

    }
    public void clientConn(View view) {
        System.out.println("here");
        client = new ClientNetwork();
        thread = new Thread(client);
        thread.start();

        return;

    }

    public void serverConn() {
        System.out.println("here");
        server = new ServerNetwork(chessGame);
        thread = new Thread(server);
        thread.start();

        return;

    }
}