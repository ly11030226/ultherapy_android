package com.aimyskin.laserserialmodule.responseClassify.configuration;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommonUtil {

    public static int getEnergyMax(List<ConfigurationDataBean.ConfigurationData.ConfigurationBean> beanList) {
        int energy = 0;
        for (ConfigurationDataBean.ConfigurationData.ConfigurationBean configurationBean : beanList) {
            if (configurationBean.getEnergy() > energy) {
                energy = configurationBean.getEnergy();
            }
        }
        return energy;
    }

    public static List<ConfigurationDataBean.ConfigurationData.ConfigurationBean> getEnergyList(List<ConfigurationDataBean.ConfigurationData.ConfigurationBean> beanList) {
        Comparator comparatorEnergy = Comparator.comparingInt(ConfigurationDataBean.ConfigurationData.ConfigurationBean::getEnergy);
        List<ConfigurationDataBean.ConfigurationData.ConfigurationBean> temporaryList = new ArrayList<>();
        temporaryList.addAll(beanList);
        temporaryList.sort(comparatorEnergy);

        List<ConfigurationDataBean.ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        int energy = 0;
        for (ConfigurationDataBean.ConfigurationData.ConfigurationBean configurationBean : temporaryList) {
            if (configurationBean.getEnergy() > energy) {
                list.add(configurationBean);
                energy = configurationBean.getEnergy();
            }
        }
        return list;
    }

    public static void logd(String name, ConfigurationDataBean bean) {
        String text = new Gson().toJson(bean);
        showLogCompletion(name, text, 2500);
    }

    private static void showLogCompletion(String name, String log, int showCount) {
        if (log.length() > showCount) {
            String show = log.substring(0, showCount);
            Log.d("TAG", name + " : " + show);
            String partLog = log.substring(showCount, log.length());
            showLogCompletion(name, partLog, showCount);
        } else {
            Log.d("TAG", name + " : " + log);
        }
    }

}
