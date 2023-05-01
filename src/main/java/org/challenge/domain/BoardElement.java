package org.challenge.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BoardElement {
    public static  HashMap<String,Label> labels = new HashMap<>();
    protected String title;
    protected String type;
    protected String assignee;
}
