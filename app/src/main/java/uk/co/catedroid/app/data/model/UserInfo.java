package uk.co.catedroid.app.data.model;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    private final String name;
    private final String login;
    private final String cid;
    private final String status;
    private final String department;
    private final String category;
    private final String email;
    @SerializedName("personal_tutor") private final String personalTutor;

    public UserInfo(String name, String login, String cid, String status, String department,
                    String category, String email, String personalTutor) {
        this.name = name;
        this.login = login;
        this.cid = cid;
        this.status = status;
        this.department = department;
        this.category = category;
        this.email = email;
        this.personalTutor = personalTutor;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getCid() {
        return cid;
    }

    public String getStatus() {
        return status;
    }

    public String getDepartment() {
        return department;
    }

    public String getCategory() {
        return category;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonalTutor() {
        return personalTutor;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
