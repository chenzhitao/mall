/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.monitor.service.impl;

import co.yixiang.modules.monitor.domain.Visits;
import co.yixiang.modules.monitor.repository.VisitsRepository;
import co.yixiang.modules.monitor.service.VisitsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import co.yixiang.repository.LogRepository;
import co.yixiang.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitsServiceImpl implements VisitsService {

    private final VisitsRepository visitsRepository;
    private final LogRepository logRepository;

    @Override
    public void save() {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsRepository.findByDate(localDate.toString());
        if(visits == null){
            visits = new Visits();
            visits.setWeekDay(StringUtils.getWeekDay());
            visits.setPvCounts(1L);
            visits.setIpCounts(1L);
            visits.setDate(localDate.toString());
            visitsRepository.save(visits);
        }
    }

    @Override
    public void count(HttpServletRequest request) {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsRepository.findByDate(localDate.toString());
        visits.setPvCounts(visits.getPvCounts()+1);
        long ipCounts = logRepository.findIp(localDate.toString(), localDate.plusDays(1).toString());
        visits.setIpCounts(ipCounts);
        visitsRepository.save(visits);
    }

    @Override
    public Object get() {
        Map map = new HashMap();
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsRepository.findByDate(localDate.toString());
        List<Visits> list = visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());

        long recentVisits = 0, recentIp = 0;
        for (Visits data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits",visits.getPvCounts());
        map.put("newIp",visits.getIpCounts());
        map.put("recentVisits",recentVisits);
        map.put("recentIp",recentIp);
        return map;
    }

    @Override
    public Object getChartData() {
        Map map = new HashMap();
        LocalDate localDate = LocalDate.now();
        List<Visits> list = visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
        map.put("weekDays",list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData",list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
        map.put("ipData",list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
        return map;
    }
}
