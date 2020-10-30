package com.example.gameclub.Ui.Friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;
import com.example.gameclub.Users.User;

import java.util.List;

public class FriendsListFragment extends Fragment {

    private Button homeButton;
    static User user1 = new User();
    static User user2 = new User();
    static User user3 = new User();
    static User user4 = new User();
    static int userNum = 0;

    public FriendsListFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.friends_list, container, false);
        homeButton = root.findViewById(R.id.friend_list_back_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(FriendsListFragment.this).navigate((R.id.action_friendsListFragment_to_nav_home));
            }
        });

        // Get text views
        TextView first = root.findViewById(R.id.textView7);
        TextView second = root.findViewById(R.id.person2);
        TextView third = root.findViewById(R.id.person3);
        TextView fourth = root.findViewById(R.id.person4);

        // Get linear layouts
        LinearLayout friend1 = root.findViewById(R.id.friend_layout_1);
        LinearLayout friend2 = root.findViewById(R.id.friend_layout_2);
        LinearLayout friend3 = root.findViewById(R.id.friend_layout_3);
        LinearLayout friend4 = root.findViewById(R.id.friend_layout_4);

        List<String> friendList = MainActivity.currentUser.getFriendList();

        // Check friend list size
        int size = friendList.size();
        if (friendList.get(0).equals("")) {
            size = 0;
        }
        System.out.println("Size is: " + size);

        // Display friends depending on number of friends
        if (size <= 0) {
            friend1.setVisibility(View.INVISIBLE);
            friend2.setVisibility(View.INVISIBLE);
            friend3.setVisibility(View.INVISIBLE);
            friend4.setVisibility(View.INVISIBLE);
        }
        if (size > 0) {
            first.setText(friendList.get(0));
            Button firstPic = root.findViewById(R.id.pic);
            firstPic.setBackgroundResource(getPic(friendList.get(0)));
            user1.setId("900");
            user1.setFirst(friendList.get(0));
            user1.setLast("Smith");
            user1.setInterest("Fishing");
            user1.setCountry("Australia");
            user1.setEmail("smithfishing@hotmail.com");
            friend1.setVisibility(View.VISIBLE);
            friend2.setVisibility(View.INVISIBLE);
            friend3.setVisibility(View.INVISIBLE);
            friend4.setVisibility(View.INVISIBLE);
            firstPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNum = 1;
                    NavHostFragment.findNavController(FriendsListFragment.this).navigate((R.id.action_friendsListFragment_to_fragmentProfileOther));
                }
            });
        }

        if (size > 1) {
            second.setText(friendList.get(1));
            Button secondPic = root.findViewById(R.id.pic2);
            secondPic.setBackgroundResource(getPic(friendList.get(1)));
            user2.setId("902");
            user2.setFirst(friendList.get(1));
            user2.setLast("Smither");
            user2.setInterest("Camping");
            user2.setCountry("Australia");
            user2.setEmail("Camping@hotmail.com");
            friend1.setVisibility(View.VISIBLE);
            friend2.setVisibility(View.VISIBLE);
            friend3.setVisibility(View.INVISIBLE);
            friend4.setVisibility(View.INVISIBLE);
            secondPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNum = 2;
                    NavHostFragment.findNavController(FriendsListFragment.this).navigate((R.id.action_friendsListFragment_to_fragmentProfileOther));
                }
            });
        }

        if (size > 2) {
            third.setText(friendList.get(2));
            Button thirdPic = root.findViewById(R.id.pic3);
            thirdPic.setBackgroundResource(getPic(friendList.get(2)));
            user3.setId("903");
            user3.setFirst(friendList.get(2));
            user3.setLast("Smithy");
            user3.setInterest("Hunting");
            user3.setCountry("Australia");
            user3.setEmail("Hunting@hotmail.com");
            friend1.setVisibility(View.VISIBLE);
            friend2.setVisibility(View.VISIBLE);
            friend3.setVisibility(View.VISIBLE);
            friend4.setVisibility(View.INVISIBLE);
            thirdPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNum = 3;
                    NavHostFragment.findNavController(FriendsListFragment.this).navigate((R.id.action_friendsListFragment_to_fragmentProfileOther));
                }
            });
        }

        if (size > 3) {
            user4.setId("904");
            user4.setFirst(friendList.get(3));
            user4.setLast("Smithmeister");
            user4.setInterest("Wasabi");
            user4.setCountry("Australia");
            user4.setEmail("Wasabi@hotmail.com");
            fourth.setText(friendList.get(3));
            Button fourthPic = root.findViewById(R.id.pic4);
            fourthPic.setBackgroundResource(getPic(friendList.get(3)));
            friend1.setVisibility(View.VISIBLE);
            friend2.setVisibility(View.VISIBLE);
            friend3.setVisibility(View.VISIBLE);
            friend4.setVisibility(View.VISIBLE);
            fourthPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userNum = 4;
                    NavHostFragment.findNavController(FriendsListFragment.this).navigate((R.id.action_friendsListFragment_to_fragmentProfileOther));
                }
            });
        }
        return root;
    }

    /**
     * Returns user picture id
     */
    public int getPic(String name) {
        if (name.equals("jason")) {
            return R.drawable.jason_photo;
        } else if (name.equals("jack")) {
            return R.drawable.jack_photo;
        } else if (name.equals("rose")) {
            return R.drawable.rose_photo;
        } else if (name.equals("sam")) {
            return R.drawable.jay_photo;
        } else if (name.equals("sue")) {
            return R.drawable.sue_photo;
        } else if (name.equals("clay")) {
            return R.drawable.clay_photo;
        }
        return R.drawable.jason_photo;
    }
}
