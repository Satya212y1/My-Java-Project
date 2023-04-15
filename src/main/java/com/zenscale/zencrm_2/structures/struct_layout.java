package com.zenscale.zencrm_2.structures;

import lombok.Data;

@Data
public class struct_layout implements Comparable<struct_layout> {

    private int rptid;
    private int colid;
    private String colfield;
    private String colval;
    private int pos;
    private int width;
    private String fieldType;
    private String hide;




    @Override
    public int compareTo(struct_layout o) {
        // TODO Auto-generated method stub
        int compareage = ((struct_layout) o).getPos();

        // For Ascending order
        return this.pos - compareage;
    }




}