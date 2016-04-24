package sg.xs_tech.mybadmintonscores;

public class Friend {
    private String id;
    private String fname;
    private String email;

    public Friend(String fname) {
        this.fname = fname;
    }

    public Friend(String id, String fname) {
        this.id = id;
        this.fname = fname;
    }

    protected String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
