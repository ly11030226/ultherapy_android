package com.aimyskin.laserserialmodule.responseClassify.device808Classify;


import android.content.Context;

import com.aimyskin.laserserialmodule.interceptor.CRC16Interceptor;
import com.aimyskin.laserserialmodule.interceptor.Frame5AA5Struct;
import com.aimyskin.laserserialmodule.interceptor.FrameCheck5AA5Interceptor;
import com.aimyskin.laserserialmodule.responseClassify.ResponseClassify;
import com.aimyskin.laserserialmodule.responseClassify.device808.OnButtonListener;
import com.aimyskin.serialasciicrlfimpl.AsciiCRLFPipelineInterceptor;
import com.aimyskin.serialasciicrlfimpl.HexToAsciiInterceptor;
import com.aimyskin.serialasciicrlfimpl.XorOperateInterceptor;
import com.aimyskin.serialmodule.EventListener;
import com.aimyskin.serialmodule.RealPipeline;
import com.aimyskin.serialmodule.Request;
import com.aimyskin.serialmodule.Response;
import com.aimyskin.serialmodule.SerialClient;
import com.aimyskin.serialmodule.interceptor.LogHexInterceptor;
import com.aimyskin.serialmodule.internal.Call;
import com.aimyskin.serialmodule.internal.Callback;
import com.aimyskin.serialmodule.serialport.SerialPortCommon;
import com.elvishew.xlog.XLog;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import bolts.Task;
import javolution.io.Struct;
import okio.ByteString;

public class Response808 implements ResponseClassify {

    private SerialClient client;
    private RealPipeline pipeline;
    private OnButtonListener onButtonListener;

    @Override
    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public Response808(Context context) {
        initLaserSerial(context);
    }

