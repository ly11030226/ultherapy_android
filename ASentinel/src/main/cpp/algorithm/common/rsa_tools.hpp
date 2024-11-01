//
//  rsa_tools.hpp
//  AppSentinel
//
//  Created by GuHaiJun on 2022/9/3.
//

#ifndef rsa_tools_hpp
#define rsa_tools_hpp

#include <string>
#include "rsa.h"
#include "helpers.h"

#define KEY_LEN 128

namespace SentinelCommon {

using namespace std;

typedef struct {
    string E;
    string P;
    string Q;
    string DP;
    string DQ;
    string QP;
    string N;
    string D;
} rsa_key_data_def;

int rsa_private(rsa_key_data_def private_key, string input, string *output);
int rsa_public(rsa_key_data_def public_key, string input, string *output);

void rand_hex_string(unsigned char *output, int len);

}

#endif /* rsa_tools_hpp */
