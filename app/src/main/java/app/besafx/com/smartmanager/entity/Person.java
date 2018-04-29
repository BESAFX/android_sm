package app.besafx.com.smartmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String nickname;

    private String shortName;

    private String address;

    private String mobile;

    private String nationality;

    private String identityNumber;

    private String options;

    private String photo;

    private String qualification;

    private String email;

    private String password;

    private Boolean technicalSupport;

    private Boolean enabled;

    private Boolean tokenExpired;

    private Boolean active;

    private String hiddenPassword;

    private Date lastLoginDate;

    private String lastLoginLocation;

    private String ipAddress;

    private Double mainSalary;

    private Double houseAllowance;

    private Double transportAllowance;

    private Date lastUpdate;

    @Override
    public String toString() {
        return this.shortName;
    }
}