    public void initLaserSerial(Context context) {
        init();
        if (client != null) {
            close();
        }
        SerialPortCommon serialPortCommon = SerialPortCommon.Builder.aSerialPortCommon()
                .baudrate(Serial808Util.getSerialBaudrateSentinelControl(context))
                .serialPath(Serial808Util.getSerialPathSentinelControl(context))
                .build();

        client = SerialClient.Builder.aSerialClient()
                .addInterceptor(FrameCheck5AA5Interceptor.class)
                .addInterceptor(LogHexInterceptor.class)
                .addInterceptor(CRC16Interceptor.class)
                .addInterceptor(LogHexInterceptor.class)
                .addInterceptor(XorOperateInterceptor.class)
                .addInterceptor(HexToAsciiInterceptor.class)
                .addCallInterceptor(AsciiCRLFPipelineInterceptor.class)
                .serialPort(serialPortCommon)
                .eventListener(new EventListener() {
                    @Override
                    public void callStart(Call call) {
                        super.callStart(call);
                    }

                    @Override
                    public void callEnd(Call call) {
                        super.callEnd(call);
                    }

                    @Override
                    public void callFailed(Call call, Exception ioe) {
                        super.callFailed(call, ioe);
                        ioe.printStackTrace();
                    }
                })
                .build();

        pipeline = client.newPipeline(new Callback() {
            @Override

            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws Exception {
                Task.call(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        setResponse(response);
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });
        try {
            pipeline.open();
        } catch (Exception e) {
        }
    }

    private void setResponse(Response response) {
        byte[] datas = response.data.toByteArray();
        Frame5AA5Struct frame = new Frame5AA5Struct();
        frame.setByteBuffer(ByteBuffer.wrap(datas), 0);
        int instruct = frame.instruct.get();
        int address = frame.address.get();
        int addressNum = frame.addressNum.get();
        if (instruct == CommandWordEnum.COMMAND_WORD_94.commandWord
                || instruct == CommandWordEnum.COMMAND_WORD_93.commandWord) {
            refreshData(address, addressNum, frame.datas);
        }
    }

    @Override
    public void sendData(String data, int delay) {
        if (pipeline != null) {
            pipeline.pipeout(new Request(ByteString.decodeHex(data.replaceAll(" ", ""))), delay);
        }
    }

    @Override
    public void close() {
        if (client != null) {
            try {
                client.serialPort().close();
            } catch (Exception e) {
                XLog.e("close: ", e);
            }
            client = null;
        }
        if (pipeline != null) {
            pipeline.close();
            pipeline = null;
        }
    }

    // 存储地址和参数的映射关系
    public static volatile Map<Integer, Integer> addressParameterMap;
    // 存储地址的顺序
    public volatile List<Integer> addressList;

    private void init() {
        if (addressParameterMap == null) {
            addressParameterMap = new ConcurrentHashMap<>();
        }
        addressParameterMap.clear();
        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        addressList.clear();
        for (State808Enum value : State808Enum.values()) {
            addressList.add(value.getAddress());//租赁控制
        }
        Collections.sort(addressList);
    }

    // 根据地址获取对应的参数
    public static int getParameterByAddress(int address) {
        Integer value = addressParameterMap.get(address);
        if (value == null) {
            value = 0;
        }
        return value;
    }

    // 根据地址为对应参数赋值
    private void refreshData(int address, int addressNum, Struct.Unsigned16[] datas) {
        int startIndexOf = addressList.indexOf(address);
        for (int i = 0; i < addressNum; i++) {
            int addressKey = addressList.get(startIndexOf + i);
            if (!addressList.contains(addressKey )) {
                addressList.add(addressList.get(startIndexOf + i));
                Collections.sort(addressList);
            }
            addressParameterMap.put(addressList.get(startIndexOf + i), (int) datas[i].get());

            if (onButtonListener != null && addressKey == State808Enum.STATE_808_0015.getAddress()) {
                int flow = Response808.getParameterByAddress(State808Enum.STATE_808_0015.getAddress());
                int frequencyAdd = (flow >> 3) & 1;//频率
                int frequencyMinus = (flow >> 4) & 1;
                int energyAdd = (flow >> 5) & 1;// 能量
                int energyMinus = (flow >> 6) & 1;
                int pulseWidthAdd = (flow >> 7) & 1;//脉宽
                int pulseWidthMinus = (flow >> 8) & 1;
                int standbyReady = (flow >> 15) & 1;
                if (onButtonListener != null && standbyReady == 1) {
                    onButtonListener.buttonStandbyReady();
                }
                if (onButtonListener != null && frequencyAdd == 1) {
                    onButtonListener.buttonFrequencyAdd();
                }
                if (onButtonListener != null && frequencyMinus == 1) {
                    onButtonListener.buttonFrequencyMinus();
                }
                if (onButtonListener != null && pulseWidthAdd == 1) {
                    onButtonListener.buttonPulseWidthAdd();
                }
                if (onButtonListener != null && pulseWidthMinus == 1) {
                    onButtonListener.buttonPulseWidthMinus();
                }
                if (onButtonListener != null && energyAdd == 1) {
                    onButtonListener.buttonEnergyAdd();
                }
                if (onButtonListener != null && energyMinus == 1) {
                    onButtonListener.buttonEnergyMinus();
                }
                int refrigerateOff = (flow >> 9) & 1;
                int refrigerate_1 = (flow >> 10) & 1;
                int refrigerate_2 = (flow >> 11) & 1;
                int refrigerate_3 = (flow >> 12) & 1;
                int refrigerate_4 = (flow >> 13) & 1;
                int refrigerate_5 = (flow >> 14) & 1;

                if (onButtonListener != null ) {
                    if (refrigerateOff == 1 ){
                        onButtonListener.buttonHandleRefrigerateGearKey(0);
                    }else if (refrigerate_1 == 1 ){
                        onButtonListener.buttonHandleRefrigerateGearKey(1);
                    }else if (refrigerate_2 == 1 ){
                        onButtonListener.buttonHandleRefrigerateGearKey(2);
                    }else if (refrigerate_3 == 1 ){
                        onButtonListener.buttonHandleRefrigerateGearKey(3);
                    }else if (refrigerate_4 == 1 ){
                        onButtonListener.buttonHandleRefrigerateGearKey(4);
                    }else if (refrigerate_5 == 1 ){
                        onButtonListener.buttonHandleRefrigerateGearKey(5);
                    }

                }
            }
        }
    }

}
