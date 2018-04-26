package app.besafx.com.smartmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

    private String code;

    private String title;

    private String message;

    private Date date;

    private String type;

    private String icon;

    private String sender;

    private String receiver;
}
