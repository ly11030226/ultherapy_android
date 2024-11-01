LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES += $(LOCAL_PATH)/algorithm/RSA/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/algorithm/RSA/library
LOCAL_C_INCLUDES += $(LOCAL_PATH)/algorithm/common
LOCAL_C_INCLUDES += $(LOCAL_PATH)/algorithm/sentinel
LOCAL_C_INCLUDES += $(LOCAL_PATH)/algorithm

ALGORITHM_SRC_FILES := $(wildcard $(LOCAL_PATH)/algorithm/RSA/library/*.c)
ALGORITHM_SRC_FILES += $(wildcard $(LOCAL_PATH)/algorithm/common/*.cpp)
ALGORITHM_SRC_FILES += $(wildcard $(LOCAL_PATH)/algorithm/sentinel/*.cpp)
ALGORITHM_SRC_FILES += $(wildcard $(LOCAL_PATH)/algorithm/*.cpp)
ALGORITHM_SRC_FILES += $(wildcard $(LOCAL_PATH)/*.cpp)
LOCAL_SRC_FILES += $(ALGORITHM_SRC_FILES:$(LOCAL_PATH)/%=%)

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_LDLIBS     += -llog -ldl -ljnigraphics
LOCAL_MODULE     := asentinel

include $(BUILD_SHARED_LIBRARY)
