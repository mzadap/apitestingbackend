package model;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.util.ArrayList;

@Generated("net.hexar.json2pojo")
@Getter
@Setter
public class CreateFirstIssue {
    public String title;
    public String body;
    public ArrayList<String> assignees;
    public ArrayList<String> labels;

}
