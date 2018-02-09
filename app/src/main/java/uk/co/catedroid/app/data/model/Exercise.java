package uk.co.catedroid.app.data.model;

import com.google.gson.annotations.SerializedName;

public class Exercise {
    private String code;
    private String name;
    private String start;
    private String end;
    @SerializedName("assessed_status") private String assessedStatus;
    @SerializedName("submission_status") private String submissionStatus;
    @SerializedName("module_number") private String moduleNumber;
    @SerializedName("module_name") private String moduleName;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getAssessedStatus() {
        return assessedStatus;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }

    public String getModuleNumber() {
        return moduleNumber;
    }

    public String getModuleName() {
        return moduleName;
    }
}
