package com.example.gameclub.ui.gallery;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ChessFragment extends Fragment {
    Button resetBoardButton;
    private ChessViewModel chessViewModel;
    Button boardButton;
    View rootSave;
    int knightMoves[][] = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        chessViewModel =
                ViewModelProviders.of(this).get(ChessViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_chess, container, false);
        rootSave = root;
        resetBoardButton = root.findViewById(R.id.reset_board_button);
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
                                if (((ColorDrawable)view.getBackground()).getColor() == Color.RED) {
                                    chessViewModel.moveSelectedPiece(newX,newY);
                                    redrawBoard();
                                } else {
                                    redrawBoard();
                                    String id = view.getResources().getResourceEntryName(view.getId());
                                    id = id.substring(5);
                                    int x = Integer.parseInt(id.substring(0, 1));
                                    int y = Integer.parseInt(id.substring(1));
                                    String boardId = "board" + x + y;
                                    int boardFinder = getResources().getIdentifier(boardId, "id", getActivity().getPackageName());
                                    Button selectedBoard = root.findViewById(boardFinder);
                                    if (chessViewModel.selectChessPiece(x, y)) {
                                        selectedBoard.setBackgroundColor(Color.GREEN);
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
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
                                                    newY = newY + 1;
                                                    if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                        newBoardId = "board" + x + newY;
                                                        newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        newSelectedBoard = root.findViewById(newBoardFinder);
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
                                                    }
                                                }
                                            } else {
                                                //if its not on the starting point
                                                if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                    String newBoardId = "board" + x + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
                                                }
                                            } else if (x != 0 && chessViewModel.getChessBoard()[x - 1][y + 1] != null) {
                                                if (!chessViewModel.getChessBoard()[x - 1][y + 1].contains("w")) {
                                                    int dX = x - 1;
                                                    int dY = y + 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
                                                    newY = newY - 1;
                                                    if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                        newBoardId = "board" + x + newY;
                                                        newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                        newSelectedBoard = root.findViewById(newBoardFinder);
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
                                                    }
                                                }
                                            } else {
                                                //if its not on the starting point
                                                if (chessViewModel.getChessBoard()[x][newY] == null) {
                                                    String newBoardId = "board" + x + newY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
                                                }
                                            }
                                            //check diagonals
                                            if (x != 7 && chessViewModel.getChessBoard()[x + 1][y - 1] != null) {
                                                if (!chessViewModel.getChessBoard()[x + 1][y - 1].contains("w")) {
                                                    int dX = x + 1;
                                                    int dY = y - 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
                                                }
                                            } else if (x != 0 && chessViewModel.getChessBoard()[x - 1][y - 1] != null) {
                                                if (!chessViewModel.getChessBoard()[x - 1][y - 1].contains("b")) {
                                                    int dX = x - 1;
                                                    int dY = y - 1;
                                                    String newBoardId = "board" + dX + dY;
                                                    int newBoardFinder = getResources().getIdentifier(newBoardId, "id", getActivity().getPackageName());
                                                    Button newSelectedBoard = root.findViewById(newBoardFinder);
                                                    newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);

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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);

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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);

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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                                newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);

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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);

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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);

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
                                                        newSelectedBoard.setBackgroundColor(Color.RED);
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
                                                            newSelectedBoard.setBackgroundColor(Color.RED);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ((y - x) % 2 == 0) {
                                                selectedBoard.setBackgroundColor(Color.parseColor("#FF424242"));
                                            } else {
                                                selectedBoard.setBackgroundColor(Color.WHITE);
                                            }
                                        }
                                    }
                                }
                            }
                        });

                        //draw
                        if (currentBoard[x][y] != null) {
                            boardButton.setText(currentBoard[x][y]);
                        }
                    }
                }
            }
        });
        return root;
    }

    public void redrawBoard() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                String boardId = "board" + x + y;
                int id = getResources().getIdentifier(boardId, "id", getActivity().getPackageName());
                boardButton = rootSave.findViewById(id);
                if ((y - x) % 2 == 0) {
                    boardButton.setBackgroundColor(Color.parseColor("#FF424242"));
                } else {
                    boardButton.setBackgroundColor(Color.WHITE);
                }
                if (chessViewModel.getChessBoard()[x][y] != null) {
                    boardButton.setText(chessViewModel.getChessBoard()[x][y]);
                } else {
                    boardButton.setText("");
                }
            }
        }
    }
}