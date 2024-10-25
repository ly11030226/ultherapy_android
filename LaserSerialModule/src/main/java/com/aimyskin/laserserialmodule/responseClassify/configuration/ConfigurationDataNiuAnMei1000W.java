package com.aimyskin.laserserialmodule.responseClassify.configuration;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConfigurationDataNiuAnMei1000W extends ConfigurationDataBean implements ConfigurationDataClassify {

    public ConfigurationDataNiuAnMei1000W() {
        handleRefrigerateMax = 5;
        energyMin = 1;//能量最小值
        pulseWidthMin = 10;
        pulseWidthMax = 200;
        hzMin = 1;
        hzMax = 10;
        hrList = getDefaultHrList();
        dataList = getConfigurationData();
        returnDataBean = new ReturnDataBean(1, 1, 10, 34);

    }

    private List<ConfigurationData> getConfigurationData() {
        Comparator comparatorPulse = Comparator.comparingInt(ConfigurationData.ConfigurationBean::getPulseWidth);
        List<ConfigurationData> list = new ArrayList<>();

        ConfigurationData data_1 = new ConfigurationData();
        data_1.setHz(1);
        data_1.setPulseWidthMax(200);
        List<ConfigurationData.ConfigurationBean> listPulse1 = getBeanList_1();
        listPulse1.sort(comparatorPulse);
        data_1.setBeanList(listPulse1);
        data_1.setBeanEnergyList(CommonUtil.getEnergyList(data_1.getBeanList()));
        data_1.setEnergyMax(CommonUtil.getEnergyMax(data_1.getBeanEnergyList()));
        energyMax = Math.max(data_1.getEnergyMax(), energyMax);
        list.add(data_1);

        ConfigurationData data_2 = new ConfigurationData();
        data_2.setHz(2);
        data_2.setPulseWidthMax(90);
        List<ConfigurationData.ConfigurationBean> listPulse2 = getBeanList_2();
        listPulse2.sort(comparatorPulse);
        data_2.setBeanList(listPulse2);
        data_2.setBeanEnergyList(CommonUtil.getEnergyList(data_2.getBeanList()));
        data_2.setEnergyMax(CommonUtil.getEnergyMax(data_2.getBeanEnergyList()));
        energyMax = Math.max(data_2.getEnergyMax(), energyMax);
        list.add(data_2);

        ConfigurationData data_3 = new ConfigurationData();
        data_3.setHz(3);
        data_3.setPulseWidthMax(60);
        List<ConfigurationData.ConfigurationBean> listPulse3 = getBeanList_3();
        listPulse3.sort(comparatorPulse);
        data_3.setBeanList(listPulse3);
        data_3.setBeanEnergyList(CommonUtil.getEnergyList(data_3.getBeanList()));
        data_3.setEnergyMax(CommonUtil.getEnergyMax(data_3.getBeanEnergyList()));
        energyMax = Math.max(data_3.getEnergyMax(), energyMax);
        list.add(data_3);

        ConfigurationData data_4 = new ConfigurationData();
        data_4.setHz(4);
        data_4.setPulseWidthMax(50);
        List<ConfigurationData.ConfigurationBean> listPulse4 = getBeanList_4();
        listPulse4.sort(comparatorPulse);
        data_4.setBeanList(listPulse4);
        data_4.setBeanEnergyList(CommonUtil.getEnergyList(data_4.getBeanList()));
        data_4.setEnergyMax(CommonUtil.getEnergyMax(data_4.getBeanEnergyList()));
        energyMax = Math.max(data_4.getEnergyMax(), energyMax);
        list.add(data_4);

        ConfigurationData data_5 = new ConfigurationData();
        data_5.setHz(5);
        data_5.setPulseWidthMax(40);
        List<ConfigurationData.ConfigurationBean> listPulse5 = getBeanList_5();
        listPulse5.sort(comparatorPulse);
        data_5.setBeanList(listPulse5);
        data_5.setBeanEnergyList(CommonUtil.getEnergyList(data_5.getBeanList()));
        data_5.setEnergyMax(CommonUtil.getEnergyMax(data_5.getBeanEnergyList()));
        energyMax = Math.max(data_5.getEnergyMax(), energyMax);
        list.add(data_5);

        ConfigurationData data_6 = new ConfigurationData();
        data_6.setHz(6);
        data_6.setPulseWidthMax(30);
        List<ConfigurationData.ConfigurationBean> listPulse6 = getBeanList_6();
        listPulse6.sort(comparatorPulse);
        data_6.setBeanList(listPulse6);
        data_6.setBeanEnergyList(CommonUtil.getEnergyList(data_6.getBeanList()));
        data_6.setEnergyMax(CommonUtil.getEnergyMax(data_6.getBeanEnergyList()));
        energyMax = Math.max(data_6.getEnergyMax(), energyMax);
        list.add(data_6);

        ConfigurationData data_7 = new ConfigurationData();
        data_7.setHz(7);
        data_7.setPulseWidthMax(20);
        List<ConfigurationData.ConfigurationBean> listPulse7 = getBeanList_7();
        listPulse7.sort(comparatorPulse);
        data_7.setBeanList(listPulse7);
        data_7.setBeanEnergyList(CommonUtil.getEnergyList(data_7.getBeanList()));
        data_7.setEnergyMax(CommonUtil.getEnergyMax(data_7.getBeanEnergyList()));
        energyMax = Math.max(data_7.getEnergyMax(), energyMax);
        list.add(data_7);

        ConfigurationData data_8 = new ConfigurationData();
        data_8.setHz(8);
        data_8.setPulseWidthMax(20);
        List<ConfigurationData.ConfigurationBean> listPulse8 = getBeanList_7();
        listPulse8.sort(comparatorPulse);
        data_8.setBeanList(listPulse8);
        data_8.setBeanEnergyList(CommonUtil.getEnergyList(data_8.getBeanList()));
        data_8.setEnergyMax(CommonUtil.getEnergyMax(data_8.getBeanEnergyList()));
        energyMax = Math.max(data_8.getEnergyMax(), energyMax);
        list.add(data_8);

        ConfigurationData data_9 = new ConfigurationData();
        data_9.setHz(9);
        data_9.setPulseWidthMax(20);
        List<ConfigurationData.ConfigurationBean> listPulse9 = getBeanList_7();
        listPulse9.sort(comparatorPulse);
        data_9.setBeanList(listPulse9);
        data_9.setBeanEnergyList(CommonUtil.getEnergyList(data_9.getBeanList()));
        data_9.setEnergyMax(CommonUtil.getEnergyMax(data_9.getBeanEnergyList()));
        energyMax = Math.max(data_9.getEnergyMax(), energyMax);
        list.add(data_9);

        ConfigurationData data_10 = new ConfigurationData();
        data_10.setHz(10);
        data_10.setPulseWidthMax(20);
        List<ConfigurationData.ConfigurationBean> listPulse10 = getBeanList_7();
        listPulse10.sort(comparatorPulse);
        data_10.setBeanList(listPulse10);
        data_10.setBeanEnergyList(CommonUtil.getEnergyList(data_10.getBeanList()));
        data_10.setEnergyMax(CommonUtil.getEnergyMax(data_10.getBeanEnergyList()));
        energyMax = Math.max(data_10.getEnergyMax(), energyMax);
        list.add(data_10);

        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getBeanList_1() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 50));
        list.addAll(getCommon(88, 51, 100));
        list.addAll(getCommon(70, 101, 200));
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getBeanList_2() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 50));
        list.addAll(getCommon(88, 51, 90));
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getBeanList_3() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 50));
        list.addAll(getCommon(88, 51, 60));
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getBeanList_4() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 50));
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getBeanList_5() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 40));
        return list;
    }
    private List<ConfigurationData.ConfigurationBean> getBeanList_6() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 30));
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getBeanList_7() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        list.addAll(getCommon_1());
        list.addAll(getCommon(98, 10, 20));
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getCommon(int coefficient, int start, int end) {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            int energy = getEnergy_10_above(coefficient, i);
            list.add(new ConfigurationData.ConfigurationBean(i, energy, 100));
        }
        return list;
    }

    private List<ConfigurationData.ConfigurationBean> getCommon_1() {
        List<ConfigurationData.ConfigurationBean> list = new ArrayList<>();
        for (int i = 1; i < getEnergy_10_above(98, 10); i++) {
            int work = getWorkCurrentPercent_10_Within(i);
            list.add(new ConfigurationData.ConfigurationBean(10, i, work));
        }
        return list;
    }

    private int getWorkCurrentPercent_10_Within(int e) {
        // ((能量 / (条数bar * 脉宽 * 倾斜率 * 能量降低系数) + 阈值电流) * 换算成百分比 / 最大工作电流)
        int work = (int) Math.round(((e / (10 * 0.01 * 1.24 * 0.95) + 18) * 100 / 98));
        if (work > 100) {
            work = 100;
        }
        return work;
    }

    /**
     * 根据脉宽计算10以上能量值
     *
     * @param coefficient 系数
     */
    private int getEnergy_10_above(int coefficient, int ms) {
        // J=(工作电流98-阈值电流18)*斜效率1.24 * 能量降低系数 *bar条数10*(脉宽/1000)
        int energy = (int) Math.round((coefficient - 18) * 1.24 * 0.95 * 10 * ms / 1000);
        return energy;
    }

    /**
     * todo 待核对
     *
     * @return
     */
    private List<HRBean> getDefaultHrList() {
        List<HRBean> list = new ArrayList<>();
        // 女性
        list.add(new HRBean(0, 0, 0, 4, 1, 10));
        list.add(new HRBean(0, 0, 1, 16, 1, 20));
        list.add(new HRBean(0, 0, 2, 24, 2, 30));
        list.add(new HRBean(0, 0, 3, 26, 2, 20));
        list.add(new HRBean(0, 0, 4, 24, 2, 30));
        list.add(new HRBean(0, 0, 5, 4, 1, 10));

        list.add(new HRBean(0, 1, 0, 3, 1, 10));
        list.add(new HRBean(0, 1, 1, 5, 1, 10));
        list.add(new HRBean(0, 1, 2, 24, 2, 30));
        list.add(new HRBean(0, 1, 3, 5, 1, 10));
        list.add(new HRBean(0, 1, 4, 24, 2, 30));
        list.add(new HRBean(0, 1, 5, 3, 1, 10));

        list.add(new HRBean(0, 2, 0, 3, 1, 10));
        list.add(new HRBean(0, 2, 1, 7, 1, 10));
        list.add(new HRBean(0, 2, 2, 24, 2, 30));
        list.add(new HRBean(0, 2, 3, 16, 2, 20));
        list.add(new HRBean(0, 2, 4, 24, 1, 30));
        list.add(new HRBean(0, 2, 5, 3, 1, 10));

        list.add(new HRBean(0, 3, 0, 3, 1, 10));
        list.add(new HRBean(0, 3, 1, 6, 1, 10));
        list.add(new HRBean(0, 3, 2, 7, 2, 10));
        list.add(new HRBean(0, 3, 3, 6, 2, 10));
        list.add(new HRBean(0, 3, 4, 16, 2, 20));
        list.add(new HRBean(0, 3, 5, 3, 1, 10));

        list.add(new HRBean(0, 4, 0, 2, 1, 10));
        list.add(new HRBean(0, 4, 1, 4, 1, 10));
        list.add(new HRBean(0, 4, 2, 16, 2, 20));
        list.add(new HRBean(0, 4, 3, 4, 2, 10));
        list.add(new HRBean(0, 4, 4, 16, 2, 20));
        list.add(new HRBean(0, 4, 5, 2, 1, 10));

        list.add(new HRBean(0, 5, 0, 2, 1, 10));
        list.add(new HRBean(0, 5, 1, 3, 1, 10));
        list.add(new HRBean(0, 5, 2, 16, 2, 20));
        list.add(new HRBean(0, 5, 3, 16, 1, 20));
        list.add(new HRBean(0, 5, 4, 16, 2, 20));
        list.add(new HRBean(0, 5, 5, 2, 1, 10));

        // 男性
        list.add(new HRBean(1, 0, 0, 4, 1, 10));
        list.add(new HRBean(1, 0, 1, 16, 1, 20));
        list.add(new HRBean(1, 0, 2, 24, 2, 30));
        list.add(new HRBean(1, 0, 3, 16, 2, 20));
        list.add(new HRBean(1, 0, 4, 24, 2, 30));
        list.add(new HRBean(1, 0, 5, 4, 1, 10));

        list.add(new HRBean(1, 1, 0, 3, 1, 10));
        list.add(new HRBean(1, 1, 1, 16, 1, 20));
        list.add(new HRBean(1, 1, 2, 24, 2, 30));
        list.add(new HRBean(1, 1, 3, 5, 1, 10));
        list.add(new HRBean(1, 1, 4, 24, 2, 30));
        list.add(new HRBean(1, 1, 5, 3, 1, 10));

        list.add(new HRBean(1, 2, 0, 3, 1, 10));
        list.add(new HRBean(1, 2, 1, 7, 1, 10));
        list.add(new HRBean(1, 2, 2, 24, 2, 30));
        list.add(new HRBean(1, 2, 3, 16, 2, 20));
        list.add(new HRBean(1, 2, 4, 24, 1, 30));
        list.add(new HRBean(1, 2, 5, 3, 1, 10));

        list.add(new HRBean(1, 3, 0, 3, 1, 10));
        list.add(new HRBean(1, 3, 1, 6, 1, 10));
        list.add(new HRBean(1, 3, 2, 16, 2, 20));//
        list.add(new HRBean(1, 3, 3, 6, 2, 10));
        list.add(new HRBean(1, 3, 4, 16, 2, 20));
        list.add(new HRBean(1, 3, 5, 3, 1, 10));

        list.add(new HRBean(1, 4, 0, 2, 1, 10));
        list.add(new HRBean(1, 4, 1, 4, 1, 10));
        list.add(new HRBean(1, 4, 2, 16, 2, 20));
        list.add(new HRBean(1, 4, 3, 4, 2, 10));
        list.add(new HRBean(1, 4, 4, 16, 2, 20));
        list.add(new HRBean(1, 4, 5, 2, 1, 10));

        list.add(new HRBean(1, 5, 0, 2, 1, 10));
        list.add(new HRBean(1, 5, 1, 3, 1, 10));
        list.add(new HRBean(1, 5, 2, 16, 2, 20));
        list.add(new HRBean(1, 5, 3, 16, 1, 20));
        list.add(new HRBean(1, 5, 4, 16, 2, 20));
        list.add(new HRBean(1, 5, 5, 2, 1, 10));

        return list;
    }

    @Override
    public ConfigurationDataBean getThisBean() {
        CommonUtil.logd("ConfigurationDataNiuAnMei800W" , this );
        return this;
    }

    @Override
    public ReturnDataBean initConfiguration(int hz, int ms, int energy) {
        returnDataBean.setHz(hz);
        returnDataBean.setPulseWidth(ms);
        returnDataBean.setEnergy(energy);
        returnDataBean.setWorkCurrentPercent(100);
        jump:
        for (ConfigurationData data : dataList) {
            if (data.getHz() == hz) {
                for (ConfigurationData.ConfigurationBean configurationBean : data.getBeanList()) {
                    if (configurationBean.getEnergy() == energy && configurationBean.getPulseWidth() == ms) {
                        returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                        return returnDataBean;
                    }
                    if (configurationBean.getPulseWidth() > ms) {
                        break jump;
                    }
                }
                break jump;
            }
        }
        jump:
        for (ConfigurationData data : dataList) {
            if (data.getHz() == hz) {
                int energyMax = 0;
                for (ConfigurationData.ConfigurationBean configurationBean : data.getBeanList()) {
                    if (configurationBean.getPulseWidth() > ms) {
                        break jump;
                    }
                    if (ms > pulseWidthMin) {
                        if (configurationBean.getPulseWidth() == ms) {
                            returnDataBean.setEnergy(configurationBean.getEnergy());
                            returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                            return returnDataBean;
                        }
                    } else if (ms == pulseWidthMin) {
                        if (configurationBean.getEnergy() > energyMax) {
                            returnDataBean.setEnergy(configurationBean.getEnergy());
                            returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                        }
                    }
                }
                return returnDataBean;
            }
        }
        if (ms <= 10) {
            if (energy >= getEnergy_10_above(98, 10)) {
                returnDataBean.setWorkCurrentPercent(100);
            } else {
                returnDataBean.setWorkCurrentPercent(getWorkCurrentPercent_10_Within(energy));
            }
        }
        return returnDataBean;
    }

    @Override
    public ReturnDataBean setFrequency(int inputHz) {
        if (inputHz > hzMax || inputHz < hzMin) {
            return returnDataBean;
        }
        jump:
        for (ConfigurationData data : dataList) {
            if (data.getHz() == inputHz) {
                if (data.getPulseWidthMax() < returnDataBean.getPulseWidth()) {
                    // 去该脉宽下最大脉宽一组数据 用于增大频率
                    ConfigurationData.ConfigurationBean configurationBean = data.getBeanList().get(data.getBeanList().size() - 1);
                    if (configurationBean != null) {
                        returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                        returnDataBean.setEnergy(configurationBean.getEnergy());
                        returnDataBean.setPulseWidth(configurationBean.getPulseWidth());
                        returnDataBean.setHz(inputHz);
                    }
                    return returnDataBean;
                } else {
                    returnDataBean.setHz(inputHz);
                    return returnDataBean;
                }
            }
        }
        return returnDataBean;
    }

    @Override
    public ReturnDataBean setPulseWidth(int inputMs) {
        if (inputMs > pulseWidthMax || inputMs < pulseWidthMin) {
            return returnDataBean;
        }
        if (inputMs == pulseWidthMin && returnDataBean.getPulseWidth() == pulseWidthMin) {
            return returnDataBean;
        }
        if (inputMs == pulseWidthMin && returnDataBean.getPulseWidth() != pulseWidthMin) {
            // 调整脉宽到最小值了 返回 脉宽最小值 对应能量的最大值
            for (ConfigurationData data : dataList) {
                if (data.getHz() == returnDataBean.getHz()) {
                    ConfigurationData.ConfigurationBean bean = null;
                    int maxEnergy = 0;
                    jump_2:
                    for (ConfigurationData.ConfigurationBean configurationBean : data.getBeanList()) {
                        if (configurationBean.getPulseWidth() > inputMs) {
                            break jump_2;
                        }
                        if (inputMs == configurationBean.getPulseWidth() && configurationBean.getEnergy() > maxEnergy) {
                            maxEnergy = configurationBean.getEnergy();
                            bean = configurationBean;
                        }
                    }
                    if (bean != null) {
                        returnDataBean.setWorkCurrentPercent(bean.getWorkCurrentPercent());
                        returnDataBean.setEnergy(bean.getEnergy());
                        returnDataBean.setPulseWidth(bean.getPulseWidth());
                        returnDataBean.setHz(returnDataBean.getHz());
                    }
                    return returnDataBean;
                }
            }
            return returnDataBean;
        }

        if (inputMs > returnDataBean.getPulseWidth()) {
            // 增大脉宽 有可能需要改变频率
            for (ConfigurationData data : dataList) {
                if (data.getHz() == returnDataBean.getHz()) {
                    if (inputMs > data.getPulseWidthMax()) {
                        returnDataBean.setHz(returnDataBean.getHz() - 1);
                        setPulseWidth(inputMs);
                        break;
                    }
                    for (ConfigurationData.ConfigurationBean configurationBean : data.getBeanList()) {
                        if (configurationBean.getPulseWidth() == inputMs) {
                            returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                            returnDataBean.setEnergy(configurationBean.getEnergy());
                            returnDataBean.setPulseWidth(configurationBean.getPulseWidth());
                            returnDataBean.setHz(data.getHz());
                            return returnDataBean;
                        }
                    }
                }
            }
        } else {
            // 减小脉宽  不会改变频率
            for (ConfigurationData data : dataList) {
                if (data.getHz() == returnDataBean.getHz()) {
                    for (ConfigurationData.ConfigurationBean configurationBean : data.getBeanList()) {
                        if (configurationBean.getPulseWidth() == inputMs) {
                            returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                            returnDataBean.setEnergy(configurationBean.getEnergy());
                            returnDataBean.setPulseWidth(configurationBean.getPulseWidth());
                            returnDataBean.setHz(data.getHz());
                            return returnDataBean;
                        }
                    }
                }
            }
        }
        return returnDataBean;
    }

    @Override
    public ReturnDataBean setEnergy(int inputEnergy) {
        if (inputEnergy < energyMin || inputEnergy > energyMax) {
            return returnDataBean;
        }
        // 能量有可能在循环的时候无法找到对应的值
        // 增大能量同时会增大脉宽，继而可能减小频率
        // 减小能量同时会减小脉宽，不会影响频率
        if (inputEnergy > returnDataBean.getEnergy()) {
            // 增加能量
            jump:
            for (ConfigurationData data : dataList) {
                if (inputEnergy > data.getEnergyMax()) {
                    // 超过改组能量最大值则降低频率
                    returnDataBean.setHz(returnDataBean.getHz() - 1);
                    setEnergy(inputEnergy);
                    break jump;
                }
                if (returnDataBean.getHz() == data.getHz()) {
                    for (ConfigurationData.ConfigurationBean configurationBean : data.getBeanEnergyList()) {
                        if (inputEnergy == configurationBean.getEnergy() || configurationBean.getEnergy() > inputEnergy) {
                            returnDataBean.setHz(returnDataBean.getHz());
                            returnDataBean.setPulseWidth(configurationBean.getPulseWidth());
                            returnDataBean.setEnergy(configurationBean.getEnergy());
                            returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                            return returnDataBean;
                        }
                    }
                    ConfigurationData.ConfigurationBean bean = data.getBeanEnergyList().get(data.getBeanEnergyList().size() - 1);
                    if (bean != null) {
                        returnDataBean.setHz(returnDataBean.getHz());
                        returnDataBean.setPulseWidth(bean.getPulseWidth());
                        returnDataBean.setEnergy(bean.getEnergy());
                        returnDataBean.setWorkCurrentPercent(bean.getWorkCurrentPercent());
                        return returnDataBean;
                    }
                }
            }
        } else {
            // 减小能量
            for (ConfigurationData data : dataList) {
                if (returnDataBean.getHz() == data.getHz()) {
                    for (int i = data.getBeanEnergyList().size() - 1; i >= 0; i--) {
                        ConfigurationData.ConfigurationBean configurationBean = data.getBeanEnergyList().get(i);
                        if (inputEnergy == configurationBean.getEnergy() || configurationBean.getEnergy() < inputEnergy) {
                            returnDataBean.setHz(returnDataBean.getHz());
                            returnDataBean.setPulseWidth(configurationBean.getPulseWidth());
                            returnDataBean.setEnergy(configurationBean.getEnergy());
                            returnDataBean.setWorkCurrentPercent(configurationBean.getWorkCurrentPercent());
                            return returnDataBean;
                        }
                    }
                }
            }
        }
        return returnDataBean;
    }
}
