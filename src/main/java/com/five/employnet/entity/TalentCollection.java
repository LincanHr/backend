package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TalentCollection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    private String user_id;
    private String talent_id;
}
