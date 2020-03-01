package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.Vip;

@Mapper
public interface VipMapper extends BaseMapper<Vip> {

    int deleteByPrimaryKey(Long vipId);

    int insert(Vip record);

    int insertSelective(Vip record);

    Vip selectByPrimaryKey(Long vipId);

    int updateByPrimaryKeySelective(Vip record);

    int updateByPrimaryKey(Vip record);

    Vip selectByUserId(Long userId);

    int updateByUserIdSelective(Vip vip);
}