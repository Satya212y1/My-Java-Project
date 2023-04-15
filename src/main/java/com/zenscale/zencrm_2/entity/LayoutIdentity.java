package com.zenscale.zencrm_2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class LayoutIdentity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9205950755614513058L;

	private int bukrs;
	private String uid;
	private int rptid;
	private int colid;

}