//
//  helpers.h
//  AppSentinelTools
//
//  Created by GuHaiJun on 2022/6/20.
//

#ifndef helpers_h
#define helpers_h

#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

int unhexify(unsigned char *obuf, size_t olen, const char *ibuf);
void hexify(unsigned char *obuf, const unsigned char *ibuf, int ilen);

#ifdef __cplusplus
}
#endif

#endif /* helpers_h */
