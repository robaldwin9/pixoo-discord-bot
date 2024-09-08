package bot.pixxoo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixooSendAnimationRequest {

    @Expose
    @SerializedName("Command")
    protected String command = "Draw/SendHttpGif";

    @Expose
    @SerializedName("PicNum")
    protected int picNum = 1;

    @Expose
    @SerializedName("PicWidth")
    protected int picWidth = 64;

    @Expose
    @SerializedName("PicOffset")
    protected int picOffset = 0;

    @Expose
    @SerializedName("PicID")
    protected long picId = 1;

    @Expose
    @SerializedName("PicSpeed")
    protected int picSpeed = 100;

    @Expose
    @SerializedName("PicData")
    protected String picData = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicOffset() {
        return picOffset;
    }

    public void setPicOffset(int picOffset) {
        this.picOffset = picOffset;
    }

    public long getPicId() {
        return picId;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    public int getPicSpeed() {
        return picSpeed;
    }

    public void setPicSpeed(int picSpeed) {
        this.picSpeed = picSpeed;
    }

    public String getPicData() {
        return picData;
    }

    public void setPicData(String picData) {
        this.picData = picData;
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

}
