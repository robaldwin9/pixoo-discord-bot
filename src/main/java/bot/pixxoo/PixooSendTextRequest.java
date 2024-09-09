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
    protected int textId = 1;

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
    protected int speed = 0;

    @Expose
    @SerializedName("color")
    protected String color = "#FFFF00";

    @Expose
    @SerializedName("align")
    protected int align = 1;

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
