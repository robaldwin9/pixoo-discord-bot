package bot.pixxoo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixooBuzzer {
    @Expose
    @SerializedName("Command")
    String command = "Device/PlayBuzzer";

    @Expose
    @SerializedName("ActiveTimeInCycle")
    int activeTimeInCycle = 1000;

    @Expose
    @SerializedName("OffTimeInCycle")
    int offTimeInCycle = 0;

    @Expose
    @SerializedName("PlayTotalTime")
    int playTotalTime = 1000;

    public String toJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

}
