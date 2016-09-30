package com.example.crypsis.mylayerapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crypsis on 26/9/16.
 */

public class NewUserModel {
    String layerid;
    public NewUserModel(List<String> participants, boolean distinct) {
        this.participants = participants;
        this.distinct = distinct;
    }
    public NewUserModel(String layerid,String participant,boolean distinct)
    {
        this.layerid=layerid;
        this.participants=new ArrayList<>();
        this.participants.add(participant);
        this.distinct=distinct;
    }
    Metadata metadata;
class Metadata{
    String background_color;

    }
    List<String> participants;
    boolean distinct;

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}
