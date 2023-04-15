package com.zenscale.zencrm_2.structures;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class sl_users {

    private String uid;
    private String name;
    private String email;
    private String mobile;
    private String whatsApp;
    private String creon;

}
