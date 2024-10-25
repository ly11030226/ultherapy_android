package com.aimyskin.laserserialmodule.responseClassify.configuration;

public interface ConfigurationDataClassify {

    ConfigurationDataBean getThisBean();

    ReturnDataBean initConfiguration( int hz, int ms, int energy);

    ReturnDataBean setFrequency(int inputHz);

    ReturnDataBean setPulseWidth(int inputMs);

    ReturnDataBean setEnergy( int inputEnergy);
}
