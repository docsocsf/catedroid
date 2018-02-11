package uk.co.catedroid.app.data.model;

import com.google.gson.annotations.SerializedName;

public class Note {
    private String number;
    private String title;
    private String type;
    private String size;
    private String loaded;
    private String owner;
    private String hits;
    private String url;
    private String filekey;
    @SerializedName("module_number") private String moduleNumber;
    @SerializedName("module_name") private String moduleName;

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getLoaded() {
        return loaded;
    }

    public String getOwner() {
        return owner;
    }

    public String getHits() {
        return hits;
    }

    public String getUrl() {
        return url;
    }

    public String getFilekey() {
        return filekey;
    }

    public String getModuleNumber() {
        return moduleNumber;
    }

    public String getModuleName() {
        return moduleName;
    }
}
