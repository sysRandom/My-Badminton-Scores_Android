package sg.xs_tech.mybadmintonscores;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {

    private String id;
    private Friend team_player1;
    private Friend team_player2;
    private Friend opponent_player1;
    private Friend opponent_player2;
    private Friend poster;
    private String match_date;
    private String create_date;
    private String modify_date;

    public Match(String id, Friend team_player1, Friend team_player2, Friend opponent_player1,
                 Friend opponent_player2, Friend poster, String match_date, String create_date,
                 String modify_date) {
        this.id = id;
        this.team_player1 = team_player1;
        this.team_player2 = team_player2;
        this.opponent_player1 = opponent_player1;
        this.opponent_player2 = opponent_player2;
        this.poster = poster;
        this.match_date = match_date;
        this.create_date = create_date;
        this.modify_date = modify_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
