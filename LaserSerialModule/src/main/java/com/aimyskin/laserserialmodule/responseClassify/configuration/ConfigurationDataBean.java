package com.aimyskin.laserserialmodule.responseClassify.configuration;

import java.util.List;


/**
 * 配置文件
 */
public class ConfigurationDataBean {

    protected ReturnDataBean returnDataBean;
    protected int energyMax;//能量最大值
    protected int energyMin;//能量最小值
    protected int pulseWidthMax;
    protected int pulseWidthMin;
    protected int hzMax;
    protected int hzMin;
    protected int handleRefrigerateMax;// 手柄制冷等级长度 0 为关闭状态
    protected List<ConfigurationData> dataList;
    protected List<HRBean> hrList;// 可调节界面默认数据数据

    public static class ConfigurationData {
        private int hz;
        private int pulseWidthMax;
        private int energyMax;
        private List<ConfigurationBean> beanList;
        private List<ConfigurationBean> beanEnergyList;

        public int getEnergyMax() {
            return energyMax;
        }

        public void setEnergyMax(int energyMax) {
            this.energyMax = energyMax;
        }

        public List<ConfigurationBean> getBeanEnergyList() {
            return beanEnergyList;
        }

        public void setBeanEnergyList(List<ConfigurationBean> beanEnergyList) {
            this.beanEnergyList = beanEnergyList;
        }

        public int getPulseWidthMax() {
            return pulseWidthMax;
        }

        public void setPulseWidthMax(int pulseWidthMax) {
            this.pulseWidthMax = pulseWidthMax;
        }

        public int getHz() {
            return hz;
        }

        public void setHz(int hz) {
            this.hz = hz;
        }

        public List<ConfigurationBean> getBeanList() {
            return beanList;
        }

        public void setBeanList(List<ConfigurationBean> beanList) {
            this.beanList = beanList;
        }

        public static class ConfigurationBean {
            public int workCurrentPercent; // 工作电流百分比，用于发送电流数据
            public int energy;// 可能会重复
            public int pulseWidth;// 递增到最大值，

            public ConfigurationBean() {
            }

            public ConfigurationBean(int pulseWidth, int energy, int work) {
                this.pulseWidth = pulseWidth;
                this.energy = energy;
                this.workCurrentPercent = work;
            }

            public int getWorkCurrentPercent() {
                return workCurrentPercent;
            }

            public void setWorkCurrentPercent(int workCurrentPercent) {
                this.workCurrentPercent = workCurrentPercent;
            }

            public int getEnergy() {
                return energy;
            }

            public void setEnergy(int energy) {
                this.energy = energy;
            }

            public int getPulseWidth() {
                return pulseWidth;
            }

            public void setPulseWidth(int pulseWidth) {
                this.pulseWidth = pulseWidth;
            }
        }
    }

    public ReturnDataBean getReturnDataBean() {
        return returnDataBean;
    }

    public void setReturnDataBean(ReturnDataBean returnDataBean) {
        this.returnDataBean = returnDataBean;
    }

    public List<HRBean> getHrList() {
        return hrList;
    }

    public void setHrList(List<HRBean> hrList) {
        this.hrList = hrList;
    }

    public int getEnergyMax() {
        return energyMax;
    }

    public void setEnergyMax(int energyMax) {
        this.energyMax = energyMax;
    }

    public int getEnergyMin() {
        return energyMin;
    }

    public void setEnergyMin(int energyMin) {
        this.energyMin = energyMin;
    }

    public int getPulseWidthMax() {
        return pulseWidthMax;
    }

    public void setPulseWidthMax(int pulseWidthMax) {
        this.pulseWidthMax = pulseWidthMax;
    }

    public int getPulseWidthMin() {
        return pulseWidthMin;
    }

    public void setPulseWidthMin(int pulseWidthMin) {
        this.pulseWidthMin = pulseWidthMin;
    }

    public int getHzMax() {
        return hzMax;
    }

    public void setHzMax(int hzMax) {
        this.hzMax = hzMax;
    }

    public int getHzMin() {
        return hzMin;
    }

    public void setHzMin(int hzMin) {
        this.hzMin = hzMin;
    }

    public int getHandleRefrigerateMax() {
        return handleRefrigerateMax;
    }

    public void setHandleRefrigerateMax(int handleRefrigerateMax) {
        this.handleRefrigerateMax = handleRefrigerateMax;
    }

    public List<ConfigurationData> getDataList() {
        return dataList;
    }

    public void setDataList(List<ConfigurationData> dataList) {
        this.dataList = dataList;
    }

    public static class HRBean {

            private int sex; // 0 女 1 男
            private int skinColor;// 0 开始
            private int bodyPart;// 0 开始
            private int energy;
            private int pulse;
            private int hz;

        public HRBean() {
        }

        public HRBean(int sex, int skinColor, int bodyPart, int energy, int hz, int pulse) {
            this.sex = sex;
            this.skinColor = skinColor;
            this.bodyPart = bodyPart;
            this.energy = energy;
            this.pulse = pulse;
            this.hz = hz;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getSkinColor() {
            return skinColor;
        }

        public void setSkinColor(int skinColor) {
            this.skinColor = skinColor;
        }

        public int getBodyPart() {
            return bodyPart;
        }

        public void setBodyPart(int bodyPart) {
            this.bodyPart = bodyPart;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public int getPulse() {
            return pulse;
        }

        public void setPulse(int pulse) {
            this.pulse = pulse;
        }

        public int getHz() {
            return hz;
        }

        public void setHz(int hz) {
            this.hz = hz;
        }
    }
}
