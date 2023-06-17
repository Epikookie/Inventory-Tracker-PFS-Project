import java.time.LocalDateTime;

public class Session {

    public int staffID;
    public LocalDateTime loginTime;

    public Session(String staffID) {
        this.staffID = Integer.parseInt(staffID);
        this.loginTime = LocalDateTime.now();
    }

}
