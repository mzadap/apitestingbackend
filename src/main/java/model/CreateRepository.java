package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;

@Generated("net.hexar.json2pojo")
@Getter
@Setter
public class CreateRepository {

    public String name;
    public String description;
    public String homepage;
    @JsonProperty("private")
    public boolean myprivate;
    public boolean is_template;
}
