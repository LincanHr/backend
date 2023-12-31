package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Visitor;
import com.five.employnet.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/visitor")
//@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class VisitorController {
    final private VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/view/total")
    R<Integer> totalView() {
        int total = visitorService.count();
        return R.success(total);
    }

    @GetMapping("/view/{date}")
    R<Integer> viewAt(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int count = visitorService.getViewAt(localDate);
        return R.success(count);
    }

    @GetMapping("/view/week/{date}")
    R<List<Integer>> getViewWeekAt(@PathVariable String date) {
        LocalDate currentDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<LocalDate> dateList = getWeekDateRange(currentDate);
        List<Integer> countList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            int count = visitorService.getViewAt(localDate);
            countList.add(count);
        }
        return R.success(countList);
    }

    @GetMapping("/number/total")
    R<Integer> totalNumber() {
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("COUNT(DISTINCT user_id, date) as count");

        Map<String, Object> result = visitorService.getMap(queryWrapper);
        int count = Integer.parseInt(result.get("count").toString());
        return R.success(count);
    }

    @GetMapping("/number/{date}")
    R<Integer> numberAt(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int count = visitorService.getNumberAt(localDate);
        return R.success(count);
    }

    @GetMapping("/number/week/{date}")
    R<List<Integer>> getNumberWeekAt(@PathVariable String date) {
        LocalDate currentDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<LocalDate> dateList = getWeekDateRange(currentDate);
        List<Integer> countList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            int count = visitorService.getNumberAt(localDate);
            countList.add(count);
        }
        return R.success(countList);
    }

    // 获取给定日期所在周的所有日期
    private static List<LocalDate> getWeekDateRange(LocalDate currentDate) {
        List<LocalDate> weekDates = new ArrayList<>();

        // 获取周一的日期
        LocalDate monday = currentDate;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        // 添加本周的日期
        for (int i = 0; i < 7; i++) {
            weekDates.add(monday.plusDays(i));
        }

        return weekDates;
    }
}
