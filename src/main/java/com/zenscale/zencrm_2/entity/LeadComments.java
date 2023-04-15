package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lead_comments")
public class LeadComments {

    @EmbeddedId
    private LeadCommentsId id;

    private int leadId;
    private int stageId;
    private String comments;
}
