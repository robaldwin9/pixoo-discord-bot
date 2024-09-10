package bot.pixxoo.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixooNoiseTool {
    @Expose
    @SerializedName("Command")
    String command = "Tools/SetNoiseStatus";

    @Expose
    @SerializedName("NoiseStatus")
    int noiseToolStatus;

    public int getNoiseToolStatus() {
        return noiseToolStatus;
    }

    public void setNoiseToolStatus(int noiseToolStatus) {
        this.noiseToolStatus = noiseToolStatus;
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
