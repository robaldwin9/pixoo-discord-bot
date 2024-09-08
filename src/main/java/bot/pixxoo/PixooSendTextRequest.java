package bot.pixxoo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixooSendTextRequest {
    @Expose
    @SerializedName("Command")
    protected String command = "Draw/SendHttpText";

    @Expose
    @SerializedName("TextId")
    protected int textId = 0;

    @Expose
    @SerializedName("x")
    protected int x = 0;

    @Expose
    @SerializedName("y")
    protected int y = 40;

    @Expose
    @SerializedName("dir")
    protected int dir = 0;

    @Expose
    @SerializedName("font")
    protected int font = 4;

    @Expose
    @SerializedName("TextWidth")
    protected int textWidth = 56;

    @Expose
    @SerializedName("TextString")
    protected String textString = "default";

    @Expose
    @SerializedName("speed")
    protected int speed = 1000;

    @Expose
    @SerializedName("color")
    protected String color = "#FFFF00";

    @Expose
    @SerializedName("align")
    protected int align = 2;

    public String toJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
