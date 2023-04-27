package org.alan.etude.dto.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class TestMemberVO {

    private int memberNo;
    private String memberId;
    private String memberPassword;
    private String memberName;
    private Timestamp registerDate;


}
