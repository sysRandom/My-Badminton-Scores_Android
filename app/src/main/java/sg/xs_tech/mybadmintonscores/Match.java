package sg.xs_tech.mybadmintonscores;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {

    private String id;
    private Friend team_player1;
    private Friend team_player2;
    private int team_score;
    private Friend opponent_player1;
    private Friend opponent_player2;
    private int opponent_score;
    private Friend poster;
    private String match_date;
    private String create_date;
    private String modify_date;

    public Match(String id, Friend team_player1, Friend team_player2, int team_score, Friend opponent_player1,
                 Friend opponent_player2, int opponent_score, Friend poster, String match_date, String create_date,
                 String modify_date) {
        this.id = id;
        this.team_player1 = team_player1;
        this.team_player2 = team_player2;
        this.team_score = team_score;
        this.opponent_player1 = opponent_player1;
        this.opponent_player2 = opponent_player2;
        this.opponent_score = opponent_score;
        this.poster = poster;
        this.match_date = match_date;
        this.create_date = create_date;
        this.modify_date = modify_date;
    }

    public Friend getTeam_player1() {
        return team_player1;
    }

    public String getId() {
        return id;
    }

    public Friend getPoster() {
        return poster;
    }

    public Friend getTeam_player2() {
        return team_player2;
    }

    public int getTeam_score() {
        return team_score;
    }

    public int getOpponent_score() {
        return opponent_score;
    }

    public Friend getOpponent_player1() {
        return opponent_player1;
    }

    public Friend getOpponent_player2() {
        return opponent_player2;
    }

    public String getMatch_date() {
        return match_date;
    }

    private Match(Parcel in) {
        id = in.readString();
        team_player1 = in.readParcelable(Friend.class.getClassLoader());
        team_player2 = in.readParcelable(Friend.class.getClassLoader());
        opponent_player1 = in.readParcelable(Friend.class.getClassLoader());
        opponent_player2 = in.readParcelable(Friend.class.getClassLoader());
        poster = in.readParcelable(Friend.class.getClassLoader());
        match_date = in.readString();
        create_date = in.readString();
        modify_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
}
