package com.aimyskin.serialmodule.internal;

import com.aimyskin.serialmodule.Response;

public interface Callback {
  void onFailure(Call call, Exception e);
  void onResponse(Call call, Response response) throws Exception;
}
