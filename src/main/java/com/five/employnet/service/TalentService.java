package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Talent;

public interface TalentService extends IService<Talent> {
    void saveOneTalent(Talent talent);
    void completeTalent(Talent talent);
    Talent getTalentByUserId(String userId);
}
