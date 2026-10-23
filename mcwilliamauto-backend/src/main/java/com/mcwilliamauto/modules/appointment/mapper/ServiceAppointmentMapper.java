package com.mcwilliamauto.modules.appointment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mcwilliamauto.modules.appointment.entity.ServiceAppointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceAppointmentMapper extends BaseMapper<ServiceAppointment> {
}
