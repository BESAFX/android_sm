package app.besafx.com.smartmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    private Long id;

    private Integer code;

    private String name;

    private String address;

    private String phone;

    private String mobile;

    private String fax;

    private String email;

    private String website;

    private String commericalRegisteration;

    private String logo;

    private String options;
}
