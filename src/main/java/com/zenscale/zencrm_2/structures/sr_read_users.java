package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class sr_read_users {


    private int status;
    private String msg;

    private Integer totalUsers;

    private List<sl_users> usersList;




    public sr_read_users(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
