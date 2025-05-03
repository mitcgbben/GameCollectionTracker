package com.example.bennettmitchell_final;

// objects that can be used to identify a game.
// this is a bad explanation
public interface GameID {
    int getID();
    byte[] getIcon();
    String getName();

    void setID(int id);
    void setName(String name);
    void setIcon(byte[] icon);
}
// coming back at 1 am i realize i have wrapped back around to just having status as the gameID Object but i dont want to remove this quite yet
