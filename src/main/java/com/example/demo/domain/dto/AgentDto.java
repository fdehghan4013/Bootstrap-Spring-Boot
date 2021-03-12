package com.example.demo.domain.dto;

import com.example.demo.domain.Key;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AgentDto {
    private long id;
    private String firstName;
    private String lastName;
    private String createdDate;

    private List<KeyDto> keys = new ArrayList<>();
}
