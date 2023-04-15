package com.zenscale.zencrm_2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "layout_client")
@Data
@NoArgsConstructor
@ToString
public class layout {

	@EmbeddedId
	private LayoutIdentity layoutIdentity;

	private String colfield;
	private String colval;
	private int pos;
	private int width;
	private String hide;

}