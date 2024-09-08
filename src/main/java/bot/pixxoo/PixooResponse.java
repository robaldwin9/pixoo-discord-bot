package bot.pixxoo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PixooResponse {
    @Expose
    @SerializedName("error_code")
    String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode() {
        errorCode = errorCode;
    }
}
