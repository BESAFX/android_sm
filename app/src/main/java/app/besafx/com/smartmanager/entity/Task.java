package app.besafx.com.smartmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer code;

    private String title;

    @Override
    public String toString() {
        return this.title;
    }
}
