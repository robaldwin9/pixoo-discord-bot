package bot.pixxoo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixooCountdown {
    @Expose
    @SerializedName("Command")
    String command = "Tools/SetTimer";

    @Expose
    @SerializedName("Minute")
    int minutes;

    @Expose
    @SerializedName("Second")
    int seconds;

    @Expose
    @SerializedName("Status")
    int status = 0;

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
