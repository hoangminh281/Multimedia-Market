package com.thm.hoangminh.multimediamarket.presenters.GamePresenters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.models.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInteractor {
    GameListener listener;
    DatabaseReference mRef;

    public GameInteractor(GameListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void LoadGameBySection( String section_id) {
        mRef.child("sections/" + section_id + "/game_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    listener.onLoadSectionSuccess(new ArrayList<>(map.values()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void LoadGameBySectionPaging(List<String> game_Id_List, int item_count) {
        int i = 0;
        while (!game_Id_List.isEmpty() && i < item_count) {
            String id = game_Id_List.get(0);
            game_Id_List.remove(0);
            mRef.child("games/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        listener.onLoadGameBySectionSuccess(dataSnapshot.getValue(Game.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            i++;
        }
    }

}
