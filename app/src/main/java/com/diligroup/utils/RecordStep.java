package com.diligroup.utils;

import com.diligroup.mode.StepData;
import com.diligroup.utils.record_steps.DbUtils;
import com.diligroup.utils.record_steps.StepDcretor;

import java.util.List;

/**
 * Created by hjf on 2016/10/31.
 */
public class RecordStep {
    public static String CURRENTDATE = "";

    public static void save() {
        CURRENTDATE=DateUtils.getTodayDate();
        LogUtils.e("当前的日期 save()",CURRENTDATE+"步数=");
        int tempStep = StepDcretor.CURRENT_SETP;
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENTDATE);
            data.setStep(tempStep + "");
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtils.update(data);
        } else {
        }
    }
}
